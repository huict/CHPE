package com.mygdx.honestmirror;

import android.app.Application;
import android.content.Context;

import com.mygdx.honestmirror.application.common.DebugLog;

public class GlobalApplication extends Application {

    private static Context appContext;
    private static int progress;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();

    }

    public static Context getAppContext() {
        return appContext;
    }

    public  static void addToProgressBar(int i)
    {
        progress += i;
    }

    public static int getProgress()
    {
        return progress;
    }

}
