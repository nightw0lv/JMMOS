package gameserver.util;

/**
 * @author Pantelis Andrianakis
 * @since September 12th 2020
 */
public class ColorUtil
{
	public static int hexStringToInt(String hex)
	{
		return Integer.parseInt(hex, 16);
	}
}
