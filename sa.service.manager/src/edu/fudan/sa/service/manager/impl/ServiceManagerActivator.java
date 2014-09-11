package edu.fudan.sa.service.manager.impl;

import edu.fudan.sa.IService;
import edu.fudan.sa.ISystemService;
import edu.fudan.sa.android.GUIService;
import edu.fudan.sa.service.CircleManager;
import edu.fudan.sa.service.PublicationService;
import edu.fudan.sa.service.ServiceManager;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import java.util.Dictionary;
import java.util.Hashtable;

/**
 * @author ming
 * @since 2014-03-27 10:53.
 */
public class ServiceManagerActivator implements BundleActivator {
	static BundleContext bundleContext;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		ServiceManagerActivator.bundleContext = bundleContext;
		String[] classes = {IService.class.getName(), ISystemService.class.getName(), GUIService.class.getName(),
				ServiceManager.class.getName(), CircleManager.class.getName(), PublicationService.class.getName()};
		Dictionary<String, ?> metadata = new Hashtable<String, Object>();
		bundleContext.registerService(classes, GUIAndroidServiceManager.getInstance(), metadata);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {

	}
}
