package common.util;

import java.io.FileInputStream;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * @author Pantelis Andrianakis
 * @version November 7th 2018
 */
public final class ConfigReader
{
	private static final Logger LOGGER = Logger.getLogger(ConfigReader.class.getName());
	
	private final Properties _properties = new Properties();
	private final String _fileName;
	
	public ConfigReader(String fileName)
	{
		_fileName = fileName;
		try
		{
			_properties.load(new FileInputStream(fileName));
		}
		catch (Exception e)
		{
			LOGGER.warning("Could not load " + fileName + ". " + e);
		}
	}
	
	public String getString(String config, String defaultValue)
	{
		if (!_properties.containsKey(config))
		{
			LOGGER.warning("Missing config " + config + " from file " + _fileName + ". Default value " + defaultValue + " will be used instead.");
			return defaultValue;
		}
		return _properties.getProperty(config);
	}
	
	public boolean getBoolean(String config, boolean defaultValue)
	{
		if (!_properties.containsKey(config))
		{
			LOGGER.warning("Missing config " + config + " from file " + _fileName + ". Default value " + defaultValue + " will be used instead.");
			return defaultValue;
		}
		
		try
		{
			return Boolean.parseBoolean(_properties.getProperty(config));
		}
		catch (Exception e)
		{
			LOGGER.warning("Config " + config + " from file " + _fileName + " should be Boolean. Using default value " + defaultValue + " instead.");
			return defaultValue;
		}
	}
	
	public int getInt(String config, int defaultValue)
	{
		if (!_properties.containsKey(config))
		{
			LOGGER.warning("Missing config " + config + " from file " + _fileName + ". Default value " + defaultValue + " will be used instead.");
			return defaultValue;
		}
		
		try
		{
			return Integer.parseInt(_properties.getProperty(config));
		}
		catch (Exception e)
		{
			LOGGER.warning("Config " + config + " from file " + _fileName + " should be Integer. Using default value " + defaultValue + " instead.");
			return defaultValue;
		}
	}
	
	public long getLong(String config, long defaultValue)
	{
		if (!_properties.containsKey(config))
		{
			LOGGER.warning("Missing config " + config + " from file " + _fileName + ". Default value " + defaultValue + " will be used instead.");
			return defaultValue;
		}
		
		try
		{
			return Long.parseLong(_properties.getProperty(config));
		}
		catch (Exception e)
		{
			LOGGER.warning("Config " + config + " from file " + _fileName + " should be Long. Using default value " + defaultValue + " instead.");
			return defaultValue;
		}
	}
	
	public float getFloat(String config, float defaultValue)
	{
		if (!_properties.containsKey(config))
		{
			LOGGER.warning("Missing config " + config + " from file " + _fileName + ". Default value " + defaultValue + " will be used instead.");
			return defaultValue;
		}
		
		try
		{
			return Float.parseFloat(_properties.getProperty(config));
		}
		catch (Exception e)
		{
			LOGGER.warning("Config " + config + " from file " + _fileName + " should be Float. Using default value " + defaultValue + " instead.");
			return defaultValue;
		}
	}
	
	public double getDouble(String config, double defaultValue)
	{
		if (!_properties.containsKey(config))
		{
			LOGGER.warning("Missing config " + config + " from file " + _fileName + ". Default value " + defaultValue + " will be used instead.");
			return defaultValue;
		}
		
		try
		{
			return Double.parseDouble(_properties.getProperty(config));
		}
		catch (Exception e)
		{
			LOGGER.warning("Config " + config + " from file " + _fileName + " should be Double. Using default value " + defaultValue + " instead.");
			return defaultValue;
		}
	}
}
