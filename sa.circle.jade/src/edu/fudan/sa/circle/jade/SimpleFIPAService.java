package edu.fudan.sa.circle.jade;


import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 * @author ming
 * @since 2014-05-10 13:24.
 */
public class SimpleFIPAService {
	private static int cnt = 0;

	private synchronized static int getNextInt() {
		int ret = cnt;
		cnt = (cnt < 9999 ? (++cnt) : 0);
		return ret;
	}

	public static MessageTemplate doFipaRequest(Agent agent, ACLMessage request) {
		String key = agent.getLocalName() + System.currentTimeMillis() + '-' + getNextInt();
		request.setReplyWith("rw-" + key);
		request.setConversationId("cid-" + key);
		agent.send(request);
		return MessageTemplate.MatchInReplyTo(request.getReplyWith());
	}
}
