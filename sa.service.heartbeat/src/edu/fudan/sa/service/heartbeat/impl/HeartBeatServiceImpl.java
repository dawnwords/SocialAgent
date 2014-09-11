package edu.fudan.sa.service.heartbeat.impl;

import edu.fudan.sa.ontology.Service;
import edu.fudan.sa.service.HeartBeatService;
import edu.fudan.sa.service.ResourceManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * @author ming
 * @since 2014-06-15 14:11.
 */
public class HeartBeatServiceImpl implements HeartBeatService, ResourceManager {

	private static HeartBeatServiceImpl instance;
	private Collection<HeartBeatListener> listeners;
	private boolean toStop;
	private Map<String, Serializable> resources;

	private HeartBeatServiceImpl() {
		this.listeners = new ArrayList<HeartBeatListener>();
	}

	public static HeartBeatService getInstance() {
		if (instance == null)
			instance = new HeartBeatServiceImpl();
		return instance;
	}

	protected void heartBeat() {
		while (!toStop) {
			final long delay = computeDelay();
			notifyListeners(delay);
			try {
				this.wait(delay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private long computeDelay() {
		this.resources = this.retrieveResources();
		//TODO
		return 30000l;
	}

	private Map<String, Serializable> retrieveResources() {
		//TODO
		return null;
	}

	@Override
	public Map<String, Serializable> getResources() {
		return this.resources;
	}

	@Override
	public void start() {
		this.heartBeat();
	}

	@Override
	public void restart() {
		this.toStop = false;
		this.start();
	}

	@Override
	public void stop() {
		this.toStop = true;
	}

	@Override
	public void addListener(HeartBeatListener listener) {
		this.listeners.add(listener);
	}

	@Override
	public void removeListener(HeartBeatListener listener) {
		this.listeners.remove(listener);
	}

	protected void notifyListeners(long delay) {
		for (HeartBeatListener listener : this.listeners) {
			listener.onHeartBeat(delay);
		}
	}

	@Override
	public Service getDescription() {
		return null;
	}
}
