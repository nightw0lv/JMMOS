package gameserver.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import common.managers.DatabaseManager;
import common.managers.LogManager;
import gameserver.actor.Monster;
import gameserver.actor.Npc;
import gameserver.holders.LocationHolder;
import gameserver.holders.NpcHolder;
import gameserver.holders.SpawnHolder;

/**
 * @author Pantelis Andrianakis
 * @since November 28th 2019
 */
public class SpawnData
{
	private static final String RESTORE_SPAWNS = "SELECT * FROM spawnlist";
	
	public static void init()
	{
		int spawnCount = 0;
		
		try (Connection con = DatabaseManager.getConnection();
			PreparedStatement ps = con.prepareStatement(RESTORE_SPAWNS))
		{
			try (ResultSet rset = ps.executeQuery())
			{
				while (rset.next())
				{
					final int npcId = rset.getInt("npc_id");
					final NpcHolder npcHolder = NpcData.getNpcHolder(npcId);
					if (npcHolder == null)
					{
						LogManager.log("SpawnData: Could not find NPC template with id " + npcId + ".");
					}
					else
					{
						spawnNpc(npcId, new LocationHolder(rset.getFloat("x"), rset.getFloat("y"), rset.getFloat("z"), rset.getInt("heading")), rset.getInt("respawn_delay"));
						spawnCount++;
					}
				}
			}
		}
		catch (Exception e)
		{
			LogManager.log(e);
		}
		
		LogManager.log("SpawnData: Loaded " + spawnCount + " spawns.");
	}
	
	public static Npc spawnNpc(int npcId, LocationHolder location, int respawnDelay)
	{
		final NpcHolder npcHolder = NpcData.getNpcHolder(npcId);
		final SpawnHolder spawn = new SpawnHolder(location, respawnDelay);
		Npc npc = null;
		switch (npcHolder.getNpcType())
		{
			case NPC:
				npc = new Npc(npcHolder, spawn);
				break;
			
			case MONSTER:
				npc = new Monster(npcHolder, spawn);
				break;
		}
		return npc;
	}
}
