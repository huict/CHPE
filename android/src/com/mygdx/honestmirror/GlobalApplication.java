package com.mygdx.honestmirror;

import android.app.Application;
import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class GlobalApplication extends Application {

    private static Context appContext;
    public enum Language{Dutch, English}
    private Language language;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
        language = Language.Dutch;
    }

    public static Context getAppContext() {
        return appContext;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }
    
}
