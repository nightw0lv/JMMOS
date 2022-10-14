package common.network;

/**
 * @author Pantelis Andrianakis
 * @since October 4th 2022
 */
public class NetConfig
{
	private int _readPoolSize = 100;
	private int _executePoolSize = 50;
	private int _connectionTimeout = 800;
	private int _packetQueueLimit = 80;
	private boolean _packetFloodDisconnect = false;
	private boolean _packetFloodDrop = false;
	private boolean _droppedPacketLog = true;
	private boolean _tcpNoDelay = true;
	
	/**
	 * @return the NetClient pool size for reading client packets.
	 */
	public int getReadPoolSize()
	{
		return _readPoolSize;
	}
	
	/**
	 * Sets the NetClient pool size for reading client packets.
	 * @param clientPoolSize
	 */
	public void setReadPoolSize(int clientPoolSize)
	{
		_readPoolSize = clientPoolSize;
	}
	
	/**
	 * @return the NetClient pool size for executing client packets.
	 */
	public int getExecutePoolSize()
	{
		return _executePoolSize;
	}
	
	/**
	 * Sets the NetClient pool size for executing client packets.
	 * @param executePoolSize
	 */
	public void setExecutePoolSize(int executePoolSize)
	{
		_executePoolSize = executePoolSize;
	}
	
	/**
	 * @return the timeout until a connection is established.
	 */
	public int getConnectionTimeout()
	{
		return _connectionTimeout;
	}
	
	/**
	 * Sets the timeout until a connection is established.
	 * @param connectionTimeout
	 */
	public void setConnectionTimeout(int connectionTimeout)
	{
		_connectionTimeout = connectionTimeout;
	}
	
	/**
	 * @return the packet queue limit of receivable packets.
	 */
	public int getPacketQueueLimit()
	{
		return _packetQueueLimit;
	}
	
	/**
	 * Sets the packet queue limit of receivable packets.
	 * @param packetQueueLimit
	 */
	public void setPacketQueueLimit(int packetQueueLimit)
	{
		_packetQueueLimit = packetQueueLimit;
	}
	
	/**
	 * @return if disconnect when packets that exceed the packet queue limit.
	 */
	public boolean isPacketFloodDisconnect()
	{
		return _packetFloodDisconnect;
	}
	
	/**
	 * Sets to disconnect when the packet queue limit is exceeded.
	 * @param packetFloodDisconnect
	 */
	public void setPacketFloodDisconnect(boolean packetFloodDisconnect)
	{
		_packetFloodDisconnect = packetFloodDisconnect;
	}
	
	/**
	 * @return if packets that exceed the packet queue limit are dropped.
	 */
	public boolean isPacketFloodDrop()
	{
		return _packetFloodDrop;
	}
	
	/**
	 * Sets if packets that exceed the packet queue limit are dropped.
	 * @param packetQueueDrop
	 */
	public void setPacketFloodDrop(boolean packetQueueDrop)
	{
		_packetFloodDrop = packetQueueDrop;
	}
	
	/**
	 * @return if dropped packets are logged.
	 */
	public boolean isDroppedPacketLogEnabled()
	{
		return _droppedPacketLog;
	}
	
	/**
	 * Sets if dropped packets are logged.
	 * @param droppedPacketLog
	 */
	public void setDroppedPacketLog(boolean droppedPacketLog)
	{
		_droppedPacketLog = droppedPacketLog;
	}
	
	/**
	 * @return if TCP_NODELAY (Nagle's Algorithm) is used.
	 */
	public boolean isTcpNoDelay()
	{
		return _tcpNoDelay;
	}
	
	/**
	 * Sets if TCP_NODELAY (Nagle's Algorithm) is used.
	 * @param tcpNoDelay
	 */
	public void setTcpNoDelay(boolean tcpNoDelay)
	{
		_tcpNoDelay = tcpNoDelay;
	}
}
