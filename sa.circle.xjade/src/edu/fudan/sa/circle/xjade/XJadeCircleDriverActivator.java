package edu.fudan.sa.circle.xjade;

import edu.fudan.sa.IService;
import edu.fudan.sa.ISystemService;
import edu.fudan.sa.service.CircleDriver;
import edu.fudan.sa.service.HeartBeatService;
import edu.fudan.sa.service.InitialCircleDriver;
import jade.core.AID;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import java.util.Dictionary;
import java.util.Hashtable;

/**
 * @author ming
 * @since 2014-04-21 14:51.
 */
public class XJadeCircleDriverActivator implements BundleActivator {
	static BundleContext bundleContext;
	private XJadeCircleDriverImpl circleDriver;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		XJadeCircleDriverActivator.bundleContext = bundleContext;
		String[] classes = {IService.class.getName(), ISystemService.class.getName(), CircleDriver.class.getName(), InitialCircleDriver.class.getName(), HeartBeatService.HeartBeatListener.class.getName()};
		AID server = null;//TODO init circle manager server;
		this.circleDriver = new XJadeCircleDriverImpl(server);
		Dictionary<String, ?> metadata = new Hashtable<>();
		bundleContext.registerService(classes, this.circleDriver, metadata);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
	}
}
