package edu.fudan.sa.service.circle.local;

import edu.fudan.sa.IService;
import edu.fudan.sa.ServiceReference;
import edu.fudan.sa.exception.RemoteException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;

/**
 * @author ming
 * @since 2014-04-09 15:57.
 */
public class LocalServiceReference<T extends IService> implements ServiceReference {
	private T service;

	public LocalServiceReference(T s) {
		this.service = s;
	}

	@Override
	public Object invoke(Method method, Object[] args) throws RemoteException {
		try {
			return method.invoke(service, args);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Properties getProperties() {
		return null;
	}
}
