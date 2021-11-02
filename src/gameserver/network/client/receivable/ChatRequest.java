package gameserver.network.client.receivable;

import common.network.ReceivablePacket;
import gameserver.actor.Player;
import gameserver.managers.ChatManager;
import gameserver.network.client.GameClient;

/**
 * @author Pantelis Andrianakis
 * @since November 7th 2018
 */
public class ChatRequest
{
	public static void process(GameClient client, ReceivablePacket packet)
	{
		// Get player.
		final Player player = client.getActiveChar();
		if (player == null)
		{
			return;
		}
		
		// Read data.
		final String message = packet.readString();
		
		// Handle message.
		ChatManager.handleChat(player, message);
	}
}
