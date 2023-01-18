package common.threads;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Pantelis Andrianakis
 * @since October 18th 2022
 */
public class ThreadProvider implements ThreadFactory
{
	private final AtomicInteger _id = new AtomicInteger();
	private final String _prefix;
	
	public ThreadProvider(String prefix)
	{
		_prefix = prefix + " ";
	}
	
	@Override
	public Thread newThread(Runnable runnable)
	{
		final Thread thread = new Thread(runnable, _prefix + _id.incrementAndGet());
		thread.setPriority(Thread.NORM_PRIORITY);
		thread.setDaemon(false);
		return thread;
	}
}
