package gameserver.network.client.receivable;

import java.sql.Connection;
import java.sql.PreparedStatement;

import common.managers.DatabaseManager;
import common.managers.LogManager;
import common.network.ReceivablePacket;
import gameserver.network.client.GameClient;

/**
 * @author Pantelis Andrianakis
 * @since November 7th 2018
 */
public class CharacterSelectUpdate
{
	private static final String CHARACTER_SELECTED_RESET_QUERY = "UPDATE characters SET selected=0 WHERE account=?";
	private static final String CHARACTER_SELECTED_UPDATE_QUERY = "UPDATE characters SET selected=1 WHERE account=? AND slot=?";
	
	public static void process(GameClient client, ReceivablePacket packet)
	{
		// Read data.
		final int slot = packet.readByte();
		
		// Make existing characters selected value false.
		try (Connection con = DatabaseManager.getConnection();
			PreparedStatement ps = con.prepareStatement(CHARACTER_SELECTED_RESET_QUERY))
		{
			ps.setString(1, client.getAccountName());
			ps.execute();
		}
		catch (Exception e)
		{
			LogManager.log(e);
		}
		
		// Set character selected.
		try (Connection con = DatabaseManager.getConnection();
			PreparedStatement ps = con.prepareStatement(CHARACTER_SELECTED_UPDATE_QUERY))
		{
			ps.setString(1, client.getAccountName());
			ps.setInt(2, slot);
			ps.execute();
		}
		catch (Exception e)
		{
			LogManager.log(e);
		}
	}
}
