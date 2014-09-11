package edu.fudan.sa.service.manager.impl;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import edu.fudan.sa.IRemotableService;
import edu.fudan.sa.SocialCircle;
import edu.fudan.sa.android.BundleView;
import edu.fudan.sa.android.GUIService;
import edu.fudan.sa.ontology.Service;

import java.util.ArrayList;
import java.util.Collection;

public class GUIAndroidServiceManager extends ServiceManagerImpl implements GUIService {
	private static GUIAndroidServiceManager instance;

	private GUIAndroidServiceManager() {
	}

	public static GUIAndroidServiceManager getInstance() {
		if (instance == null) {
			instance = new GUIAndroidServiceManager();
		}
		return instance;
	}

	@Override
	public Fragment getView(Context context) {
		return new PublicationBundleView();
	}

	class PublicationBundleView extends BundleView {
		private ArrayList<Service> services = new ArrayList<Service>();
		private SocialCircle initialCircle;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			Context context = container.getContext();
			LinearLayout layout = new LinearLayout(context);
			layout.setOrientation(LinearLayout.VERTICAL);
			layout.setVerticalScrollBarEnabled(true);

			Collection<IRemotableService> services = getRemotableServices();
			for (IRemotableService s : services) {
				Service sd = s.getDescription();

				CheckBox box = new CheckBox(context);
				box.setTag(sd);
				box.setText(sd.getName());
				box.setOnCheckedChangeListener(new CheckBoxListener());
				layout.addView(box);

				TextView label = new TextView(context);
				label.setText(sd.getDescription());
				layout.addView(label);
			}

			Button publishBtn = new Button(context);
			publishBtn.setText("Publish");
			publishBtn.setOnClickListener(new ButtonClickListener());
			layout.addView(publishBtn);

			Button activateBtn = new Button(context);
			activateBtn.setText("activate initial circle");
			activateBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					try {
						initialCircle.activatePublisher(ServiceManagerActivator.bundleContext);
						initialCircle.activateResolver(ServiceManagerActivator.bundleContext);
						initialCircle.activateListener(ServiceManagerActivator.bundleContext);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			layout.addView(activateBtn);

			Button joinBtn = new Button(context);
			joinBtn.setText("Join Initial Circle");
			joinBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					try {
						initialCircle = GUIAndroidServiceManager.super.joinInitialCircle();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			layout.addView(joinBtn);

			ScrollView sc = new ScrollView(context);
			sc.addView(layout);
			return sc;
		}

		class CheckBoxListener implements CompoundButton.OnCheckedChangeListener {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					services.add((Service) buttonView.getTag());
				} else {
					services.remove(buttonView.getTag());
				}
			}
		}

		class ButtonClickListener implements View.OnClickListener {
			public void onClick(View v) {
				try {
					publish(initialCircle, services);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}