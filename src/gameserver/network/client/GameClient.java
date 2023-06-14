package gameserver.network.client;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import common.network.NetClient;
import common.network.WritablePacket;
import gameserver.actor.Player;
import gameserver.managers.WorldManager;

/**
 * @author Pantelis Andrianakis
 * @since September 7th 2020
 */
public class GameClient extends NetClient
{
	private String _accountName;
	private Player _activeChar;
	
	public String getAccountName()
	{
		return _accountName;
	}
	
	public void setAccountName(String accountName)
	{
		_accountName = accountName;
	}
	
	public Player getActiveChar()
	{
		return _activeChar;
	}
	
	public void setActiveChar(Player activeChar)
	{
		_activeChar = activeChar;
	}
	
	public void sendPacket(WritablePacket packet)
	{
		final SocketChannel channel = getChannel();
		if ((channel != null) && channel.isConnected())
		{
			final ByteBuffer byteBuffer = packet.getSendableByteBuffer();
			if (byteBuffer != null)
			{
				try
				{
					// Send the packet data.
					channel.write(byteBuffer);
					
					// Continue write if there are remaining bytes in the buffer.
					if (byteBuffer.hasRemaining())
					{
						int attempt = 0; // Keep it under 100 attempts (1000ms).
						while (attempt++ < 100)
						{
							Thread.sleep(10);
							channel.write(byteBuffer);
							
							// Check if write is complete.
							if (!byteBuffer.hasRemaining())
							{
								break;
							}
						}
					}
				}
				catch (Exception ignored)
				{
				}
			}
		}
	}
	
	@Override
	public void onDisconnection()
	{
		WorldManager.removeClient(this);
	}
	
	@Override
	public String toString()
	{
		final StringBuilder sb = new StringBuilder();
		sb.append("[Account: ");
		sb.append(_accountName);
		sb.append(" - IP: ");
		sb.append(getIp() == null ? "disconnected" : getIp());
		sb.append("]");
		return sb.toString();
	}
}