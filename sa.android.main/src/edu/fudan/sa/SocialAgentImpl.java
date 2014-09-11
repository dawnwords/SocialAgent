package edu.fudan.sa;

import android.content.Context;
import edu.fudan.sa.platform.ServicePlatform;
import org.osgi.framework.BundleException;

import java.util.HashSet;
import java.util.Set;

class SocialAgentImpl extends SocialAgent {
    private ServicePlatform platform;
    private Set<AgentStateListener> listeners = new HashSet<AgentStateListener>();

    SocialAgentImpl(Context context) {
        this.platform = ServicePlatform.getPlatform(context);
    }

    @Override
    public synchronized void start() {
        notifyAgentStarting();
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    SocialAgentImpl.this.platform.startup();
                } catch (BundleException e) {
                    notifyAgentStartFailed(e);
                }
                notifyAgentStarted();
            }
        }.start();
    }

    @Override
    public synchronized void die() {
        platform.shutdown();
    }

    @Override
    public void addAgentStateListener(AgentStateListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public ServicePlatform getServicePlatform() {
        return this.platform;
    }

    private void notifyAgentStarting() {
        for (AgentStateListener listener : listeners)
            listener.onAgentStarting(this);
    }

    private void notifyAgentStarted() {
        for (AgentStateListener listener : listeners)
            listener.onAgentStarted(this);
    }

    private void notifyAgentStartFailed(BundleException e) {
        for (AgentStateListener listener : listeners)
            listener.onAgentStartFailed(e);
    }
}