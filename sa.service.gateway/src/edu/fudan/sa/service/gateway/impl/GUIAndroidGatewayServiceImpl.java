package edu.fudan.sa.service.gateway.impl;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import edu.fudan.sa.android.BundleView;
import edu.fudan.sa.android.GUIService;
import jade.android.JadeGateway;
import jade.core.Profile;
import jade.imtp.leap.JICP.JICPProtocol;

import java.net.ConnectException;
import java.util.Properties;

public class GUIAndroidGatewayServiceImpl extends GatewayServiceImpl implements GUIService {
    private static GUIAndroidGatewayServiceImpl instance;
    private GatewayBundleView view;

    private GUIAndroidGatewayServiceImpl() {
        this.view = new GatewayBundleView();
    }

    public static GUIAndroidGatewayServiceImpl getInstance() {
        if (instance == null)
            instance = new GUIAndroidGatewayServiceImpl();
        return instance;
    }

    @Override
    public Fragment getView(Context context) {
        return this.view;
    }

    @Override
    public void config(Properties config) throws Exception {
        super.config(config);
        this.view.notifyConfigUpdated();
    }

    @Override
    protected void notifyConnected(JadeGateway gateway) {
        this.view.notifyConnected();
        super.notifyConnected(gateway);
    }

    @Override
    protected void notifyDisconnected() {
        this.view.notifyDisconnected();
        super.notifyDisconnected();
    }

    class GatewayBundleView extends BundleView {

        private TextView portText;
        private TextView ipText;
        private TextView agentText;
        private Button connectBtn;
        private TextView classText;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            Context context = container.getContext();
            LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            TextView ipLabel = new TextView(context);
            ipLabel.setText("host:");
            ipLabel.setWidth(50);
            ipText = new TextView(context);
            ipText.setText(getConfig().getProperty(Profile.MAIN_HOST));
            ipText.setLayoutParams(params);
            LinearLayout ipRow = new LinearLayout(context);
            ipRow.setOrientation(LinearLayout.HORIZONTAL);
            ipRow.addView(ipLabel);
            ipRow.addView(ipText);

            TextView portLabel = new TextView(context);
            portLabel.setText("port:");
            portLabel.setWidth(50);
            portText = new TextView(context);
            portText.setText(getConfig().getProperty(Profile.MAIN_PORT));
            portText.setLayoutParams(params);
            LinearLayout portRow = new LinearLayout(context);
            portRow.setOrientation(LinearLayout.HORIZONTAL);
            portRow.addView(portLabel);
            portRow.addView(portText);

            TextView agentLabel = new TextView(context);
            agentLabel.setText("name:");
            agentLabel.setWidth(50);
            agentText = new TextView(context);
            agentText.setText(getConfig().getProperty(JICPProtocol.MSISDN_KEY));
            agentText.setLayoutParams(params);
            LinearLayout agentRow = new LinearLayout(context);
            agentRow.setOrientation(LinearLayout.HORIZONTAL);
            agentRow.addView(agentLabel);
            agentRow.addView(agentText);


            TextView classLabel = new TextView(context);
            classLabel.setText("agent class:");
            classText = new TextView(context);
            classText.setText(getConfig().getProperty(WORKERAGENT_CLASS_NAME));
            classText.setLayoutParams(params);
            LinearLayout classRow = new LinearLayout(context);
            classRow.setOrientation(LinearLayout.VERTICAL);
            classRow.addView(classLabel);
            classRow.addView(classText);

            connectBtn = new Button(context);
            if (isConnected()) {
                this.notifyConnected();
            } else {
                this.notifyDisconnected();
            }

            layout.addView(ipRow);
            layout.addView(portRow);
            layout.addView(agentRow);
            layout.addView(classRow);
            layout.addView(connectBtn);
            return layout;
        }

        protected void notifyConfigUpdated() {
            if (this.getActivity() == null)
                return;
            this.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ipText.setText(getConfig().getProperty(Profile.MAIN_HOST));
                    portText.setText(getConfig().getProperty(Profile.MAIN_PORT));
                    agentText.setText(getConfig().getProperty(JICPProtocol.MSISDN_KEY));
                }
            });
        }

        public void notifyConnected() {
            if (this.getActivity() == null)
                return;
            this.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    connectBtn.setText("Disconnect Now!");
                    connectBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                disconnect();
                            } catch (ConnectException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });
        }

        public void notifyDisconnected() {
            if (this.getActivity() == null)
                return;
            this.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    connectBtn.setText("Connect Now!");
                    connectBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                connect();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });
        }
    }
}
