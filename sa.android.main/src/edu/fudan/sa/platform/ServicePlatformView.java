package edu.fudan.sa.platform;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import edu.fudan.sa.SocialAgentViewImpl;
import edu.fudan.sa.android.GUIService;
import edu.fudan.sa.view.R;
import jade.util.Logger;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.framework.ServiceReference;

import java.util.LinkedList;
import java.util.List;

public class ServicePlatformView {

    private final ListView drawerList;
    private final DrawerLayout container;
    private SocialAgentViewImpl mainView;
    private CharSequence drawerTitle;
    private List<Bundle> bundles;
    private ActionBarDrawerToggle drawerToggle;

    public ServicePlatformView(final SocialAgentViewImpl mainView) {
        this.mainView = mainView;
        ServicePlatform platform = mainView.getAgent().getServicePlatform();
        platform.addBundleListener(new BundleListener() {
            @Override
            public void bundleChanged(BundleEvent bundleEvent) {
                if (bundleEvent.getType() == BundleEvent.RESOLVED) {
                    Logger logger = Logger.getMyLogger(ServicePlatformView.class.getName());
                    logger.log(Logger.INFO, "***new bundle:" + bundleEvent.getBundle().getSymbolicName() + " ***");
                }
            }
        });
        this.drawerTitle = mainView.getTitle();
        this.bundles = new LinkedList<Bundle>();
        container = (DrawerLayout) mainView.findViewById(R.id.drawer_layout);
        container.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        drawerList = (ListView) mainView.findViewById(R.id.left_drawer);
        final BundlesListAdapter drawerListAdapter = new BundlesListAdapter(mainView, R.layout.item_bundle, bundles);
        drawerList.setAdapter(drawerListAdapter);
        this.drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });
        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        this.drawerToggle = new BundlesDrawerListener(mainView, container, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close);
        container.setDrawerListener(this.drawerToggle);

        this.mainView.getAgent().getServicePlatform().addBundleListener(new BundleListener() {
            @Override
            public void bundleChanged(BundleEvent bundleEvent) {
                Bundle bundle = bundleEvent.getBundle();
                if (bundleEvent.getType() == BundleEvent.INSTALLED) {
                    bundles.add(bundle);
                    invalidate();
                } else if (bundleEvent.getType() == BundleEvent.UNINSTALLED) {
                    bundles.remove(bundle);
                    invalidate();
                }
            }

            private void invalidate() {
                mainView.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        drawerListAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    //    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void selectItem(int position) {
        Bundle bundle = this.bundles.get(position);
        // update the main content by replacing fragments
        try {
            ServiceReference<?>[] ss = bundle.getRegisteredServices();
            for (ServiceReference sr : ss) {
                Object s = bundle.getBundleContext().getService(sr);
                if (s instanceof GUIService) {
                    GUIService service = (GUIService) s;

                    Fragment fragment = service.getView(this.mainView);
                    android.os.Bundle args = new android.os.Bundle();
                    fragment.setArguments(args);
                    FragmentManager fragmentManager = mainView.getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                    drawerList.setItemChecked(position, true);

                    mainView.setTitle(bundles.get(position).getSymbolicName());
                    container.closeDrawer(drawerList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CharSequence getTitle() {
        return this.drawerTitle;
    }

    public void syncState() {
        drawerToggle.syncState();
    }

    public void changeConfig(Configuration newConfig) {
        drawerToggle.onConfigurationChanged(newConfig);
    }

    class BundlesListAdapter extends ArrayAdapter<Bundle> {

        public BundlesListAdapter(Context context, int resource, List<Bundle> objects) {
            super(context, resource, objects);
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).getBundleId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("ViewHolder")
            View rowView = inflater.inflate(R.layout.item_bundle, parent, false);
            TextView textView = (TextView) rowView.findViewById(R.id.item_bundle_name);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.item_bundle_logo);
            Bundle bundle = getItem(position);
            textView.setText(bundle.getSymbolicName());
            imageView.setImageResource(R.drawable.earth);
            return rowView;
        }
    }

    private class BundlesDrawerListener extends ActionBarDrawerToggle {
        public BundlesDrawerListener(Activity activity, DrawerLayout drawerLayout, int drawerImageRes, int openDrawerContentDescRes, int closeDrawerContentDescRes) {
            super(activity, drawerLayout, drawerImageRes, openDrawerContentDescRes, closeDrawerContentDescRes);
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        public void onDrawerClosed(View view) {
            mainView.setTitle(null);
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        public void onDrawerOpened(View drawerView) {
            mainView.setTitle(getTitle());
        }
    }
}
