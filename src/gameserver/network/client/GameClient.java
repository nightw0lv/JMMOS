package gameserver.network.client;

import common.network.NetClient;
import common.network.SendablePacket;
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
	
	public void sendPacket(SendablePacket packet)
	{
		if ((getChannel() != null) && getChannel().isConnected())
		{
			try
			{
				getChannel().write(packet.getSendableByteBuffer());
			}
			catch (Exception ignored)
			{
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