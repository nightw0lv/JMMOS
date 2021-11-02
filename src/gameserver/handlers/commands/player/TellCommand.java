package gameserver.handlers.commands.player;

import common.Config;
import common.managers.LogManager;
import gameserver.actor.Player;
import gameserver.managers.ChatManager;
import gameserver.managers.WorldManager;

/**
 * @author Pantelis Andrianakis
 * @since November 29th 2019
 */
public class TellCommand
{
	public static void handle(Player sender, String lowercaseMessage, String message)
	{
		final String[] lowercaseMessageSplit = lowercaseMessage.split(" ");
		if (lowercaseMessageSplit.length < 3) // Check for parameters.
		{
			ChatManager.sendSystemMessage(sender, "Incorrect syntax. Use /tell [name] [message].");
			return;
		}
		
		final Player receiver = WorldManager.getPlayerByName(lowercaseMessageSplit[1]);
		if (receiver == null)
		{
			ChatManager.sendSystemMessage(sender, "Player was not found.");
		}
		else
		{
			// Step by step cleanup, to avoid problems with extra/double spaces on original message.
			message = message.substring(lowercaseMessageSplit[0].length(), message.length()).trim(); // Remove command.
			message = message.substring(lowercaseMessageSplit[1].length(), message.length()).trim(); // Remove receiver name.
			// Send message.
			ChatManager.sendPrivateMessage(sender, receiver, message);
			// Log chat.
			if (Config.LOG_CHAT)
			{
				StringBuilder sb = new StringBuilder();
				sb.append("[");
				sb.append(sender.getName());
				sb.append("] to [");
				sb.append(receiver.getName());
				sb.append("] ");
				sb.append(message);
				LogManager.logChat(sb.toString());
			}
		}
	}
}
