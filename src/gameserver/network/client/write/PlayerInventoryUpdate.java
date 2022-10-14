package gameserver.network.client.write;

import common.network.WritablePacket;
import gameserver.actor.Player;
import gameserver.holders.ItemHolder;
import gnu.trove.map.TIntObjectMap;

/**
 * @author Pantelis Andrianakis
 * @since March 12th 2020
 */
public class PlayerInventoryUpdate extends WritablePacket
{
	public PlayerInventoryUpdate(Player player)
	{
		// Expected size.
		super(32);
		
		// Packet id.
		writeShort(13);
		
		// Get the item list.
		final TIntObjectMap<ItemHolder> items = player.getInventory().getItems();
		
		// Write information.
		writeInt(items.size());
		for (int slot : items.keys())
		{
			final ItemHolder item = items.get(slot);
			writeInt(item.getTemplate().getItemId()); // Item id.
			writeInt(slot); // Slot.
			writeInt(item.getQuantity()); // Quantity.
			writeInt(item.getEnchant()); // Enchant.
		}
	}
}
