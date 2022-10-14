package gameserver.network.client.write;

import common.network.WritablePacket;
import gameserver.actor.WorldObject;

/**
 * @author Pantelis Andrianakis
 */
public class DeleteObject extends WritablePacket
{
	public DeleteObject(WorldObject object)
	{
		// Expected size.
		super(14);
		
		// Packet id.
		writeShort(8);
		
		// Send the data.
		writeLong(object.getObjectId()); // ID of object to delete.
	}
}
