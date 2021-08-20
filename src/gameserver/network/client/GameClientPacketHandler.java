package gameserver.network.client;

import common.managers.LogManager;
import common.network.ReceivablePacket;
import gameserver.network.client.receivable.AccountAuthenticationRequest;
import gameserver.network.client.receivable.AnimatorUpdateRequest;
import gameserver.network.client.receivable.CharacterCreationRequest;
import gameserver.network.client.receivable.CharacterDeletionRequest;
import gameserver.network.client.receivable.CharacterSelectUpdate;
import gameserver.network.client.receivable.CharacterSelectionInfoRequest;
import gameserver.network.client.receivable.CharacterSlotUpdate;
import gameserver.network.client.receivable.ChatRequest;
import gameserver.network.client.receivable.EnterWorldRequest;
import gameserver.network.client.receivable.ExitWorldRequest;
import gameserver.network.client.receivable.LocationUpdateRequest;
import gameserver.network.client.receivable.ObjectInfoRequest;
import gameserver.network.client.receivable.PlayerOptionsUpdate;
import gameserver.network.client.receivable.TargetUpdateRequest;

/**
 * @author Pantelis Andrianakis
 * @version November 7th 2018
 */
public class GameClientPacketHandler
{
	public static void handle(GameClient client, ReceivablePacket packet)
	{
		int packetId = -1;
		try
		{
			packetId = packet.readShort();
			switch (packetId)
			{
				case 1:
				{
					new AccountAuthenticationRequest(client, packet);
					break;
				}
				case 2:
				{
					new CharacterSelectionInfoRequest(client, packet);
					break;
				}
				case 3:
				{
					new CharacterCreationRequest(client, packet);
					break;
				}
				case 4:
				{
					new CharacterDeletionRequest(client, packet);
					break;
				}
				case 5:
				{
					new CharacterSlotUpdate(client, packet);
					break;
				}
				case 6:
				{
					new CharacterSelectUpdate(client, packet);
					break;
				}
				case 7:
				{
					new EnterWorldRequest(client, packet);
					break;
				}
				case 8:
				{
					new ExitWorldRequest(client, packet);
					break;
				}
				case 9:
				{
					new LocationUpdateRequest(client, packet);
					break;
				}
				case 10:
				{
					new AnimatorUpdateRequest(client, packet);
					break;
				}
				case 11:
				{
					new ObjectInfoRequest(client, packet);
					break;
				}
				case 12:
				{
					new PlayerOptionsUpdate(client, packet);
					break;
				}
				case 13:
				{
					new ChatRequest(client, packet);
					break;
				}
				case 14:
				{
					new TargetUpdateRequest(client, packet);
					break;
				}
				default:
				{
					LogManager.log("GameClientPacketHandler: Received unknown packet id " + packetId + " from " + client);
				}
			}
		}
		catch (Exception e)
		{
			LogManager.log("GameClientPacketHandler: Problem with " + client + " [Packet id: " + packetId + "]");
			LogManager.log(e);
		}
	}
}
