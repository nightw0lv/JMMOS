package gameserver.network.client.sendable;

import common.network.SendablePacket;

/**
 * @author Pantelis Andrianakis
 * @version November 7th 2018
 */
public class CharacterDeletionResult extends SendablePacket
{
	public CharacterDeletionResult()
	{
		// Expected size.
		super(4);
		
		// Packet id.
		writeShort(4);
	}
}
