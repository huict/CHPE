package com.mygdx.honestmirror.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mygdx.honestmirror.application.common.DebugLog;
import com.mygdx.honestmirror.R;
import com.mygdx.honestmirror.application.domain.analysis.Action;
import com.mygdx.honestmirror.application.domain.analysis.Analysis;
import com.mygdx.honestmirror.application.domain.analysis.Data;
import com.mygdx.honestmirror.application.domain.analysis.DatabaseData;
import com.mygdx.honestmirror.data.persistance.PersistenceClient;

import java.util.ArrayList;
import java.util.HashMap;
/**
 * class (activity) where all the results will be shown after the latest
 * analysis from the app.
 * @author Gianluca Picardo
 */
public class CurrentResultActivity extends AppCompatActivity {
    /**
     * Declaration of variables to make a onscreen list
     */
    ListView listView;
    ArrayList<String> feedback = new ArrayList<>();
    ArrayAdapter adapter;
    HashMap<Action, Boolean> results = new HashMap<>();
    Handler handler = new Handler();
    /**
     * Constructor
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_result);
        /**
         * Declaration and initialization of variables for feedback information
         */
        listView = findViewById(R.id.listView);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, feedback);
        listView.setAdapter(adapter);

        /*
        * Declaration and initialization of variables for feedback information
        */
        Context context = getApplicationContext();
        Data data = new DatabaseData(PersistenceClient.getInstance(context).getAppDatabase());
        final Analysis program = new Analysis(data);

        // launch a thread that filters the data
        final Thread filterThread = new Thread(new Runnable() {
            @Override
            public void run() {
                program.process();

            }
        });
        filterThread.start();

        // launch a thread that waits for filtering to complete
        Thread waitThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // wait for filter thread to complete
                    filterThread.join();
                    // detect actions
                    results = program.detect();
                    // start a handle that communicates with the UI thread,
                    // updates the ListView
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            updateList();
                        }
                    });
                } catch (InterruptedException e) {
                    DebugLog.log(e.getMessage());
                }

            }
        });
        waitThread.start();

        /**
         * Android Version control for colored status bar
         */
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.rgb(0.902f,0.188f,0.157f));
        }
        else {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(0);
        }
    }

    void updateList() {
        /**
         * filling HashMap with testing data, will be removed upon final version
         */
        for(HashMap.Entry<Action, Boolean> pair : results.entrySet()) {
            if(pair.getValue()) {
                feedback.add(pair.getKey().getName() + " happened.");
            } else {
                feedback.add(pair.getKey().getName() + " didn't happen.");
            }
        }
        adapter.notifyDataSetChanged();
    }
}
