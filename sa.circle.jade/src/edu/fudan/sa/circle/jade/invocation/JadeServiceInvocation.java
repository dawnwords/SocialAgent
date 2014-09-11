package edu.fudan.sa.circle.jade.invocation;

import edu.fudan.sa.ontology.Invocation;
import edu.fudan.sa.ontology.Return;
import edu.fudan.sa.service.rsi.ServiceInvocation;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.io.Serializable;

/**
 * @author ming
 * @since 2014-04-14 10:44.
 */
public class JadeServiceInvocation extends ServiceInvocation {
	public static final int INVOKE = ACLMessage.REQUEST;
	public static final int RETURN = ACLMessage.CONFIRM;
	private final ACLMessage message;
	private final Agent myAgent;

	JadeServiceInvocation(Agent myAgent, ACLMessage message) {
		this.myAgent = myAgent;
		this.message = message;
	}

	JadeServiceInvocation(Invocation invocation, Agent myAgent, ACLMessage message) {
		super(invocation);
		this.myAgent = myAgent;
		this.message = message;
	}

	@Override
	public void returnResult(Serializable result) throws IOException {
		ACLMessage reply = this.message.createReply();
		reply.setPerformative(JadeServiceInvocation.RETURN);
		Return ret = new Return(result);
//        try {
		reply.setContentObject(ret);
//            this.myAgent.getContentManager().fillContent(reply, ret);
//        } catch (Codec.CodecException e) {
//            e.printStackTrace();
//        } catch (OntologyException e) {
//            e.printStackTrace();
//        }
		this.myAgent.send(reply);
	}
}
