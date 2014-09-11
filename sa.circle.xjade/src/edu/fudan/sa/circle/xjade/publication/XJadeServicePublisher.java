package edu.fudan.sa.circle.xjade.publication;

import edu.fudan.sa.Me;
import edu.fudan.sa.circle.xjade.XJadeCircle;
import edu.fudan.sa.ontology.InvocationOntology;
import edu.fudan.sa.ontology.Service;
import edu.fudan.sa.service.ServicePublisher;
import edu.fudan.sa.service.WorkerService;
import edu.fudan.sa.xjade.XAgent;
import jade.content.lang.leap.LEAPCodec;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.FIPAManagementVocabulary;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

/**
 * @author ming
 * @since 2014-04-21 13:09.
 */
public class XJadeServicePublisher implements ServicePublisher {
	private final XAgent xagent;
	private XJadeCircle circle;
	private ServiceTracker<WorkerService, WorkerService> workerTracker;

	public XJadeServicePublisher(Me me, XJadeCircle circle) {
		this.xagent = new XAgent(me.getAgent());
		this.circle = circle;
	}

	@Override
	public void start(BundleContext context) {
		this.workerTracker = new ServiceTracker<>(context, WorkerService.class.getName(), null);
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
		dfd.setName(xagent.getAgent().getAID());
		ACLMessage msg = DFService.createRequestMessage(xagent.getAgent(), circle.getServiceRegistry(),
				FIPAManagementVocabulary.REGISTER, dfd, null);
		ACLMessage reply = xagent.request(msg);
		return DFService.decodeDone(reply.getContent());
	}

	@Override
	public void unpublish(Service service) throws Exception {
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.addServices(toFIPAServiceDescription(service));
		dfd.setName(xagent.getAgent().getAID());
		ACLMessage msg = DFService.createRequestMessage(xagent.getAgent(), circle.getServiceRegistry(),
				FIPAManagementVocabulary.DEREGISTER, dfd, null);
		ACLMessage reply = xagent.request(msg);
		System.out.println("deregister service successfully:" + reply.getContent());
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
}
