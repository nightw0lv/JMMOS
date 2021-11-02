package gameserver.network.client.sendable;

import common.network.SendablePacket;

/**
 * @author Pantelis Andrianakis
 * @since November 7th 2018
 */
public class AccountAuthenticationResult extends SendablePacket
{
	public AccountAuthenticationResult(int result)
	{
		// Expected size.
		super(5);
		
		// Packet id.
		writeShort(1);
		
		// Send the data.
		writeByte(result); // 0 does not exist, 1 banned, 2 requires activation, 3 wrong password, 4 already logged, 5 too many online, 6 incorrect client, 100 authenticated
	}
}
