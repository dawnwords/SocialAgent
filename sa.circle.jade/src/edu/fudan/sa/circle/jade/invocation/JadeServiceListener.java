package edu.fudan.sa.circle.jade.invocation;

import edu.fudan.sa.agent.MessageListener;
import edu.fudan.sa.ontology.Invocation;
import edu.fudan.sa.ontology.InvocationOntology;
import edu.fudan.sa.service.WorkerService;
import edu.fudan.sa.service.rsi.InvocationDispatcher;
import edu.fudan.sa.service.rsi.InvocationListener;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

import java.io.ByteArrayInputStream;

/**
 * @author ming
 * @since 2014-04-14 09:42.
 */
public class JadeServiceListener implements InvocationListener {

	private InvocationListener listener;
	private ServiceTracker<InvocationDispatcher, InvocationDispatcher> dispatcherTracker;

	@Override
	public void start(BundleContext context) throws Exception {
		if (this.listener != null)
			return;
		this.dispatcherTracker
				= new ServiceTracker<InvocationDispatcher, InvocationDispatcher>(context, InvocationDispatcher.class, null);
		dispatcherTracker.open();
		ServiceReference<WorkerService> wsr = context.getServiceReference(WorkerService.class);
		WorkerService workerService = context.getService(wsr);
		if (workerService == null)
			throw new Exception("no worker service");
		this.listener = new InvocationListener(context);
		workerService.listen(this.listener);
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
					byte[] data = msg.getByteSequenceContent();
					BundleObjectInputStream bois = new BundleObjectInputStream(new ByteArrayInputStream(data), context);
					Invocation invocation = (Invocation) bois.readObject();
					JadeServiceInvocation serviceInvocation = new JadeServiceInvocation(invocation, agent, msg);
					dispatcher.addInvocation(serviceInvocation);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		public MessageTemplate getPattern() {
			return MessageTemplate.and(MessageTemplate.and(
							MessageTemplate.MatchConversationId(JadeServiceInvocation.CONVERSATIONID),
							MessageTemplate.MatchOntology(InvocationOntology.NAME)),
					MessageTemplate.MatchPerformative(JadeServiceInvocation.INVOKE));//TODO
		}
	}
}
