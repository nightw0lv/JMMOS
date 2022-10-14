package gameserver.handlers.commands.player;

import common.Config;
import gameserver.actor.Player;
import gameserver.network.client.write.LocationUpdate;

/**
 * @author Pantelis Andrianakis
 * @since November 29th 2019
 */
public class ReturnCommand
{
	public static void handle(Player player)
	{
		player.setTeleporting();
		player.setLocation(Config.STARTING_LOCATION);
		player.sendPacket(new LocationUpdate(player, true));
	}
}
