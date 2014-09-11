package edu.fudan.sa.service.gateway.impl;

import android.content.Context;
import edu.fudan.sa.agent.WorkerAgent;
import edu.fudan.sa.android.service.GatewayService;
import edu.fudan.sa.exception.GatewayConnectedException;
import edu.fudan.sa.ontology.Service;
import jade.android.ConnectionListener;
import jade.android.JadeGateway;
import jade.core.Profile;
import jade.imtp.leap.JICP.JICPProtocol;
import jade.util.Logger;
import jade.util.leap.Properties;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;

import java.net.ConnectException;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

public class GatewayServiceImpl extends GatewayService implements ManagedService {

    protected Logger logger = Logger.getMyLogger(GatewayServiceImpl.class.getName());
    protected volatile Context context;
    private Properties config = new Properties();
    private volatile EventAdmin eventAdmin;

    @Override
    public boolean connect() throws Exception, GatewayConnectedException {
        System.out.println("***gateway service connecting***");
        if (this.isConnected())
            throw new GatewayConnectedException();
        if(config.size()<1) {
            config.put(Profile.MAIN_HOST, "10.131.252.41");
            config.put(Profile.MAIN_PORT, "1099");
            config.put(JICPProtocol.MSISDN_KEY, "socialagent");
        }
//        String className = config.getProperty(WORKERAGENT_CLASS_NAME);
//        String bundleId = config.getProperty(WORKERAGENT_BUNDLE_ID);
//        Bundle bundle = FrameworkUtil.getBundle(Class.forName(className));
//        String p = bundle.getBundleContext().getProperty("felix.fileinstall.dir");

        JadeGateway.connect(WorkerAgent.class.getName(), null, config, context, new ConnectionListener() {
            @Override
            public void onConnected(JadeGateway gateway) {
                logger.log(Logger.INFO, "***Gateway connected!!!***");
                GatewayServiceImpl.this.gateway = gateway;
                GatewayServiceImpl.this.notifyConnected(gateway);
            }

            @Override
            public void onDisconnected() {
                logger.log(Logger.INFO, "***Gateway connected!!!***");
                GatewayServiceImpl.this.gateway = null;
                GatewayServiceImpl.this.notifyDisconnected();
            }
        });
        return true;
    }

    @Override
    public void updated(Dictionary<String, ?> config) throws ConfigurationException {
        if (config != null) {
            Properties props = new Properties();
            Enumeration<String> keys = config.keys();
            while (keys.hasMoreElements()) {
                String key = keys.nextElement();
                props.put(key, config.get(key));
            }
            try {
                this.config(props);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            throw new ConfigurationException(PID_GATEWAY_CONFIG, "configuration required");
        }
    }

    @Override
    public void config(java.util.Properties config) throws Exception {
        if (this.isConnected())
            throw new GatewayConnectedException();
        if (config == null) {
            throw new ConfigurationException(PID_GATEWAY_CONFIG, "configuration required");
        } else {
            if (isEmpty(config.get(Profile.MAIN_HOST))
                    || isEmpty(config.get(Profile.MAIN_PORT))
                    || isEmpty(config.get(GatewayService.WORKERAGENT_CLASS_NAME))
                    || isEmpty(config.get(JICPProtocol.MSISDN_KEY))) {
                String property = Profile.MAIN_HOST + "&&"
                        + Profile.MAIN_PORT + "&&"
                        + JICPProtocol.MSISDN_KEY + "&&"
                        + GatewayService.WORKERAGENT_CLASS_NAME;
                throw new ConfigurationException(property, "all required");
            }
            this.config.putAll(config);
        }
    }

    @Override
    public void disconnect() throws ConnectException {
        if (gateway != null)
            gateway.shutdownJADE();
        if (gateway != null)
            gateway.disconnect(context);
        this.notifyDisconnected();
    }

    protected void notifyConnected(JadeGateway gateway) {
        Dictionary<String, Object> properties = new Hashtable<String, Object>();
        properties.put("time", System.currentTimeMillis());
        properties.put(GatewayService.GATEWAY_SERVICE, this);
        Event event = new Event(GatewayService.TOPIC_GATEWAY_CONNECTED, properties);
        eventAdmin.sendEvent(event);
    }

    protected void notifyDisconnected() {
        Dictionary<String, Object> properties = new Hashtable<String, Object>();
        properties.put("time", System.currentTimeMillis());
        Event event = new Event(GatewayService.TOPIC_GATEWAY_DISCONNECTED, properties);
        eventAdmin.sendEvent(event);
    }

    @Override
    public boolean isConnected() {
        if (this.gateway == null)
            return false;
        try {
            this.gateway.getAgentName();
        } catch (ConnectException e) {
            return false;
        }
        return true;
    }

    @Override
    public Properties getConfig() {
        return this.config;
    }

    private boolean isEmpty(Object str) {
        return str == null || String.valueOf(str).trim().equals("");
    }

    @Override
    public Service getDescription() {
        return null;
    }
}
