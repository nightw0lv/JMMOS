package gameserver.holders;

import gameserver.enums.SkillType;

/**
 * @author Pantelis Andrianakis
 * @since May 5th 2019
 */
public class SkillHolder
{
	private final int _skillId;
	private final int _level;
	private final SkillType _skillType;
	private final int _reuse;
	private final int _range;
	private final int _param1;
	private final int _param2;
	
	public SkillHolder(int skillId, int level, SkillType skillType, int reuse, int range, int param1, int param2)
	{
		_skillId = skillId;
		_level = level;
		_skillType = skillType;
		_reuse = reuse;
		_range = range;
		_param1 = param1;
		_param2 = param2;
	}
	
	public int getSkillId()
	{
		return _skillId;
	}
	
	public int getLevel()
	{
		return _level;
	}
	
	public SkillType getSkillType()
	{
		return _skillType;
	}
	
	public int getReuse()
	{
		return _reuse;
	}
	
	public int getRange()
	{
		return _range;
	}
	
	public int getParam1()
	{
		return _param1;
	}
	
	public int getParam2()
	{
		return _param2;
	}
}
