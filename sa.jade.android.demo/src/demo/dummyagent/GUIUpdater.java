package demo.dummyagent;

import android.os.Handler;
import android.widget.Toast;
import jade.android.JadeGateway;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;

/**
 * This class implements the ACLMessageListener interface
 * and it is called by the agent to update the GUI when a message is received
 *
 * @author Stefano Semeria  Reply Cluster
 * @author Tiziana Trucco Telecomitalia
 */

class GUIUpdater implements ACLMessageListener {

    private static final Logger myLogger = Logger.getMyLogger(JadeGateway.class.getName());
    private Handler handler;
    private SocialAgentActivity activity;

    public GUIUpdater(SocialAgentActivity baseActivity) {
        handler = new Handler();
        activity = baseActivity;
    }

    @Override
    public void onMessageReceived(ACLMessage msg) {
        myLogger.log(Logger.INFO, "onMessageReceived(): GuiUpdater has received message");
        Updater up = new Updater(activity);
        handler.post(up);
    }

    private class Updater implements Runnable {
        private SocialAgentActivity sendMsgActivity;

        public Updater(SocialAgentActivity sm) {
            sendMsgActivity = sm;
        }

        public void run() {
            Toast.makeText(sendMsgActivity, sendMsgActivity.getResources().getText(R.string.notify_msg_received), Toast.LENGTH_SHORT).show();
        }
    }
}

