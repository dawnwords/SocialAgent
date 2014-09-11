package edu.fudan.sa.service.example.hello;

import edu.fudan.sa.IRemotableService;
import edu.fudan.sa.exception.RemoteException;

/**
 * @author ming
 * @since 2014-03-24 14:28.
 */
public interface HelloService extends IRemotableService {
	String sayHello(String to) throws RemoteException;

	String sayHelloWorld() throws RemoteException;
}
