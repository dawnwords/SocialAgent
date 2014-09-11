package edu.fudan.sa.service.manager.impl;

import edu.fudan.sa.IRemotableService;
import edu.fudan.sa.IService;
import edu.fudan.sa.SocialCircle;
import edu.fudan.sa.exception.ConstraintsException;
import edu.fudan.sa.ontology.Service;
import edu.fudan.sa.service.*;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author ming
 * @since 2014-03-27 10:31.
 */
public class ServiceManagerImpl implements PublicationService, ServiceManager, CircleManager {
	private Collection<SocialCircle> circles = new ArrayList<SocialCircle>();
	private SocialCircle initialCircle = null;

	@Override
	public void publish(SocialCircle circle, ArrayList<Service> services) throws Exception {
		for (Service service : services)
			circle.getPublisher().publish(service);
	}

	@Override
	public void unpublish(SocialCircle circle, ArrayList<Service> services) throws Exception {
		for (Service service : services)
			circle.getPublisher().unpublish(service);
	}

	@Override
	public Collection<IService> getServices() {
		return null;
	}

	@Override
	public Collection<IRemotableService> getRemotableServices() {
		Collection<IRemotableService> remotableServices = new ArrayList<IRemotableService>();
		try {
			Collection<ServiceReference<IRemotableService>> references
					= ServiceManagerActivator.bundleContext.getServiceReferences(IRemotableService.class, null);
			for (ServiceReference<IRemotableService> r : references) {
				IRemotableService remotableService = ServiceManagerActivator.bundleContext.getService(r);
				remotableServices.add(remotableService);
			}
		} catch (InvalidSyntaxException e) {
			e.printStackTrace();
		}
		return remotableServices;
	}

	protected SocialCircle joinInitialCircle() throws Exception {
		ServiceReference<InitialCircleDriver> driverRef =
				ServiceManagerActivator.bundleContext.getServiceReference(InitialCircleDriver.class);
		InitialCircleDriver iDriver = ServiceManagerActivator.bundleContext.getService(driverRef);
		SocialCircle initialCircle = iDriver.getInitialCircle();
		this.joinCircle(initialCircle);
		return initialCircle;
	}

	@Override
	public Collection<SocialCircle> getMyCircles() {
		return this.circles;
	}

	@Override
	public void joinCircle(SocialCircle circle) throws Exception {
		if (this.circles.contains(circle)) {
			throw new ConstraintsException("you have joined in circle " + circle);
		}
		ServiceReference<WorkerService> wsr = ServiceManagerActivator.bundleContext.getServiceReference(WorkerService.class);
		WorkerService workerService = ServiceManagerActivator.bundleContext.getService(wsr);
		if (circle.join(workerService.getMe())) {
			this.circles.add(circle);
		}
		this.initialCircle = circle;
	}

	@Override
	public void leftCircle(SocialCircle circle) throws Exception {
		if (this.circles.contains(circle) && circle.left()) {
			this.circles.remove(circle);
		}
	}

	@Override
	public Collection<SocialCircle> getAvailCircles() throws Exception {
		Collection<SocialCircle> circles = new ArrayList<SocialCircle>();
		Collection<ServiceReference<CircleDriver>> refs =
				ServiceManagerActivator.bundleContext.getServiceReferences(CircleDriver.class, null);
		for (ServiceReference<CircleDriver> ref : refs) {
			CircleDriver circleDriver = ServiceManagerActivator.bundleContext.getService(ref);
			circles.addAll(circleDriver.getAvailCircles());
		}
		return circles;
	}

	@Override
	public Service getDescription() {
		return null;
	}
}
