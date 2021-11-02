package common.managers;

import java.sql.Connection;

import org.mariadb.jdbc.MariaDbPoolDataSource;

import common.Config;

/**
 * @author Pantelis Andrianakis
 * @since November 10th 2018
 */
public class DatabaseManager
{
	private static final MariaDbPoolDataSource DATABASE_POOL = new MariaDbPoolDataSource(Config.DATABASE_URL + "&user=" + Config.DATABASE_LOGIN + "&password=" + Config.DATABASE_PASSWORD + "&maxPoolSize=" + Config.DATABASE_MAX_CONNECTIONS);
	
	public static void init()
	{
		// Test if connection is valid.
		try
		{
			DATABASE_POOL.getConnection().close();
			LogManager.log("Database: Initialized.");
		}
		catch (Exception e)
		{
			LogManager.log(e);
		}
	}
	
	public static Connection getConnection()
	{
		Connection con = null;
		while (con == null)
		{
			try
			{
				con = DATABASE_POOL.getConnection();
			}
			catch (Exception e)
			{
				LogManager.log("DatabaseManager: Cound not get a connection.");
				LogManager.log(e);
			}
		}
		return con;
	}
	
	public static void close()
	{
		try
		{
			DATABASE_POOL.close();
		}
		catch (Exception e)
		{
			LogManager.log("DatabaseManager: There was a problem closing the data source.");
			LogManager.log(e);
		}
	}
}
