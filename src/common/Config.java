package common;

import java.io.File;

import common.managers.LogManager;
import common.util.ConfigReader;
import gameserver.holders.LocationHolder;
import gameserver.util.ColorUtil;
import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;

/**
 * @author Pantelis Andrianakis
 * @since November 7th 2018
 */
public final class Config
{
	// --------------------------------------------------
	// Config File Definitions
	// --------------------------------------------------
	private static final String ACCOUNT_CONFIG_FILE = "config" + File.separator + "Account.ini";
	private static final String DATABASE_CONFIG_FILE = "config" + File.separator + "Database.ini";
	private static final String LOGGING_CONFIG_FILE = "config" + File.separator + "Logging.ini";
	private static final String NETWORK_CONFIG_FILE = "config" + File.separator + "Network.ini";
	private static final String PLAYER_CONFIG_FILE = "config" + File.separator + "Player.ini";
	private static final String THREADS_CONFIG_FILE = "config" + File.separator + "Threads.ini";
	
	// --------------------------------------------------
	// Account
	// --------------------------------------------------
	public static boolean ACCOUNT_AUTO_CREATE;
	public static int ACCOUNT_MAX_CHARACTERS;
	
	// --------------------------------------------------
	// Database
	// --------------------------------------------------
	public static String DATABASE_URL;
	public static String DATABASE_LOGIN;
	public static String DATABASE_PASSWORD;
	public static int DATABASE_MAX_CONNECTIONS;
	
	// --------------------------------------------------
	// Logging
	// --------------------------------------------------
	public static boolean LOG_FILE_SIZE_LIMIT_ENABLED = false;
	public static long LOG_FILE_SIZE_LIMIT = 1073741824;
	public static boolean LOG_CHAT;
	public static boolean LOG_WORLD;
	public static boolean LOG_ADMIN;
	
	// --------------------------------------------------
	// Network
	// --------------------------------------------------
	public static int GAMESERVER_PORT;
	public static int CLIENT_READ_POOL_SIZE;
	public static int CLIENT_SEND_POOL_SIZE;
	public static int CLIENT_EXECUTE_POOL_SIZE;
	public static int PACKET_QUEUE_LIMIT;
	public static boolean PACKET_FLOOD_DISCONNECT;
	public static boolean PACKET_FLOOD_DROP;
	public static boolean PACKET_FLOOD_LOGGED;
	public static boolean FAILED_DECRYPTION_LOGGED;
	public static boolean TCP_NO_DELAY;
	public static int MAXIMUM_ONLINE_USERS;
	public static double CLIENT_VERSION;
	
	// --------------------------------------------------
	// Player
	// --------------------------------------------------
	public static LocationHolder STARTING_LOCATION;
	public static int[] STARTING_ITEMS;
	public static TIntList VALID_SKIN_COLORS = new TIntArrayList();
	
	// --------------------------------------------------
	// Threads
	// --------------------------------------------------
	public static int SCHEDULED_THREAD_POOL_SIZE;
	public static int INSTANT_THREAD_POOL_SIZE;
	
