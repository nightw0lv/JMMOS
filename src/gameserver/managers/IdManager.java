package gameserver.managers;

/**
 * @author Pantelis Andrianakis
 * @version November 7th 2018
 */
public class IdManager
{
	private static volatile long _lastId = 0;
	
	public static synchronized long getNextId()
	{
		return _lastId++;
	}
}
