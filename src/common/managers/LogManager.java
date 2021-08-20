package common.managers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import common.Config;

/**
 * @author Pantelis Andrianakis
 * @version November 7th 2018
 */
public class LogManager
{
	public static final SimpleDateFormat LOG_DATE_FORMAT = new SimpleDateFormat("dd/MM HH:mm:ss");
	
	private static final int WRITE_TASK_DELAY = 1000;
	private static final SimpleDateFormat LOG_FILE_NAME_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	private static final String LOG_PATH = "log" + File.separator;
	private static final String LOG_FILE_EXT = ".txt";
	protected static final String LOG_FILE_CONSOLE = "Console ";
	protected static final String LOG_FILE_WORLD = "World ";
	protected static final String LOG_FILE_CHAT = "Chat ";
	protected static final String LOG_FILE_ADMIN = "Admin ";
	protected static final List<String> CONSOLE_LOG_CACHE = new ArrayList<>();
	protected static final List<String> WORLD_LOG_CACHE = new ArrayList<>();
	protected static final List<String> CHAT_LOG_CACHE = new ArrayList<>();
	protected static final List<String> ADMIN_LOG_CACHE = new ArrayList<>();
	protected static final List<Date> WORLD_DATE_CACHE = new ArrayList<>();
	protected static final List<Date> CHAT_DATE_CACHE = new ArrayList<>();
	protected static final List<Date> ADMIN_DATE_CACHE = new ArrayList<>();
	
	public static void init()
	{
		// Create Log directory used by LogManager.
		final File logFolder = new File(".", "log");
		logFolder.mkdir();
		
		// Start the write to disk task.
		ThreadManager.scheduleAtFixedRate(new LogManagerTask(), WRITE_TASK_DELAY, WRITE_TASK_DELAY);
	}
	
	private static class LogManagerTask implements Runnable
	{
		// Keep the same StringBuilder object.
		private static final StringBuilder STRING_BUILDER = new StringBuilder();
		
		public LogManagerTask()
		{
		}
		
		@Override
		public void run()
		{
			// Update time needed for file name format.
			final Date date = new Date();
			
			// Append to "log\Console yyyy-MM-dd.txt" file.
			int writeCount = CONSOLE_LOG_CACHE.size();
			if (writeCount > 0)
			{
				try (FileWriter fw = new FileWriter(getFileName(LOG_FILE_CONSOLE, date), true);
					BufferedWriter bw = new BufferedWriter(fw);
					PrintWriter out = new PrintWriter(bw))
				{
					for (int i = 0; i < writeCount; i++)
					{
						out.println(CONSOLE_LOG_CACHE.get(i));
					}
				}
				catch (Exception ignored)
				{
				}
				// Remove from cache.
				synchronized (CONSOLE_LOG_CACHE)
				{
					for (int i = writeCount - 1; i >= 0; i--)
					{
						CONSOLE_LOG_CACHE.remove(i);
					}
				}
			}
			
			// Append to "log\World yyyy-MM-dd.txt" file.
			writeCount = WORLD_LOG_CACHE.size();
			if (writeCount > 0)
			{
				try (FileWriter fw = new FileWriter(getFileName(LOG_FILE_WORLD, date), true);
					BufferedWriter bw = new BufferedWriter(fw);
					PrintWriter out = new PrintWriter(bw))
				{
					for (int i = 0; i < writeCount; i++)
					{
						STRING_BUILDER.setLength(0);
						STRING_BUILDER.append("[");
						STRING_BUILDER.append(LOG_DATE_FORMAT.format(WORLD_DATE_CACHE.get(i)));
						STRING_BUILDER.append("] ");
						STRING_BUILDER.append(WORLD_LOG_CACHE.get(i));
						out.println(STRING_BUILDER.toString());
					}
				}
				catch (Exception ignored)
				{
				}
				// Remove from cache.
				synchronized (WORLD_LOG_CACHE)
				{
					for (int i = writeCount - 1; i < 0; i--)
					{
						WORLD_LOG_CACHE.remove(i);
						WORLD_DATE_CACHE.remove(i);
					}
				}
			}
			
			// Append to "log\Chat yyyy-MM-dd.txt" file.
			writeCount = CHAT_LOG_CACHE.size();
			if (writeCount > 0)
			{
				try (FileWriter fw = new FileWriter(getFileName(LOG_FILE_CHAT, date), true);
					BufferedWriter bw = new BufferedWriter(fw);
					PrintWriter out = new PrintWriter(bw))
				{
					for (int i = 0; i < writeCount; i++)
					{
						STRING_BUILDER.setLength(0);
						STRING_BUILDER.append("[");
						STRING_BUILDER.append(LOG_DATE_FORMAT.format(CHAT_DATE_CACHE.get(i)));
						STRING_BUILDER.append("] ");
						STRING_BUILDER.append(CHAT_LOG_CACHE.get(i));
						out.println(STRING_BUILDER.toString());
					}
				}
				catch (Exception ignored)
				{
				}
				// Remove from cache.
				synchronized (CHAT_LOG_CACHE)
				{
					for (int i = writeCount - 1; i < 0; i--)
					{
						CHAT_LOG_CACHE.remove(i);
						CHAT_DATE_CACHE.remove(i);
					}
				}
			}
			
			// Append to "log\Admin yyyy-MM-dd.txt" file.
			writeCount = ADMIN_LOG_CACHE.size();
			if (writeCount > 0)
			{
				try (FileWriter fw = new FileWriter(getFileName(LOG_FILE_ADMIN, date), true);
					BufferedWriter bw = new BufferedWriter(fw);
					PrintWriter out = new PrintWriter(bw))
				{
					for (int i = 0; i < writeCount; i++)
					{
						STRING_BUILDER.setLength(0);
						STRING_BUILDER.append("[");
						STRING_BUILDER.append(LOG_DATE_FORMAT.format(ADMIN_DATE_CACHE.get(i)));
						STRING_BUILDER.append("] ");
						STRING_BUILDER.append(ADMIN_LOG_CACHE.get(i));
						out.println(STRING_BUILDER.toString());
					}
				}
				catch (Exception ignored)
				{
				}
				// Remove from cache.
				synchronized (ADMIN_LOG_CACHE)
				{
					for (int i = writeCount - 1; i < 0; i--)
					{
						ADMIN_LOG_CACHE.remove(i);
						ADMIN_DATE_CACHE.remove(i);
					}
				}
			}
		}
	}
	
