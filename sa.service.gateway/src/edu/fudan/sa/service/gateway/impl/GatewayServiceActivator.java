package edu.fudan.sa.service.gateway.impl;

import android.content.Context;
import edu.fudan.sa.IService;
import edu.fudan.sa.ISystemService;
import edu.fudan.sa.android.GUIService;
import edu.fudan.sa.android.service.GatewayService;
import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.event.EventAdmin;

import java.util.Dictionary;
import java.util.Hashtable;

public class GatewayServiceActivator extends DependencyActivatorBase {

    @Override
    public void init(BundleContext context, DependencyManager manager) throws Exception {
        System.out.println("***gateway bundle installing***");
        Dictionary config = new Hashtable();
        config.put("service.pid", GatewayService.PID_GATEWAY_CONFIG);

        ServiceReference<Context> cs = context.getServiceReference(Context.class);
        ServiceReference<EventAdmin> es = context.getServiceReference(EventAdmin.class);

        String[] classes = {IService.class.getName(), ISystemService.class.getName(), GUIService.class.getName(), GatewayService.class.getName(), ManagedService.class.getName()};
        manager.add(createComponent()
                        .setInterface(classes, config)
                        .setImplementation(GUIAndroidGatewayServiceImpl.getInstance())
                        .add(createServiceDependency()
                                .setService(Context.class)
                                .setRequired(true))
                        .add(createServiceDependency()
                                .setService(EventAdmin.class)
                                .setRequired(false))
        );
    }

    @Override
    public void destroy(BundleContext context, DependencyManager manager) throws Exception {
        manager.clear();
    }
}
