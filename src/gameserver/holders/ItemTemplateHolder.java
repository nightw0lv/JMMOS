package gameserver.holders;

import gameserver.enums.ItemSlot;
import gameserver.enums.ItemType;

/**
 * @author Pantelis Andrianakis
 * @version May 5th 2019
 */
public class ItemTemplateHolder
{
	private final int _itemId;
	private final ItemSlot _itemSlot;
	private final ItemType _itemType;
	private final boolean _stackable;
	private final boolean _tradable;
	private final int _stamina;
	private final int _strength;
	private final int _dexterity;
	private final int _intelect;
	private final SkillHolder _skillHolder;
	
	public ItemTemplateHolder(int itemId, ItemSlot itemSlot, ItemType itemType, boolean stackable, boolean tradable, int stamina, int strength, int dexterity, int intelect, SkillHolder skillHolder)
	{
		_itemId = itemId;
		_itemSlot = itemSlot;
		_itemType = itemType;
		_stackable = stackable;
		_tradable = tradable;
		_stamina = stamina;
		_strength = strength;
		_dexterity = dexterity;
		_intelect = intelect;
		_skillHolder = skillHolder;
	}
	
	public int getItemId()
	{
		return _itemId;
	}
	
	public ItemSlot getItemSlot()
	{
		return _itemSlot;
	}
	
	public ItemType getItemType()
	{
		return _itemType;
	}
	
	public boolean isStackable()
	{
		return _stackable;
	}
	
	public boolean isTradable()
	{
		return _tradable;
	}
	
	public int getSTA()
	{
		return _stamina;
	}
	
	public int getSTR()
	{
		return _strength;
	}
	
	public int getDEX()
	{
		return _dexterity;
	}
	
	public int getINT()
	{
		return _intelect;
	}
	
	public SkillHolder getSkillHolder()
	{
		return _skillHolder;
	}
}
