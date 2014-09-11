package edu.fudan.sa.service;

import edu.fudan.sa.IService;
import edu.fudan.sa.ontology.Service;
import org.osgi.framework.BundleContext;

/**
 * @author ming
 * @since 2014-04-19 17:03.
 */
public interface ServicePublisher extends IService {
	void start(BundleContext context);

	void stop(BundleContext context);

	jade.domain.FIPAAgentManagement.DFAgentDescription publish(Service service) throws Exception;

	void unpublish(Service service) throws Exception;

	String[] getTags();
}
