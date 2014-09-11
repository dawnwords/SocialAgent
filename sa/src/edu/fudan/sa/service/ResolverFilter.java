package edu.fudan.sa.service;

import edu.fudan.sa.ontology.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * @author ming
 * @since 2014-04-15 10:44.
 */
public interface ResolverFilter {
    Collection<ServiceResolver> filter(HashMap<String, List<ServiceResolver>> resolvers, Service service);
}
