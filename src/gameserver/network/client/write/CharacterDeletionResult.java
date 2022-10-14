package gameserver.network.client.write;

import common.network.WritablePacket;

/**
 * @author Pantelis Andrianakis
 * @since November 7th 2018
 */
public class CharacterDeletionResult extends WritablePacket
{
	public CharacterDeletionResult()
	{
		// Expected size.
		super(4);
		
		// Packet id.
		writeShort(4);
	}
}
