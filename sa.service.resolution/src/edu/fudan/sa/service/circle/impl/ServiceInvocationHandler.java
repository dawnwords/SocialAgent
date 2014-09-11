package edu.fudan.sa.service.circle.impl;

import edu.fudan.sa.ServiceReference;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Project {SocialAgent}
 *
 * @author Ming
 * @since 15:04 2014-09-06.
 */
public class ServiceInvocationHandler implements InvocationHandler {

	private final ServiceReference reference;

	public ServiceInvocationHandler(ServiceReference reference) {
		this.reference = reference;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		return this.reference.invoke(method, args);
	}
}
