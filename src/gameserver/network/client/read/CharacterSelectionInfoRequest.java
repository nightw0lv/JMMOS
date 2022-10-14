package gameserver.network.client.read;

import common.network.ReadablePacket;
import gameserver.network.client.GameClient;
import gameserver.network.client.write.CharacterSelectionInfoResult;

/**
 * @author Pantelis Andrianakis
 * @since November 7th 2018
 */
public class CharacterSelectionInfoRequest
{
	public static void process(GameClient client, ReadablePacket packet)
	{
		// Read data.
		final String accountName = packet.readString().toLowerCase();
		
		// If account has logged send the information.
		if (client.getAccountName().equals(accountName))
		{
			client.sendPacket(new CharacterSelectionInfoResult(accountName));
		}
	}
}
