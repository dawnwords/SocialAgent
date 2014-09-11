package edu.fudan.sa;

import android.content.Context;
import edu.fudan.sa.platform.ServicePlatform;

public abstract class SocialAgent {
    private static SocialAgent agent;

    public abstract void start();

    public abstract void die();

    public abstract void addAgentStateListener(AgentStateListener listener);

    public abstract ServicePlatform getServicePlatform();

    public static SocialAgent getAgent(Context context) {
        if (agent == null)
            agent = new SocialAgentImpl(context);
        return agent;
    }

    public static SocialAgent getAgent() {
        if (agent != null)
            return agent;
        return null;
    }

    public interface AgentStateListener {
        public void onAgentStarting(SocialAgentImpl socialAgent);

        public void onAgentStarted(SocialAgent platform);

        public void onAgentStartFailed(Exception e);
    }
}
