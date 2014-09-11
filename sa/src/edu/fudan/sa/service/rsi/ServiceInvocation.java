package edu.fudan.sa.service.rsi;

import edu.fudan.sa.ontology.Invocation;

import java.io.Serializable;

/**
 * @author ming
 * @since 2014-04-14 13:25.
 */
public abstract class ServiceInvocation extends Invocation {

    protected String namespace;

    public ServiceInvocation() {
    }

    public ServiceInvocation(Invocation invocation) {
        this.invoker = invocation.getInvoker();
        this.operation = invocation.getOperation();
    }

    public abstract void returnResult(Serializable result) throws Exception;

    public String getNamespace() {
        return this.namespace;
    }
}
