package common.managers;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import common.Config;
import common.threads.RejectedExecutionHandlerImpl;
import common.threads.RunnableWrapper;
import common.threads.ThreadProvider;

/**
 * This class is a thread pool manager that handles two types of thread pools, the scheduled pool and the instant pool, using a ScheduledThreadPoolExecutor and a ThreadPoolExecutor respectively.<br>
 * It uses the Config class to set the size of the pools and has a method to remove old tasks. It also provides scheduling methods and logs useful information in case of exceptions.
 * @author Pantelis Andrianakis
 */
public class ThreadManager
{
	private static final ScheduledThreadPoolExecutor SCHEDULED_POOL = new ScheduledThreadPoolExecutor(Config.SCHEDULED_THREAD_POOL_SIZE, new ThreadProvider("JMMOS ScheduledThread"), new ThreadPoolExecutor.CallerRunsPolicy());
	private static final ThreadPoolExecutor INSTANT_POOL = new ThreadPoolExecutor(Config.INSTANT_THREAD_POOL_SIZE, Integer.MAX_VALUE, 1, TimeUnit.MINUTES, new LinkedBlockingQueue<>(), new ThreadProvider("JMMOS Thread"));
	private static final long MAX_DELAY = 3155695200000L; // One hundred years.
	private static final long MIN_DELAY = 0L;
	
	public static void init()
	{
		LogManager.log("ThreadManager: Initialized");
		
		// Configure ScheduledThreadPoolExecutor.
		SCHEDULED_POOL.setRejectedExecutionHandler(new RejectedExecutionHandlerImpl());
		SCHEDULED_POOL.setRemoveOnCancelPolicy(true);
		SCHEDULED_POOL.prestartAllCoreThreads();
		
		// Configure ThreadPoolExecutor.
		INSTANT_POOL.setRejectedExecutionHandler(new RejectedExecutionHandlerImpl());
		INSTANT_POOL.prestartAllCoreThreads();
		
		// Schedule the purge task.
		scheduleAtFixedRate(ThreadManager::purge, 60000, 60000);
		
		// Log information.
		LogManager.log("...scheduled pool executor with " + Config.SCHEDULED_THREAD_POOL_SIZE + " total threads.");
		LogManager.log("...instant pool executor with " + Config.INSTANT_THREAD_POOL_SIZE + " total threads.");
		
	}
	
	public static void purge()
	{
		SCHEDULED_POOL.purge();
		INSTANT_POOL.purge();
	}
	
	/**
	 * Creates and executes a one-shot action that becomes enabled after the given delay.
	 * @param runnable : the task to execute.
	 * @param delay : the time from now to delay execution.
	 * @return a ScheduledFuture representing pending completion of the task and whose get() method will return null upon completion.
	 */
	public static ScheduledFuture<?> schedule(Runnable runnable, long delay)
	{
		try
		{
			return SCHEDULED_POOL.schedule(new RunnableWrapper(runnable), validate(delay), TimeUnit.MILLISECONDS);
		}
		catch (Exception e)
		{
			LogManager.log(runnable.getClass().getSimpleName() + System.lineSeparator() + e.getMessage() + System.lineSeparator() + e.getStackTrace());
			return null;
		}
	}
	
	/**
	 * Creates and executes a periodic action that becomes enabled first after the given initial delay.
	 * @param runnable : the task to execute.
	 * @param initialDelay : the time to delay first execution.
	 * @param period : the period between successive executions.
	 * @return a ScheduledFuture representing pending completion of the task and whose get() method will throw an exception upon cancellation.
	 */
	public static ScheduledFuture<?> scheduleAtFixedRate(Runnable runnable, long initialDelay, long period)
	{
		try
		{
			return SCHEDULED_POOL.scheduleAtFixedRate(new RunnableWrapper(runnable), validate(initialDelay), validate(period), TimeUnit.MILLISECONDS);
		}
		catch (Exception e)
		{
			LogManager.log(runnable.getClass().getSimpleName() + System.lineSeparator() + e.getMessage() + System.lineSeparator() + e.getStackTrace());
			return null;
		}
	}
	
	/**
	 * Executes the given task sometime in the future.
	 * @param runnable : the task to execute.
	 */
	public static void execute(Runnable runnable)
	{
		try
		{
			INSTANT_POOL.execute(new RunnableWrapper(runnable));
		}
		catch (Exception e)
		{
			LogManager.log(runnable.getClass().getSimpleName() + System.lineSeparator() + e.getMessage() + System.lineSeparator() + e.getStackTrace());
		}
	}
	
	/**
	 * @param delay : the delay to validate.
	 * @return a valid value, from MIN_DELAY to MAX_DELAY.
	 */
	private static long validate(long delay)
	{
		if (delay < MIN_DELAY)
		{
			final Exception e = new Exception();
			LogManager.log("ThreadManager found delay " + delay + "!");
			LogManager.log(e);
			return MIN_DELAY;
		}
		if (delay > MAX_DELAY)
		{
			final Exception e = new Exception();
			LogManager.log("ThreadManager found delay " + delay + "!");
			LogManager.log(e);
			return MAX_DELAY;
		}
		return delay;
	}
	
	/**
	 * Shutdown thread pooling system correctly. Send different informations.
	 */
	public static void close()
	{
		try
		{
			LogManager.log("ThreadManager: Shutting down.");
			SCHEDULED_POOL.shutdownNow();
			INSTANT_POOL.shutdownNow();
		}
		catch (Throwable t)
		{
			LogManager.log("ThreadManager: Problem at Shutting down. " + t.getMessage());
		}
	}
}