package common.threads;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import common.managers.LogManager;

/**
 * @author NB4L1
 */
public class RejectedExecutionHandlerImpl implements RejectedExecutionHandler
{
	@Override
	public void rejectedExecution(Runnable runnable, ThreadPoolExecutor executor)
	{
		if (executor.isShutdown())
		{
			return;
		}
		
		LogManager.log(runnable.getClass().getSimpleName() + System.lineSeparator() + runnable + " from " + executor + " " + new RejectedExecutionException());
		
		if (Thread.currentThread().getPriority() > Thread.NORM_PRIORITY)
		{
			new Thread(runnable).start();
		}
		else
		{
			runnable.run();
		}
	}
}
