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
 * @since November 7th 2018
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
