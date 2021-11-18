package gameserver.network.client.receivable;

import java.util.List;

import common.managers.ThreadManager;
import common.network.ReceivablePacket;
import gameserver.actor.Player;
import gameserver.actor.WorldObject;
import gameserver.managers.WorldManager;
import gameserver.network.client.GameClient;
import gameserver.network.client.sendable.NpcInformation;
import gameserver.network.client.sendable.PlayerInformation;
import gameserver.network.client.sendable.PlayerInventoryUpdate;
import gameserver.network.client.sendable.PlayerOptionsInformation;

/**
 * @author Pantelis Andrianakis
 * @since November 7th 2018
 */
public class EnterWorldRequest
{
	public static void process(GameClient client, ReceivablePacket packet)
	{
		// Read data.
		final String characterName = packet.readString();
		
		// Create a new PlayerInstance.
		final Player player = new Player(client, characterName);
		// Add object to the world.
		WorldManager.addObject(player);
		// Assign this player to client.
		client.setActiveChar(player);
		
		// Send user interface information to client.
		client.sendPacket(new PlayerOptionsInformation(player));
		
		// Send all inventory items to client.
		client.sendPacket(new PlayerInventoryUpdate(player));
		
		// Use a task to send and receive nearby player information,
		// because we need to have player initialization be complete in client side.
		ThreadManager.schedule(new BroadcastAndReceiveInfo(player), 1000);
	}
	
	private static class BroadcastAndReceiveInfo implements Runnable
	{
		private final Player _player;
		
		public BroadcastAndReceiveInfo(Player player)
		{
			_player = player;
		}
		
		@Override
		public void run()
		{
			// Send and receive visible object information.
			final PlayerInformation playerInfo = new PlayerInformation(_player);
			final List<Player> players = WorldManager.getVisiblePlayers(_player);
			for (int i = 0; i < players.size(); i++)
			{
				final Player nearby = players.get(i);
				// Send the information to the current player.
				_player.sendPacket(new PlayerInformation(nearby));
				// Send information to the other player as well.
				nearby.sendPacket(playerInfo);
			}
			
			// Send nearby NPC information.
			final List<WorldObject> objects = WorldManager.getVisibleObjects(_player);
			for (int i = 0; i < objects.size(); i++)
			{
				final WorldObject nearby = objects.get(i);
				if (!nearby.isNpc())
				{
					continue;
				}
				
				_player.sendPacket(new NpcInformation(nearby.asNpc()));
			}
		}
	}
}
