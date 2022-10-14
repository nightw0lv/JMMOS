package gameserver.network.client.read;

import common.network.ReadablePacket;
import gameserver.actor.Player;
import gameserver.holders.AnimationHolder;
import gameserver.managers.WorldManager;
import gameserver.network.client.GameClient;
import gameserver.network.client.write.AnimatorUpdate;

/**
 * @author Pantelis Andrianakis
 * @since February 4th 2019
 */
public class AnimatorUpdateRequest
{
	public static void process(GameClient client, ReadablePacket packet)
	{
		// Get player.
		final Player player = client.getActiveChar();
		if (player == null)
		{
			return;
		}
		
		// Read data.
		final float velocityX = packet.readFloat();
		final float velocityZ = packet.readFloat();
		final boolean triggerJump = packet.readByte() == 1;
		final boolean isInWater = packet.readByte() == 1;
		final boolean isGrounded = packet.readByte() == 1;
		
		// Set last known world object animations.
		player.setAnimations(new AnimationHolder(velocityX, velocityZ, triggerJump, isInWater, isGrounded));
		
		// Broadcast movement.
		WorldManager.broadcastPacketToVisiblePlayers(player, new AnimatorUpdate(player.getObjectId(), velocityX, velocityZ, triggerJump, isInWater, isGrounded));
	}
}
