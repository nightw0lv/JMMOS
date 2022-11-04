package gameserver.network.client;

import common.Config;
import common.managers.LogManager;
import common.managers.ThreadManager;
import common.network.PacketHandlerInterface;
import common.network.ReadablePacket;
import gameserver.network.client.read.AccountAuthenticationRequest;
import gameserver.network.client.read.AnimatorUpdateRequest;
import gameserver.network.client.read.CharacterCreationRequest;
import gameserver.network.client.read.CharacterDeletionRequest;
import gameserver.network.client.read.CharacterSelectUpdate;
import gameserver.network.client.read.CharacterSelectionInfoRequest;
import gameserver.network.client.read.CharacterSlotUpdate;
import gameserver.network.client.read.ChatRequest;
import gameserver.network.client.read.EnterWorldRequest;
import gameserver.network.client.read.ExitWorldRequest;
import gameserver.network.client.read.LocationUpdateRequest;
import gameserver.network.client.read.ObjectInfoRequest;
import gameserver.network.client.read.PlayerOptionsUpdate;
import gameserver.network.client.read.TargetUpdateRequest;

/**
 * @author Pantelis Andrianakis
 * @since November 7th 2018
 */
public class PacketHandler implements PacketHandlerInterface<GameClient>
{
	@Override
	public void handle(GameClient client, ReadablePacket packet)
	{
		// Read packet id.
		int packetId = -1;
		try
		{
			packetId = packet.readShort();
		}
		catch (Exception e)
		{
			LogManager.log("PacketHandler: Problem receiving packet id from " + client);
			LogManager.log(e);
			client.disconnect();
			return;
		}
		
		// Continue on another thread.
		if (Config.THREADS_FOR_CLIENT_PACKETS)
		{
			ThreadManager.execute(new ExecuteTask(client, packet, packetId));
		}
		else // Wait for execution.
		{
			try
			{
				process(client, packet, packetId);
			}
			catch (Exception e)
			{
				LogManager.log("PacketHandler: Problem with " + client + " [Packet id: " + packetId + "]");
				LogManager.log(e);
			}
		}
	}
	
	private void process(GameClient client, ReadablePacket packet, int packetId)
	{
		switch (packetId)
		{
			case 1:
			{
				AccountAuthenticationRequest.process(client, packet);
				break;
			}
			case 2:
			{
				CharacterSelectionInfoRequest.process(client, packet);
				break;
			}
			case 3:
			{
				CharacterCreationRequest.process(client, packet);
				break;
			}
			case 4:
			{
				CharacterDeletionRequest.process(client, packet);
				break;
			}
			case 5:
			{
				CharacterSlotUpdate.process(client, packet);
				break;
			}
			case 6:
			{
				CharacterSelectUpdate.process(client, packet);
				break;
			}
			case 7:
			{
				EnterWorldRequest.process(client, packet);
				break;
			}
			case 8:
			{
				ExitWorldRequest.process(client, packet);
				break;
			}
			case 9:
			{
				LocationUpdateRequest.process(client, packet);
				break;
			}
			case 10:
			{
				AnimatorUpdateRequest.process(client, packet);
				break;
			}
			case 11:
			{
				ObjectInfoRequest.process(client, packet);
				break;
			}
			case 12:
			{
				PlayerOptionsUpdate.process(client, packet);
				break;
			}
			case 13:
			{
				ChatRequest.process(client, packet);
				break;
			}
			case 14:
			{
				TargetUpdateRequest.process(client, packet);
				break;
			}
			default:
			{
				LogManager.log("PacketHandler: Received unknown packet id " + packetId + " from " + client);
			}
		}
	}
	
	private class ExecuteTask implements Runnable
	{
		private final GameClient _client;
		private final ReadablePacket _packet;
		private final int _packetId;
		
		public ExecuteTask(GameClient client, ReadablePacket packet, int packetId)
		{
			_client = client;
			_packet = packet;
			_packetId = packetId;
		}
		
		@Override
		public void run()
		{
			try
			{
				process(_client, _packet, _packetId);
			}
			catch (Exception e)
			{
				LogManager.log("PacketHandler->ExecuteTask: Problem with " + _client + " [Packet id: " + _packetId + "]");
				LogManager.log(e);
			}
		}
	}
}
