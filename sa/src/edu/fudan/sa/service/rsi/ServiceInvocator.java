package edu.fudan.sa.service.rsi;

import edu.fudan.sa.service.WorkerService;

/**
 * @author ming
 * @since 2014-03-27 09:52.
 */
public interface ServiceInvocator {
	boolean execute(ServiceInvocation invocation, WorkerService workerService);

	String getNamespace();
}
