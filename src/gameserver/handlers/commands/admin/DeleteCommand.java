package gameserver.handlers.commands.admin;

import java.sql.Connection;
import java.sql.PreparedStatement;

import common.Config;
import common.managers.DatabaseManager;
import common.managers.LogManager;
import gameserver.actor.Npc;
import gameserver.actor.Player;
import gameserver.actor.WorldObject;
import gameserver.holders.LocationHolder;
import gameserver.holders.SpawnHolder;
import gameserver.managers.ChatManager;

/**
 * @author Pantelis Andrianakis
 * @version November 29th 2019
 */
public class DeleteCommand
{
	private static final String SPAWN_DELETE_QUERY = "DELETE FROM spawnlist WHERE npc_id=? AND x=? AND y=? AND z=? AND heading=? AND respawn_delay=?";
	
	public static void Handle(Player player)
	{
		// Gather information.
		final WorldObject target = player.getTarget();
		if (target == null)
		{
			ChatManager.sendSystemMessage(player, "You must select a target.");
			return;
		}
		final Npc npc = target.asNpc();
		if (npc == null)
		{
			ChatManager.sendSystemMessage(player, "You must select an NPC.");
			return;
		}
		
		// Log admin activity.
		final SpawnHolder npcSpawn = npc.getSpawnHolder();
		final LocationHolder npcLocation = npcSpawn.getLocation();
		final StringBuilder sb = new StringBuilder();
		if (Config.LOG_ADMIN)
		{
			sb.append(player.getName());
			sb.append(" used command /delete ");
			sb.append(npc);
			sb.append(" ");
			sb.append(npcLocation);
			LogManager.logAdmin(sb.toString());
			sb.setLength(0);
		}
		
		// Delete NPC.
		npc.deleteMe();
		
		// Send player success message.
		int npcId = npc.getNpcHolder().getNpcId();
		sb.append("You have deleted ");
		sb.append(npcId);
		sb.append(" from ");
		sb.append(npcLocation);
		ChatManager.sendSystemMessage(player, sb.toString());
		
		// Store in database.
		try (Connection con = DatabaseManager.getConnection();
			PreparedStatement ps = con.prepareStatement(SPAWN_DELETE_QUERY))
		{
			ps.setInt(1, npcId);
			ps.setFloat(2, npcLocation.getX());
			ps.setFloat(3, npcLocation.getY());
			ps.setFloat(4, npcLocation.getZ());
			ps.setFloat(5, npcLocation.getHeading());
			ps.setInt(6, npcSpawn.getRespawnDelay());
			ps.execute();
		}
		catch (Exception e)
		{
			LogManager.log(e);
		}
	}
}
