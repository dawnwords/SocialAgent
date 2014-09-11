package edu.fudan.sa.platform;

import android.content.Context;
import edu.fudan.sa.service.basic.BasicServiceActivator;
import edu.fudan.sa.view.R;
import org.apache.felix.eventadmin.impl.Activator;
import org.apache.felix.fileinstall.internal.FileInstall;
import org.apache.felix.framework.Felix;
import org.apache.felix.framework.util.FelixConstants;
import org.osgi.framework.*;

import java.io.InputStream;
import java.util.*;

class ServicePlatformImpl extends ServicePlatform {
    private Context context;
    private Properties configuration;
    private Felix felix;
    private Set<BundleListener> listeners = new HashSet<BundleListener>();

    ServicePlatformImpl(Context context) {
        this.context = context;
        this.init();
    }

    private void init() {
        String rootPath = this.context.getFilesDir().getAbsolutePath();
        InputStream configFile = this.context.getResources().openRawResource(R.raw.config);
        configuration = ServicePlatformConfig.getConfig(rootPath, configFile);

        // activator which loads bundles from Res and installs to file dir and starts the bundles
        BasicServiceActivator initActivator = new BasicServiceActivator(this.context.getResources(), rootPath);
        FileInstall fileinstall = new FileInstall();
        Activator eventAdmin = new Activator();
//        ConfigurationManager configAdmin = new ConfigurationManager();
        org.apache.felix.metatype.internal.Activator metatype = new org.apache.felix.metatype.internal.Activator();
        org.apache.felix.dm.runtime.Activator dependencyManager = new org.apache.felix.dm.runtime.Activator();
//        PreferencesManager preferencesManager = new PreferencesManager();
        org.apache.felix.log.Activator log = new org.apache.felix.log.Activator();

        List<BundleActivator> activatorList = new ArrayList<BundleActivator>();
        activatorList.add(metatype);
        activatorList.add(log);
//        activatorList.add(preferencesManager);
        activatorList.add(dependencyManager);
        activatorList.add(eventAdmin);
//        activatorList.add(configAdmin);
        activatorList.add(fileinstall);
        activatorList.add(initActivator);

        // add list of activators which shall be started with system bundle to config
        configuration.put(FelixConstants.SYSTEMBUNDLE_ACTIVATORS_PROP, activatorList);
    }

    @Override
    public void startup() throws BundleException {
        felix = new Felix(configuration);
        felix.start();
        final BundleContext bundleContext = felix.getBundleContext();
        bundleContext.addBundleListener(new BundleListener() {
            @Override
            public void bundleChanged(BundleEvent bundleEvent) {
                notifyBundleEvent(bundleEvent);
            }
        });
        bundleContext.registerService(Context.class, this.context.getApplicationContext(), new Hashtable<String, Object>());
    }

    private void notifyBundleEvent(BundleEvent bundleEvent) {
        System.out.println("*********bundle event:" + bundleEvent.getType());
        for (BundleListener listener : this.listeners) {
            listener.bundleChanged(bundleEvent);
        }
    }

    @Override
    public void addBundleListener(BundleListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public Bundle getBundle(long bundleId) {
        return this.felix.getBundleContext().getBundle(bundleId);
    }

    @Override
    public Bundle[] getBundles() {
        Bundle[] bundles = this.felix.getBundleContext().getBundles();
        if (bundles == null)
            bundles = new Bundle[]{};
        return bundles;
    }

    @Override
    public void shutdown() {
        try {
            felix.stop();
        } catch (BundleException e) {
            e.printStackTrace();
            System.out.println("Cannot stop HostApplication");
        }
        try {
            felix.waitForStop(10000);
        } catch (InterruptedException e) {
            System.out.println("Thread has waited and was then interrupted");
            e.printStackTrace();
        }
        felix = null;
    }
}
