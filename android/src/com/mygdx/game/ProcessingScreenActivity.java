package com.mygdx.game;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.lang.Thread;

/**
 * Class where the user is brought the moment the neural network service starts
 * @author Gianluca Piccardo
 */
public class ProcessingScreenActivity extends AppCompatActivity {
    /**
     * Declaration of Views and Buttons
     */
    View embeddedView;
    Button startButton;
    Button stopButton;
    /**
     * Constructor
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_processing_screen);
        /**
         * Initialization of Views and Buttons
         */
        embeddedView = findViewById(R.id.processingView);
        startButton = findViewById(R.id.startButton);
        stopButton = findViewById(R.id.stopButton);
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

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeToast();
            }
        });

        final Intent doneIntent = new Intent(this, CurrentResultActivity.class);
        final Intent serviceIntent = new Intent(this, ForegroundService.class);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                //ForegroundService.join();
                stopService(serviceIntent);
                startActivity(doneIntent);
            }
        });
        t.start();
    }

    public void makeToast() {
        Toast toast = Toast.makeText(getApplicationContext(), "INPUT RECEIVED", Toast.LENGTH_LONG);
        toast.show();
    }
}
