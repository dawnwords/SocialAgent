package edu.fudan.sa.circle.xjade.resolution;

import edu.fudan.sa.IService;
import edu.fudan.sa.Me;
import edu.fudan.sa.ServiceReference;
import edu.fudan.sa.circle.xjade.XJadeCircle;
import edu.fudan.sa.exception.ResolutionException;
import edu.fudan.sa.ontology.InvocationOntology;
import edu.fudan.sa.ontology.Service;
import edu.fudan.sa.service.ServiceResolver;
import edu.fudan.sa.service.WorkerService;
import edu.fudan.sa.xjade.XAgent;
import jade.content.lang.leap.LEAPCodec;
import jade.core.AID;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.FIPAManagementVocabulary;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

/**
 * @author ming
 * @since 2014-04-08 14:23.
 */
public class XJadeServiceResolver implements ServiceResolver {
	private final XJadeCircle circle;
	private ServiceTracker<WorkerService, WorkerService> workerTracker;
	private ServiceRegistration<?> registration;

	public XJadeServiceResolver(XJadeCircle circle) {
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
				ServiceReference sr = new XJadeServiceReference(provider, service, workerService.getMe().getAgent());
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
		try {
			final WorkerService workerService = this.workerTracker.getService();
			if (workerService != null) {
				SearchConstraints constraints = new SearchConstraints();
				constraints.setMaxResults(-1l);
				Me me = workerService.getMe();
				ACLMessage msg = DFService.createRequestMessage(me.getAgent(), dfName,
						FIPAManagementVocabulary.SEARCH, description, constraints);
				XAgent xagent = new XAgent(workerService.getMe().getAgent());
				System.out.println("******************waiting for result");
				ACLMessage result = xagent.request(msg);
				return DFService.decodeResult(result.getContent());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Service getDescription() {
		return null;
	}
}
