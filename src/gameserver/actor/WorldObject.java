package gameserver.actor;

import java.util.List;

import common.managers.ThreadManager;
import common.util.Chronos;
import gameserver.holders.AnimationHolder;
import gameserver.holders.LocationHolder;
import gameserver.holders.RegionHolder;
import gameserver.managers.IdManager;
import gameserver.managers.WorldManager;
import gameserver.network.client.sendable.DeleteObject;
import gameserver.network.client.sendable.LocationUpdate;
import gameserver.network.client.sendable.NpcInformation;

/**
 * @author Pantelis Andrianakis
 * @since November 7th 2018
 */
public class WorldObject
{
	private final long _objectId = IdManager.getNextId();
	private final long _spawnTime = Chronos.currentTimeMillis();
	private AnimationHolder _animations = null;
	private final LocationHolder _location = new LocationHolder(0, -1000, 0, 0);
	private RegionHolder _region = null;
	private long _regionHash = Long.MIN_VALUE;
	private boolean _isTeleporting = false;
	
	public long getObjectId()
	{
		return _objectId;
	}
	
	public long getSpawnTime()
	{
		return _spawnTime;
	}
	
	public AnimationHolder getAnimations()
	{
		return _animations;
	}
	
	public void setAnimations(AnimationHolder animations)
	{
		_animations = animations;
	}
	
	public LocationHolder getLocation()
	{
		return _location;
	}
	
	public synchronized void setLocation(LocationHolder location)
	{
		setLocation(location.getX(), location.getY(), location.getZ(), location.getHeading());
	}
	
	public void setLocation(float posX, float posY, float posZ, float heading)
	{
		_location.update(posX, posY, posZ, heading);
		
		final int x = (int) posX / WorldManager.REGION_SIZE;
		final int z = (int) posZ / WorldManager.REGION_SIZE;
		final long regionHash = WorldManager.calculateRegionHash(x, z);
		if (regionHash == _regionHash)
		{
			return;
		}
		_regionHash = regionHash;
		
		// When changing location get appropriate region.
		RegionHolder newRegion = WorldManager.getRegion(regionHash, x, z);
		if (_region != null)
		{
			// Remove this object from the region.
			_region.removeObject(this);
			
			// Broadcast change to players left behind when teleporting.
			if (_isTeleporting)
			{
				final DeleteObject deleteObject = new DeleteObject(this);
				final RegionHolder[] regions = _region.getSurroundingRegions();
				for (int i = 0; i < regions.length; i++)
				{
					final List<WorldObject> objects = regions[i].getObjects();
					for (int j = 0; j < objects.size(); j++)
					{
						final WorldObject nearby = objects.get(j);
						if ((nearby == null) || (nearby == this) || !nearby.isPlayer())
						{
							continue;
						}
						nearby.asPlayer().sendPacket(deleteObject);
					}
				}
			}
		}
		
		// Assign new region.
		_region = newRegion;
		_region.addObject(this);
		
		// Send visible NPC information.
		// TODO: Exclude known NPCs?
		if (isPlayer())
		{
			final List<WorldObject> objects = WorldManager.getVisibleObjects(this);
			for (int i = 0; i < objects.size(); i++)
			{
				final WorldObject nearby = objects.get(i);
				if (!nearby.isNpc())
				{
					continue;
				}
				asPlayer().sendPacket(new NpcInformation(nearby.asNpc()));
			}
		}
	}
	
	public RegionHolder getRegion()
	{
		return _region;
	}
	
	public void setTeleporting()
	{
		_isTeleporting = true;
		ThreadManager.schedule(new StopTeleportingTask(), 1000);
	}
	
	private class StopTeleportingTask implements Runnable
	{
		public StopTeleportingTask()
		{
		}
		
		@Override
		public void run()
		{
			stopTeleporting();
		}
	}
	
	protected void stopTeleporting()
	{
		_isTeleporting = false;
		
		// Broadcast location to nearby players after teleporting.
		final LocationUpdate locationUpdate = new LocationUpdate(this);
		final List<Player> players = WorldManager.getVisiblePlayers(this);
		final Player player = asPlayer();
		for (int i = 0; i < players.size(); i++)
		{
			final Player nearby = players.get(i);
			if (nearby.isPlayer())
			{
				nearby.asPlayer().sendPacket(locationUpdate);
			}
			if (isPlayer())
			{
				player.sendPacket(new LocationUpdate(nearby));
			}
		}
	}
	
	public boolean isTeleporting()
	{
		return _isTeleporting;
	}
	
	public void deleteMe()
	{
		// Remove from region.
		_region.removeObject(this);
		
		// Broadcast NPC deletion.
		final DeleteObject delete = new DeleteObject(this);
		final RegionHolder[] regions = _region.getSurroundingRegions();
		for (int i = 0; i < regions.length; i++)
		{
			final List<WorldObject> objects = regions[i].getObjects();
			for (int j = 0; j < objects.size(); j++)
			{
				final WorldObject nearby = objects.get(j);
				if ((nearby != null) && nearby.isPlayer())
				{
					nearby.asPlayer().sendPacket(delete);
				}
			}
		}
		
		// Set region to null.
		_region = null;
	}
	
	/**
	 * Calculates distance between this GameObject and given x, y , z.
	 * @param x the X coordinate
	 * @param y the Y coordinate
	 * @param z the Z coordinate
	 * @return distance between object and given x, y, z.
	 */
	public double calculateDistance(float x, float y, float z)
	{
		return Math.pow(x - _location.getX(), 2) + Math.pow(y - _location.getY(), 2) + Math.pow(z - _location.getZ(), 2);
	}
	
	/**
	 * Calculates distance between this GameObject and another GameObject.
	 * @param object GameObject
	 * @return distance between object and given x, y, z.
	 */
	public double calculateDistance(WorldObject object)
	{
		return calculateDistance(object.getLocation().getX(), object.getLocation().getY(), object.getLocation().getZ());
	}
	
	/**
	 * Verify if object is instance of Creature.
	 * @return {@code true} if object is instance of Creature, {@code false} otherwise.
	 */
	public boolean isCreature()
	{
		return false;
	}
	
	/**
	 * @return {@link Creature} instance if current object is such, {@code null} otherwise.
	 */
	public Creature asCreature()
	{
		return null;
	}
	
	/**
	 * Verify if object is instance of Player.
	 * @return {@code true} if object is instance of Player, {@code false} otherwise.
	 */
	public boolean isPlayer()
	{
		return false;
	}
	
	/**
	 * @return {@link Player} instance if current object is such, {@code null} otherwise.
	 */
	public Player asPlayer()
	{
		return null;
	}
	
	/**
	 * Verify if object is instance of Npc.
	 * @return {@code true} if object is instance of Npc, {@code false} otherwise.
	 */
	public boolean isNpc()
	{
		return false;
	}
	
	/**
	 * @return {@link Npc} instance if current object is such, {@code null} otherwise.
	 */
	public Npc asNpc()
	{
		return null;
	}
	
	/**
	 * Verify if object is instance of Monster.
	 * @return {@code true} if object is instance of Monster, {@code false} otherwise.
	 */
	public boolean isMonster()
	{
		return false;
	}
	
	/**
	 * @return {@link Monster} instance if current object is such, {@code null} otherwise.
	 */
	public Monster asMonster()
	{
		return null;
	}
	
	@Override
	public String toString()
	{
		return "WorldObject [" + _objectId + "]";
	}
}
