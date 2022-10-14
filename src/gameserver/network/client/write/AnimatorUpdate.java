package gameserver.network.client.write;

import common.network.WritablePacket;

/**
 * @author Pantelis Andrianakis
 * @since February 4th 2019
 */
public class AnimatorUpdate extends WritablePacket
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
