package common.managers;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import common.Config;
import common.threads.RejectedExecutionHandlerImpl;
import common.threads.RunnableWrapper;

/**
 * This class handles thread pooling system.<br>
 * It relies on two ThreadPoolExecutor arrays, which pool size is set using config.<br>
 * Those arrays hold following pools:<br>
 * <ul>
 * <li>Scheduled pool keeps a track about incoming, future events.</li>
 * <li>Instant pool handles short-life events.</li>
 * </ul>
 */
public final class ThreadManager
{
	private static final ScheduledThreadPoolExecutor[] SCHEDULED_POOLS = new ScheduledThreadPoolExecutor[Config.SCHEDULED_THREAD_POOL_COUNT];
	private static final ThreadPoolExecutor[] INSTANT_POOLS = new ThreadPoolExecutor[Config.INSTANT_THREAD_POOL_COUNT];
	private static int SCHEDULED_THREAD_RANDOMIZER = 0;
	private static int INSTANT_THREAD_RANDOMIZER = 0;
	
	public static void init()
	{
		LogManager.log("ThreadManager: Initialized");
		
		// Feed scheduled pool.
		for (int i = 0; i < Config.SCHEDULED_THREAD_POOL_COUNT; i++)
		{
			SCHEDULED_POOLS[i] = new ScheduledThreadPoolExecutor(Config.THREADS_PER_SCHEDULED_THREAD_POOL);
		}
		
		LogManager.log("..." + Config.SCHEDULED_THREAD_POOL_COUNT + " scheduled pool executors with " + (Config.SCHEDULED_THREAD_POOL_COUNT * Config.THREADS_PER_SCHEDULED_THREAD_POOL) + " total threads.");
		
		// Feed instant pool.
		for (int i = 0; i < Config.INSTANT_THREAD_POOL_COUNT; i++)
		{
			INSTANT_POOLS[i] = new ThreadPoolExecutor(Config.THREADS_PER_INSTANT_THREAD_POOL, Config.THREADS_PER_INSTANT_THREAD_POOL, 0, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100000));
		}
		
		LogManager.log("..." + Config.INSTANT_THREAD_POOL_COUNT + " instant pool executors with " + (Config.INSTANT_THREAD_POOL_COUNT * Config.THREADS_PER_INSTANT_THREAD_POOL) + " total threads.");
		
		// Prestart core threads.
		for (ScheduledThreadPoolExecutor threadPool : SCHEDULED_POOLS)
		{
			threadPool.setRejectedExecutionHandler(new RejectedExecutionHandlerImpl());
			threadPool.setRemoveOnCancelPolicy(true);
			threadPool.prestartAllCoreThreads();
		}
		
		for (ThreadPoolExecutor threadPool : INSTANT_POOLS)
		{
			threadPool.setRejectedExecutionHandler(new RejectedExecutionHandlerImpl());
			threadPool.prestartAllCoreThreads();
		}
		
		// Launch purge task.
		scheduleAtFixedRate(ThreadManager::purge, 60000, 60000);
	}
	
	public static void purge()
	{
		for (ScheduledThreadPoolExecutor threadPool : SCHEDULED_POOLS)
		{
			threadPool.purge();
		}
		
		for (ThreadPoolExecutor threadPool : INSTANT_POOLS)
		{
			threadPool.purge();
		}
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
			return SCHEDULED_POOLS[SCHEDULED_THREAD_RANDOMIZER++ % Config.SCHEDULED_THREAD_POOL_COUNT].schedule(new RunnableWrapper(runnable), delay, TimeUnit.MILLISECONDS);
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
			return SCHEDULED_POOLS[SCHEDULED_THREAD_RANDOMIZER++ % Config.SCHEDULED_THREAD_POOL_COUNT].scheduleAtFixedRate(new RunnableWrapper(runnable), initialDelay, period, TimeUnit.MILLISECONDS);
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
			INSTANT_POOLS[INSTANT_THREAD_RANDOMIZER++ % Config.INSTANT_THREAD_POOL_COUNT].execute(new RunnableWrapper(runnable));
		}
		catch (Exception e)
		{
			LogManager.log(runnable.getClass().getSimpleName() + System.lineSeparator() + e.getMessage() + System.lineSeparator() + e.getStackTrace());
		}
	}
	
	/**
	 * Shutdown thread pooling system correctly. Send different informations.
	 */
	public static void close()
	{
		try
		{
			LogManager.log("ThreadManager: Shutting down.");
			
			for (ScheduledThreadPoolExecutor threadPool : SCHEDULED_POOLS)
			{
				threadPool.shutdownNow();
			}
			
			for (ThreadPoolExecutor threadPool : INSTANT_POOLS)
			{
				threadPool.shutdownNow();
			}
		}
		catch (Throwable t)
		{
			LogManager.log("ThreadManager: Problem at Shutting down. " + t.getMessage());
		}
	}
}