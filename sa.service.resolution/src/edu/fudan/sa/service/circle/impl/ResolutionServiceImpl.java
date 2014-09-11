package edu.fudan.sa.service.circle.impl;

import edu.fudan.sa.IService;
import edu.fudan.sa.ServiceReference;
import edu.fudan.sa.exception.ResolutionException;
import edu.fudan.sa.ontology.Service;
import edu.fudan.sa.service.ResolutionService;
import edu.fudan.sa.service.ResolverFilter;
import edu.fudan.sa.service.ServiceFilter;
import edu.fudan.sa.service.ServiceResolver;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;


/**
 * @author ming
 * @since 2014-04-08 10:07.
 */
public class ResolutionServiceImpl implements ResolutionService {
	private static ResolutionServiceImpl instance;
	private ServiceFilter serviceFilter;
	private ResolverFilter resolverFilter;
	private HashMap<String, List<ServiceResolver>> resolvers = new HashMap<>();

	private ResolutionServiceImpl() {
	}

	public static ResolutionServiceImpl getInstance() {
		if (instance == null)
			instance = new ResolutionServiceImpl();
		return instance;
	}

	@Override
	public void resolve(ResolutionRequired resolutionRequired) throws ResolutionException {
		resolutionRequired.resolve(this);
	}

	@Override
	public <T extends IService> T resolve(Class<T> clazz, Service service) throws ResolutionException {
		List<ServiceReference> references = new ArrayList<>();
		Collection<ServiceResolver> rs = this.filterResolvers(service, this.resolvers);
		for (ServiceResolver serviceResolver : rs) {
			references.addAll(serviceResolver.resolve(clazz, service));
		}
		if (references == null || references.size() < 1)
			return null;
		ServiceReference reference = this.filterServices(service, references);
		try {
			InvocationHandler handler = new ServiceInvocationHandler(reference);
			return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, handler);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public IService resolve(Service service) throws ResolutionException {
		return this.resolve(service.getType(), service);
	}

	private ServiceReference filterServices(Service service, List<ServiceReference> references) {
		if (this.serviceFilter == null) {
			return references.get(0);
		} else {
			return serviceFilter.filter(references, service);
		}
	}

	private Collection<ServiceResolver> filterResolvers(Service service, HashMap<String, List<ServiceResolver>> resolvers) {
		if (this.resolverFilter == null) {
			List<ServiceResolver> resolvers1 = new ArrayList<>();
			for (List<ServiceResolver> ss : resolvers.values()) {
				resolvers1.addAll(ss);
			}
			return resolvers1;
		} else {
			return this.resolverFilter.filter(resolvers, service);
		}
	}

	@Override
	public void registerResolver(ServiceResolver serviceResolver) {
		for (String tag : serviceResolver.getTags()) {
			List<ServiceResolver> rs = this.resolvers.get(tag);
			if (rs == null) {
				rs = new ArrayList<>();
				this.resolvers.put(tag, rs);
			}
			rs.add(serviceResolver);
		}
	}

	@Override
	public void unregisterResolver(ServiceResolver serviceResolver) {
		for (String tag : serviceResolver.getTags()) {
			List<ServiceResolver> rs = this.resolvers.get(tag);
			if (rs != null)
				rs.remove(serviceResolver);
		}
	}

	@Override
	public void setResolverFilter(ResolverFilter resolverFilter) {
		this.resolverFilter = resolverFilter;
	}

	@Override
	public void setServiceFilter(ServiceFilter serviceFilter) {
		this.serviceFilter = serviceFilter;
	}

	@Override
	public Service getDescription() {
		return null;
	}

}
