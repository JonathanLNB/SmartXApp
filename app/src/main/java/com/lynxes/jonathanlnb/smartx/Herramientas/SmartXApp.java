package com.lynxes.jonathanlnb.smartx.Herramientas;

import android.app.Application;
import android.content.Context;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

public class SmartXApp extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        SmartXApp.context = getApplicationContext();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }

    public static Context getAppContext() {
        return SmartXApp.context;
    }
}