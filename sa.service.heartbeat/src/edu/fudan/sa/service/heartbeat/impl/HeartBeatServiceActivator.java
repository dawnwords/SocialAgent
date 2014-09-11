package edu.fudan.sa.service.heartbeat.impl;

import edu.fudan.sa.IService;
import edu.fudan.sa.ISystemService;
import edu.fudan.sa.service.HeartBeatService;
import edu.fudan.sa.service.ResourceManager;
import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;

import java.util.Dictionary;
import java.util.Hashtable;

/**
 * @author ming
 * @since 2014-06-15 14:36.
 */
public class HeartBeatServiceActivator extends DependencyActivatorBase {

	@Override
	public void init(BundleContext context, DependencyManager manager) throws Exception {
		String[] classes = {IService.class.getName(), ISystemService.class.getName(), HeartBeatService.class.getName(), ResourceManager.class.getName()};
		Dictionary<String, ?> properties = new Hashtable<String, Object>();
		manager.add(createComponent()
						.setInterface(classes, properties)
						.setImplementation(HeartBeatServiceImpl.getInstance())
						.add(createServiceDependency()
								.setService(HeartBeatService.HeartBeatListener.class)
								.setRequired(false)
								.setCallbacks("addListener", "removeListener"))
		);
	}

	@Override
	public void destroy(BundleContext context, DependencyManager manager) throws Exception {

	}
}
