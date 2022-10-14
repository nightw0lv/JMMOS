package gameserver.network.client.write;

import common.network.WritablePacket;

/**
 * @author Pantelis Andrianakis
 * @since November 7th 2018
 */
public class Logout extends WritablePacket
{
	public Logout()
	{
		// Expected size.
		super(4);
		
		// Packet id.
		writeShort(9);
	}
}
