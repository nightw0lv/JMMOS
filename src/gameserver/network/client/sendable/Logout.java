package gameserver.network.client.sendable;

import common.network.SendablePacket;

/**
 * @author Pantelis Andrianakis
 * @version November 7th 2018
 */
public class Logout extends SendablePacket
{
	public Logout()
	{
		// Expected size.
		super(4);
		
		// Packet id.
		writeShort(9);
	}
}
