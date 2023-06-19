package common.network;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import common.managers.LogManager;

/**
 * @author Pantelis Andrianakis
 * @since September 7th 2020
 */
public class NetClient
{
	private String _ip;
	private Socket _socket;
	private InputStream _inputStream;
	private OutputStream _outputStream;
	private NetConfig _netConfig;
	private Queue<byte[]> _packetData;
	private byte[] _pendingData;
	private int _pendingPacketSize;
	
	/**
	 * Initialize the client.
	 * @param socket
	 * @param netConfig
	 */
	public void init(Socket socket, NetConfig netConfig)
	{
		_socket = socket;
		_netConfig = netConfig;
		_packetData = new ConcurrentLinkedQueue<>();
		_ip = socket.getInetAddress().toString().substring(1); // Trim out /127.0.0.1
		
		try
		{
			_inputStream = _socket.getInputStream();
			_outputStream = _socket.getOutputStream();
		}
		catch (Exception ignored)
		{
		}
		
		// Client is ready for communication.
		onConnection();
	}
	
	/**
	 * Called when client is connected.
	 */
	public void onConnection()
	{
	}
	
	/**
	 * Called when client is disconnected.
	 */
	public void onDisconnection()
	{
	}
	
	/**
	 * Disconnect the client.
	 */
	public void disconnect()
	{
		if (_socket != null)
		{
			try
			{
				_socket.close();
				_socket = null;
				_inputStream = null;
				_outputStream = null;
			}
			catch (Exception ignored)
			{
			}
		}
		
		if (_packetData != null)
		{
			_packetData.clear();
		}
		
		if (_pendingData != null)
		{
			_pendingData = null;
		}
	}
	
	/**
	 * Add packet data to the queue.
	 * @param data
	 */
	public void addPacketData(byte[] data)
	{
		// Check packet flooding.
		final int size = _packetData.size();
		if (size >= _netConfig.getPacketQueueLimit())
		{
			if (_netConfig.isPacketFloodDisconnect())
			{
				disconnect();
				return;
			}
			
			if (_netConfig.isPacketFloodDrop())
			{
				if (_netConfig.isPacketFloodLogged() && ((size % _netConfig.getPacketQueueLimit()) == 0))
				{
					final StringBuilder sb = new StringBuilder();
					sb.append(this);
					sb.append(" packet queue size(");
					sb.append(size);
					sb.append(") exceeded limit(");
					sb.append(_netConfig.getPacketQueueLimit());
					sb.append(").");
					LogManager.log(sb.toString());
				}
				return;
			}
		}
		
		// Add to queue.
		_packetData.add(data);
	}
	
	/**
	 * @return the pending packet data.
	 */
	public Queue<byte[]> getPacketData()
	{
		return _packetData;
	}
	
	/**
	 * @return the pending read <b>byte[]</b>.
	 */
	public byte[] getPendingData()
	{
		return _pendingData;
	}
	
	/**
	 * Set the pending read <b>byte[]</b>.
	 * @param pendingData the pending read <b>byte[]</b>.
	 */
	public void setPendingData(byte[] pendingData)
	{
		_pendingData = pendingData;
	}
	
	/**
	 * @return the expected pending packet size.
	 */
	public int getPendingPacketSize()
	{
		return _pendingPacketSize;
	}
	
	/**
	 * Set the expected pending packet size.
	 * @param pendingPacketSize the expected packet size.
	 */
	public void setPendingPacketSize(int pendingPacketSize)
	{
		_pendingPacketSize = pendingPacketSize;
	}
	
	/**
	 * Sends a packet over the network using the default encryption.
	 * @param packet The packet to send.
	 */
	public void sendPacket(WritablePacket packet)
	{
		if ((_socket == null) || !_socket.isConnected())
		{
			return;
		}
		
		final byte[] sendableBytes = packet.getSendableBytes(getEncryption());
		if (sendableBytes == null)
		{
			return;
		}
		
		try
		{
			synchronized (this)
			{
				_outputStream.write(sendableBytes);
				_outputStream.flush();
			}
		}
		catch (Exception ignored)
		{
		}
	}
	
	/**
	 * @return the Encryption of this client.
	 */
	public EncryptionInterface getEncryption()
	{
		return null;
	}
	
	/**
	 * @return the Socket of this client.
	 */
	public Socket getSocket()
	{
		return _socket;
	}
	
	/**
	 * @return the InputStream of this client.
	 */
	public InputStream getInputStream()
	{
		return _inputStream;
	}
	
	/**
	 * @return the OutputStream of this client.
	 */
	public OutputStream getOutputStream()
	{
		return _outputStream;
	}
	
	/**
	 * @return the network configurations of this client.
	 */
	public NetConfig getNetConfig()
	{
		return _netConfig;
	}
	
	/**
	 * @return the IP address of this client.
	 */
	public String getIp()
	{
		return _ip;
	}
	
	@Override
	public String toString()
	{
		final StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append(" - IP: ");
		sb.append(_ip);
		sb.append("]");
		return sb.toString();
	}
}
