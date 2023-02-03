package gameserver.managers;

import common.Config;
import common.managers.LogManager;
import gameserver.actor.Player;
import gameserver.handlers.commands.admin.DeleteCommand;
import gameserver.handlers.commands.admin.SpawnCommand;
import gameserver.handlers.commands.player.LocCommand;
import gameserver.handlers.commands.player.ReturnCommand;
import gameserver.handlers.commands.player.RollCommand;
import gameserver.handlers.commands.player.TellCommand;
import gameserver.network.client.write.ChatResult;

/**
 * @author Pantelis Andrianakis
 * @since November 7th 2018
 */
public class ChatManager
{
	public static final byte CHAT_TYPE_SYSTEM = 0;
	private static final byte CHAT_TYPE_NORMAL = 1;
	private static final byte CHAT_TYPE_MESSAGE = 2;
	private static final String SYS_NAME = "System";
	private static final String MSG_TO = "To ";
	// Normal player commands
	private static final String COMMAND_PERSONAL_MESSAGE = "/tell ";
	private static final String COMMAND_LOCATION = "/loc";
	private static final String COMMAND_RETURN = "/return";
	private static final String COMMAND_ROLL = "/roll";
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
				LocCommand.handle(sender);
				return;
			}
			else if (lowercaseMessage.equals(COMMAND_RETURN))
			{
				ReturnCommand.handle(sender);
				return;
			}
			else if (lowercaseMessage.startsWith(COMMAND_ROLL))
			{
				RollCommand.handle(sender);
				return;
			}
			else if (lowercaseMessage.startsWith(COMMAND_PERSONAL_MESSAGE))
			{
				TellCommand.handle(sender, lowercaseMessage, message);
				return;
			}
			
			// Admin commands.
			// TODO: Access levels.
			if (sender.getAccessLevel() > 99)
			{
				if (lowercaseMessage.startsWith(COMMAND_SPAWN))
				{
					SpawnCommand.handle(sender, lowercaseMessage);
					return;
				}
				else if (lowercaseMessage.equals(COMMAND_DELETE))
				{
					DeleteCommand.handle(sender);
					return;
				}
			}
		}
		
		// Normal message.
		sender.sendPacket(new ChatResult(CHAT_TYPE_NORMAL, sender.getName(), message));
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
		sender.sendPacket(new ChatResult(CHAT_TYPE_MESSAGE, MSG_TO + receiver.getName(), message));
		receiver.sendPacket(new ChatResult(CHAT_TYPE_MESSAGE, sender.getName(), message));
	}
	
	public static void sendSystemMessage(Player player, String message)
	{
		player.sendPacket(new ChatResult(CHAT_TYPE_SYSTEM, SYS_NAME, message));
	}
}
