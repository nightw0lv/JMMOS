package gameserver.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import common.managers.DatabaseManager;
import common.managers.LogManager;
import gameserver.enums.SkillType;
import gameserver.holders.SkillHolder;
import gnu.trove.map.TLongObjectMap;
import gnu.trove.map.hash.TLongObjectHashMap;

/**
 * @author Pantelis Andrianakis
 * @since May 5th 2019
 */
public class SkillData
{
	private static final String RESTORE_SKILLS = "SELECT * FROM skills";
	private static final TLongObjectMap<SkillHolder> SKILLS = new TLongObjectHashMap<>();
	
	public static void init()
	{
		SKILLS.clear();
		
		try (Connection con = DatabaseManager.getConnection();
			PreparedStatement ps = con.prepareStatement(RESTORE_SKILLS))
		{
			try (ResultSet rset = ps.executeQuery())
			{
				while (rset.next())
				{
					final int skillId = rset.getInt("skill_id");
					final int skillLevel = rset.getInt("level");
					SKILLS.put(getSkillHashCode(skillId, skillLevel), new SkillHolder(skillId, skillLevel, Enum.valueOf(SkillType.class, rset.getString("type")), rset.getInt("reuse"), rset.getInt("range"), rset.getInt("param_1"), rset.getInt("param_2")));
				}
			}
		}
		catch (Exception e)
		{
			LogManager.log(e);
		}
		
		LogManager.log("SkillData: Loaded " + SKILLS.size() + " skills.");
	}
	
	private static long getSkillHashCode(int skillId, int skillLevel)
	{
		return (skillId * 100000) + skillLevel;
	}
	
	public static SkillHolder getSkillHolder(int skillId, int skillLevel)
	{
		return SKILLS.get(getSkillHashCode(skillId, skillLevel));
	}
}
