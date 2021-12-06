package gameserver.managers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import common.Config;
import common.managers.LogManager;
import common.network.SendablePacket;
import gameserver.actor.Player;
import gameserver.actor.WorldObject;
import gameserver.holders.RegionHolder;
import gameserver.network.client.GameClient;
import gameserver.network.client.sendable.DeleteObject;
import gnu.trove.TCollections;
import gnu.trove.map.TLongObjectMap;
import gnu.trove.map.hash.TLongObjectHashMap;

/**
 * @author Pantelis Andrianakis
 * @since November 14th 2020
 */
public class WorldManager
{
	private static final Object REGION_WRITE_LOCK = new Object();
	private static final TLongObjectMap<RegionHolder> REGIONS = TCollections.synchronizedMap(new TLongObjectHashMap<RegionHolder>());
	private static final Set<GameClient> ONLINE_CLIENTS = ConcurrentHashMap.newKeySet();
	private static final Set<Player> PLAYER_OBJECTS = ConcurrentHashMap.newKeySet();
	public static final int REGION_SIZE = 300;
	
	public static void init()
	{
		LogManager.log("WorldManager: Initialized.");
	}
	
	public static long calculateRegionHash(int x, int z)
	{
		return (((byte) x) & 0xff) //
			| ((((byte) (x >> 8)) & 0xffL) << 8) //
			| ((((byte) (x >> 16)) & 0xffL) << 16) //
			| ((((byte) (x >> 24)) & 0xffL) << 24) //
			| ((((byte) z) & 0xffL) << 32) //
			| ((((byte) (z >> 8)) & 0xffL) << 40) //
			| ((((byte) (z >> 16)) & 0xffL) << 48) //
			| ((((byte) (z >> 24)) & 0xffL) << 56);
	}
	
	public static RegionHolder getRegion(long hash, int x, int z)
	{
		// Find existing region.
		RegionHolder region = REGIONS.get(hash);
		
		// Create region if it does not exist.
		if (region == null)
		{
			synchronized (REGION_WRITE_LOCK)
			{
				region = REGIONS.get(hash);
				if (region == null)
				{
					region = new RegionHolder(x, z);
					REGIONS.put(hash, region);
				}
			}
		}
		
		// Dynamically add nearby regions when empty.
		if (region.getSurroundingRegions() == null)
		{
			synchronized (REGION_WRITE_LOCK)
			{
				if (region.getSurroundingRegions() == null)
				{
					final List<RegionHolder> surroundingRegions = new ArrayList<>(9);
					final int minX = (x * REGION_SIZE) - REGION_SIZE;
					final int maxX = (x * REGION_SIZE) + REGION_SIZE;
					final int minZ = (z * REGION_SIZE) - REGION_SIZE;
					final int maxZ = (z * REGION_SIZE) + REGION_SIZE;
					for (int nx = minX; nx <= maxX; nx = nx + REGION_SIZE)
					{
						for (int nz = minZ; nz <= maxZ; nz = nz + REGION_SIZE)
						{
							final int divX = nx / REGION_SIZE;
							final int divZ = nz / REGION_SIZE;
							final long nearbyHash = calculateRegionHash(divX, divZ);
							RegionHolder nearbyRegion = REGIONS.get(nearbyHash);
							if (nearbyRegion == null)
							{
								nearbyRegion = new RegionHolder(divX, divZ);
								REGIONS.put(nearbyHash, nearbyRegion);
							}
							surroundingRegions.add(nearbyRegion);
						}
					}
					RegionHolder[] regionArray = new RegionHolder[surroundingRegions.size()];
					regionArray = surroundingRegions.toArray(regionArray);
					region.setSurroundingRegions(regionArray);
				}
			}
		}
		
		return region;
	}
	
	public static void addObject(WorldObject obj)
	{
		if (obj.isPlayer())
		{
			final Player player = obj.asPlayer();
			
			if (!PLAYER_OBJECTS.contains(player))
			{
				PLAYER_OBJECTS.add(player);
				ONLINE_CLIENTS.add(player.getClient());
				
				// Log world access.
				if (Config.LOG_WORLD)
				{
					LogManager.logWorld("Player [" + player.getName() + "] Account [" + player.getClient().getAccountName() + "] Entered the world.");
				}
			}
		}
	}
	
