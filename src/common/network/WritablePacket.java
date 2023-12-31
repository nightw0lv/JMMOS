package common.network;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Writable packet backed up by a byte array, with a maximum raw data size of 65533 bytes.
 * @author Pantelis Andrianakis
 * @since October 29th 2020
 */
public abstract class WritablePacket
{
	private byte[] _data;
	private byte[] _sendableBytes;
	private int _position = 2; // Allocate space for size (max length 65535 - size header).
	
	/**
	 * Construct a WritablePacket with an initial data size of 32 bytes.
	 */
	protected WritablePacket()
	{
		this(32);
	}
	
	/**
	 * Construct a WritablePacket with a given initial data size.
	 * @param initialSize
	 */
	protected WritablePacket(int initialSize)
	{
		_data = new byte[initialSize];
	}
	
	public void write(byte value)
	{
		// Check current size.
		if (_position < 65535)
		{
			// Check capacity.
			if (_position == _data.length)
			{
				_data = Arrays.copyOf(_data, _data.length * 2); // Double the capacity.
			}
			
			// Set value.
			_data[_position++] = value;
			return;
		}
		
		throw new IndexOutOfBoundsException("Packet data exceeded the raw data size limit of 65533!");
	}
	
	/**
	 * Write <b>boolean</b> to the packet data.<br>
	 * 8bit integer (00) or (01)
	 * @param value
	 */
	public void writeBoolean(boolean value)
	{
		writeByte(value ? 1 : 0);
	}
	
	/**
	 * Write <b>String</b> to the packet data.
	 * @param text
	 */
	public void writeString(String text)
	{
		if (text != null)
		{
			try
			{
				final byte[] byteArray = text.getBytes(StandardCharsets.UTF_8);
				writeShort(byteArray.length & 0xffff);
				writeBytes(byteArray);
			}
			catch (Exception e)
			{
				writeShort(0);
			}
		}
		else
		{
			writeShort(0);
		}
	}
	
	/**
	 * Write <b>byte[]</b> to the packet data.<br>
	 * 8bit integer array (00...)
	 * @param array
	 */
	public void writeBytes(byte[] array)
	{
		for (int i = 0; i < array.length; i++)
		{
			write(array[i]);
		}
	}
	
	/**
	 * Write <b>byte</b> to the packet data.<br>
	 * 8bit integer (00)
	 * @param value
	 */
	public void writeByte(int value)
	{
		write((byte) value);
	}
	
	/**
	 * Write <b>boolean</b> to the packet data.<br>
	 * 8bit integer (00) or (01)
	 * @param value
	 */
	public void writeByte(boolean value)
	{
		writeByte(value ? 1 : 0);
	}
	
	/**
	 * Write <b>short</b> to the packet data.<br>
	 * 16bit integer (00 00)
	 * @param value
	 */
	public void writeShort(int value)
	{
		write((byte) value);
		write((byte) (value >> 8));
	}
	
	/**
	 * Write <b>boolean</b> to the packet data.<br>
	 * 16bit integer (00 00)
	 * @param value
	 */
	public void writeShort(boolean value)
	{
		writeShort(value ? 1 : 0);
	}
	
	/**
	 * Write <b>int</b> to the packet data.<br>
	 * 32bit integer (00 00 00 00)
	 * @param value
	 */
	public void writeInt(int value)
	{
		write((byte) value);
		write((byte) (value >> 8));
		write((byte) (value >> 16));
		write((byte) (value >> 24));
	}
	
	/**
	 * Write <b>boolean</b> to the packet data.<br>
	 * 32bit integer (00 00 00 00)
	 * @param value
	 */
	public void writeInt(boolean value)
	{
		writeInt(value ? 1 : 0);
	}
	
	/**
	 * Write <b>long</b> to the packet data.<br>
	 * 64bit integer (00 00 00 00 00 00 00 00)
	 * @param value
	 */
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
	
	/**
	 * Write <b>boolean</b> to the packet data.<br>
	 * 64bit integer (00 00 00 00 00 00 00 00)
	 * @param value
	 */
	public void writeLong(boolean value)
	{
		writeLong(value ? 1 : 0);
	}
	
	/**
	 * Write <b>float</b> to the packet data.<br>
	 * 32bit single precision float (00 00 00 00)
	 * @param value
	 */
	public void writeFloat(float value)
	{
		writeInt(Float.floatToRawIntBits(value));
	}
	
	/**
	 * Write <b>double</b> to the packet data.<br>
	 * 64bit double precision float (00 00 00 00 00 00 00 00)
	 * @param value
	 */
	public void writeDouble(double value)
	{
		writeLong(Double.doubleToRawLongBits(value));
	}
	
	/**
	 * @return <b>byte[]</b> of the sendable packet data, including a size header.
	 */
	public byte[] getSendableBytes()
	{
		return getSendableBytes(null);
	}
	
	/**
	 * @param encryption if EncryptionInterface is used.
	 * @return <b>byte[]</b> of the sendable packet data, including a size header.
	 */
	public synchronized byte[] getSendableBytes(EncryptionInterface encryption)
	{
		// Generate sendable byte array.
		if ((_sendableBytes == null /* Not processed */) || (encryption != null /* Encryption can change */))
		{
			// Check if data was written.
			if (_position > 2)
			{
				// Trim array of data.
				_sendableBytes = Arrays.copyOf(_data, _position);
				
				// Add size info at start (unsigned short - max size 65535).
				final int position = _position - 2;
				_sendableBytes[0] = (byte) (position & 0xff);
				_sendableBytes[1] = (byte) ((position >> 8) & 0xffff);
				
				// Encrypt data.
				if (encryption != null)
				{
					encryption.encrypt(_sendableBytes, 2, position);
				}
			}
		}
		
		// Return the data.
		return _sendableBytes;
	}
	
	/**
	 * @return The length of the data (includes size header).
	 */
	public int getLength()
	{
		return _position;
	}
}
