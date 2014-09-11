package edu.fudan.sa.service.rsi;

import edu.fudan.sa.ISystemService;

/**
 * @author ming
 * @since 2014-04-13 20:18.
 */
public interface InvocationDispatcher extends ISystemService {
    void registerInvocator(ServiceInvocator serviceInvocator);

    void unregisterInvocator(ServiceInvocator serviceInvocator);

    boolean addInvocation(ServiceInvocation serviceInvocation);

    boolean isToStop();

    void stop();
}
