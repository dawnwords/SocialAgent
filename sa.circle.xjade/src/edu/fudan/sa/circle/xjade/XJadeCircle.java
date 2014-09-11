package edu.fudan.sa.circle.xjade;

import edu.fudan.sa.Me;
import edu.fudan.sa.SocialCircle;
import edu.fudan.sa.circle.xjade.invocation.XJadeServiceListener;
import edu.fudan.sa.circle.xjade.publication.XJadeServicePublisher;
import edu.fudan.sa.circle.xjade.resolution.XJadeServiceResolver;
import edu.fudan.sa.ontology.InvocationOntology;
import edu.fudan.sa.service.CircleDriver;
import edu.fudan.sa.service.HeartBeatService;
import edu.fudan.sa.service.ServicePublisher;
import edu.fudan.sa.service.ServiceResolver;
import jade.content.lang.leap.LEAPCodec;
import jade.core.AID;
import jade.domain.AMSService;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.lang.acl.ACLMessage;
import org.osgi.framework.BundleContext;

/**
 * @author ming
 * @since 2014-04-21 13:50.
 */
public class XJadeCircle extends SocialCircle implements HeartBeatService.HeartBeatListener {

	protected AID serviceRegistryId;
	private Me owner;
	private XJadeServiceResolver resolver;
	private XJadeServicePublisher publisher;
	private XJadeServiceListener listener;

	public XJadeCircle(CircleDriver circleDriver, AID memberRegistryId) {
		super(circleDriver, memberRegistryId);
		//TODO get the default dfName based on the memberRegistryId(ams)
		this.serviceRegistryId = null;
	}

	@Override
	public void onHeartBeat(long delay) {
		ACLMessage resourceMessage = new ACLMessage(ACLMessage.INFORM);

		//TODO
		this.owner.getAgent().send(resourceMessage);
	}
//
//	public AMSAgentDescription[] getParticipants() {
//		AMSAgentDescription[] agents = null;
//		SearchConstraints constraints = new SearchConstraints();
//		constraints.setMaxResults(-1l);
//		try {
//			agents = AMSService.search(this.owner.getAgent(), new AMSAgentDescription(), constraints);
//		} catch (FIPAException e) {
//			e.printStackTrace();
//		}
//		return agents;
//	}

	private void init(Me me) {
		this.owner = me;
		if (this.circleId == null)
			this.circleId = this.owner.getAgent().getAMS();
		if (this.serviceRegistryId == null)
			this.serviceRegistryId = this.owner.getAgent().getDefaultDF();
		LEAPCodec codec = new LEAPCodec();
		this.owner.getAgent().getContentManager().registerLanguage(codec, LEAPCodec.NAME);
		this.owner.getAgent().getContentManager().registerOntology(InvocationOntology.getInstance());
		this.resolver = new XJadeServiceResolver(this);
		this.publisher = new XJadeServicePublisher(this.owner, this);
		this.listener = new XJadeServiceListener();
	}

	@Override
	public boolean left() throws Exception {
		if (this.owner == null)
			throw new Exception("you haven't joined in this circle");
		if (this.owner.getAgent().getAMS().equals(this.getMemberRegistry()))
			return false;
		AMSService.deregister(this.owner.getAgent(), this.getMemberRegistry());
		this.owner = null;
		return true;
	}

	@Override
	public boolean join(Me owner) throws Exception {
		if (this.owner != null)
			throw new Exception("you have joined in this circle");
		this.init(owner);
		if (this.owner.getAgent().getAMS().equals(this.getMemberRegistry()))
			return false;
		AMSAgentDescription description = new AMSAgentDescription();
		AMSService.register(this.owner.getAgent(), this.getMemberRegistry(), description);
		return true;
	}

	@Override
	public void activatePublisher(BundleContext context) {
		this.publisher.start(context);
	}

	@Override
	public void deactivatePublisher(BundleContext context) {
		this.publisher.stop(context);
	}

	@Override
	public void activateListener(BundleContext context) throws Exception {
		this.listener.start(context);
	}

	@Override
	public void deactivateListener(BundleContext context) throws Exception {
		this.listener.stop(context);
	}

	@Override
	public void activateResolver(BundleContext context) throws Exception {
		this.resolver.start(context);
	}

	@Override
	public void deactivateResolver(BundleContext context) throws Exception {
		this.resolver.stop(context);
	}

	@Override
	public ServiceResolver getResolver() {
		return this.resolver;
	}

	@Override
	public ServicePublisher getPublisher() {
		return this.publisher;
	}

	public AID getMemberRegistry() {
		return (AID) this.circleId;
	}

	public AID getServiceRegistry() {
		return this.serviceRegistryId;
	}

}
