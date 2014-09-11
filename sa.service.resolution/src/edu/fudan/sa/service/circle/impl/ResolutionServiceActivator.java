package edu.fudan.sa.service.circle.impl;

import edu.fudan.sa.IService;
import edu.fudan.sa.ISystemService;
import edu.fudan.sa.service.ResolutionService;
import edu.fudan.sa.service.ServiceResolver;
import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;

import java.util.Dictionary;
import java.util.Hashtable;

/**
 * @author ming
 * @since 2014-04-08 10:07.
 */
public class ResolutionServiceActivator extends DependencyActivatorBase {

    @Override
    public void init(BundleContext context, DependencyManager manager) throws Exception {
        String[] classes = {IService.class.getName(), ISystemService.class.getName(), ResolutionService.class.getName()};
        Dictionary<String, ?> properties = new Hashtable<String, Object>();
        manager.add(createComponent()
                        .setInterface(classes, properties)
                        .setImplementation(ResolutionServiceImpl.getInstance())
                        .add(createServiceDependency()
                                .setService(ServiceResolver.class)
                                .setRequired(false)
                                .setCallbacks("registerResolver", "unregisterResolver"))
        );
    }

    @Override
    public void destroy(BundleContext context, DependencyManager manager) throws Exception {

    }
}
