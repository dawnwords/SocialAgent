package edu.fudan.sa.service.worker.impl;

import edu.fudan.sa.Me;
import edu.fudan.sa.Work;
import edu.fudan.sa.agent.MessageListener;
import edu.fudan.sa.agent.WorkerAgent;
import edu.fudan.sa.android.service.GatewayService;
import edu.fudan.sa.ontology.Service;
import edu.fudan.sa.service.ResolutionService;
import edu.fudan.sa.service.WorkerService;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import java.io.IOException;
import java.util.*;

import static edu.fudan.sa.service.ResolutionService.ResolutionRequired;

class WorkerServiceImpl implements WorkerService, EventHandler {
    private volatile ConfigurationAdmin configAdmin;
    private volatile GatewayService gatewayService;
    private volatile ResolutionService resolutionService;
    private Me me = Me.getInstance();

    @Override
    public synchronized void work(Work work) throws Exception {
        if (work instanceof ResolutionRequired) {
            this.resolutionService.resolve((ResolutionRequired) work);
        }
        this.gatewayService.execute(work);
    }

    @Override
    public void listen(MessageListener listener) {
        try {
            this.gatewayService.execute(listener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * config gateway connection properties
     *
     * @throws IOException
     */
    protected void config(Properties properties) throws Exception {
        Configuration config = configAdmin.getConfiguration(GatewayService.PID_GATEWAY_CONFIG, null);
        if (config == null)
            return;
        Dictionary<String, Object> props = config.getProperties();
        if (props == null) {
            props = new Hashtable<String, Object>();
        }
        Set<Map.Entry<Object, Object>> entries = properties.entrySet();
        for (Map.Entry<Object, Object> entry : entries)
            props.put((String) entry.getKey(), entry.getValue());
        config.update(props);
    }

    /**
     * on gateway connection event
     *
     * @param event
     */
    @Override
    public void handleEvent(Event event) {
        if (event.getTopic().equals(GatewayService.TOPIC_GATEWAY_CONNECTED)) {
            System.out.println("************worker heard that: gateway connected************");
            try {
                /**
                 * java.lang.ClassNotFoundException:
                 *  Didn't find class "edu.fudan.sa.worker.impl.WorkerAgent"
                 *  on path: DexPathList[[zip file "/data/app/edu.fudan.sa.view-2.apk"],nativeLibraryDirectories=[/data/app-lib/edu.fudan.sa.view-2, /system/lib]]
                 */
                Behaviour work = new OneShotBehaviour() {
                    @Override
                    public void action() {
                        me.setAgent(this.myAgent);
                    }
                };
                this.gatewayService.execute(work);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
                this.me.getAgent().addBehaviour(new OneShotBehaviour() {
                    @Override
                    public void action() {
                        System.out.println("...........testing worker service's agent............");
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            this.gatewayService = (GatewayService) event.getProperty(GatewayService.GATEWAY_SERVICE);
        } else if (event.getTopic().equals(GatewayService.TOPIC_GATEWAY_DISCONNECTED)) {
            System.out.println("************worker heard that: gateway disconnected************");
            WorkerServiceImpl.this.me.setAgent(null);
//            this.gatewayService = null;
        }
    }

    @Override
    public Me getMe() {
        return me;
    }

    protected String[] getAgentClasses() {
        return new String[]{WorkerAgent.class.getName()};
    }

    @Override
    public Service getDescription() {
        return null;
    }
}
