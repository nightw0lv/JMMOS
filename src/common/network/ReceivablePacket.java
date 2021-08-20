package common.network;

import java.nio.charset.StandardCharsets;

/**
 * @author Pantelis Andrianakis
 * @version October 29th 2020
 */
public class ReceivablePacket
{
	private final byte[] _bytes;
	private short _position = 0;
	
	public ReceivablePacket(byte[] bytes)
	{
		_bytes = bytes;
	}
	
	public boolean readBoolean()
	{
		return readByte() != 0;
	}
	
	public String readString()
	{
		String result = "";
		try
		{
			result = new String(readBytes(readShort()), StandardCharsets.UTF_8);
		}
		catch (Exception ignored)
		{
		}
		return result;
	}
	
	public byte[] readBytes(int length)
	{
		final byte[] result = new byte[length];
		for (int i = 0; i < length; i++)
		{
			result[i] = _bytes[_position++];
		}
		return result;
	}
	
	public int readByte()
	{
		return _bytes[_position++];
	}
	
	public int readShort()
	{
		return (_bytes[_position++] & 0xff) //
			| ((_bytes[_position++] & 0xff) << 8);
	}
	
	public int readInt()
	{
		return (_bytes[_position++] & 0xff) //
			| ((_bytes[_position++] & 0xff) << 8) //
			| ((_bytes[_position++] & 0xff) << 16) //
			| ((_bytes[_position++] & 0xff) << 24);
	}
	
	public long readLong()
	{
		return (_bytes[_position++] & 0xff) //
			| ((_bytes[_position++] & 0xffL) << 8) //
			| ((_bytes[_position++] & 0xffL) << 16) //
			| ((_bytes[_position++] & 0xffL) << 24) //
			| ((_bytes[_position++] & 0xffL) << 32) //
			| ((_bytes[_position++] & 0xffL) << 40) //
			| ((_bytes[_position++] & 0xffL) << 48) //
			| ((_bytes[_position++] & 0xffL) << 56);
	}
	
	public float readFloat()
	{
		return Float.intBitsToFloat(readInt());
	}
	
	public double readDouble()
	{
		return Double.longBitsToDouble(readLong());
	}
}
