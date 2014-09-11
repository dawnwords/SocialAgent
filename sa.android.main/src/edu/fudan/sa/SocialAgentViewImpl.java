package edu.fudan.sa;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;
import edu.fudan.sa.platform.ServicePlatformView;
import edu.fudan.sa.view.R;
import jade.util.Logger;

public class SocialAgentViewImpl extends Activity {
    private CharSequence title;
    private ServicePlatformView servicePlatformView;
    private SocialAgent agent;

    /**
     * Called when the activity is first created.
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger logger = Logger.getMyLogger(SocialAgentViewImpl.class.getName());
        this.agent = SocialAgent.getAgent(this);
        try {
            this.agent.start();
        } catch (Exception e) {
            logger.log(Logger.SEVERE, e.getMessage(), e);
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_main);

        // enable ActionBar app icon to behave as action to toggle nav drawer
        this.getActionBar().setDisplayHomeAsUpEnabled(true);
        this.getActionBar().setHomeButtonEnabled(true);

        this.title = this.getTitle();
        servicePlatformView = new ServicePlatformView(this);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        servicePlatformView.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        servicePlatformView.changeConfig(newConfig);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void setTitle(CharSequence t) {
        super.setTitle(t);
        this.title = t == null ? title : t;
        getActionBar().setTitle(this.title);
        invalidateOptionsMenu();
    }

    public SocialAgent getAgent() {
        return agent;
    }
}
