package gameserver.network.client.sendable;

import common.network.SendablePacket;
import gameserver.actor.WorldObject;

/**
 * @author Pantelis Andrianakis
 * @version November 7th 2018
 */
public class LocationUpdate extends SendablePacket
{
	private final WorldObject _obj;
	private final boolean _teleport;
	
	public LocationUpdate(WorldObject obj)
	{
		// Expected size.
		super(28);
		
		_obj = obj;
		_teleport = false;
		writeData();
	}
	
	// Only used for client active player teleports.
	public LocationUpdate(WorldObject obj, boolean teleport)
	{
		// Expected size.
		super(28);
		
		_obj = obj;
		_teleport = teleport;
		writeData();
	}
	
	private void writeData()
	{
		// Packet id.
		writeShort(10);
		
		writeLong(_teleport ? 0 : _obj.getObjectId());
		writeFloat(_obj.getLocation().getX());
		writeFloat(_obj.getLocation().getY());
		writeFloat(_obj.getLocation().getZ());
		writeFloat(_obj.getLocation().getHeading());
	}
}
