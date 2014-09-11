package edu.fudan.sa.android;

import android.app.Fragment;
import android.content.Context;
import edu.fudan.sa.IService;

/**
 * Created by ming on 2/18/14.
 */
public interface GUIService extends IService {
    public Fragment getView(Context context);
}
