package gameserver.managers;

/**
 * @author Pantelis Andrianakis
 * @since November 7th 2018
 */
public class IdManager
{
	private static volatile long LAST_ID = 0;
	
	public static synchronized long getNextId()
	{
		return LAST_ID++;
	}
}
