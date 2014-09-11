package edu.fudan.sa;

import jade.core.Agent;

/**
 * @author ming
 * @since 2014-05-07 14:25.
 */
public class Me {
    private static Me me;
    private Agent agent;

    private Me() {
    }

    public static Me getInstance() {
        if (me == null)
            me = new Me();
        return me;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }
}
