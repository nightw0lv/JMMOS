package gameserver.holders;

/**
 * @author Pantelis Andrianakis
 * @since March 12th 2020
 */
public class ItemHolder
{
	private final ItemTemplateHolder _itemTemplate;
	private int _quantity = 1;
	private int _enchant = 0;
	
	public ItemHolder(ItemTemplateHolder itemTemplate)
	{
		_itemTemplate = itemTemplate;
	}
	
	public ItemTemplateHolder getTemplate()
	{
		return _itemTemplate;
	}
	
	public void setQuantity(int quantity)
	{
		_quantity = quantity;
	}
	
	public int getQuantity()
	{
		return _quantity;
	}
	
	public void setEnchant(int enchant)
	{
		_enchant = enchant;
	}
	
	public int getEnchant()
	{
		return _enchant;
	}
}
