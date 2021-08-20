package gameserver.holders;

/**
 * @author Pantelis Andrianakis
 * @version April 21st 2019
 */
public class AnimationHolder
{
	private final float _velocityX;
	private final float _velocityZ;
	private final boolean _triggerJump;
	private final boolean _isInWater;
	private final boolean _isGrounded;
	
	public AnimationHolder(float velocityX, float velocityZ, boolean triggerJump, boolean isInWater, boolean isGrounded)
	{
		_velocityX = velocityX;
		_velocityZ = velocityZ;
		_triggerJump = triggerJump;
		_isInWater = isInWater;
		_isGrounded = isGrounded;
	}
	
	public float getVelocityX()
	{
		return _velocityX;
	}
	
	public float getVelocityZ()
	{
		return _velocityZ;
	}
	
	public boolean isTriggerJump()
	{
		return _triggerJump;
	}
	
	public boolean isInWater()
	{
		return _isInWater;
	}
	
	public boolean isGrounded()
	{
		return _isGrounded;
	}
}
