package common.util;

import common.managers.LogManager;

/**
 * @author Pantelis Andrianakis
 * @since November 7th 2018
 */
public final class Util
{
	public static final char[] ILLEGAL_CHARACTERS =
	{
		'/',
		'\n',
		'\r',
		'\t',
		'\0',
		'\f',
		'`',
		'?',
		'*',
		'\\',
		'<',
		'>',
		'|',
		'\"',
		'{',
		'}',
		'(',
		')'
	};
	
	public static void printSection(String s)
	{
		s = "=[ " + s + " ]";
		while (s.length() < 62)
		{
			s = "-" + s;
		}
		LogManager.log(s);
	}
}
