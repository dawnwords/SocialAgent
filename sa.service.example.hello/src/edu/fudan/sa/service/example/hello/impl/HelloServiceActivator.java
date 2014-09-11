package edu.fudan.sa.service.example.hello.impl;

import edu.fudan.sa.IRemotableService;
import edu.fudan.sa.IService;
import edu.fudan.sa.service.example.hello.HelloService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import java.util.Dictionary;
import java.util.Hashtable;

/**
 * edu.fudan.sa.service.example.hello.impl.HelloServiceActivator
 *
 * @author ming
 * @since 2014-03-21 18:40.
 */
public class HelloServiceActivator implements BundleActivator {

    @Override
    public void start(BundleContext bundleContext) throws Exception {
        String[] classes = {IService.class.getName(), IRemotableService.class.getName(),HelloService.class.getName()};
        Dictionary<String, String> props = new Hashtable<String, String>();
        props.put("type", "asynchronized");
        HelloService helloService = new HelloServiceImpl();
        bundleContext.registerService(classes, helloService, props);
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {

    }
}
