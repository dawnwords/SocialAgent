package edu.fudan.sa.xjade;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.ACLParser;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.ParseException;

import java.io.StringReader;

/**
 * Project {SocialAgent}
 *
 * @author Ming
 * @since 20:09 2014-08-28.
 */
public class XFIPAService {

	private static int cnt = 0;


	public static void main(String[] args) throws FIPAException {
		String content = "(INFORM\n" +
				" :sender  ( agent-identifier :name df@10.131.252.41:1099/JADE  :addresses (sequence http://Hezort:7778/acc ))\n" +
				" :receiver  (set ( agent-identifier :name test1@10.131.252.41:1099/JADE  :addresses (sequence http://Hezort:7778/acc )) )\n" +
				" :content  \"((result (action (agent-identifier :name df@10.131.252.41:1099/JADE :addresses (sequence http://Hezort:7778/acc)) (search (df-agent-description :services (set (service-description :type used_to_test))) (search-constraints :max-results -1))) (sequence (df-agent-description :name (agent-identifier :name test1@10.131.252.41:1099/JADE :addresses (sequence http://Hezort:7778/acc)) :services (set (service-description :name test1-test :type used_to_test))))))\" \n" +
				" :reply-with  test1@10.131.252.41:1099/JADE1409275609029  :in-reply-to  rw-test1@10.131.252.41:1099/JADE1409275609025-1  :language  fipa-sl0  :ontology  FIPA-Agent-Management  :protocol  fipa-request\n" +
				" :conversation-id  conv-test1@10.131.252.41:1099/JADE1409275609025-1 )";
		ACLMessage msg = XFIPAService.decode(content);
		DFAgentDescription desc = DFService.decodeDone(msg.getContent());
		System.out.println(desc.getAllServices());
	}

	public static ACLMessage decode(String content) {
		ACLParser parser = new ACLParser(new StringReader(content));
		try {
			return parser.Message();
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		return null;
	}

	public static MessageTemplate getReplyTemplate(Agent agent, ACLMessage request) {
		String key = agent.getLocalName() + System.currentTimeMillis() + '-' + getNextInt();
		request.setReplyWith("rw-" + key);
		request.setConversationId("cid-" + key);
		return MessageTemplate.MatchInReplyTo(request.getReplyWith());
	}

	private synchronized static int getNextInt() {
		int ret = cnt;
		cnt = (cnt < 9999 ? (++cnt) : 0);
		return ret;
	}
}
