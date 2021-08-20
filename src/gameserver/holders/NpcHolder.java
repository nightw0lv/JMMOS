package gameserver.holders;

import gameserver.enums.NpcType;

/**
 * @author Pantelis Andrianakis
 * @version November 28th 2019
 */
public class NpcHolder
{
	private final int _npcId;
	private final NpcType _npcType;
	private final int _level;
	private final boolean _sex;
	private final long _hp;
	private final int _stamina;
	private final int _strength;
	private final int _dexterity;
	private final int _intelect;
	
	public NpcHolder(int npcId, NpcType npcType, int level, boolean sex, long hp, int stamina, int strength, int dexterity, int intelect)
	{
		_npcId = npcId;
		_npcType = npcType;
		_level = level;
		_sex = sex;
		_hp = hp;
		_stamina = stamina;
		_strength = strength;
		_dexterity = dexterity;
		_intelect = intelect;
	}
	
	public int getNpcId()
	{
		return _npcId;
	}
	
	public NpcType getNpcType()
	{
		return _npcType;
	}
	
	public int getLevel()
	{
		return _level;
	}
	
	public boolean isFemale()
	{
		return _sex;
	}
	
	public long getHp()
	{
		return _hp;
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
}
