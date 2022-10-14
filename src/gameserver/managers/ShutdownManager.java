package gameserver.managers;

import common.managers.DatabaseManager;
import common.managers.ThreadManager;
import gameserver.GameServer;
import gameserver.actor.Player;
import gameserver.network.client.GameClient;
import gameserver.network.client.sendable.Logout;

/**
 * @author Pantelis Andrianakis
 * @since September 11th 2020
 */
public class ShutdownManager extends Thread
{
	@Override
	public void run()
	{
		// Disable login.
		GameServer.ENABLE_LOGIN = false;
		
		// Logout players.
		final Logout logout = new Logout();
		for (Player player : WorldManager.getPlayers())
		{
			final GameClient client = player.getClient();
			if (client != null)
			{
				client.sendPacket(logout);
				client.disconnect();
			}
		}
		
		// Save players.
		for (Player player : WorldManager.getPlayers())
		{
			final GameClient client = player.getClient();
			if (client != null)
			{
				WorldManager.removeClient(client);
			}
		}
		
		// Close database manager.
		DatabaseManager.close();
		
		// Close thread manager.
		ThreadManager.close();
	}
}
