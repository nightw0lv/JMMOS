package gameserver.holders;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import gameserver.actor.WorldObject;

/**
 * @author Pantelis Andrianakis
 * @since April 27th 2019
 */
public class RegionHolder
{
	private final int _x;
	private final int _z;
	private final Set<WorldObject> _objects = ConcurrentHashMap.newKeySet();
	private RegionHolder[] _surroundingRegions;
	
	public RegionHolder(int x, int z)
	{
		_x = x;
		_z = z;
	}
	
	public void setSurroundingRegions(RegionHolder[] regions)
	{
		_surroundingRegions = regions;
		
		// Make sure that this region is always the first region to improve bulk operations when this region should be updated first.
		for (int i = 0; i < _surroundingRegions.length; i++)
		{
			if (_surroundingRegions[i] == this)
			{
				final RegionHolder first = _surroundingRegions[0];
				_surroundingRegions[0] = this;
				_surroundingRegions[i] = first;
			}
		}
	}
	
	public RegionHolder[] getSurroundingRegions()
	{
		return _surroundingRegions;
	}
	
	public void addObject(WorldObject obj)
	{
		_objects.add(obj);
	}
	
	public void removeObject(WorldObject obj)
	{
		_objects.remove(obj);
	}
	
	public Collection<WorldObject> getObjects()
	{
		return _objects;
	}
	
	public long getX()
	{
		return _x;
	}
	
	public long getZ()
	{
		return _z;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof RegionHolder))
		{
			return false;
		}
		final RegionHolder region = ((RegionHolder) obj);
		return (_x == region.getX()) && (_z == region.getZ());
	}
	
	@Override
	public String toString()
	{
		return "Region [" + _x + " " + _z + "]";
	}
}
