package gameserver.network.client.receivable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import common.managers.DatabaseManager;
import common.managers.LogManager;
import common.network.ReceivablePacket;
import common.util.Chronos;
import gameserver.network.client.GameClient;
import gameserver.network.client.sendable.CharacterDeletionResult;

/**
 * @author Pantelis Andrianakis
 * @since November 7th 2018
 */
public class CharacterDeletionRequest
{
	private static final String ACCOUNT_CHARACTER_QUERY = "SELECT * FROM characters WHERE account=? ORDER BY slot ASC";
	private static final String CHARACTER_SLOT_UPDATE_QUERY = "UPDATE characters SET slot=?, selected=? WHERE account=? AND name=?";
	private static final String CHARACTER_DELETION_QUERY = "UPDATE characters SET name=?, access_level='-1' WHERE account=? AND name=?";
	private static final String CHARACTER_ITEM_DELETION_QUERY = "UPDATE character_items SET owner=? WHERE owner=?";
	private static final String CHARACTER_INTERFACE_DELETION_QUERY = "UPDATE character_options SET name=? WHERE name=?";
	
	public static void process(GameClient client, ReceivablePacket packet)
	{
		// Read data.
		final byte slot = (byte) packet.readByte();
		
		// Get remaining character names.
		final List<String> characterNames = new ArrayList<>();
		String deletedCharacterName = "";
		try (Connection con = DatabaseManager.getConnection();
			PreparedStatement ps = con.prepareStatement(ACCOUNT_CHARACTER_QUERY))
		{
			ps.setString(1, client.getAccountName());
			try (ResultSet rset = ps.executeQuery())
			{
				while (rset.next())
				{
					if (rset.getInt("slot") == slot)
					{
						deletedCharacterName = rset.getString("name");
					}
					else
					{
						characterNames.add(rset.getString("name"));
					}
				}
			}
		}
		catch (Exception e)
		{
			LogManager.log(e);
		}
		
		// Delete character. (Prefer to set access level to -1 and rename to name + deletion time.)
		final long deleteTime = Chronos.currentTimeMillis();
		try (Connection con = DatabaseManager.getConnection();
			PreparedStatement ps = con.prepareStatement(CHARACTER_DELETION_QUERY))
		{
			ps.setString(1, deletedCharacterName + deleteTime);
			ps.setString(2, client.getAccountName());
			ps.setString(3, deletedCharacterName);
			ps.execute();
		}
		catch (Exception e)
		{
			LogManager.log(e);
		}
		
		// Delete character items. (Same as above, change item owner to name + deletion time.)
		try (Connection con = DatabaseManager.getConnection();
			PreparedStatement ps = con.prepareStatement(CHARACTER_ITEM_DELETION_QUERY))
		{
			ps.setString(1, deletedCharacterName + deleteTime);
			ps.setString(2, deletedCharacterName);
			ps.execute();
		}
		catch (Exception e)
		{
			LogManager.log(e);
		}
		
		// Delete character interface. (Same as above, change interface name to name + deletion time.)
		try (Connection con = DatabaseManager.getConnection();
			PreparedStatement ps = con.prepareStatement(CHARACTER_INTERFACE_DELETION_QUERY))
		{
			ps.setString(1, deletedCharacterName + deleteTime);
			ps.setString(2, deletedCharacterName);
			ps.execute();
		}
		catch (Exception e)
		{
			LogManager.log(e);
		}
		
		// Order remaining character slots.
		byte counter = 0;
		for (String characterName : characterNames)
		{
			counter++;
			try (Connection con = DatabaseManager.getConnection();
				PreparedStatement ps = con.prepareStatement(CHARACTER_SLOT_UPDATE_QUERY))
			{
				ps.setByte(1, counter);
				ps.setInt(2, ((counter == 1) && (slot == 1)) || (counter == (slot - 1)) ? 1 : 0);
				ps.setString(3, client.getAccountName());
				ps.setString(4, characterName);
				ps.execute();
			}
			catch (Exception e)
			{
				LogManager.log(e);
			}
		}
		
		// Notify the client that character was deleted.
		client.sendPacket(new CharacterDeletionResult());
	}
}
