package gameserver.network.client.sendable;

import common.network.SendablePacket;

/**
 * @author Pantelis Andrianakis
 * @version November 7th 2018
 */
public class ChatResult extends SendablePacket
{
	public ChatResult(byte chatType, String sender, String message)
	{
		// Expected size.
		super(32);
		
		// Packet id.
		writeShort(12);
		
		// Send the data.
		writeByte(chatType); // 0 system, 1 normal chat, 2 personal message
		writeString(sender);
		writeString(message);
	}
}
