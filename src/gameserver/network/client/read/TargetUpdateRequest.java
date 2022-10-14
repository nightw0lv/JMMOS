package gameserver.network.client.read;

import java.util.Collection;

import common.network.ReadablePacket;
import gameserver.actor.Player;
import gameserver.actor.WorldObject;
import gameserver.managers.WorldManager;
import gameserver.network.client.GameClient;

/**
 * @author Pantelis Andrianakis
 * @since November 29th 2019
 */
public class TargetUpdateRequest
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
		long targetObjectId = packet.readLong();
		
		// Remove target.
		if (targetObjectId < 0)
		{
			player.setTarget(null);
			return;
		}
		
		// Find target WorldObject.
		final Collection<WorldObject> objects = WorldManager.getVisibleObjects(player);
		if (objects.isEmpty())
		{
			return;
		}
		
		for (WorldObject obj : objects)
		{
			if (obj.getObjectId() == targetObjectId)
			{
				player.setTarget(obj);
				return;
			}
		}
	}
}
