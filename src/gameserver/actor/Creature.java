package gameserver.actor;

/**
 * @author Pantelis Andrianakis
 * @since November 7th 2018
 */
public class Creature extends WorldObject
{
	private long _currentHp = 0;
	private long _currentMp = 0;
	private WorldObject _target;
	
	public Creature()
	{
	}
	
	// TODO: Implement Player level data.
	// TODO: Implement Creature stats holder.
	public long getMaxHp()
	{
		return 100;
	}
	
	public void setCurrentHp(long value)
	{
		_currentHp = value;
	}
	
	public long getCurrentHp()
	{
		return _currentHp;
	}
	
	public void setCurrentMp(long value)
	{
		_currentMp = value;
	}
	
	public long getCurrentMp()
	{
		return _currentMp;
	}
	
	public void setTarget(WorldObject worldObject)
	{
		_target = worldObject;
	}
	
	public WorldObject getTarget()
	{
		return _target;
	}
	
	@Override
	public boolean isCreature()
	{
		return true;
	}
	
	@Override
	public Creature asCreature()
	{
		return this;
	}
	
	@Override
	public String toString()
	{
		return "Creature [" + getObjectId() + "]";
	}
}
