package gameserver.network.client;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import common.Config;
import common.network.SendablePacket;
import gameserver.actor.Player;

/**
 * @author Pantelis Andrianakis
 * @version September 7th 2020
 */
public class GameClient
{
	private String _ip;
	private String _accountName;
	private SocketChannel _channel;
	private final Set<byte[]> _pendingPacketData = ConcurrentHashMap.newKeySet(Config.QUEUE_PACKET_LIMIT);
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
	
	public void channelSend(SendablePacket packet)
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
		if (_pendingPacketData.size() >= Config.QUEUE_PACKET_LIMIT)
		{
			return;
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
		return "[Account: " + _accountName + " - IP: " + (_ip == null ? "disconnected" : _ip) + "]";
	}
}