package gameserver.handlers.commands.player;

import common.Config;
import common.managers.LogManager;
import gameserver.actor.Player;
import gameserver.managers.ChatManager;
import gameserver.managers.WorldManager;
import gameserver.network.client.write.ChatResult;
import gameserver.util.Rnd;

/**
 * @author Pantelis Andrianakis
 * @since February 3rd 2023
 */
public class RollCommand
{
	public static void handle(Player player)
	{
		// Generate the roll message.
		StringBuilder sb = new StringBuilder();
		sb.append(player.getName());
		sb.append(" rolls ");
		sb.append(Rnd.get(1, 100));
		sb.append(" (1-100)");
		
		// Send the ChatResult to player and other nearby visible players.
		final ChatResult chatResult = new ChatResult(ChatManager.CHAT_TYPE_SYSTEM, "System", sb.toString());
		player.sendPacket(chatResult);
		WorldManager.broadcastPacketToVisiblePlayers(player, chatResult);
		
		// Log chat.
		if (Config.LOG_CHAT)
		{
			LogManager.logChat(sb.toString());
		}
	}
}
