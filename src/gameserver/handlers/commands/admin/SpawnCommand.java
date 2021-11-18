package gameserver.handlers.commands.admin;

import java.sql.Connection;
import java.sql.PreparedStatement;

import common.Config;
import common.managers.DatabaseManager;
import common.managers.LogManager;
import gameserver.actor.Npc;
import gameserver.actor.Player;
import gameserver.data.SpawnData;
import gameserver.holders.LocationHolder;
import gameserver.managers.ChatManager;
import gameserver.managers.WorldManager;
import gameserver.network.client.sendable.NpcInformation;

/**
 * @author Pantelis Andrianakis
 * @since November 29th 2019
 */
public class SpawnCommand
{
	private static final String SPAWN_SAVE_QUERY = "INSERT INTO spawnlist (npc_id, x, y, z, heading, respawn_delay) values (?, ?, ?, ?, ?, ?)";
	
	public static void handle(Player player, String command)
	{
		// Gather information from parameters.
		String[] commandSplit = command.split(" ");
		int npcId;
		if (commandSplit.length > 1)
		{
			npcId = Integer.parseInt(commandSplit[1]);
		}
		else
		{
			ChatManager.sendSystemMessage(player, "Proper syntax is /spawn npcId delaySeconds(optional).");
			return;
		}
		int respawnDelay = 60;
		if (commandSplit.length > 2)
		{
			respawnDelay = Integer.parseInt(commandSplit[2]);
		}
		
		// Log admin activity.
		final LocationHolder playerLocation = player.getLocation();
		final LocationHolder npcLocation = new LocationHolder(playerLocation.getX(), playerLocation.getY(), playerLocation.getZ(), playerLocation.getHeading());
		StringBuilder sb = new StringBuilder();
		if (Config.LOG_ADMIN)
		{
			sb.append(player.getName());
			sb.append(" used command /spawn ");
			sb.append(npcId);
			sb.append(" ");
			sb.append(respawnDelay);
			sb.append(" at ");
			sb.append(npcLocation);
			LogManager.logAdmin(sb.toString());
			sb.setLength(0);
		}
		
		// Spawn NPC.
		final Npc npc = SpawnData.spawnNpc(npcId, npcLocation, respawnDelay);
		
		// Broadcast NPC information.
		final NpcInformation info = new NpcInformation(npc);
		player.sendPacket(info);
		WorldManager.broadcastPacketToVisiblePlayers(player, info);
		
		// Send player success message.
		sb.append("You have spawned ");
		sb.append(npcId);
		sb.append(" at ");
		sb.append(npcLocation);
		ChatManager.sendSystemMessage(player, sb.toString());
		
		// Store in database.
		try (Connection con = DatabaseManager.getConnection();
			PreparedStatement ps = con.prepareStatement(SPAWN_SAVE_QUERY))
		{
			ps.setInt(1, npcId);
			ps.setFloat(2, npcLocation.getX());
			ps.setFloat(3, npcLocation.getY());
			ps.setFloat(4, npcLocation.getZ());
			ps.setFloat(5, npcLocation.getHeading());
			ps.setInt(6, respawnDelay);
			ps.execute();
		}
		catch (Exception e)
		{
			LogManager.log(e);
		}
	}
}
