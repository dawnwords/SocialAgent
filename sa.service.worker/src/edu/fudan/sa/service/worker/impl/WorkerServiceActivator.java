package edu.fudan.sa.service.worker.impl;

import edu.fudan.sa.IService;
import edu.fudan.sa.android.GUIService;
import edu.fudan.sa.android.service.GatewayService;
import edu.fudan.sa.service.ResolutionService;
import edu.fudan.sa.service.WorkerService;
import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;

import java.io.Serializable;
import java.util.Dictionary;
import java.util.Hashtable;

public class WorkerServiceActivator extends DependencyActivatorBase {

    public static long bundleId;

    @Override
    public void init(BundleContext bundleContext, DependencyManager manager) throws Exception {
        System.out.println("***worker bundle installing***");
        Dictionary<String, Serializable> config = new Hashtable<String, Serializable>();
        config.put("name", "worker agent proxy");

        WorkerServiceActivator.bundleId = bundleContext.getBundle().getBundleId();

        String[] topics = new String[]{GatewayService.TOPIC_GATEWAY};
        config.put(EventConstants.EVENT_TOPIC, topics);
        String[] classes = {IService.class.getName(), GUIService.class.getName(), WorkerService.class.getName(), EventHandler.class.getName()};
        manager.add(createComponent()
                        .setInterface(classes, config)
                        .setImplementation(GUIWorkerServiceImpl.getInstance())
                        .add(createServiceDependency()
                                .setService(GatewayService.class)
                                .setRequired(false))
                        .add(createServiceDependency()
                                .setService(ConfigurationAdmin.class)
                                .setRequired(false))
                        .add(createServiceDependency()
                                .setService(ResolutionService.class)
                                .setRequired(true))
        );
    }

    @Override
    public void destroy(BundleContext context, DependencyManager manager) throws Exception {
        manager.clear();
    }
}
