package gameserver.network.client.write;

import common.network.WritablePacket;

/**
 * @author Pantelis Andrianakis
 * @since November 7th 2018
 */
public class CharacterCreationResult extends WritablePacket
{
	public CharacterCreationResult(int result)
	{
		// Expected size.
		super(5);
		
		// Packet id.
		writeShort(3);
		
		// Send the data.
		writeByte(result);
	}
}
