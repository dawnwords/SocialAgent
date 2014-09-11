package edu.fudan.sa.service;

import edu.fudan.sa.ISystemService;
import edu.fudan.sa.ServiceReference;
import edu.fudan.sa.ontology.Service;

import java.util.List;

/**
 * @author ming
 * @since 2014-04-08 10:51.
 */
public interface ServiceFilter extends ISystemService {
	ServiceReference filter(List<ServiceReference> candidates, Service service);
}
