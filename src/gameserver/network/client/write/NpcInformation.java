package gameserver.network.client.write;

import common.network.WritablePacket;
import gameserver.actor.Npc;

/**
 * @author Pantelis Andrianakis
 * @since November 28th 2019
 */
public class NpcInformation extends WritablePacket
{
	public NpcInformation(Npc npc)
	{
		// Expected size.
		super(40);
		
		// Packet id.
		writeShort(7);
		
		// NPC information.
		writeLong(npc.getObjectId());
		writeInt(npc.getNpcHolder().getNpcId());
		writeFloat(npc.getLocation().getX());
		writeFloat(npc.getLocation().getY());
		writeFloat(npc.getLocation().getZ());
		writeFloat(npc.getLocation().getHeading());
		writeLong(npc.getCurrentHp());
	}
}
