package common.network;

import java.util.Set;

import common.managers.LogManager;

/**
 * @author Pantelis Andrianakis
 * @since September 7th 2020
 * @param <E> extends NetClient
 */
public class ExecuteThread<E extends NetClient> implements Runnable
{
	private final Set<E> _pool;
	private final PacketHandlerInterface<E> _packetHandler;
	
	public ExecuteThread(Set<E> pool, PacketHandlerInterface<E> packetHandler)
	{
		_pool = pool;
		_packetHandler = packetHandler;
	}
	
	@Override
	public void run()
	{
		while (true)
		{
			// No need to iterate when pool is empty.
			if (!_pool.isEmpty())
			{
				// Iterate client pool.
				ITERATE: for (E client : _pool)
				{
					if (client.getSocket() == null)
					{
						_pool.remove(client);
						continue ITERATE;
					}
					
					final byte[] data = client.getPacketData().poll();
					if (data == null)
					{
						continue ITERATE;
					}
					
					if (client.getEncryption() != null)
					{
						try
						{
							client.getEncryption().decrypt(data, 0, data.length);
						}
						catch (Exception e)
						{
							if (client.getNetConfig().isFailedDecryptionLogged())
							{
								LogManager.log("ExecuteThread: Problem with " + client + " data decryption.");
								LogManager.log(e);
							}
							client.disconnect();
							continue ITERATE;
						}
					}
					_packetHandler.handle(client, new ReadablePacket(data));
				}
			}
			
			// Prevent high CPU caused by repeatedly looping.
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
