package edu.fudan.sa.circle.jade.publication;

import edu.fudan.sa.Me;
import edu.fudan.sa.agent.MessageListener;
import edu.fudan.sa.circle.jade.JadeCircle;
import edu.fudan.sa.circle.jade.SimpleFIPAService;
import edu.fudan.sa.ontology.InvocationOntology;
import edu.fudan.sa.ontology.Service;
import edu.fudan.sa.service.ServicePublisher;
import edu.fudan.sa.service.WorkerService;
import jade.content.lang.leap.LEAPCodec;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.FIPAManagementVocabulary;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author ming
 * @since 2014-04-21 13:09.
 */
public class JadeServicePublisher implements ServicePublisher {
	private final Me me;
	private JadeCircle circle;
	private ServiceTracker<WorkerService, WorkerService> workerTracker;

	public JadeServicePublisher(Me me, JadeCircle circle) {
		this.me = me;
		this.circle = circle;
	}

	@Override
	public void start(BundleContext context) {
		this.workerTracker = new ServiceTracker<WorkerService, WorkerService>(context, WorkerService.class.getName(), null);
		this.workerTracker.open();
	}

	@Override
	public void stop(BundleContext context) {
		this.workerTracker.close();
	}

	@Override
	public DFAgentDescription publish(Service service) throws Exception {
		final DFAgentDescription dfd = new DFAgentDescription();
		dfd.addServices(toFIPAServiceDescription(service));
		dfd.setName(me.getAgent().getAID());
		ACLMessage request = DFService.createRequestMessage(me.getAgent(), circle.getServiceRegistry(),
				FIPAManagementVocabulary.REGISTER, dfd, null);
		MessageTemplate mt = SimpleFIPAService.doFipaRequest(me.getAgent(), request);
		BlockingQueue<DFAgentDescription> descContainer = new ArrayBlockingQueue<DFAgentDescription>(1);
		WorkerService workerService = workerTracker.getService();
		workerService.listen(new PublicationListener(descContainer, mt));
		System.out.println("******************waiting for publication result");
		DFAgentDescription desc = descContainer.poll(30000, TimeUnit.MILLISECONDS);
		return desc;
	}

	@Override
	public void unpublish(Service service) throws Exception {
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.addServices(toFIPAServiceDescription(service));
		dfd.setName(me.getAgent().getAID());
		ACLMessage request = DFService.createRequestMessage(me.getAgent(), circle.getServiceRegistry(),
				FIPAManagementVocabulary.DEREGISTER, dfd, null);
		MessageTemplate mt = SimpleFIPAService.doFipaRequest(me.getAgent(), request);
		WorkerService workerService = workerTracker.getService();
		workerService.listen(new PublicationListener(null, mt));
	}

	@Override
	public String[] getTags() {
		return new String[0];
	}

	private ServiceDescription toFIPAServiceDescription(Service service) {
		//TODO operations are also part of service description and should be recorded
		ServiceDescription sd = new ServiceDescription();
		sd.setType(service.getType().getName());
		sd.setName(service.getName());
		sd.addLanguages(LEAPCodec.NAME);
		sd.addOntologies(InvocationOntology.NAME);
		return sd;
	}

	@Override
	public Service getDescription() {
		return null;
	}

	class PublicationListener extends MessageListener {
		private BlockingQueue<DFAgentDescription> descContainer;
		private MessageTemplate pattern;
		private Timer timer = new Timer();

		public PublicationListener(BlockingQueue<DFAgentDescription> descContainer, MessageTemplate pattern) {
			this.pattern = pattern;
			this.descContainer = descContainer;
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					remove();
				}
			}, 30000);
		}

		@Override
		public void onMessageReceived(Agent agent, ACLMessage msg) {
			try {
				if (descContainer != null) {
					DFAgentDescription desc = DFService.decodeDone(msg.getContent());
					descContainer.offer(desc);
					System.out.println("register service successfully:" + descContainer);
				} else {
					System.out.println("deregister service successfully:" + descContainer);
				}
			} catch (FIPAException e) {
				e.printStackTrace();
			}
			this.remove();
		}

		@Override
		public MessageTemplate getPattern() {
			return this.pattern;
		}

		@Override
		protected void finalize() throws Throwable {
			timer.cancel();
			super.finalize();
		}
	}
}
