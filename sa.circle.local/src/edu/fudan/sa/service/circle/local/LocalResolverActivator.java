package edu.fudan.sa.service.circle.local;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * @author ming
 * @since 2014-04-09 16:33.
 */
public class LocalResolverActivator implements BundleActivator {

	private LocalServiceResolver resolver;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		this.resolver = LocalServiceResolver.getInstance();
		resolver.start(bundleContext);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		this.resolver.stop(bundleContext);
	}
}
