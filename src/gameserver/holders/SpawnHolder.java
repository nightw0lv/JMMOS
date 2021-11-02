package gameserver.holders;

/**
 * @author Pantelis Andrianakis
 * @since November 28th 2019
 */
public class SpawnHolder
{
	private final LocationHolder _location;
	private final int _respawnDelay;
	
	public SpawnHolder(LocationHolder location, int respawnDelay)
	{
		_location = location;
		_respawnDelay = respawnDelay;
	}
	
	public LocationHolder getLocation()
	{
		return _location;
	}
	
	public int getRespawnDelay()
	{
		return _respawnDelay;
	}
}
