package gameserver.network.client.sendable;

import common.network.SendablePacket;

/**
 * @author Pantelis Andrianakis
 * @version November 7th 2018
 */
public class CharacterCreationResult extends SendablePacket
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
