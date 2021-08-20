package gameserver.network.client.sendable;

import common.network.SendablePacket;

/**
 * @author Pantelis Andrianakis
 * @version February 4th 2019
 */
public class AnimatorUpdate extends SendablePacket
{
	public AnimatorUpdate(long objectId, float velocityX, float velocityZ, boolean triggerJump, boolean isInWater, boolean isGrounded)
	{
		// Expected size.
		super(23);
		
		// Packet id.
		writeShort(11);
		
		// Send the data.
		writeLong(objectId);
		writeFloat(velocityX);
		writeFloat(velocityZ);
		writeByte(triggerJump ? 1 : 0);
		writeByte(isInWater ? 1 : 0);
		writeByte(isGrounded ? 1 : 0);
	}
}
