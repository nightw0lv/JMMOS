package gameserver.network.client.read;

import java.sql.Connection;
import java.sql.PreparedStatement;

import common.managers.DatabaseManager;
import common.managers.LogManager;
import common.network.ReadablePacket;
import gameserver.network.client.GameClient;

/**
 * @author Pantelis Andrianakis
 * @since November 7th 2018
 */
public class CharacterSlotUpdate
{
	private static final String CHARACTER_SLOT_UPDATE_QUERY = "UPDATE characters SET slot=? WHERE account=? AND slot=?";
	
	public static void process(GameClient client, ReadablePacket packet)
	{
		// Read data.
		final byte oldSlot = (byte) packet.readByte();
		final byte newSlot = (byte) packet.readByte();
		
		// Database queries.
		try (Connection con = DatabaseManager.getConnection();
			PreparedStatement ps = con.prepareStatement(CHARACTER_SLOT_UPDATE_QUERY))
		{
			ps.setInt(1, 0);
			ps.setString(2, client.getAccountName());
			ps.setInt(3, oldSlot);
			ps.execute();
		}
		catch (Exception e)
		{
			LogManager.log(e);
		}
		
		try (Connection con = DatabaseManager.getConnection();
			PreparedStatement ps = con.prepareStatement(CHARACTER_SLOT_UPDATE_QUERY))
		{
			ps.setInt(1, oldSlot);
			ps.setString(2, client.getAccountName());
			ps.setInt(3, newSlot);
			ps.execute();
		}
		catch (Exception e)
		{
			LogManager.log(e);
		}
		
		try (Connection con = DatabaseManager.getConnection();
			PreparedStatement ps = con.prepareStatement(CHARACTER_SLOT_UPDATE_QUERY))
		{
			ps.setInt(1, newSlot);
			ps.setString(2, client.getAccountName());
			ps.setInt(3, 0);
			ps.execute();
		}
		catch (Exception e)
		{
			LogManager.log(e);
		}
	}
}
