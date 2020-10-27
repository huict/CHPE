package com.mygdx.game;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.mygdx.game.Analysis.JSONLoader;
import com.mygdx.game.Persistance.AppDatabase;
import com.mygdx.game.Simulation.MyGdxGame;
import com.mygdx.game.Persistance.PersistenceClient;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import static com.mygdx.game.Persistance.PersistenceClient.getInstance;

public class HomeScreen extends AndroidApplication {
    /**
     * Button declaration for onscreen buttons
     */
    Button previousResultScreenButton;
    Button galleryScreenButton;
    /**
     * Button declaration for testing purposes, will be removed when app is in final stages
     */
    Button JUMP;
    /**
     * Views where UI elements and LibGDX views go
     */
    View libGDXView;
    View embeddedView;
    /**
     * Loads a JSON file
     */
    JSONLoader loader;
    /**
     * Context needed for animations onscreen
     */
    private static Context context;
    /**
     * Constructor
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * LibGDX config, needed for Views
         */
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        /**
         * Temporary code for Database testing purposes, will be removed upon final stages of the app
         */
        getInstance(getApplicationContext());
        AssetManager am = getApplicationContext().getAssets();
        InputStream is = null;
        try {
            is = am.open("data/wave.json");
        } catch (IOException e) {
            DebugLog.log("Unable to load asset norm json");
        }
        Reader r = new InputStreamReader(is);
        /**
         * Android Version control for colored status bar
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.rgb(0.902f, 0.188f, 0.157f));
        } else {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(0);
        }
        /**
         * Temporary code for Database testing purposes, will be removed upon final stages of the app
         */
        loader = new JSONLoader(r);
        DebugLog.log(loader.toString());
        DebugLog.log(String.valueOf(loader.getFrameCount()));
        AppDatabase appDatabase = PersistenceClient.getInstance(getApplicationContext()).getAppDatabase();
        MockData mockData = new MockData(appDatabase, loader.getArray());
        
        setContentView(R.layout.activity_home_screen);
        /**
         * Initializing Views & Buttons by finding id's
         */
        libGDXView = initializeForView(new MyGdxGame(), config);
        embeddedView = findViewById(R.id.gameView);
        previousResultScreenButton = findViewById(R.id.previousResults);
        galleryScreenButton = findViewById(R.id.galleryScreenButton);
        JUMP = findViewById(R.id.JUMP);
        /**
         * @function Binds the click to a function
         * @author Gianluca Piccardo
         * @param View.OnClickListener creates a new view for the new Intent
         */
        previousResultScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPreviousResultScreen();
            }
        });
        /**
         * @function Binds the click to a function
         * @author Gianluca Piccardo
         * @param View.OnClickListener creates a new view for the new Intent
         */
        galleryScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLoadVideoScreen();
            }
        });
        /**
         * @function Binds the click to a function
         * @author Gianluca Piccardo
         * @param View.OnClickListener creates a new view for the new Intent
         */
        JUMP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openJUMP();
            }
        });
        /**
         * @function Replaces part of the View with a LibGDX View
         * @author Gianluca Piccardo
         * @param embeddedView normal home-screen view
         * @param libGDXView new overlapping view
         */
        replaceView(embeddedView, libGDXView);
        HomeScreen.context = getApplicationContext();
    }
    /**
     * @author Nico van Bentum
     * @return Homescreen.context
     */
    public static Context getAppContext(){
        return HomeScreen.context;
    }
    /**
     * Opens up a new Activity by setting an Intent
     * @author Gianluca Piccardo
     // @param this current class
     // @param PreviousresultActivity.class class to go to
     * @return void
     */
    public void openPreviousResultScreen(){
        Intent intent = new Intent(this, PreviousResultActivity.class);
        startActivity(intent);
    }
    /**
     * Opens up a new Activity by setting an Intent
     * @author Gianluca Piccardo
     // @param this current class
     // @param CurrenresultActivity.class class to go to
     * @return void
     */
    public void openJUMP(){
        Intent intent = new Intent(this, CurrentResultActivity.class);
        startActivity(intent);
    }
    /**
     * Opens up a new Activity by setting an Intent
     * @author Gianluca Piccardo
     // @param this current class
     // @param GalleryScreen.class class to go to
     * @return void
     */
    public void openLoadVideoScreen(){
        Intent intent = new Intent(this, GalleryScreen.class);
        startActivity(intent);
    }
    /**
     * Replaces a part of the HomeScreen View with a LibGDX View
     * @author Nico van Bentum
     * @param oldView view that needs to be overlapped
     * @param newView overlapping view
     * @return void
     */
    private void replaceView(View oldView, View newView) {
        ViewGroup parent = (ViewGroup)oldView.getParent();
        ViewGroup.LayoutParams oldParameters = oldView.getLayoutParams();
        newView.setLayoutParams(oldParameters);
        if(parent == null) {
            return;
        }
        int index = parent.indexOfChild(oldView);
        parent.removeViewAt(index);
        parent.addView(newView, index);
    }
}
