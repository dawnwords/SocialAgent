package edu.fudan.sa.circle.xjade.invocation;

import edu.fudan.sa.agent.MessageListener;
import edu.fudan.sa.ontology.Invocation;
import edu.fudan.sa.ontology.InvocationOntology;
import edu.fudan.sa.service.WorkerService;
import edu.fudan.sa.service.rsi.InvocationDispatcher;
import edu.fudan.sa.service.rsi.InvocationListener;
import edu.fudan.sa.xjade.XAgent;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

import java.io.ByteArrayInputStream;
import java.util.Base64;

/**
 * @author ming
 * @since 2014-04-14 09:42.
 */
public class XJadeServiceListener implements InvocationListener {

	private InvocationListener listener;
	private ServiceTracker<InvocationDispatcher, InvocationDispatcher> dispatcherTracker;

	@Override
	public void start(BundleContext context) throws Exception {
		if (this.listener != null)
			return;
		this.dispatcherTracker = new ServiceTracker<>(context, InvocationDispatcher.class, null);
		dispatcherTracker.open();
		ServiceReference<WorkerService> wsr = context.getServiceReference(WorkerService.class);
		XAgent xagent = new XAgent(context.getService(wsr).getMe().getAgent());
		this.listener = new InvocationListener(context);
		xagent.receive(this.listener);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		if (this.listener != null) {
			this.listener.remove();
		}
		this.listener = null;
		this.dispatcherTracker.close();
	}

	private class InvocationListener extends MessageListener {
		private final BundleContext context;

		public InvocationListener(BundleContext context) {
			this.context = context;
		}

		@Override
		public void onMessageReceived(Agent agent, ACLMessage msg) {
			InvocationDispatcher dispatcher = dispatcherTracker.getService();
			if (dispatcher != null) {
				try {
					byte[] data = Base64.getDecoder().decode(msg.getContent());//TODO modification may be needed
					BundleObjectInputStream bois = new BundleObjectInputStream(new ByteArrayInputStream(data), context);
					Invocation invocation = (Invocation) bois.readObject();
					XJadeServiceInvocation serviceInvocation = new XJadeServiceInvocation(invocation, new XAgent(agent), msg);
					dispatcher.addInvocation(serviceInvocation);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		public MessageTemplate getPattern() {
			return MessageTemplate.and(MessageTemplate.and(
							MessageTemplate.MatchConversationId(XJadeServiceInvocation.CONVERSATIONID),
							MessageTemplate.MatchOntology(InvocationOntology.NAME)),
					MessageTemplate.MatchPerformative(XJadeServiceInvocation.INVOKE));//TODO
		}
	}
}
