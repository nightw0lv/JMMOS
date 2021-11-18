package gameserver.network.client;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import common.Config;
import common.managers.LogManager;
import common.network.SendablePacket;
import gameserver.actor.Player;

/**
 * @author Pantelis Andrianakis
 * @since September 7th 2020
 */
public class GameClient
{
	private String _ip;
	private String _accountName;
	private SocketChannel _channel;
	private final Set<byte[]> _pendingPacketData = ConcurrentHashMap.newKeySet(Config.PACKET_QUEUE_LIMIT);
	private Player _activeChar;
	
	public GameClient(SocketChannel channel)
	{
		_channel = channel;
		
		try
		{
			_ip = _channel.getRemoteAddress().toString();
			_ip = _ip.substring(1, _ip.lastIndexOf(':')); // Trim out /127.0.0.1:12345
		}
		catch (Exception ignored)
		{
		}
	}
	
	public SocketChannel getChannel()
	{
		return _channel;
	}
	
	public void setChannel(SocketChannel channel)
	{
		_channel = channel;
	}
	
	public String getIp()
	{
		return _ip;
	}
	
	public String getAccountName()
	{
		return _accountName;
	}
	
	public void setAccountName(String accountName)
	{
		_accountName = accountName;
	}
	
	public void sendPacket(SendablePacket packet)
	{
		if ((_channel != null) && _channel.isConnected())
		{
			try
			{
				_channel.write(ByteBuffer.wrap(packet.getSendableBytes()));
			}
			catch (Exception ignored)
			{
			}
		}
	}
	
	public void addPacketData(byte[] data)
	{
		// Drop packets if queue is too big.
		final int size = _pendingPacketData.size();
		if (size >= Config.PACKET_QUEUE_LIMIT)
		{
			if (Config.PACKET_QUEUE_LOG)
			{
				final StringBuilder sb = new StringBuilder();
				sb.append(this);
				sb.append(" packet queue size(");
				sb.append(size);
				sb.append(") exceeded limit(");
				sb.append(Config.PACKET_QUEUE_LIMIT);
				sb.append(") for packet id ");
				sb.append((data[0] & 0xff) | ((data[1] & 0xff) << 8));
				sb.append(".");
				sb.append(this);
				LogManager.log(sb.toString());
			}
			if (Config.PACKET_QUEUE_DROP)
			{
				return;
			}
		}
		
		// Add to queue.
		_pendingPacketData.add(data);
	}
	
	public Set<byte[]> getPacketData()
	{
		return _pendingPacketData;
	}
	
	public Player getActiveChar()
	{
		return _activeChar;
	}
	
	public void setActiveChar(Player activeChar)
	{
		_activeChar = activeChar;
	}
	
	@Override
	public String toString()
	{
		final StringBuilder sb = new StringBuilder();
		sb.append("[Account: ");
		sb.append(_accountName);
		sb.append(" - IP: ");
		sb.append(_ip == null ? "disconnected" : _ip);
		sb.append("]");
		return sb.toString();
	}
}