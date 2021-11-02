package gameserver.network.client;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Set;

import common.util.Chronos;
import gameserver.managers.WorldManager;

/**
 * @author Pantelis Andrianakis
 * @since September 7th 2020
 */
public class GameClientPacketReadPoolTask implements Runnable
{
	private final ByteBuffer _sizeBuffer = ByteBuffer.allocate(2); // Reusable size buffer.
	private final Set<GameClient> _pool;
	
	public GameClientPacketReadPoolTask(Set<GameClient> pool)
	{
		_pool = pool;
	}
	
	@Override
	public void run()
	{
		long executionStart;
		while (true)
		{
			executionStart = Chronos.currentTimeMillis();
			
			// No need to iterate when pool is empty.
			if (!_pool.isEmpty())
			{
				// Iterate client pool.
				ITERATE: for (GameClient client : _pool)
				{
					try
					{
						final SocketChannel channel = client.getChannel();
						if (channel == null) // Unexpected disconnection?
						{
							// LogManager.log("Null SocketChannel: " + client);
							onDisconnection(client);
							continue ITERATE;
						}
						
						// Read incoming packet size (short).
						_sizeBuffer.clear();
						switch (channel.read(_sizeBuffer))
						{
							// Disconnected.
							case -1:
							{
								onDisconnection(client);
								continue ITERATE;
							}
							// Nothing read.
							case 0:
							{
								continue ITERATE;
							}
							// Read actual packet bytes.
							default:
							{
								// Allocate a new ByteBuffer based on packet size read.
								final ByteBuffer packetByteBuffer = ByteBuffer.allocate(calculatePacketSize());
								switch (channel.read(packetByteBuffer))
								{
									// Disconnected.
									case -1:
									{
										onDisconnection(client);
										continue ITERATE;
									}
									// Nothing read.
									case 0:
									{
										continue ITERATE;
									}
									// Send data read to the client packet queue.
									default:
									{
										client.addPacketData(packetByteBuffer.array());
									}
								}
							}
						}
					}
					catch (Exception e) // Unexpected disconnection?
					{
						onDisconnection(client);
						// LogManager.log(e);
					}
				}
			}
			
			// Prevent high CPU caused by repeatedly looping.
			if ((Chronos.currentTimeMillis() - executionStart) < 1)
			{
				try
				{
					Thread.sleep(1);
				}
				catch (Exception ignored)
				{
				}
			}
		}
	}
	
	private int calculatePacketSize()
	{
		_sizeBuffer.rewind();
		return (_sizeBuffer.get() & 0xff) | ((_sizeBuffer.get() << 8) & 0xff00);
	}
	
	private void onDisconnection(GameClient client)
	{
		_pool.remove(client);
		WorldManager.removeClient(client);
	}
}
