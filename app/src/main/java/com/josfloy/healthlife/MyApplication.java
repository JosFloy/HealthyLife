package com.josfloy.healthlife;

import android.app.Application;
import com.josfloy.healthlife.utils.SaveKeyValues;

/**
 * Created by Jos on 2019/7/13 0013.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SaveKeyValues.createSharedPreferences(this);
    }
}
