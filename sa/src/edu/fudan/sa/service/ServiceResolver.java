package edu.fudan.sa.service;

import edu.fudan.sa.IService;
import edu.fudan.sa.ServiceReference;
import edu.fudan.sa.exception.ResolutionException;
import edu.fudan.sa.ontology.Service;
import org.osgi.framework.BundleContext;

import java.util.List;

/**
 * @author ming
 * @since 2014-04-08 14:16.
 */
public interface ServiceResolver extends IService {

	public <T extends IService> List<ServiceReference> resolve(Class<T> clazz, Service service) throws ResolutionException;

    String[] getTags();

    void start(BundleContext context);

    void stop(BundleContext context);
}
