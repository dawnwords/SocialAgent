package edu.fudan.sa;

import edu.fudan.sa.service.CircleDriver;
import edu.fudan.sa.service.ServicePublisher;
import edu.fudan.sa.service.ServiceResolver;
import org.osgi.framework.BundleContext;

/**
 * @author ming
 * @since 2014-04-21 13:32.
 */
public abstract class SocialCircle {

	protected CircleDriver circleDriver;
	protected Object circleId;
	protected String name;
	protected String description;

	public SocialCircle(CircleDriver circleDriver, Object circleId) {
		this.circleDriver = circleDriver;
		this.circleId = circleId;
	}

	public abstract boolean left() throws Exception;

	public abstract boolean join(Me owner) throws Exception;

	public abstract void activatePublisher(BundleContext bundleContext);

	public abstract void deactivatePublisher(BundleContext bundleContext);

	public abstract void activateListener(BundleContext context) throws Exception;

	public abstract void deactivateListener(BundleContext context) throws Exception;

	public abstract void activateResolver(BundleContext context) throws Exception;

	public abstract void deactivateResolver(BundleContext context) throws Exception;

	public CircleDriver getCircleDriver() {
		return this.circleDriver;
	}

	public abstract ServiceResolver getResolver();

	public abstract ServicePublisher getPublisher();

	public Object getCircleId() {
		return this.circleId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

}
