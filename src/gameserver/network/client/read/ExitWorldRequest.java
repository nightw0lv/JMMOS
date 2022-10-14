package gameserver.network.client.read;

import common.network.ReadablePacket;
import gameserver.managers.WorldManager;
import gameserver.network.client.GameClient;

/**
 * @author Pantelis Andrianakis
 * @since February 3rd 2019
 */
public class ExitWorldRequest
{
	public static void process(GameClient client, ReadablePacket packet)
	{
		WorldManager.removeObject(client.getActiveChar());
		client.setActiveChar(null);
	}
}
