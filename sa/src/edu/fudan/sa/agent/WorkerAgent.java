package edu.fudan.sa.agent;

import edu.fudan.sa.ShortWork;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.wrapper.gateway.GatewayAgent;

import java.util.LinkedList;
import java.util.List;

public class WorkerAgent extends GatewayAgent {
	private List<MessageListener> listeners = new LinkedList<>();

	protected void processCommand(final Object command) {
		if (command instanceof Behaviour) {
			SequentialBehaviour sb = new SequentialBehaviour(this);
			if (command instanceof ShortWork) {
				sb.addSubBehaviour(((Behaviour) command));
			} else {
				ThreadedBehaviourFactory tbf = new ThreadedBehaviourFactory();
				sb.addSubBehaviour(tbf.wrap((Behaviour) command));
			}
			sb.addSubBehaviour(new OneShotBehaviour(this) {
				public void action() {
					WorkerAgent.this.releaseCommand(command);
				}
			});
			this.addBehaviour(sb);
		} else if (command instanceof MessageListener) {
			System.out.println("*************processCommand(): New message listener received and registered!");
			MessageListener listener = (MessageListener) command;
			listener.setContainer(listeners);
			listeners.add(listener);
			releaseCommand(command);
		} else {
			System.out.println("*************processCommand().Unknown command " + command);
		}
	}

	protected void setup() {
		super.setup();
		System.out.println("*************social agent setup*************");
		ThreadedBehaviourFactory factory = new ThreadedBehaviourFactory();
		addBehaviour(factory.wrap(new MessageReceiverBehaviour()));
	}

	private class MessageReceiverBehaviour extends CyclicBehaviour {
		public void action() {
			ACLMessage msg = myAgent.receive();
			System.out.println("*************MessageReceiverBehaviour().Message received: " + msg);
			if (msg != null) {
				for (int i = listeners.size() - 1; i > -1; i--) {
					MessageListener listener = listeners.get(i);
					if (listener.getPattern().match(msg)) {
						listener.onMessageReceived(this.myAgent, msg);
					}
				}
			} else {
				block();
			}
		}
	}
}
