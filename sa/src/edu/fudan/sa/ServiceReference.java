package edu.fudan.sa;

import edu.fudan.sa.exception.RemoteException;

import java.lang.reflect.Method;
import java.util.Properties;

/**
 * @author ming
 * @since 2014-04-10 14:38.
 */
public interface ServiceReference{

	Object invoke(Method method, Object[] args) throws RemoteException;

    Properties getProperties();
}