	public static void load()
	{
		final ConfigReader accountConfigs = new ConfigReader(ACCOUNT_CONFIG_FILE);
		ACCOUNT_AUTO_CREATE = accountConfigs.getBoolean("AccountAutoCreate", false);
		ACCOUNT_MAX_CHARACTERS = accountConfigs.getInt("AccountMaxCharacters", 5);
		
		final ConfigReader databaseConfigs = new ConfigReader(DATABASE_CONFIG_FILE);
		DATABASE_URL = databaseConfigs.getString("URL", "jdbc:mariadb://localhost/edws");
		DATABASE_LOGIN = databaseConfigs.getString("Login", "root");
		DATABASE_PASSWORD = databaseConfigs.getString("Password", "");
		DATABASE_MAX_CONNECTIONS = databaseConfigs.getInt("MaximumDbConnections", 100);
		
		final ConfigReader loggingConfigs = new ConfigReader(LOGGING_CONFIG_FILE);
		LOG_FILE_SIZE_LIMIT_ENABLED = loggingConfigs.getBoolean("LogFileSizeLimitEnabled", false);
		LOG_FILE_SIZE_LIMIT = loggingConfigs.getLong("LogFileSizeLimit", 1073741824);
		LOG_CHAT = loggingConfigs.getBoolean("LogChat", true);
		LOG_WORLD = loggingConfigs.getBoolean("LogWorld", true);
		LOG_ADMIN = loggingConfigs.getBoolean("LogAdmin", true);
		
		final ConfigReader threadConfigs = new ConfigReader(THREADS_CONFIG_FILE);
		SCHEDULED_THREAD_POOL_SIZE = threadConfigs.getInt("ScheduledThreadPoolSize", -1);
		if (SCHEDULED_THREAD_POOL_SIZE == -1)
		{
			SCHEDULED_THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors() * 4;
		}
		INSTANT_THREAD_POOL_SIZE = threadConfigs.getInt("InstantThreadPoolSize", -1);
		if (INSTANT_THREAD_POOL_SIZE == -1)
		{
			INSTANT_THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors() * 2;
		}
		
		final ConfigReader networkConfigs = new ConfigReader(NETWORK_CONFIG_FILE);
		GAMESERVER_PORT = networkConfigs.getInt("GameserverPort", 5055);
		CLIENT_READ_POOL_SIZE = networkConfigs.getInt("ClientReadPoolSize", 100);
		CLIENT_SEND_POOL_SIZE = networkConfigs.getInt("ClientSendPoolSize", 25);
		CLIENT_EXECUTE_POOL_SIZE = networkConfigs.getInt("ClientExecutePoolSize", 50);
		PACKET_QUEUE_LIMIT = networkConfigs.getInt("PacketQueueLimit", 20);
		PACKET_FLOOD_DISCONNECT = networkConfigs.getBoolean("PacketFloodDisconnect", false);
		PACKET_FLOOD_DROP = networkConfigs.getBoolean("PacketFloodDrop", false);
		PACKET_FLOOD_LOGGED = networkConfigs.getBoolean("PacketFloodLogged", true);
		FAILED_DECRYPTION_LOGGED = networkConfigs.getBoolean("FailedDecryptionLogged", true);
		TCP_NO_DELAY = networkConfigs.getBoolean("TcpNoDelay", true);
		MAXIMUM_ONLINE_USERS = networkConfigs.getInt("MaximumOnlineUsers", 2000);
		CLIENT_VERSION = networkConfigs.getDouble("ClientVersion", 1.0);
		
		final ConfigReader playerConfigs = new ConfigReader(PLAYER_CONFIG_FILE);
		final String[] startingLocation = playerConfigs.getString("StartingLocation", "0.12;-2.5;-2.75263").split(";");
		STARTING_LOCATION = new LocationHolder(Float.parseFloat(startingLocation[0]), Float.parseFloat(startingLocation[1]), Float.parseFloat(startingLocation[2]), startingLocation.length > 3 ? Integer.parseInt(startingLocation[3]) : 0);
		final TIntList startingItems = new TIntArrayList();
		for (String itemString : playerConfigs.getString("StartingItems", "").split(";"))
		{
			String trimmedString = itemString.trim();
			if (!trimmedString.equals(""))
			{
				int itemId = Integer.parseInt(trimmedString);
				if (itemId > 0)
				{
					startingItems.add(itemId);
				}
			}
		}
		STARTING_ITEMS = startingItems.toArray();
		VALID_SKIN_COLORS.clear();
		for (String colorCode : playerConfigs.getString("ValidSkinColorCodes", "F1D1BD;F1C4AD;E7B79C;E19F7E;AF7152;7E472E;4A2410;F7DDC0;F3D1A9;C5775A;B55B44;863923;672818;3F1508").split(";"))
		{
			VALID_SKIN_COLORS.add(ColorUtil.hexStringToInt(colorCode));
		}
		
		LogManager.log("Configs: Initialized.");
	}
}
