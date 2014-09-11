package edu.fudan.sa.service;

import edu.fudan.sa.IRemotableService;
import edu.fudan.sa.IService;
import edu.fudan.sa.ISystemService;

import java.util.Collection;

/**
 * @author ming
 * @since 2014-04-17 09:49.
 */
public interface ServiceManager extends ISystemService {
    Collection<IService> getServices();

    Collection<IRemotableService> getRemotableServices();
}
