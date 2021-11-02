package gameserver.network.client.receivable;

import common.network.ReceivablePacket;
import gameserver.managers.WorldManager;
import gameserver.network.client.GameClient;

/**
 * @author Pantelis Andrianakis
 * @since February 3rd 2019
 */
public class ExitWorldRequest
{
	public static void process(GameClient client, ReceivablePacket packet)
	{
		WorldManager.removeObject(client.getActiveChar());
		client.setActiveChar(null);
	}
}
