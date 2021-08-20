package gameserver.handlers.commands.player;

import gameserver.actor.Player;
import gameserver.holders.LocationHolder;
import gameserver.managers.ChatManager;

/**
 * @author Pantelis Andrianakis
 * @version November 29th 2019
 */
public class LocCommand
{
	public static void Handle(Player player)
	{
		final LocationHolder location = player.getLocation();
		
		// Send player success message.
		StringBuilder sb = new StringBuilder();
		sb.append("Your location is ");
		sb.append(location.getX());
		sb.append(" ");
		sb.append(location.getZ());
		sb.append(" ");
		sb.append(location.getY());
		ChatManager.sendSystemMessage(player, sb.toString());
	}
}
