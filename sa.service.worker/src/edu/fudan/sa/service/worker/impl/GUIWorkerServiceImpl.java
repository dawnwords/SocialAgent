package edu.fudan.sa.service.worker.impl;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import edu.fudan.sa.android.BundleView;
import edu.fudan.sa.android.GUIService;
import edu.fudan.sa.android.service.GatewayService;
import jade.core.Profile;
import jade.imtp.leap.JICP.JICPProtocol;

import java.util.Properties;

import static android.widget.LinearLayout.LayoutParams;

public class GUIWorkerServiceImpl extends WorkerServiceImpl implements GUIService {

    private final WorkerBundleView view;
    private static GUIWorkerServiceImpl instance;

    private GUIWorkerServiceImpl() {
        this.view = new WorkerBundleView();
    }

    public static GUIWorkerServiceImpl getInstance() {
        if (instance == null)
            instance = new GUIWorkerServiceImpl();
        return instance;
    }

    @Override
    public Fragment getView(Context context) {
        return this.view;
    }

    class WorkerBundleView extends BundleView {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            final Context context = container.getContext();

            LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.VERTICAL);

            LinearLayout ipRow = new LinearLayout(context);
            ipRow.setOrientation(LinearLayout.HORIZONTAL);
            TextView ipLabel = new TextView(context);
            ipLabel.setText("host:");
            ipLabel.setWidth(50);
            final EditText ipInput = new EditText(context);
            LayoutParams ipParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
            ipInput.setLayoutParams(ipParams);
            ipInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_CLASS_TEXT);
            ipInput.setSingleLine();
            ipRow.addView(ipLabel);
            ipRow.addView(ipInput);

            LinearLayout portRow = new LinearLayout(context);
            portRow.setOrientation(LinearLayout.HORIZONTAL);
            TextView portLabel = new TextView(context);
            portLabel.setText("port:");
            portLabel.setWidth(50);
            final EditText portInput = new EditText(context);
            LayoutParams portParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
            portInput.setLayoutParams(portParams);
            portInput.setInputType(InputType.TYPE_CLASS_NUMBER);
            portInput.setSingleLine();
            portRow.addView(portLabel);
            portRow.addView(portInput);

            LinearLayout nameRow = new LinearLayout(context);
            nameRow.setOrientation(LinearLayout.HORIZONTAL);
            TextView nameLabel = new TextView(context);
            nameLabel.setText("name:");
            nameLabel.setWidth(50);
            final EditText nameInput = new EditText(context);
            LayoutParams nameParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
            nameInput.setLayoutParams(nameParams);
            nameInput.setInputType(InputType.TYPE_CLASS_TEXT);
            nameInput.setSingleLine();
            nameRow.addView(nameLabel);
            nameRow.addView(nameInput);

            LinearLayout classRow = new LinearLayout(context);
            classRow.setOrientation(LinearLayout.VERTICAL);
            TextView classLabel = new TextView(context);
            classLabel.setText("agent class:");
            final Spinner spinner = new Spinner(context);
            LayoutParams classParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
            spinner.setLayoutParams(classParams);
            String[] data = GUIWorkerServiceImpl.super.getAgentClasses();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, data);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setVisibility(View.VISIBLE);
            classRow.addView(classLabel);
            classRow.addView(spinner);

            Button configBtn = new Button(context);
            configBtn.setText("Config Now!");
            configBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Editable ip = ipInput.getText();
                    Editable port = portInput.getText();
                    Editable name = nameInput.getText();
                    String clazz = String.valueOf(spinner.getSelectedItem());
                    Properties config = new Properties();
                    config.put(Profile.MAIN_HOST, ip.toString());
                    config.put(Profile.MAIN_PORT, port.toString());
                    config.put(JICPProtocol.MSISDN_KEY, name.toString());
                    config.put(GatewayService.WORKERAGENT_BUNDLE_ID, WorkerServiceActivator.bundleId);
                    config.put(GatewayService.WORKERAGENT_CLASS_NAME, clazz);
                    try {
                        GUIWorkerServiceImpl.super.config(config);
                    } catch (Exception e) {
                        Toast.makeText(context, "configuration error", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            });

            layout.addView(ipRow);
            layout.addView(portRow);
            layout.addView(nameRow);
            layout.addView(classRow);
            layout.addView(configBtn);
            return layout;
        }
    }
}
