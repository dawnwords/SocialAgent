package edu.fudan.sa.circle.jade;

import edu.fudan.sa.SocialCircle;
import edu.fudan.sa.ontology.Service;
import edu.fudan.sa.service.InitialCircleDriver;
import edu.fudan.sa.service.WorkerService;
import jade.core.AID;
import jade.domain.AMSService;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author ming
 * @since 2014-04-21 14:46.
 */
public class JadeCircleDriverImpl implements InitialCircleDriver {
	private AID circleManagerServer;

	public JadeCircleDriverImpl(AID circleManagerServer) {
		this.circleManagerServer = circleManagerServer;
	}

	@Override
	public SocialCircle getInitialCircle() {
		return new JadeCircle(this, null);
	}

	@Override
	public Collection<SocialCircle> getAvailCircles() throws Exception {
		SearchConstraints constraints = new SearchConstraints();
		constraints.setMaxResults(-1l);
		BundleContext context = JadeCircleDriverActivator.bundleContext;
		ServiceReference<WorkerService> wsr = context.getServiceReference(WorkerService.class);
		WorkerService workerService = context.getService(wsr);
		if (workerService == null)
			throw new Exception("no worker service");
		AMSAgentDescription[] descriptions = AMSService.search(workerService.getMe().getAgent(), this.circleManagerServer, new AMSAgentDescription(), constraints);
		Collection<SocialCircle> circles = new ArrayList<SocialCircle>();
		for (AMSAgentDescription description : descriptions) {
			circles.add(new JadeCircle(this, description.getName()));
		}
		return circles;
	}

	@Override
	public Service getDescription() {
		return null;
	}

}
