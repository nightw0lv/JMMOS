package common.network;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * @author Pantelis Andrianakis
 * @since October 29th 2020
 */
public class SendablePacket
{
	private byte[] _bytes;
	private short _size = 2; // Allocate space for size (short - max length 32767).
	
	public SendablePacket(int initialSize)
	{
		_bytes = new byte[initialSize];
	}
	
	private void write(byte value)
	{
		// Check size.
		if (_size == _bytes.length)
		{
			_bytes = Arrays.copyOf(_bytes, _bytes.length * 2);
		}
		
		// Set value.
		_bytes[_size++] = value;
	}
	
	public void writeBoolean(boolean value)
	{
		write((byte) (value ? 1 : 0));
	}
	
	public void writeString(String value)
	{
		if (value != null)
		{
			try
			{
				final byte[] byteArray = value.getBytes(StandardCharsets.UTF_8);
				writeShort(byteArray.length);
				writeBytes(byteArray);
			}
			catch (Exception e)
			{
				write((byte) 0);
			}
		}
		else
		{
			write((byte) 0);
		}
	}
	
	public void writeBytes(byte[] array)
	{
		for (int i = 0; i < array.length; i++)
		{
			write(array[i]);
		}
	}
	
	public void writeByte(int value)
	{
		write((byte) value);
	}
	
	public void writeShort(int value)
	{
		write((byte) value);
		write((byte) (value >> 8));
	}
	
	public void writeInt(int value)
	{
		write((byte) value);
		write((byte) (value >> 8));
		write((byte) (value >> 16));
		write((byte) (value >> 24));
	}
	
	public void writeLong(long value)
	{
		write((byte) value);
		write((byte) (value >> 8));
		write((byte) (value >> 16));
		write((byte) (value >> 24));
		write((byte) (value >> 32));
		write((byte) (value >> 40));
		write((byte) (value >> 48));
		write((byte) (value >> 56));
	}
	
	public void writeFloat(float value)
	{
		writeInt(Float.floatToRawIntBits(value));
	}
	
	public void writeDouble(double value)
	{
		writeLong(Double.doubleToRawLongBits(value));
	}
	
	public byte[] getSendableBytes()
	{
		// Get array of bytes.
		final byte[] byteArray = Arrays.copyOf(_bytes, _size);
		
		// Add size info at start (short - max length 32767).
		_size -= 2;
		byteArray[0] = (byte) _size;
		byteArray[1] = (byte) (_size >> 8);
		
		// Return the data.
		return byteArray;
	}
}
