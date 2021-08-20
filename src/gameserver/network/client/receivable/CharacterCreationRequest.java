package gameserver.network.client.receivable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import common.Config;
import common.managers.DatabaseManager;
import common.managers.LogManager;
import common.network.ReceivablePacket;
import common.util.Util;
import gameserver.data.ItemData;
import gameserver.enums.ItemSlot;
import gameserver.enums.ItemType;
import gameserver.holders.ItemTemplateHolder;
import gameserver.network.client.GameClient;
import gameserver.network.client.sendable.CharacterCreationResult;

/**
 * @author Pantelis Andrianakis
 * @version November 7th 2018
 */
public class CharacterCreationRequest
{
	private static final String ACCOUNT_CHARACTER_QUERY = "SELECT * FROM characters WHERE access_level>'-1' AND account=?";
	private static final String NAME_EXISTS_QUERY = "SELECT * FROM characters WHERE name=?";
	private static final String CHARACTER_SELECTED_RESET_QUERY = "UPDATE characters SET selected=0 WHERE account=?";
	private static final String CHARACTER_CREATE_QUERY = "INSERT INTO characters (account, name, slot, selected, race, height, belly, hair_type, hair_color, skin_color, eye_color, x, y, z, heading, experience, hp, mp) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String CHARACTER_CREATE_OPTIONS_QUERY = "INSERT INTO character_options (name) values (?)";
	private static final String CHARACTER_ITEM_START = "INSERT INTO character_items VALUES ";
	
	private static final int INVALID_NAME = 0;
	private static final int NAME_IS_TOO_SHORT = 1;
	private static final int NAME_ALREADY_EXISTS = 2;
	private static final int CANNOT_CREATE_ADDITIONAL_CHARACTERS = 3;
	private static final int INVALID_PARAMETERS = 4;
	private static final int SUCCESS = 100;
	
