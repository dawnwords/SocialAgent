package edu.fudan.sa.service.circle.local;

import edu.fudan.sa.IService;
import edu.fudan.sa.ServiceReference;
import edu.fudan.sa.exception.ResolutionException;
import edu.fudan.sa.ontology.Service;
import edu.fudan.sa.service.ServiceResolver;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceRegistration;

import java.util.*;

/**
 * may work as a Fragment Bundle of ResolutionService Bundle.
 *
 * @author ming
 * @since 2014-04-08 14:23.
 */
public class LocalServiceResolver implements ServiceResolver {
	private static LocalServiceResolver instance;
	private BundleContext bundleContext;
	private ServiceRegistration<?> registration;

	private LocalServiceResolver() {
	}

	public static LocalServiceResolver getInstance() {
		if (instance == null)
			instance = new LocalServiceResolver();
		return instance;
	}

	@Override
	public void start(BundleContext context) {
		this.bundleContext = context;
		Dictionary<String, ?> metadata = new Hashtable<>();
		String[] classes = {IService.class.getName(), ServiceResolver.class.getName()};
		registration = bundleContext.registerService(classes, LocalServiceResolver.getInstance(), metadata);
	}

	@Override
	public void stop(BundleContext context) {
		this.bundleContext = null;
		registration.unregister();
	}

	@Override
	public <T extends IService> List<ServiceReference> resolve(Class<T> clazz, Service service) throws ResolutionException {
		//TODO create filter with properties
		String filter = null;
		List<ServiceReference> references = new ArrayList<>();
		try {
			Collection<org.osgi.framework.ServiceReference<T>> srs = this.bundleContext.getServiceReferences(clazz, filter);
			for (org.osgi.framework.ServiceReference<T> sr : srs) {
				T s = this.bundleContext.getService(sr);
				references.add(new LocalServiceReference<>(s));
			}
		} catch (InvalidSyntaxException e) {
			e.printStackTrace();
		}
		return references;
	}

	@Override
	public String[] getTags() {
		return new String[]{"local"};
	}

	@Override
	public Service getDescription() {
		return null;
	}
}
