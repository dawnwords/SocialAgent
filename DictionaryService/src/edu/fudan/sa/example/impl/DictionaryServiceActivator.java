/*
 * Apache Felix OSGi tutorial.
 **/

package edu.fudan.sa.example.impl;

import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import edu.fudan.sa.example.DictionaryService;

/**
 * This class implements a simple bundle that uses the bundle context to
 * register an English language dictionary service with the OSGi framework. The
 * dictionary service interface is defined in a separate class file and is
 * implemented by an inner class.
 */
public class DictionaryServiceActivator implements BundleActivator {
    /**
     * Implements BundleActivator.start(). Registers an instance of a dictionary
     * service using the bundle context; attaches properties to the service that
     * can be queried when performing a service look-up.
     *
     * @param context the framework context for the bundle.
     */
    public void start(BundleContext context) {
        System.out.println("****************bundle started************");
        Hashtable<String, String> props = new Hashtable<String, String>();
        props.put("Language", "English");
        context.registerService(DictionaryService.class.getName(),
                new DictionaryServiceImpl(), props);
    }

    /**
     * Implements BundleActivator.stop(). Does nothing since the framework will
     * automatically unregister any registered services.
     *
     * @param context the framework context for the bundle.
     */
    public void stop(BundleContext context) {
        // NOTE: The service is automatically unregistered.
    }
}