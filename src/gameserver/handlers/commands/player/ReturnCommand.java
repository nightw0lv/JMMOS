package gameserver.handlers.commands.player;

import common.Config;
import gameserver.actor.Player;
import gameserver.network.client.sendable.LocationUpdate;

/**
 * @author Pantelis Andrianakis
 * @version November 29th 2019
 */
public class ReturnCommand
{
	public static void Handle(Player player)
	{
		player.setTeleporting();
		player.setLocation(Config.STARTING_LOCATION);
		player.channelSend(new LocationUpdate(player, true));
	}
}
