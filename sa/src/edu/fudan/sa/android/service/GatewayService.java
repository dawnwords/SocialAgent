package edu.fudan.sa.android.service;

import edu.fudan.sa.ISystemService;
import edu.fudan.sa.exception.GatewayConnectedException;
import jade.android.JadeGateway;

import java.net.ConnectException;
import java.util.Properties;

/**
 * <p>
 * !!! you are NOT supposed to use this service unless you are developing a WorkerService
 * </p>
 * Created by ming on 2014-03-14 14:57.
 */
public abstract class GatewayService implements ISystemService {
    public static final String GATEWAY_SERVICE = "edu.fudan.sa.service.gateway";
    public static final String PID_GATEWAY_CONFIG = GatewayService.class.getName();
    public static final String WORKERAGENT_CLASS_NAME = "edu.fudan.sa.service.impl.worker.classname";
    public static final String WORKERAGENT_BUNDLE_ID = "edu.fudan.sa.service.impl.worker.bundleid";
    public static final String TOPIC_GATEWAY = "edu/fudan/sa/service/gateway/*";
    public static final String TOPIC_GATEWAY_CONNECTED = "edu/fudan/sa/service/gateway/CONNECTED";
    public static final String TOPIC_GATEWAY_DISCONNECTED = "edu/fudan/sa/service/gateway/DISCONNECTED";
    protected JadeGateway gateway;

    protected abstract boolean connect() throws Exception, GatewayConnectedException;

    protected abstract void disconnect() throws ConnectException;

    public abstract void config(Properties config) throws Exception, GatewayConnectedException;

    public abstract boolean isConnected();

    public abstract Properties getConfig();

    public void execute(Object command) throws Exception {
        this.gateway.execute(command);
    }

    public void execute(Object command, long timeout) throws Exception {
        this.gateway.execute(command, timeout);
    }
}
