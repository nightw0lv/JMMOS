package gameserver.network.client.receivable;

import common.network.ReceivablePacket;
import gameserver.network.client.GameClient;
import gameserver.network.client.sendable.CharacterSelectionInfoResult;

/**
 * @author Pantelis Andrianakis
 * @version November 7th 2018
 */
public class CharacterSelectionInfoRequest
{
	public CharacterSelectionInfoRequest(GameClient client, ReceivablePacket packet)
	{
		// Read data.
		final String accountName = packet.readString().toLowerCase();
		
		// If account has logged send the information.
		if (client.getAccountName().equals(accountName))
		{
			client.channelSend(new CharacterSelectionInfoResult(accountName));
		}
	}
}