	protected static String getFileName(String prefix, Date date)
	{
		final String formatedDate = LOG_FILE_NAME_FORMAT.format(date);
		String fileName = LOG_PATH + prefix + formatedDate + LOG_FILE_EXT;
		File file = new File(fileName);
		if (Config.LOG_FILE_SIZE_LIMIT_ENABLED && file.exists())
		{
			int counter = 1;
			long fileSize = file.length();
			while (fileSize >= Config.LOG_FILE_SIZE_LIMIT)
			{
				fileName = LOG_PATH + prefix + formatedDate + "-" + counter++ + LOG_FILE_EXT;
				file = new File(fileName);
				fileSize = file.exists() ? file.length() : 0;
			}
		}
		return fileName;
	}
	
	public static void log(Exception e)
	{
		final StringWriter errors = new StringWriter();
		e.printStackTrace(new PrintWriter(errors));
		log(errors.toString());
	}
	
	public static void log(String message)
	{
		// Format message with date.
		final StringBuilder sb = new StringBuilder();
		sb.append("[");
		sb.append(LOG_DATE_FORMAT.format(new Date()));
		sb.append("] ");
		sb.append(message);
		message = sb.toString();
		
		// Write to console.
		System.out.println(message);
		
		// Cache message for write to file task.
		synchronized (CONSOLE_LOG_CACHE)
		{
			CONSOLE_LOG_CACHE.add(message);
		}
	}
	
	public static void logWorld(String message)
	{
		// Keep current date.
		final Date date = new Date();
		
		// Cache date with message for write to file task.
		synchronized (WORLD_LOG_CACHE)
		{
			WORLD_DATE_CACHE.add(date);
			WORLD_LOG_CACHE.add(message);
		}
	}
	
	public static void logChat(String message)
	{
		// Keep current date.
		final Date date = new Date();
		
		// Cache date with message for write to file task.
		synchronized (CHAT_LOG_CACHE)
		{
			CHAT_DATE_CACHE.add(date);
			CHAT_LOG_CACHE.add(message);
		}
	}
	
	public static void logAdmin(String message)
	{
		// Keep current date.
		final Date date = new Date();
		
		// Cache date with message for write to file task.
		synchronized (ADMIN_LOG_CACHE)
		{
			ADMIN_DATE_CACHE.add(date);
			ADMIN_LOG_CACHE.add(message);
		}
	}
}
