package edu.fudan.sa.service.basic;

import android.content.res.Resources;
import edu.fudan.sa.view.R;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import java.io.InputStream;

public class BasicServiceActivator implements BundleActivator {

    private String rootPath = "";
    private Resources resources;

    public BasicServiceActivator(Resources res, String rootPath) {
        this.resources = res;
        this.rootPath = rootPath;
    }

    public void start(BundleContext context) throws Exception {
        // get from R and install to apps private files dir

        InputStream is = resources.openRawResource(R.raw.repository);
        Bundle repository = context.installBundle(rootPath + "/felix/bundle/repository.jar", is);
        is = resources.openRawResource(R.raw.shell);
        Bundle shell = context.installBundle(rootPath + "/felix/bundle/shell.jar", is);
        is = resources.openRawResource(R.raw.ipojo);
        Bundle ipojo = context.installBundle(rootPath + "/felix/bundle/ipojo.jar", is);
        is = resources.openRawResource(R.raw.ipojoannotations);
        Bundle ipojoannotations = context.installBundle(rootPath + "/felix/bundle/ipojoannotations.jar", is);
        is = resources.openRawResource(R.raw.ipojoarch);
        Bundle ipojoarch = context.installBundle(rootPath + "/felix/bundle/ipojoarch.jar", is);
        is = resources.openRawResource(R.raw.configadmin);
        Bundle configadmin = context.installBundle(rootPath + "/felix/bundle/configadmin.jar", is);
//        is = resources.openRawResource(R.raw.eventadmin);
//        Bundle eventadmin = context.installBundle(rootPath + "/felix/bundle/eventadmin.jar", is);

        shell.start();
        repository.start();
        ipojo.start();
        ipojoannotations.start();
        ipojoarch.start();
//        eventadmin.start();
        configadmin.start();
    }

    public void stop(BundleContext context) throws Exception {
    }
}
