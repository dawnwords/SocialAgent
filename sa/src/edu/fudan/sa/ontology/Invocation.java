package edu.fudan.sa.ontology;

import jade.content.AgentAction;
import jade.core.AID;

/**
 * @author ming
 * @since 2014-05-05 21:53.
 */
public class Invocation implements AgentAction {
    public static final String CONVERSATIONID = "social-agent-service-invocation";
    protected AID invoker;
    protected Operation operation;

    public AID getInvoker() {
        return invoker;
    }

    public void setInvoker(AID invoker) {
        this.invoker = invoker;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }
}