	public static void removeObject(WorldObject obj)
	{
		// Broadcast deletion to nearby players.
		broadcastPacketToVisiblePlayers(obj, new DeleteObject(obj));
		
		// Remove from list and take necessary actions.
		if (obj.isPlayer())
		{
			final Player player = obj.asPlayer();
			PLAYER_OBJECTS.remove(player);
			
			// Store player.
			player.storeMe();
			
			// Log world access.
			if (Config.LOG_WORLD && (player.getClient().getActiveChar() != null))
			{
				LogManager.logWorld("Player [" + player.getName() + "] Account [" + player.getClient().getAccountName() + "] Left the world.");
			}
		}
		
		obj.getRegion().removeObject(obj);
	}
	
	public static Collection<WorldObject> getVisibleObjects(WorldObject obj)
	{
		final List<WorldObject> result = new LinkedList<>();
		final RegionHolder[] regions = obj.getRegion().getSurroundingRegions();
		if (regions != null)
		{
			for (int i = 0; i < regions.length; i++)
			{
				final Collection<WorldObject> objects = regions[i].getObjects();
				if (objects.isEmpty())
				{
					continue;
				}
				
				for (WorldObject nearby : objects)
				{
					if (nearby.getObjectId() == obj.getObjectId())
					{
						continue;
					}
					
					result.add(nearby);
				}
			}
		}
		return result;
	}
	
	public static Collection<Player> getVisiblePlayers(WorldObject obj)
	{
		final List<Player> result = new LinkedList<>();
		final RegionHolder[] regions = obj.getRegion().getSurroundingRegions();
		if (regions != null)
		{
			for (int i = 0; i < regions.length; i++)
			{
				final Collection<WorldObject> objects = regions[i].getObjects();
				if (objects.isEmpty())
				{
					continue;
				}
				
				for (WorldObject nearby : objects)
				{
					if (!nearby.isPlayer())
					{
						continue;
					}
					if (nearby.getObjectId() == obj.getObjectId())
					{
						continue;
					}
					
					result.add(nearby.asPlayer());
				}
			}
		}
		return result;
	}
	
	public static void broadcastPacketToVisiblePlayers(WorldObject obj, SendablePacket packet)
	{
		final RegionHolder[] regions = obj.getRegion().getSurroundingRegions();
		if (regions != null)
		{
			for (int i = 0; i < regions.length; i++)
			{
				final Collection<WorldObject> objects = regions[i].getObjects();
				if (objects.isEmpty())
				{
					continue;
				}
				
				for (WorldObject nearby : objects)
				{
					if (!nearby.isPlayer())
					{
						continue;
					}
					if (nearby.getObjectId() == obj.getObjectId())
					{
						continue;
					}
					
					nearby.asPlayer().sendPacket(packet);
				}
			}
		}
	}
	
	public static Player getPlayerByName(String name)
	{
		for (Player player : PLAYER_OBJECTS)
		{
			if (player.getName().equalsIgnoreCase(name))
			{
				return player;
			}
		}
		return null;
	}
	
	public static int getOnlineCount()
	{
		return ONLINE_CLIENTS.size();
	}
	
	public static void addClient(GameClient client)
	{
		if (!ONLINE_CLIENTS.contains(client))
		{
			ONLINE_CLIENTS.add(client);
		}
	}
	
	public static void removeClient(GameClient client)
	{
		// Store and remove player.
		final Player player = client.getActiveChar();
		if (player != null)
		{
			removeObject(player);
			client.setActiveChar(null);
		}
		
		// Remove from list.
		ONLINE_CLIENTS.remove(client);
	}
	
	public static GameClient getClientByAccountName(String accountName)
	{
		for (GameClient client : ONLINE_CLIENTS)
		{
			if (client.getAccountName().equals(accountName))
			{
				return client;
			}
		}
		return null;
	}
	
	public static Set<Player> getPlayers()
	{
		return PLAYER_OBJECTS;
	}
}
