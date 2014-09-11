package edu.fudan.sa.service.rsi;

import org.osgi.framework.BundleContext;

/**
 * @author ming
 * @since 2014-04-14 09:42.
 */
public interface InvocationListener {
    void start(BundleContext context) throws Exception;

    void stop(BundleContext context) throws Exception;
}
