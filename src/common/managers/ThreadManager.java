package common.managers;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import common.Config;

/**
 * @author Pantelis Andrianakis
 * @version June 9th 2019
 */
public final class ThreadManager
{
	private static final ScheduledThreadPoolExecutor SCHEDULED_POOL = new ScheduledThreadPoolExecutor(Config.SCHEDULED_THREAD_POOL_COUNT);
	private static final ThreadPoolExecutor INSTANT_POOL = new ThreadPoolExecutor(Config.INSTANT_THREAD_POOL_COUNT, Config.INSTANT_THREAD_POOL_COUNT, 0, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(100000));
	
	public static void init()
	{
		// Set pool options.
		SCHEDULED_POOL.setRemoveOnCancelPolicy(true);
		SCHEDULED_POOL.prestartAllCoreThreads();
		INSTANT_POOL.prestartAllCoreThreads();
		
		// Launch purge task.
		scheduleAtFixedRate(new PurgeTask(), 60000, 60000);
		
		LogManager.log("ThreadPool: Initialized");
		LogManager.log("...scheduled pool executor with " + Config.SCHEDULED_THREAD_POOL_COUNT + " total threads.");
		LogManager.log("...instant pool executor with " + Config.INSTANT_THREAD_POOL_COUNT + " total threads.");
	}
	
	private static class PurgeTask implements Runnable
	{
		public PurgeTask()
		{
		}
		
		@Override
		public void run()
		{
			purge();
		}
	}
	
	protected static void purge()
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
			return SCHEDULED_POOL.schedule(new RunnableWrapper(runnable), delay, TimeUnit.MILLISECONDS);
		}
		catch (Exception e)
		{
			LogManager.log(e);
			return null;
		}
	}
	
	/**
	 * Creates and executes a periodic action that becomes enabled first after the given initial delay.
	 * @param runnable : the task to execute.
	 * @param initialDelay : the time to delay first execution.
	 * @param period : the period between successive executions.
	 * @return a ScheduledFuture representing pending completion of the task, and whose get() method will throw an exception upon cancellation.
	 */
	public static ScheduledFuture<?> scheduleAtFixedRate(Runnable runnable, long initialDelay, long period)
	{
		try
		{
			return SCHEDULED_POOL.scheduleAtFixedRate(new RunnableWrapper(runnable), initialDelay, period, TimeUnit.MILLISECONDS);
		}
		catch (Exception e)
		{
			LogManager.log(e);
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
			LogManager.log(e);
		}
	}
	
	/**
	 * Shutdown thread pooling system correctly. Send different informations.
	 */
	public static void close()
	{
		try
		{
			LogManager.log("ThreadPool: Shutting down.");
			SCHEDULED_POOL.shutdownNow();
			INSTANT_POOL.shutdownNow();
		}
		catch (Throwable t)
		{
			LogManager.log("ThreadPool: Problem at Shutting down. " + t.getMessage());
		}
	}
	
	private static class RunnableWrapper implements Runnable
	{
		private final Runnable _runnable;
		
		public RunnableWrapper(Runnable runnable)
		{
			_runnable = runnable;
		}
		
		@Override
		public void run()
		{
			try
			{
				_runnable.run();
			}
			catch (Throwable e)
			{
				final Thread t = Thread.currentThread();
				final UncaughtExceptionHandler h = t.getUncaughtExceptionHandler();
				if (h != null)
				{
					h.uncaughtException(t, e);
				}
			}
		}
	}
}