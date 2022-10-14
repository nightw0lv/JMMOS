package gameserver;

import common.Config;
import common.managers.DatabaseManager;
import common.managers.LogManager;
import common.managers.ThreadManager;
import common.network.NetServer;
import common.util.Util;
import gameserver.data.ItemData;
import gameserver.data.NpcData;
import gameserver.data.SkillData;
import gameserver.data.SpawnData;
import gameserver.managers.ShutdownManager;
import gameserver.managers.WorldManager;
import gameserver.network.client.GameClient;
import gameserver.network.client.PacketHandler;

/**
 * @author Pantelis Andrianakis
 * @since September 7th 2020
 */
public class GameServer
{
	public static boolean ENABLE_LOGIN = true;
	
	public static void main(String[] args)
	{
		new GameServer();
	}
	
	private GameServer()
	{
		// Keep start time for later.
		final long serverLoadStart = System.currentTimeMillis();
		
		Util.printSection("Configs");
		Config.load();
		
		Util.printSection("ThreadPool");
		ThreadManager.init();
		
		// Start after Configs and ThreadPool.
		LogManager.init();
		
		Util.printSection("Database");
		DatabaseManager.init();
		
		Util.printSection("World");
		WorldManager.init();
		
		Util.printSection("Skills");
		SkillData.init();
		
		Util.printSection("Items");
		ItemData.init();
		
		Util.printSection("NPCs");
		NpcData.init();
		SpawnData.init();
		
		// Post info.
		Util.printSection("Info");
		LogManager.log("Server loaded in " + ((System.currentTimeMillis() - serverLoadStart) / 1000) + " seconds.");
		System.gc();
		LogManager.log("Started, using " + ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576) + " of " + (Runtime.getRuntime().maxMemory() / 1048576) + " MB total memory.");
		
		// Initialize server.
		final NetServer server = new NetServer(Config.GAMESERVER_PORT, new PacketHandler(), GameClient.class);
		server.setName(getClass().getSimpleName());
		server.getNetConfig().setReadPoolSize(Config.CLIENT_READ_POOL_SIZE);
		server.getNetConfig().setExecutePoolSize(Config.CLIENT_EXECUTE_POOL_SIZE);
		server.getNetConfig().setPacketQueueLimit(Config.PACKET_QUEUE_LIMIT);
		server.getNetConfig().setPacketFloodDisconnect(Config.PACKET_FLOOD_DISCONNECT);
		server.getNetConfig().setPacketFloodDrop(Config.PACKET_FLOOD_DROP);
		server.getNetConfig().setDroppedPacketLog(Config.PACKET_FLOOD_LOG);
		server.getNetConfig().setTcpNoDelay(Config.TCP_NO_DELAY);
		server.getNetConfig().setConnectionTimeout(Config.CONNECTION_TIMEOUT);
		server.start();
		
		// Assign shutdown hook.
		Runtime.getRuntime().addShutdownHook(new ShutdownManager());
	}
}