package gameserver.network.client.read;

import java.sql.Connection;
import java.sql.PreparedStatement;

import common.managers.DatabaseManager;
import common.managers.LogManager;
import common.network.ReadablePacket;
import gameserver.actor.Player;
import gameserver.network.client.GameClient;

/**
 * @author Pantelis Andrianakis
 * @since February 14th 2019
 */
public class PlayerOptionsUpdate
{
	private static final String UPDATE_CHARACTER_OPTIONS_QUERY = "UPDATE character_options SET chat_color_normal=?, chat_color_message=?, chat_color_system=?, chat_use_timestamps=?, key_up_1=?, key_up_2=?, key_down_1=?, key_down_2=?, key_left_1=?, key_left_2=?, key_right_1=?, key_right_2=?, key_jump_1=?, key_jump_2=?, key_character_1=?, key_character_2=?, key_inventory_1=?, key_inventory_2=?, key_skills_1=?, key_skills_2=?, key_shortcut_1_1=?, key_shortcut_1_2=?, key_shortcut_2_1=?, key_shortcut_2_2=?, key_shortcut_3_1=?, key_shortcut_3_2=?, key_shortcut_4_1=?, key_shortcut_4_2=?, key_shortcut_5_1=?, key_shortcut_5_2=?, key_shortcut_6_1=?, key_shortcut_6_2=?, key_shortcut_7_1=?, key_shortcut_7_2=?, key_shortcut_8_1=?, key_shortcut_8_2=?, key_shortcut_9_1=?, key_shortcut_9_2=?, key_shortcut_10_1=?, key_shortcut_10_2=?, key_shortcut_11_1=?, key_shortcut_11_2=?, key_shortcut_12_1=?, key_shortcut_12_2=? WHERE name=?";
	
	public static void process(GameClient client, ReadablePacket packet)
	{
		// Get player.
		final Player player = client.getActiveChar();
		if (player == null)
		{
			return;
		}
		
		try (Connection con = DatabaseManager.getConnection();
			PreparedStatement ps = con.prepareStatement(UPDATE_CHARACTER_OPTIONS_QUERY))
		{
			ps.setInt(1, packet.readInt());
			ps.setInt(2, packet.readInt());
			ps.setInt(3, packet.readInt());
			ps.setInt(4, packet.readByte());
			ps.setInt(5, packet.readShort());
			ps.setInt(6, packet.readShort());
			ps.setInt(7, packet.readShort());
			ps.setInt(8, packet.readShort());
			ps.setInt(9, packet.readShort());
			ps.setInt(10, packet.readShort());
			ps.setInt(11, packet.readShort());
			ps.setInt(12, packet.readShort());
			ps.setInt(13, packet.readShort());
			ps.setInt(14, packet.readShort());
			ps.setInt(15, packet.readShort());
			ps.setInt(16, packet.readShort());
			ps.setInt(17, packet.readShort());
			ps.setInt(18, packet.readShort());
			ps.setInt(19, packet.readShort());
			ps.setInt(20, packet.readShort());
			ps.setInt(21, packet.readShort());
			ps.setInt(22, packet.readShort());
			ps.setInt(23, packet.readShort());
			ps.setInt(24, packet.readShort());
			ps.setInt(25, packet.readShort());
			ps.setInt(26, packet.readShort());
			ps.setInt(27, packet.readShort());
			ps.setInt(28, packet.readShort());
			ps.setInt(29, packet.readShort());
			ps.setInt(30, packet.readShort());
			ps.setInt(31, packet.readShort());
			ps.setInt(32, packet.readShort());
			ps.setInt(33, packet.readShort());
			ps.setInt(34, packet.readShort());
			ps.setInt(35, packet.readShort());
			ps.setInt(36, packet.readShort());
			ps.setInt(37, packet.readShort());
			ps.setInt(38, packet.readShort());
			ps.setInt(39, packet.readShort());
			ps.setInt(40, packet.readShort());
			ps.setInt(41, packet.readShort());
			ps.setInt(42, packet.readShort());
			ps.setInt(43, packet.readShort());
			ps.setInt(44, packet.readShort());
			ps.setString(45, player.getName());
			ps.execute();
		}
		catch (Exception e)
		{
			LogManager.log(e);
		}
	}
}
