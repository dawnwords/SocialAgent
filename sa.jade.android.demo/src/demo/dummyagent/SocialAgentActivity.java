package demo.dummyagent;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import jade.android.ConnectionListener;
import jade.android.JadeGateway;
import jade.core.Profile;
import jade.imtp.leap.JICP.JICPProtocol;
import jade.util.Logger;
import jade.util.leap.Properties;

import java.net.ConnectException;

/**
 * This class implements the Activity associated with the SocialAgent application
 * This activity starts a Jade MicroRuntime with a GatewayAgent in it that replicates
 * the capabilities of the <code>jade.tools.SocialAgent</code>
 * This class replicates on ANDROID SDK the capabilities of the GUI of SocialAgent
 * It provides support to send message to other jade agent running on the same Jade
 * platform and show the received ones.
 * A notify implemented with the Tast mechanism is sent to the View when a new message
 * has arrived
 *
 * @author Stefano Semeria  Reply Cluster
 * @author Tiziana Trucco Telecomitalia
 * @author Marco Ughetti Telecomitalia
 */

public class SocialAgentActivity extends Activity implements ConnectionListener {

    private final Logger myLogger = Logger.getMyLogger(SocialAgentActivity.class.getName());

    //Keys for parameters to Message details activity
    private final int STATUSBAR_NOTIFICATION = R.layout.send_message;

    private final int JADE_EXIT_ID = Menu.FIRST;
    private JadeGateway gateway;
    private NotificationManager nManager;
    private GUIUpdater updater;

    protected void onCreate(Bundle icicle) {

        super.onCreate(icicle);
        myLogger.log(Logger.INFO, "SendMessageActivity.onCreate() : starting onCreate method");

        setContentView(R.layout.send_message);

        nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        updater = new GUIUpdater(this);

        Properties props = new Properties();
        props.setProperty(Profile.MAIN_HOST, getResources().getString(R.string.host));
        props.setProperty(Profile.MAIN_PORT, getResources().getString(R.string.port));
        props.setProperty(JICPProtocol.MSISDN_KEY, getResources().getString(R.string.msisdn));

        try {
            JadeGateway.connect(SocialAgent.class.getName(), null, props, this, this);
        } catch (Exception e) {
            myLogger.log(Logger.SEVERE, e.getMessage(), e);
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void onConnected(JadeGateway jadeGateway) {
        gateway = jadeGateway;
        try {
            gateway.execute(updater);
        } catch (ConnectException ce) {
            Log.e("jade.android", ce.getMessage(), ce);
            Toast.makeText(this, ce.getMessage(), Toast.LENGTH_LONG).show();
        } catch (Exception e1) {
            Log.e("jade.android", e1.getMessage(), e1);
            Toast.makeText(this, e1.getMessage(), Toast.LENGTH_LONG).show();
        }

        int icon = R.drawable.dummyagent;
        CharSequence ticker = this.getString(R.string.gateway_connected_ticker);
        Notification.Builder builder = new Notification.Builder(this);
        PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(Intent.ACTION_DEFAULT), PendingIntent.FLAG_ONE_SHOT);
        CharSequence title = this.getString(R.string.gateway_connection);
        CharSequence content = this.getString(R.string.gateway_connected);
        builder.setSmallIcon(icon).setTicker(ticker).setContentTitle(title).setContentText(content).setContentIntent(pi);
        this.nManager.notify(STATUSBAR_NOTIFICATION, builder.build());

    }

    public void onDisconnected() {
        Log.v("jade.android.demo", "OnDisconnected has been called!!!!");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(Menu.NONE, JADE_EXIT_ID, Menu.NONE, R.string.menu_item_exit);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        super.onMenuItemSelected(featureId, item);
        switch (item.getItemId()) {
            case JADE_EXIT_ID:
                finish();
                break;
        }
        return true;
    }

    protected void onDestroy() {
        super.onDestroy();
        Log.v("jade.android.demo", "SendMessageActivity.onDestroy() : calling onDestroy method");
        nManager.cancel(STATUSBAR_NOTIFICATION);
        try {
            if (gateway != null)
                gateway.shutdownJADE();

        } catch (ConnectException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        if (gateway != null)
            gateway.disconnect(this);
    }
}