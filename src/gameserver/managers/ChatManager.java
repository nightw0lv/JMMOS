package gameserver.managers;

import common.Config;
import common.managers.LogManager;
import gameserver.actor.Player;
import gameserver.handlers.commands.admin.DeleteCommand;
import gameserver.handlers.commands.admin.SpawnCommand;
import gameserver.handlers.commands.player.LocCommand;
import gameserver.handlers.commands.player.ReturnCommand;
import gameserver.handlers.commands.player.TellCommand;
import gameserver.network.client.sendable.ChatResult;

/**
 * @author Pantelis Andrianakis
 * @version November 7th 2018
 */
public class ChatManager
{
	private static final byte CHAT_TYPE_SYSTEM = 0;
	private static final byte CHAT_TYPE_NORMAL = 1;
	private static final byte CHAT_TYPE_MESSAGE = 2;
	private static final String SYS_NAME = "System";
	private static final String MSG_TO = "To ";
	// Normal player commands
	private static final String COMMAND_PERSONAL_MESSAGE = "/tell ";
	private static final String COMMAND_LOCATION = "/loc";
	private static final String COMMAND_RETURN = "/return";
	// Administrator commands
	private static final String COMMAND_SPAWN = "/spawn ";
	private static final String COMMAND_DELETE = "/delete";
	
	public static void handleChat(Player sender, String message)
	{
		// Check if message is empty.
		message = message.trim();
		if (message.isEmpty())
		{
			return;
		}
		
		final String lowercaseMessage = message.toLowerCase().replaceAll("\\s{2,}", " "); // Also remove all double spaces.
		
		// Commands.
		if (lowercaseMessage.startsWith("/"))
		{
			// Normal user commands.
			if (lowercaseMessage.equals(COMMAND_LOCATION))
			{
				LocCommand.Handle(sender);
				return;
			}
			else if (lowercaseMessage.equals(COMMAND_RETURN))
			{
				ReturnCommand.Handle(sender);
				return;
			}
			else if (lowercaseMessage.startsWith(COMMAND_PERSONAL_MESSAGE))
			{
				TellCommand.Handle(sender, lowercaseMessage, message);
				return;
			}
			
			// Admin commands.
			// TODO: Access levels.
			if (sender.getAccessLevel() > 99)
			{
				if (lowercaseMessage.startsWith(COMMAND_SPAWN))
				{
					SpawnCommand.Handle(sender, lowercaseMessage);
					return;
				}
				else if (lowercaseMessage.equals(COMMAND_DELETE))
				{
					DeleteCommand.Handle(sender);
					return;
				}
			}
		}
		
		// Normal message.
		sender.channelSend(new ChatResult(CHAT_TYPE_NORMAL, sender.getName(), message));
		WorldManager.broadcastPacketToVisiblePlayers(sender, new ChatResult(CHAT_TYPE_NORMAL, sender.getName(), message));
		
		// Log chat.
		if (Config.LOG_CHAT)
		{
			StringBuilder sb = new StringBuilder();
			sb.append("[");
			sb.append(sender.getName());
			sb.append("] ");
			sb.append(message);
			LogManager.logChat(sb.toString());
		}
	}
	
	public static void sendPrivateMessage(Player sender, Player receiver, String message)
	{
		sender.channelSend(new ChatResult(CHAT_TYPE_MESSAGE, MSG_TO + receiver.getName(), message));
		receiver.channelSend(new ChatResult(CHAT_TYPE_MESSAGE, sender.getName(), message));
	}
	
	public static void sendSystemMessage(Player player, String message)
	{
		player.channelSend(new ChatResult(CHAT_TYPE_SYSTEM, SYS_NAME, message));
	}
}
