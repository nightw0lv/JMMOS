package gameserver.util;

/**
 * @author Pantelis Andrianakis
 * @version September 12th 2020
 */
public class ColorUtil
{
	public static int HexStringToInt(String hex)
	{
		return Integer.parseInt(hex, 16);
	}
}
