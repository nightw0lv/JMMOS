package gameserver.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import common.managers.DatabaseManager;
import common.managers.LogManager;
import gameserver.enums.NpcType;
import gameserver.holders.NpcHolder;
import gnu.trove.map.TLongObjectMap;
import gnu.trove.map.hash.TLongObjectHashMap;

/**
 * @author Pantelis Andrianakis
 * @since November 28th 2019
 */
public class NpcData
{
	private static final String RESTORE_NPCS = "SELECT * FROM npcs";
	private static final TLongObjectMap<NpcHolder> NPCS = new TLongObjectHashMap<>();
	
	public static void init()
	{
		NPCS.clear();
		
		try (Connection con = DatabaseManager.getConnection();
			PreparedStatement ps = con.prepareStatement(RESTORE_NPCS))
		{
			try (ResultSet rset = ps.executeQuery())
			{
				while (rset.next())
				{
					final int npcId = rset.getInt("npc_id");
					NPCS.put(npcId, new NpcHolder(npcId, Enum.valueOf(NpcType.class, rset.getString("type")), rset.getInt("level"), rset.getBoolean("sex"), rset.getLong("hp"), rset.getInt("stamina"), rset.getInt("strength"), rset.getInt("dexterity"), rset.getInt("intelect")));
				}
			}
		}
		catch (Exception e)
		{
			LogManager.log(e);
		}
		
		LogManager.log("NpcData: Loaded " + NPCS.size() + " npcs.");
	}
	
	public static NpcHolder getNpcHolder(int npcId)
	{
		return NPCS.get(npcId);
	}
}
