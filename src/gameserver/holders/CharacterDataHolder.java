package gameserver.holders;

/**
 * @author Pantelis Andrianakis
 * @version December 28th 2017
 */
public class CharacterDataHolder
{
	private String _name = "";
	private byte _slot = 0;
	private boolean _selected = false;
	private byte _race = 0;
	private float _height = 0.5f;
	private float _belly = 0.5f;
	private int _hairType = 0;
	private int _hairColor = 2695723;
	private int _skinColor = 15847869;
	private int _eyeColor = 2695723;
	private int _headItem = 0;
	private int _chestItem = 0;
	private int _handsItem = 0;
	private int _legsItem = 0;
	private int _feetItem = 0;
	private int _rightHandItem = 0;
	private int _leftHandItem = 0;
	private float _x = 0;
	private float _y = 0;
	private float _z = 0;
	private float _heading = 0;
	private long _experience = 0;
	private long _hp = 0;
	private long _mp = 0;
	private byte _accessLevel = 0;
	
	public String getName()
	{
		return _name;
	}
	
	public void setName(String name)
	{
		_name = name;
	}
	
	public byte getSlot()
	{
		return _slot;
	}
	
	public void setSlot(byte slot)
	{
		_slot = slot;
	}
	
	public boolean isSelected()
	{
		return _selected;
	}
	
	public void setSelected(boolean selected)
	{
		_selected = selected;
	}
	
	public byte getRace()
	{
		return _race;
	}
	
	public void setRace(byte race)
	{
		_race = race;
	}
	
	public float getHeight()
	{
		return _height;
	}
	
	public void setHeight(float height)
	{
		_height = height;
	}
	
	public float getBelly()
	{
		return _belly;
	}
	
	public void setBelly(float belly)
	{
		_belly = belly;
	}
	
	public int getHairType()
	{
		return _hairType;
	}
	
	public void setHairType(int hairType)
	{
		_hairType = hairType;
	}
	
	public int getHairColor()
	{
		return _hairColor;
	}
	
	public void setHairColor(int hairColor)
	{
		_hairColor = hairColor;
	}
	
	public int getSkinColor()
	{
		return _skinColor;
	}
	
	public void setSkinColor(int skinColor)
	{
		_skinColor = skinColor;
	}
	
	public int getEyeColor()
	{
		return _eyeColor;
	}
	
	public void setEyeColor(int eyeColor)
	{
		_eyeColor = eyeColor;
	}
	
	public int getHeadItem()
	{
		return _headItem;
	}
	
	public void setHeadItem(int headItem)
	{
		_headItem = headItem;
	}
	
	public int getChestItem()
	{
		return _chestItem;
	}
	
	public void setChestItem(int chestItem)
	{
		_chestItem = chestItem;
	}
	
	public int getHandsItem()
	{
		return _handsItem;
	}
	
	public void setHandsItem(int handsItem)
	{
		_handsItem = handsItem;
	}
	
	public int getLegsItem()
	{
		return _legsItem;
	}
	
	public void setLegsItem(int legsItem)
	{
		_legsItem = legsItem;
	}
	
	public int getFeetItem()
	{
		return _feetItem;
	}
	
	public void setFeetItem(int feetItem)
	{
		_feetItem = feetItem;
	}
	
	public int getRightHandItem()
	{
		return _rightHandItem;
	}
	
	public void setRightHandItem(int rightHandItem)
	{
		_rightHandItem = rightHandItem;
	}
	
	public int getLeftHandItem()
	{
		return _leftHandItem;
	}
	
	public void setLeftHandItem(int leftHandItem)
	{
		_leftHandItem = leftHandItem;
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
	
	public long getExperience()
	{
		return _experience;
	}
	
	public void setExperience(long experience)
	{
		_experience = experience;
	}
	
	public long getHp()
	{
		return _hp;
	}
	
	public void setHp(long hp)
	{
		_hp = hp;
	}
	
	public long getMp()
	{
		return _mp;
	}
	
	public void setMp(long mp)
	{
		_mp = mp;
	}
	
	public byte getAccessLevel()
	{
		return _accessLevel;
	}
	
	public void setAccessLevel(byte accessLevel)
	{
		_accessLevel = accessLevel;
	}
}
