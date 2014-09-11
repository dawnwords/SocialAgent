package edu.fudan.sa.circle.xjade.resolution;

import edu.fudan.sa.ServiceReference;
import edu.fudan.sa.circle.xjade.invocation.XJadeServiceInvocation;
import edu.fudan.sa.exception.RemoteException;
import edu.fudan.sa.ontology.*;
import edu.fudan.sa.xjade.XAgent;
import jade.content.lang.leap.LEAPCodec;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * @author ming
 * @since 2014-04-04 15:08.
 */
public class XJadeServiceReference implements ServiceReference {
	private final Service service;
	private final XAgent xagent;
	private AID provider;

	public XJadeServiceReference(DFAgentDescription description, Service service, Agent agent) {
		this.service = service;
		this.provider = description.getName();
		this.xagent = new XAgent(agent);
	}

	@Override
	public Serializable invoke(Method method, Object[] args) throws RemoteException {
		Invocation invocation = new Invocation();
		invocation.setInvoker(this.xagent.getAgent().getAID());
		Operation operation = new Operation();
		operation.setService(this.service);
		operation.setName(method.getName());
		Input[] inputs = new Input[args.length];
		for (int i = 0; i < args.length; i++) {
			inputs[i] = new Input(args[i].getClass(), (Serializable) args[i]);
		}
		operation.setInputs(inputs);
		//TODO
		invocation.setOperation(operation);
		return this.invoke(invocation);
	}

	private Serializable invoke(Invocation invocation) {
		ACLMessage invokeMessage = new ACLMessage(XJadeServiceInvocation.INVOKE);
		invokeMessage.setLanguage(LEAPCodec.NAME);
		invokeMessage.setOntology(InvocationOntology.NAME);
		invokeMessage.setConversationId(Invocation.CONVERSATIONID);
		invokeMessage.setReplyWith(this + "");//invocation id
		invokeMessage.addReceiver(this.provider);
		try {
			invokeMessage.setContentObject(invocation);
			//Fixme jade.content.onto.OntologyException: Schema jade.content.schema.PrimitiveSchema-BO_String for element wang is not compatible with schema jade.content.schema.ConceptSchema-Serializable for slot value
			//this.xagent.getAgent().getContentManager().fillContent(invokeMessage, invocation);
			return this.xagent.request(invokeMessage).getContentObject();
		} catch (IOException | InterruptedException | TimeoutException | ExecutionException | UnreadableException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Properties getProperties() {
		return null;
	}
}
