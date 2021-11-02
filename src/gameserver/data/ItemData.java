package gameserver.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import common.managers.DatabaseManager;
import common.managers.LogManager;
import gameserver.enums.ItemSlot;
import gameserver.enums.ItemType;
import gameserver.holders.ItemTemplateHolder;
import gameserver.holders.SkillHolder;
import gnu.trove.map.TLongObjectMap;
import gnu.trove.map.hash.TLongObjectHashMap;

/**
 * @author Pantelis Andrianakis
 * @since May 5th 2019
 */
public class ItemData
{
	private static final String RESTORE_ITEMS = "SELECT * FROM items";
	private static final TLongObjectMap<ItemTemplateHolder> ITEMS = new TLongObjectHashMap<>();
	
	public static void init()
	{
		ITEMS.clear();
		
		try (Connection con = DatabaseManager.getConnection();
			PreparedStatement ps = con.prepareStatement(RESTORE_ITEMS))
		{
			try (ResultSet rset = ps.executeQuery())
			{
				while (rset.next())
				{
					final int itemId = rset.getInt("item_id");
					final int skillId = rset.getInt("skill_id");
					final int skillLevel = rset.getInt("skill_level");
					final SkillHolder skillHolder = SkillData.getSkillHolder(skillId, skillLevel);
					if ((skillId > 0) && (skillHolder == null))
					{
						LogManager.log("ItemData: Could not find skill with id " + skillId + " and level " + skillLevel + " for item " + itemId + ".");
					}
					else
					{
						ITEMS.put(itemId, new ItemTemplateHolder(itemId, Enum.valueOf(ItemSlot.class, rset.getString("slot")), Enum.valueOf(ItemType.class, rset.getString("type")), rset.getBoolean("stackable"), rset.getBoolean("tradable"), rset.getInt("stamina"), rset.getInt("strength"), rset.getInt("dexterity"), rset.getInt("intelect"), skillHolder));
					}
				}
			}
		}
		catch (Exception e)
		{
			LogManager.log(e);
		}
		
		LogManager.log("ItemData: Loaded " + ITEMS.size() + " items.");
	}
	
	public static ItemTemplateHolder getItemTemplate(int itemId)
	{
		return ITEMS.get(itemId);
	}
}
