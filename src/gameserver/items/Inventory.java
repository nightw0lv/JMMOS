package gameserver.items;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import common.managers.DatabaseManager;
import common.managers.LogManager;
import gameserver.data.ItemData;
import gameserver.holders.ItemHolder;
import gnu.trove.TCollections;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * @author Pantelis Andrianakis
 */
public class Inventory
{
	private static final String RESTORE_INVENTORY = "SELECT * FROM character_items WHERE owner=?";
	private static final String DELETE_INVENTORY = "DELETE FROM character_items WHERE owner=?";
	private static final String STORE_ITEM = "INSERT INTO character_items (owner, slot_id, item_id, quantity, enchant) values (?, ?, ?, ?, ?)";
	
	private final TIntObjectMap<ItemHolder> _items = TCollections.synchronizedMap(new TIntObjectHashMap<ItemHolder>());
	
	public Inventory(String ownerName)
	{
		// Restore information from database.
		try (Connection con = DatabaseManager.getConnection();
			PreparedStatement ps = con.prepareStatement(RESTORE_INVENTORY))
		{
			ps.setString(1, ownerName);
			try (ResultSet rset = ps.executeQuery())
			{
				while (rset.next())
				{
					final ItemHolder itemHolder = new ItemHolder(ItemData.getItemTemplate(rset.getInt("item_id")));
					itemHolder.setQuantity(rset.getInt("quantity"));
					itemHolder.setEnchant(rset.getInt("enchant"));
					_items.put(rset.getInt("slot_id"), itemHolder);
				}
			}
		}
		catch (Exception e)
		{
			LogManager.log(e);
		}
	}
	
	/**
	 * Only used when player exits the game.
	 * @param ownerName
	 */
	public void store(String ownerName)
	{
		// Delete old records.
		try (Connection con = DatabaseManager.getConnection();
			PreparedStatement ps = con.prepareStatement(DELETE_INVENTORY))
		{
			ps.setString(1, ownerName);
			ps.execute();
		}
		catch (Exception e)
		{
			LogManager.log(e);
		}
		
		// No need to store if item list is empty.
		if (_items.isEmpty())
		{
			return;
		}
		
		// Store new records.
		try (Connection con = DatabaseManager.getConnection();
			PreparedStatement ps = con.prepareStatement(STORE_ITEM))
		{
			for (int slot : _items.keys())
			{
				ps.setString(1, ownerName);
				ps.setInt(2, slot);
				ps.setInt(3, _items.get(slot).getTemplate().getItemId());
				ps.setInt(4, _items.get(slot).getQuantity());
				ps.setInt(5, _items.get(slot).getEnchant());
				ps.addBatch();
			}
			ps.executeBatch();
		}
		catch (Exception e)
		{
			LogManager.log(e);
		}
		
		// Clear item list.
		_items.clear();
	}
	
	public int getItemIdBySlot(int slotId)
	{
		if (!_items.containsKey(slotId))
		{
			return 0;
		}
		return _items.get(slotId).getTemplate().getItemId();
	}
	
	public ItemHolder getSlot(int slotId)
	{
		return _items.get(slotId);
	}
	
	public void setSlot(int slotId, int itemId)
	{
		_items.put(slotId, new ItemHolder(ItemData.getItemTemplate(itemId)));
	}
	
	public void removeSlot(int slotId)
	{
		_items.remove(slotId);
	}
	
	/**
	 * @return a Map that contains all (slotId, itemId) information.
	 */
	public TIntObjectMap<ItemHolder> getItems()
	{
		return _items;
	}
}
