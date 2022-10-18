package common.threads;

import java.util.concurrent.ThreadFactory;

/**
 * @author Pantelis Andrianakis
 * @since October 18th 2022
 */
public class ThreadProvider implements ThreadFactory
{
	private final String _name;
	
	public ThreadProvider(String name)
	{
		_name = name;
	}
	
	@Override
	public Thread newThread(Runnable runnable)
	{
		final Thread thread = new Thread(runnable, _name);
		thread.setPriority(Thread.NORM_PRIORITY);
		thread.setDaemon(false);
		return thread;
	}
}
