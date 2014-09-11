package edu.fudan.sa.platform;

import android.content.Context;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;
import org.osgi.framework.BundleListener;

public abstract class ServicePlatform {
    private static ServicePlatform platform;

    public abstract void addBundleListener(BundleListener listener);

    public abstract Bundle getBundle(long bundleId);

    public abstract Bundle[] getBundles();

    public abstract void startup() throws BundleException;

    public abstract void shutdown();

    public static ServicePlatform getPlatform(Context context) {
        if (platform == null)
            platform = new ServicePlatformImpl(context);
        return platform;
    }
}
