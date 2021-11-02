package gameserver.network.client;

import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import common.Config;
import common.managers.LogManager;
import common.managers.ThreadManager;
import common.util.Chronos;

/**
 * @author Pantelis Andrianakis
 * @since September 7th 2020
 */
public class GameClientNetworkListener
{
	private static final List<Set<GameClient>> CLIENT_READ_POOLS = new ArrayList<>();
	private static final List<Set<GameClient>> CLIENT_EXECUTE_POOLS = new ArrayList<>();
	
	public static void init()
	{
		// Run on a separate task.
		ThreadManager.execute(new GameClientNetworkListenerTask());
	}
	
	private static class GameClientNetworkListenerTask implements Runnable
	{
		public GameClientNetworkListenerTask()
		{
		}
		
		@Override
		public void run()
		{
			// Create server and bind port.
			try
			{
				final ServerSocketChannel server = ServerSocketChannel.open();
				server.bind(new InetSocketAddress(Config.GAMESERVER_PORT));
				server.configureBlocking(false); // Non-blocking I/O.
				
				// Listen for new connections.
				LogManager.log("Listening on port " + Config.GAMESERVER_PORT + " for incomming connections.");
				long executionStart;
				while (true)
				{
					executionStart = Chronos.currentTimeMillis();
					
					final SocketChannel channel = server.accept();
					if (channel != null)
					{
						channel.configureBlocking(false); // Non-blocking I/O.
						createClient(channel);
					}
					
					// Prevent high CPU caused by repeatedly polling the channel.
					if ((Chronos.currentTimeMillis() - executionStart) < 1)
					{
						Thread.sleep(1);
					}
				}
			}
			catch (Exception e)
			{
				LogManager.log("Problem initializing GameClientNetworkListener.");
				LogManager.log(e);
			}
		}
	}
	
	protected static void createClient(SocketChannel channel)
	{
		final GameClient client = new GameClient(channel);
		addToReadPool(client);
		addToExecutePool(client);
	}
	
	private static void addToReadPool(GameClient client)
	{
		synchronized (CLIENT_READ_POOLS)
		{
			// Find a pool that is not full.
			for (Set<GameClient> pool : CLIENT_READ_POOLS)
			{
				if (pool.size() < Config.CLIENT_READ_POOL_SIZE)
				{
					pool.add(client);
					return;
				}
			}
			
			// All pools are full.
			
			// Create a new client pool.
			final Set<GameClient> newPool = ConcurrentHashMap.newKeySet(Config.CLIENT_READ_POOL_SIZE);
			newPool.add(client);
			// Create a new task for the new pool.
			ThreadManager.execute(new GameClientPacketReadPoolTask(newPool));
			// Add the new pool to the pool list.
			CLIENT_READ_POOLS.add(newPool);
		}
	}
	
	private static void addToExecutePool(GameClient client)
	{
		synchronized (CLIENT_EXECUTE_POOLS)
		{
			// Find a pool that is not full.
			for (Set<GameClient> pool : CLIENT_EXECUTE_POOLS)
			{
				if (pool.size() < Config.CLIENT_EXECUTE_POOL_SIZE)
				{
					pool.add(client);
					return;
				}
			}
			
			// All pools are full.
			
			// Create a new client pool.
			final Set<GameClient> newPool = ConcurrentHashMap.newKeySet(Config.CLIENT_EXECUTE_POOL_SIZE);
			newPool.add(client);
			// Create a new task for the new pool.
			ThreadManager.execute(new GameClientPacketExecutePoolTask(newPool));
			// Add the new pool to the pool list.
			CLIENT_EXECUTE_POOLS.add(newPool);
		}
	}
}
