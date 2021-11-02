package gameserver.holders;

/**
 * @author Pantelis Andrianakis
 * @since November 7th 2018
 */
public class LocationHolder
{
	float _x;
	float _y;
	float _z;
	float _heading;
	
	public LocationHolder(float x, float y, float z)
	{
		this(x, y, z, 0);
	}
	
	public LocationHolder(float x, float y, float z, float heading)
	{
		_x = x;
		_y = y;
		_z = z;
		_heading = heading;
	}
	
	public float getX()
	{
		return _x;
	}
	
	public void setX(float x)
	{
		_x = x;
	}
	
	public float getY()
	{
		return _y;
	}
	
	public void setY(float y)
	{
		_y = y;
	}
	
	public float getZ()
	{
		return _z;
	}
	
	public void setZ(float z)
	{
		_z = z;
	}
	
	public float getHeading()
	{
		return _heading;
	}
	
	public void setHeading(float heading)
	{
		_heading = heading;
	}
	
	public void update(float x, float y, float z, float heading)
	{
		_x = x;
		_y = y;
		_z = z;
		_heading = heading;
	}
	
	public String tToString()
	{
		final String result = _x + " " + _y + " " + _z;
		return "Location [" + (_heading > 0 ? result + " " + _heading : result) + "]";
	}
}