	public CharacterCreationRequest(GameClient client, ReceivablePacket packet)
	{
		// Make sure player has authenticated.
		if ((client.getAccountName() == null) || (client.getAccountName().length() == 0))
		{
			return;
		}
		
		// Read data.
		String characterName = packet.readString();
		int race = packet.readByte();
		float height = packet.readFloat();
		float belly = packet.readFloat();
		int hairType = packet.readByte();
		int hairColor = packet.readInt();
		int skinColor = packet.readInt();
		int eyeColor = packet.readInt();
		
		// Replace illegal characters.
		for (char c : Util.ILLEGAL_CHARACTERS)
		{
			characterName = characterName.replace(c, '\'');
		}
		
		// Name character checks.
		if (characterName.contains("'"))
		{
			client.channelSend(new CharacterCreationResult(INVALID_NAME));
			return;
		}
		if ((characterName.length() < 2) || (characterName.length() > 12)) // 12 should not happen, checking it here in case of client cheat.
		{
			client.channelSend(new CharacterCreationResult(NAME_IS_TOO_SHORT));
			return;
		}
		
		// Visual exploit checks.
		if (((race < 0) || (race > 1)) //
			|| ((height < 0.39) || (height > 0.61)) //
			|| ((hairType < 0) || (hairType > 3)) //
		/* || (!Config.VALID_SKIN_COLORS.contains(skinColor)) */) // TODO: Check palette.
		{
			client.channelSend(new CharacterCreationResult(INVALID_PARAMETERS));
			return;
		}
		
		// Account character count database check.
		int characterCount = 0;
		int lastCharacterSlot = 0;
		try (Connection con = DatabaseManager.getConnection();
			PreparedStatement ps = con.prepareStatement(ACCOUNT_CHARACTER_QUERY))
		{
			ps.setString(1, client.getAccountName());
			try (ResultSet rset = ps.executeQuery())
			{
				while (rset.next())
				{
					characterCount++;
					final int slot = rset.getInt("slot");
					if (slot > lastCharacterSlot)
					{
						lastCharacterSlot = slot;
					}
				}
			}
		}
		catch (Exception e)
		{
			LogManager.log(e);
		}
		if (characterCount >= Config.ACCOUNT_MAX_CHARACTERS)
		{
			client.channelSend(new CharacterCreationResult(CANNOT_CREATE_ADDITIONAL_CHARACTERS));
			return;
		}
		
		// Check database if name exists.
		boolean characterExists = false;
		try (Connection con = DatabaseManager.getConnection();
			PreparedStatement ps = con.prepareStatement(NAME_EXISTS_QUERY))
		{
			ps.setString(1, characterName);
			try (ResultSet rset = ps.executeQuery())
			{
				while (rset.next())
				{
					characterExists = true;
				}
			}
		}
		catch (Exception e)
		{
			LogManager.log(e);
		}
		if (characterExists)
		{
			client.channelSend(new CharacterCreationResult(NAME_ALREADY_EXISTS));
			return;
		}
		
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
		
		// Create character.
		try (Connection con = DatabaseManager.getConnection();
			PreparedStatement ps = con.prepareStatement(CHARACTER_CREATE_QUERY))
		{
			ps.setString(1, client.getAccountName());
			ps.setString(2, characterName);
			ps.setInt(3, lastCharacterSlot + 1);
			ps.setInt(4, 1); // Selected character.
			ps.setInt(5, race);
			ps.setFloat(6, height);
			ps.setFloat(7, belly);
			ps.setInt(8, hairType);
			ps.setInt(9, hairColor);
			ps.setInt(10, skinColor);
			ps.setInt(11, eyeColor);
			ps.setFloat(12, Config.STARTING_LOCATION.getX());
			ps.setFloat(13, Config.STARTING_LOCATION.getY());
			ps.setFloat(14, Config.STARTING_LOCATION.getZ());
			ps.setFloat(15, Config.STARTING_LOCATION.getHeading());
			ps.setLong(16, 0); // TODO: Starting level experience.
			ps.setLong(17, 100); // TODO: Implement Player level data.
			ps.setLong(18, 100); // TODO: Implement Player level data.
			ps.execute();
		}
		catch (Exception e)
		{
			LogManager.log(e);
		}
		
		// Create a character_options entry for this character.
		try (Connection con = DatabaseManager.getConnection();
			PreparedStatement ps = con.prepareStatement(CHARACTER_CREATE_OPTIONS_QUERY))
		{
			ps.setString(1, characterName);
			ps.execute();
		}
		catch (Exception e)
		{
			LogManager.log(e);
		}
		
		// Add starting items.
		int itemCount = Config.STARTING_ITEMS.length;
		if (itemCount > 0)
		{
			// Prepare query.
			final StringBuilder query = new StringBuilder(CHARACTER_ITEM_START);
			final List<ItemSlot> usedEquipableSlots = new ArrayList<>();
			int inventorySlotCounter = 8; // First inventory item slot.
			for (int itemId : Config.STARTING_ITEMS)
			{
				query.append("('");
				query.append(characterName);
				query.append("',");
				final ItemTemplateHolder itemHolder = ItemData.getItemTemplate(itemId);
				final ItemSlot itemSlot = itemHolder.getItemSlot();
				if ((itemHolder.getItemType() == ItemType.EQUIP) && !usedEquipableSlots.contains(itemSlot))
				{
					usedEquipableSlots.add(itemSlot);
					query.append(itemHolder.getItemSlot().ordinal());
				}
				else
				{
					query.append(inventorySlotCounter++);
				}
				query.append(",");
				query.append(itemId);
				query.append(",1,0"); // quantity, enchant
				query.append(")");
				query.append(itemCount-- == 1 ? ";" : ",");
			}
			// Store new item records.
			try (Connection con = DatabaseManager.getConnection();
				PreparedStatement ps = con.prepareStatement(query.toString()))
			{
				ps.execute();
			}
			catch (Exception e)
			{
				LogManager.log(e);
			}
		}
		
		// Send success result.
		client.channelSend(new CharacterCreationResult(SUCCESS));
	}
}
