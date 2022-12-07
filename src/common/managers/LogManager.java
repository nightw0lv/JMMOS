package common.managers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import common.Config;

/**
 * @author Pantelis Andrianakis
 * @since November 7th 2018
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
	protected static final Queue<String> CONSOLE_LOG_CACHE = new ConcurrentLinkedQueue<>();
	protected static final Queue<DateString> WORLD_LOG_CACHE = new ConcurrentLinkedQueue<>();
	protected static final Queue<DateString> CHAT_LOG_CACHE = new ConcurrentLinkedQueue<>();
	protected static final Queue<DateString> ADMIN_LOG_CACHE = new ConcurrentLinkedQueue<>();
	
	private static class DateString
	{
		private final Date _date;
		private final String _message;
		
		public DateString(Date date, String message)
		{
			_date = date;
			_message = message;
		}
		
		public Date getDate()
		{
			return _date;
		}
		
		public String getMessage()
		{
			return _message;
		}
	}
	
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
		// Keep the same objects.
		private static final StringBuilder STRING_BUILDER = new StringBuilder();
		private static boolean running = false;
		private static DateString log;
		
		public LogManagerTask()
		{
		}
		
		@Override
		public void run()
		{
			// Previous run has not finished.
			if (running)
			{
				return;
			}
			running = true;
			
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
						out.println(CONSOLE_LOG_CACHE.poll());
					}
				}
				catch (Exception ignored)
				{
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
						log = WORLD_LOG_CACHE.poll();
						STRING_BUILDER.setLength(0);
						STRING_BUILDER.append("[");
						STRING_BUILDER.append(LOG_DATE_FORMAT.format(log.getDate()));
						STRING_BUILDER.append("] ");
						STRING_BUILDER.append(log.getMessage());
						out.println(STRING_BUILDER.toString());
					}
				}
				catch (Exception ignored)
				{
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
						log = CHAT_LOG_CACHE.poll();
						STRING_BUILDER.setLength(0);
						STRING_BUILDER.append("[");
						STRING_BUILDER.append(LOG_DATE_FORMAT.format(log.getDate()));
						STRING_BUILDER.append("] ");
						STRING_BUILDER.append(log.getMessage());
						out.println(STRING_BUILDER.toString());
					}
				}
				catch (Exception ignored)
				{
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
						log = ADMIN_LOG_CACHE.poll();
						STRING_BUILDER.setLength(0);
						STRING_BUILDER.append("[");
						STRING_BUILDER.append(LOG_DATE_FORMAT.format(log.getDate()));
						STRING_BUILDER.append("] ");
						STRING_BUILDER.append(log.getMessage());
						out.println(STRING_BUILDER.toString());
					}
				}
				catch (Exception ignored)
				{
				}
			}
			
			// Done running.
			running = false;
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
		final String output = sb.toString();
		
		// Write to console.
		System.out.println(output);
		
		// Cache message for write to file task.
		CONSOLE_LOG_CACHE.add(output);
	}
	
	public static void logWorld(String message)
	{
		// Keep current date.
		final Date date = new Date();
		
		// Cache date with message for write to file task.
		WORLD_LOG_CACHE.add(new DateString(date, message));
	}
	
	public static void logChat(String message)
	{
		// Keep current date.
		final Date date = new Date();
		
		// Cache date with message for write to file task.
		CHAT_LOG_CACHE.add(new DateString(date, message));
	}
	
	public static void logAdmin(String message)
	{
		// Keep current date.
		final Date date = new Date();
		
		// Cache date with message for write to file task.
		ADMIN_LOG_CACHE.add(new DateString(date, message));
	}
}
