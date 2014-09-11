package edu.fudan.sa.circle.jade.resolution;

import edu.fudan.sa.IService;
import edu.fudan.sa.ServiceReference;
import edu.fudan.sa.agent.MessageListener;
import edu.fudan.sa.circle.jade.invocation.JadeServiceInvocation;
import edu.fudan.sa.exception.RemoteException;
import edu.fudan.sa.ontology.*;
import edu.fudan.sa.service.WorkerService;
import jade.content.lang.leap.LEAPCodec;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author ming
 * @since 2014-04-04 15:08.
 */
public class JadeServiceReference<T extends IService> implements ServiceReference {
	private final WorkerService worker;
	private final Service service;
	private AID provider;

	public JadeServiceReference(DFAgentDescription description, Service service, WorkerService workerService) {
		this.service = service;
		this.provider = description.getName();
		this.worker = workerService;
	}

	@Override
	public Serializable invoke(Method method, Object[] args) throws RemoteException {
		Invocation invocation = new Invocation();
		invocation.setInvoker(this.worker.getMe().getAgent().getAID());
		Operation operation = new Operation();
		operation.setService(this.service);
		operation.setName(method.getName());
		Input[] inputs = new Input[args.length];
		for(int i=0;i<args.length;i++){
			inputs[i] = new Input(args[i].getClass(), (Serializable) args[i]);
		}
		operation.setInputs(inputs);
		//TODO
		invocation.setOperation(operation);
		return this.invoke(invocation);
	}

	private Serializable invoke(Invocation invocation) {
		ACLMessage invokeMessage = new ACLMessage(JadeServiceInvocation.INVOKE);
		invokeMessage.setLanguage(LEAPCodec.NAME);
		invokeMessage.setOntology(InvocationOntology.NAME);
		invokeMessage.setConversationId(Invocation.CONVERSATIONID);
		invokeMessage.setReplyWith(this.toString());//invocation id
		invokeMessage.addReceiver(this.provider);
		try {
			invokeMessage.setContentObject(invocation);
			//Fixme jade.content.onto.OntologyException: Schema jade.content.schema.PrimitiveSchema-BO_String for element wang is not compatible with schema jade.content.schema.ConceptSchema-Serializable for slot value
			//this.worker.getMe().getAgent().getContentManager().fillContent(invokeMessage, invocation);
			//} catch (Codec.CodecException e) {
			//   e.printStackTrace();
			//} catch (OntologyException e) {
			//    e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.worker.getMe().getAgent().send(invokeMessage);
		MessageTemplate template = MessageTemplate.and(
				MessageTemplate.and(
						MessageTemplate.MatchInReplyTo(this.toString()),
						MessageTemplate.MatchConversationId(Invocation.CONVERSATIONID)),
				MessageTemplate.and(
						MessageTemplate.MatchOntology(InvocationOntology.NAME),
						MessageTemplate.MatchPerformative(JadeServiceInvocation.RETURN))
		);
		Serializable result = null;
		try {
			result = waitResult(template);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return result;
	}

	private Serializable waitResult(final MessageTemplate template) throws InterruptedException {
		final BlockingQueue<Serializable> result = new ArrayBlockingQueue<>(1);
		worker.listen(new MessageListener() {
			@Override
			public void onMessageReceived(Agent agent, ACLMessage msg) {
				try {
					System.out.println("*********************getting result");
					Serializable re = msg.getContentObject();
					result.offer(re);
				} catch (UnreadableException e) {
					e.printStackTrace();
				}
				this.remove();
			}

			@Override
			public MessageTemplate getPattern() {
				return template;
			}
		});
		System.out.println("*********************waiting result");
		return result.poll(30, TimeUnit.SECONDS);
	}

	@Override
	public Properties getProperties() {
		return null;
	}
}
