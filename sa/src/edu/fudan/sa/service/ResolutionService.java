package edu.fudan.sa.service;

import edu.fudan.sa.IService;
import edu.fudan.sa.ISystemService;
import edu.fudan.sa.exception.ResolutionException;
import edu.fudan.sa.ontology.Service;

/**
 * project SocialAgent
 *
 * @author ming
 * @since 2014-03-24 15:40.
 */
public interface ResolutionService extends ISystemService {
    <T extends IService> T resolve(Class<T> clazz, Service service) throws ResolutionException;

    <T extends IService> T resolve(Service service) throws ResolutionException;

//    Service resolve(String clazz, Resolver.ResolutionProperties properties) throws ResolutionException;

//    Service resolve(String className, Resolver.ResolutionProperties properties, boolean toLoad) throws ResolutionException;

    void resolve(ResolutionRequired resolutionRequired) throws ResolutionException;

    void registerResolver(ServiceResolver serviceResolver);

    void unregisterResolver(ServiceResolver serviceResolver);

    void setResolverFilter(ResolverFilter resolverFilter);

    void setServiceFilter(ServiceFilter serviceFilter);

    public static interface ResolutionRequired {
        void resolve(ResolutionService resolver) throws ResolutionException;
    }
}
