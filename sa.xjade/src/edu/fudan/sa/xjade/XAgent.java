package edu.fudan.sa.xjade;

import edu.fudan.sa.agent.MessageListener;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.leap.Iterator;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * Project {SocialAgent}
 *
 * @author Ming
 * @since 15:20 2014-08-28.
 */
public class XAgent {
	private final Agent agent;
	private MessageManager manager;

	public XAgent(Agent agent) {
		this.agent = agent;
		this.manager = MessageManager.getInstance();
		this.manager.startWork();
		this.manager.setAgent(this.agent);
	}

	public void send(ACLMessage msg) {
		try {
			msg.getSender().getName().charAt(0);
		} catch (Exception e) {
			msg.setSender(this.agent.getAID());
		}
		Iterator receivers = msg.getAllReceiver();
		while (receivers.hasNext()) {
			AID receiver = (AID) receivers.next();
			String ip = SVPNService.getSVPNIP(receiver);
			this.manager.send(msg, ip);
		}
	}

	public ACLMessage request(ACLMessage msg) throws InterruptedException, ExecutionException, TimeoutException {
		return this.request(msg, null);
	}

	public ACLMessage request(ACLMessage msg, MessageTemplate template) throws InterruptedException, ExecutionException, TimeoutException {
		try {
			msg.getSender().getName().charAt(0);
		} catch (Exception e) {
			msg.setSender(this.agent.getAID());
		}
		Iterator receivers = msg.getAllReceiver();
		if (receivers.hasNext()) {//only one receiver
			AID receiver = (AID) receivers.next();
			String ip = SVPNService.getSVPNIP(receiver);
			return this.manager.request(msg, ip, template);
		}
		return null;
	}

	public Agent getAgent() {
		return agent;
	}

	public void receive(MessageListener listener) {
		this.manager.addMessageListener(listener);
	}
}
