package gameserver.network.client.read;

import java.util.Collection;

import common.managers.ThreadManager;
import common.network.ReadablePacket;
import gameserver.actor.Player;
import gameserver.actor.WorldObject;
import gameserver.holders.AnimationHolder;
import gameserver.managers.WorldManager;
import gameserver.network.client.GameClient;
import gameserver.network.client.write.AnimatorUpdate;
import gameserver.network.client.write.NpcInformation;
import gameserver.network.client.write.PlayerInformation;

/**
 * @author Pantelis Andrianakis
 * @since November 7th 2018
 */
public class ObjectInfoRequest
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
		final long objectId = packet.readLong();
		
		// Send the information.
		final Collection<WorldObject> objects = WorldManager.getVisibleObjects(player);
		if (objects.isEmpty())
		{
			return;
		}
		
		for (WorldObject obj : objects)
		{
			if (obj.getObjectId() == objectId)
			{
				if (obj.isPlayer())
				{
					client.sendPacket(new PlayerInformation(obj.asPlayer()));
				}
				else if (obj.isNpc())
				{
					client.sendPacket(new NpcInformation(obj.asNpc()));
				}
				
				// Send delayed animation update in case object was already moving.
				ThreadManager.schedule(new SendAnimationInfo(client, obj), 1000);
				break;
			}
		}
	}
	
	private static class SendAnimationInfo implements Runnable
	{
		private final GameClient _client;
		private final WorldObject _obj;
		
		public SendAnimationInfo(GameClient client, WorldObject obj)
		{
			_client = client;
			_obj = obj;
		}
		
		@Override
		public void run()
		{
			if (_obj != null)
			{
				final AnimationHolder animations = _obj.getAnimations();
				if (animations != null)
				{
					_client.sendPacket(new AnimatorUpdate(_obj.getObjectId(), animations.getVelocityX(), animations.getVelocityZ(), animations.isTriggerJump(), animations.isInWater(), animations.isGrounded()));
				}
			}
		}
	}
}
