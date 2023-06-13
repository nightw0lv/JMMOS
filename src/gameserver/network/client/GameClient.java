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
					// Loop while there are remaining bytes in the buffer.
					while (byteBuffer.hasRemaining())
					{
						channel.write(byteBuffer);
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