package edu.fudan.sa.circle.jade.resolution;

import edu.fudan.sa.IService;
import edu.fudan.sa.Me;
import edu.fudan.sa.ServiceReference;
import edu.fudan.sa.agent.MessageListener;
import edu.fudan.sa.circle.jade.JadeCircle;
import edu.fudan.sa.circle.jade.SimpleFIPAService;
import edu.fudan.sa.exception.ResolutionException;
import edu.fudan.sa.ontology.InvocationOntology;
import edu.fudan.sa.ontology.Service;
import edu.fudan.sa.service.ServiceResolver;
import edu.fudan.sa.service.WorkerService;
import jade.content.lang.leap.LEAPCodec;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.FIPAManagementVocabulary;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author ming
 * @since 2014-04-08 14:23.
 */
public class JadeServiceResolver implements ServiceResolver {
	private final JadeCircle circle;
	private ServiceTracker<WorkerService, WorkerService> workerTracker;
	private ServiceRegistration<?> registration;

	public JadeServiceResolver(JadeCircle circle) {
		this.circle = circle;
	}

	@Override
	public <T extends IService> List<ServiceReference> resolve(Class<T> clazz, Service service) throws ResolutionException {
		DFAgentDescription[] providers = this.searchProvidersFromDF(circle.getServiceRegistry(), clazz, service);
		if (providers == null)
			return null;
		List<ServiceReference> references = new ArrayList<>();
		WorkerService workerService = this.workerTracker.getService();
		if (workerService != null)
			for (DFAgentDescription provider : providers) {
				ServiceReference sr = new JadeServiceReference<T>(provider, service, workerService);
				references.add(sr);
			}
		return references;
	}

	@Override
	public String[] getTags() {
		return new String[]{"jade"};
	}

	@Override
	public void start(BundleContext context) {
		this.workerTracker = new ServiceTracker<>(context, WorkerService.class.getName(), null);
		this.workerTracker.open();
		String[] classes = {IService.class.getName(), ServiceResolver.class.getName()};
		Dictionary<String, ?> metadata = new Hashtable<>();
		registration = context.registerService(classes, this, metadata);
	}

	@Override
	public void stop(BundleContext context) {
		registration.unregister();
		this.workerTracker.close();
	}

	private DFAgentDescription[] searchProvidersFromDF(final AID dfName, Class clazz, Service service) {
		final DFAgentDescription description = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType(clazz.getName());
		sd.setName(service.getName());
		sd.addLanguages(LEAPCodec.NAME);
		sd.addOntologies(InvocationOntology.NAME);
		//TODO operations(and its IOPE) should also be checked on DF
		description.addServices(sd);
		BlockingQueue<DFAgentDescription[]> providerList = new ArrayBlockingQueue<>(1);
		final WorkerService workerService = this.workerTracker.getService();
		if (workerService != null) {
			SearchConstraints constraints = new SearchConstraints();
			constraints.setMaxResults(-1l);
			Me me = workerService.getMe();
			ACLMessage request = DFService.createRequestMessage(me.getAgent(), dfName,
					FIPAManagementVocabulary.SEARCH, description, constraints);
			MessageTemplate mt = SimpleFIPAService.doFipaRequest(me.getAgent(), request);
			workerService.listen(new ResolverListener(providerList, mt));
		}
		DFAgentDescription[] providers = null;
		try {
			System.out.println("******************waiting for result");
			providers = providerList.poll(30, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return providers;
	}

	@Override
	public Service getDescription() {
		return null;
	}

	private class ResolverListener extends MessageListener {
		private final MessageTemplate pattern;
		private BlockingQueue<DFAgentDescription[]> providers;
		private Timer timer = new Timer();

		private ResolverListener(BlockingQueue<DFAgentDescription[]> providers, MessageTemplate mt) {
			this.providers = providers;
			this.pattern = mt;
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
				System.out.println("******************getting for result");
				providers.add(DFService.decodeResult(msg.getContent()));
			} catch (FIPAException e) {
				e.printStackTrace();
			}
			this.remove();
		}

		@Override
		public MessageTemplate getPattern() {
			return pattern;
		}

		@Override
		protected void finalize() throws Throwable {
			timer.cancel();
			super.finalize();
		}
	}
}
