package gameserver.network.client.receivable;

import common.Config;
import common.network.ReceivablePacket;
import gameserver.actor.Player;
import gameserver.managers.WorldManager;
import gameserver.network.client.GameClient;
import gameserver.network.client.sendable.LocationUpdate;
import gameserver.network.client.sendable.Logout;

/**
 * @author Pantelis Andrianakis
 * @version November 7th 2018
 */
public class LocationUpdateRequest
{
	private final static int MAX_MOVE_DISTANCE = 300;
	
	public LocationUpdateRequest(GameClient client, ReceivablePacket packet)
	{
		// Get player.
		final Player player = client.getActiveChar();
		if (player == null)
		{
			return;
		}
		
		// Read data.
		final float posX = packet.readFloat();
		final float posY = packet.readFloat();
		final float posZ = packet.readFloat();
		final float heading = packet.readFloat();
		
		// Check if player is outside of world bounds.
		// if ((posX < Config.WORLD_MINIMUM_X) || (posX > Config.WORLD_MAXIMUM_X) || (posY < Config.WORLD_MINIMUM_Y) || (posY > Config.WORLD_MAXIMUM_Y) || (posZ < Config.WORLD_MINIMUM_Z) || (posZ > Config.WORLD_MAXIMUM_Z))
		// {
		// player.setLocation(Config.STARTING_LOCATION);
		// client.channelSend(new Logout());
		// return;
		// }
		
		// Check if player moved too far away via probable exploit.
		if (!player.isTeleporting() && (player.calculateDistance(posX, posY, posZ) > MAX_MOVE_DISTANCE))
		{
			player.setLocation(Config.STARTING_LOCATION);
			client.channelSend(new Logout());
			return;
		}
		
		// Update player location.
		player.setLocation(posX, posY, posZ, heading);
		
		// Broadcast movement.
		WorldManager.broadcastPacketToVisiblePlayers(player, new LocationUpdate(player));
	}
}
