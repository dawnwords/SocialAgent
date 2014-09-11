package edu.fudan.sa.service.rsi.dispatcher.impl;

import edu.fudan.sa.ontology.Service;
import edu.fudan.sa.service.WorkerService;
import edu.fudan.sa.service.rsi.InvocationDispatcher;
import edu.fudan.sa.service.rsi.ServiceInvocation;
import edu.fudan.sa.service.rsi.ServiceInvocator;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * @author ming
 * @since 2014-04-13 20:24.
 */
public class InvocationDispatcherImpl implements InvocationDispatcher, Runnable {

	private static InvocationDispatcherImpl instance;
	private volatile WorkerService workerService;
	private HashMap<String, List<ServiceInvocator>> invocators = new HashMap<String, List<ServiceInvocator>>();
	private BlockingQueue<ServiceInvocation> serviceInvocations = new LinkedBlockingQueue<ServiceInvocation>();
	private boolean toStop = false;

	private InvocationDispatcherImpl() {
	}

	public static InvocationDispatcherImpl getInstance() {
		if (instance == null) {
			instance = new InvocationDispatcherImpl();
		}
		return instance;
	}

	@Override
	public void registerInvocator(ServiceInvocator serviceInvocator) {
		String namespace = serviceInvocator.getNamespace();
		List<ServiceInvocator> serviceInvocators = this.invocators.get(namespace);
		if (serviceInvocators == null)
			serviceInvocators = new LinkedList<ServiceInvocator>();
		if (!serviceInvocators.contains(serviceInvocator))
			serviceInvocators.add(serviceInvocator);
	}

	@Override
	public void unregisterInvocator(ServiceInvocator serviceInvocator) {
		String namespace = serviceInvocator.getNamespace();
		List<ServiceInvocator> serviceInvocators = this.invocators.get(namespace);
		if (serviceInvocators == null)
			serviceInvocators.remove(serviceInvocator);
	}

	@Override
	public boolean addInvocation(ServiceInvocation serviceInvocation) {
		if (this.toStop)
			return false;
		return this.serviceInvocations.offer(serviceInvocation);
	}

	@Override
	public boolean isToStop() {
		return this.toStop;
	}

	@Override
	public void stop() {
		this.toStop = true;
	}

	//TODO can also be implemented with CyclicBehaviour
	@Override
	public void run() {
		while (!toStop) {
			ServiceInvocation invocation = null;
			try {
				invocation = this.serviceInvocations.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			List<ServiceInvocator> serviceInvocators = this.invocators.get(invocation.getNamespace());
			if (serviceInvocators == null || serviceInvocators.size() < 1) {
				DefaultServiceInvocator invocator = new DefaultServiceInvocator();
				invocator.execute(invocation, workerService);
				continue;
			}
			for (ServiceInvocator invocator : serviceInvocators) {
				if (!invocator.execute(invocation, workerService)) {
					break;
				}
			}
		}
	}

	@Override
	public Service getDescription() {
		return null;
	}
}
