package gameserver.network.client.sendable;

import common.network.SendablePacket;
import gameserver.actor.Player;

/**
 * @author Pantelis Andrianakis
 * @since November 7th 2018
 */
public class PlayerInformation extends SendablePacket
{
	public PlayerInformation(Player player)
	{
		// Expected size.
		super(108);
		
		// Packet id.
		writeShort(6);
		
		// Player information.
		writeLong(player.getObjectId());
		writeString(player.getName());
		writeByte(player.getRaceId());
		writeFloat(player.getHeight());
		writeFloat(player.getBelly());
		writeByte(player.getHairType());
		writeInt(player.getHairColor());
		writeInt(player.getSkinColor());
		writeInt(player.getEyeColor());
		writeInt(player.getInventory().getItemIdBySlot(1)); // Head
		writeInt(player.getInventory().getItemIdBySlot(2)); // Chest
		writeInt(player.getInventory().getItemIdBySlot(3)); // Legs
		writeInt(player.getInventory().getItemIdBySlot(4)); // Hands
		writeInt(player.getInventory().getItemIdBySlot(5)); // Feet
		writeInt(player.getInventory().getItemIdBySlot(6)); // Left hand
		writeInt(player.getInventory().getItemIdBySlot(7)); // Right hand
		writeFloat(player.getLocation().getX());
		writeFloat(player.getLocation().getY());
		writeFloat(player.getLocation().getZ());
		writeFloat(player.getLocation().getHeading());
		writeLong(player.getCurrentHp());
		writeLong(player.getMaxHp());
	}
}
