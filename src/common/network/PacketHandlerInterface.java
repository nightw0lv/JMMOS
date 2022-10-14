package common.network;

/**
 * @author Pantelis Andrianakis
 * @since October 4th 2022
 */
public interface PacketHandlerInterface
{
	default void handle(NetClient client, ReadablePacket packet)
	{
	}
}
