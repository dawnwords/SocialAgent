package edu.fudan.sa.service.example.hello.test;

import edu.fudan.sa.service.WorkerService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

/**
 * @author ming
 * @since 2014-04-28 10:28.
 */
public class HelloServiceTestActivator implements BundleActivator {
    @Override
    public void start(BundleContext bundleContext) throws Exception {
        ServiceReference<WorkerService> wsr = bundleContext.getServiceReference(WorkerService.class);
        WorkerService ws = bundleContext.getService(wsr);
        HelloServiceTest hst = new HelloServiceTest();
        ws.work(hst);
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {

    }
}
