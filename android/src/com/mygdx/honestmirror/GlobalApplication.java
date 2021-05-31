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
    private Language language = Language.Dutch;
    private ArrayList<String> feedbackMessages;
    private ArrayList<String> layoutMessages;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
    }

    public static Context getAppContext() {
        return appContext;
    }

    public Language getLanguage() {
        return this.language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public ArrayList<String> getFeedbackMessages() {
        return this.feedbackMessages;
    }

    public ArrayList<String> getLayoutMessages() {
        return layoutMessages;
    }

    //retrieve all the feedback messages from the file
    //saves the list in a local variable
    public void obtainMessages() throws IOException {
        InputStream is = null;
        switch (this.language){
            case Dutch:
                is = appContext.getAssets().open("feedbackmessages(NL).txt");
                break;
            case English:
                is = appContext.getAssets().open("feedbackmessages(EN).txt");
                break;
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        ArrayList<String> feedbackmessages = new ArrayList<>();
        String line;
        while (reader.readLine() != null) {
            line = reader.readLine();
            feedbackmessages.add(line);
        }
        this.feedbackMessages = feedbackmessages;
    }

    //retrieve all the layout information from the file
    //layout information contains button and notifications
    //saves the list in a local variable
    public void obtainAppText() throws IOException {
        InputStream is = null;
        switch (this.language){
            case Dutch:
                is = appContext.getAssets().open("appText[NL].txt");
                break;
            case English:
                is = appContext.getAssets().open("appText[EN].txt");
                break;
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        ArrayList<String> appText = new ArrayList<>();
        String line;
        while (reader.readLine() != null) {
            line = reader.readLine();
            appText.add(line);
        }
        this.layoutMessages = appText;
    }
}

