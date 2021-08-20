package gameserver.network.client.receivable;

import java.util.List;

import common.network.ReceivablePacket;
import gameserver.actor.Player;
import gameserver.actor.WorldObject;
import gameserver.managers.WorldManager;
import gameserver.network.client.GameClient;

/**
 * @author Pantelis Andrianakis
 * @version November 29th 2019
 */
public class TargetUpdateRequest
{
	public TargetUpdateRequest(GameClient client, ReceivablePacket packet)
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
		final List<WorldObject> objects = WorldManager.getVisibleObjects(player);
		for (int i = 0; i < objects.size(); i++)
		{
			final WorldObject obj = objects.get(i);
			if ((obj != null) && (obj.getObjectId() == targetObjectId))
			{
				player.setTarget(obj);
				return;
			}
		}
	}
}
