/*
 * Apache Felix OSGi tutorial.
 **/

package edu.fudan.sa.example.impl;

import edu.fudan.sa.android.GUIService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import java.util.Hashtable;

/**
 * This class implements a simple bundle that uses the bundle context to
 * publish an English language dictionary service with the OSGi framework. The
 * dictionary service interface is defined in a separate class file and is
 * implemented by an inner class.
 */
public class ViewDictionaryServiceActivator implements BundleActivator {

    /**
     * Implements BundleActivator.start(). Registers an instance of a dictionary
     * service using the bundle context; attaches properties to the service that
     * can be queried when performing a service look-up.
     *
     * @param context the framework context for the bundle.
     */
    public void start(BundleContext context) {
        System.out.println("****************BdView Service bundle started************");
        Hashtable<String, String> props = new Hashtable<String, String>();
        props.put("Language", "English");
        context.registerService(GUIService.class.getName(), new GUIDictionaryServiceImpl(), props);
    }

    /**
     * Implements BundleActivator.stop(). Does nothing since the framework will
     * automatically unpublish any registered services.
     *
     * @param context the framework context for the bundle.
     */
    public void stop(BundleContext context) {
        // NOTE: The service is automatically unregistered.
    }
}