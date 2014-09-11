package edu.fudan.sa.agent;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.Collection;

/**
 * Created by ming on 2014-03-10 10:10.
 */
public abstract class MessageListener {

	private Collection<MessageListener> listeners;

	public abstract void onMessageReceived(Agent agent, ACLMessage msg);

	/**
	 * get the message template that this MessageListener cares about
	 *
	 * @return null if this listener cares about all messages
	 */
	public abstract MessageTemplate getPattern();

	/**
	 * remove this MessageListener from the listeners.
	 */
	public void remove() {
		if (this.listeners != null)
			this.listeners.remove(this);
	}

	/**
	 * you are not supposed to use this method.
	 *
	 * @param listeners
	 */
	public void setContainer(Collection<MessageListener> listeners) {
		this.listeners = listeners;
	}
}
