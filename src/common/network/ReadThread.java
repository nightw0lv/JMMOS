package common.network;

import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Set;

/**
 * @author Pantelis Andrianakis
 * @since September 7th 2020
 * @param <E> extends NetClient
 */
public class ReadThread<E extends NetClient> implements Runnable
{
	private final ByteBuffer _sizeBuffer = ByteBuffer.allocate(2); // Reusable size buffer.
	private final Set<E> _pool;
	
	public ReadThread(Set<E> pool)
	{
		_pool = pool;
	}
	
	@Override
	public void run()
	{
		long executionStart;
		long currentTime;
		while (true)
		{
			executionStart = System.currentTimeMillis();
			
			// No need to iterate when pool is empty.
			if (!_pool.isEmpty())
			{
				// Iterate client pool.
				ITERATE: for (E client : _pool)
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
								final int packetSize = calculatePacketSize();
								final ByteBuffer packetByteBuffer = ByteBuffer.allocate(packetSize);
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
										// Continue read if data length is less than expected.
										if (packetByteBuffer.position() < packetSize)
										{
											int attempt = 0; // Keep it under 10 attempts.
											while ((attempt++ < 10) && (packetByteBuffer.position() < packetSize))
											{
												final ByteBuffer additionalData = ByteBuffer.allocate(packetSize - packetByteBuffer.position());
												channel.read(additionalData);
												packetByteBuffer.put(packetByteBuffer.position(), additionalData, 0, additionalData.position());
												packetByteBuffer.position(packetByteBuffer.position() + additionalData.position());
											}
										}
										
										// Add packet data to client.
										client.addPacketData(packetByteBuffer.array());
									}
								}
							}
						}
					}
					catch (SocketTimeoutException e)
					{
						// TODO: Send lag message to client.
						onDisconnection(client);
					}
					catch (Exception e) // Unexpected disconnection?
					{
						onDisconnection(client);
						// LogManager.log(e);
					}
				}
			}
			
			// Prevent high CPU caused by repeatedly looping.
			currentTime = System.currentTimeMillis();
			if ((currentTime - executionStart) < 1)
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
		return (_sizeBuffer.get() & 0xff) | ((_sizeBuffer.get() << 8) & 0xffff);
	}
	
	private void onDisconnection(E client)
	{
		_pool.remove(client);
		client.onDisconnection();
	}
}
