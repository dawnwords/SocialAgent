package edu.fudan.sa.service.rsi.dispatcher.impl;

import edu.fudan.sa.IService;
import edu.fudan.sa.ISystemService;
import edu.fudan.sa.service.WorkerService;
import edu.fudan.sa.service.rsi.InvocationDispatcher;
import edu.fudan.sa.service.rsi.ServiceInvocator;
import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;

import java.util.Dictionary;
import java.util.Hashtable;

/**
 * @author ming
 * @since 2014-04-21 15:07.
 */
public class InvocationDispatcherActivator extends DependencyActivatorBase {
	private InvocationDispatcherImpl dispatcher;

	@Override
	public void init(BundleContext context, DependencyManager manager) throws Exception {
		String[] classes = {IService.class.getName(), ISystemService.class.getName(), InvocationDispatcher.class.getName()};
		Dictionary<String, ?> properties = new Hashtable<String, Object>();
		dispatcher = InvocationDispatcherImpl.getInstance();
		manager.add(createComponent()
						.setInterface(classes, properties)
						.setImplementation(InvocationDispatcherImpl.getInstance())
						.add(createServiceDependency()
								.setService(ServiceInvocator.class)
								.setRequired(false)
								.setCallbacks("registerInvocator", "unregisterInvocator"))
						.add(createServiceDependency()
								.setService(WorkerService.class)
								.setRequired(false))
		);
		new Thread(this.dispatcher).start();
	}

	@Override
	public void destroy(BundleContext context, DependencyManager manager) throws Exception {
		this.dispatcher.stop();
	}
}
