package com.mygdx.honestmirror.view.ui;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.mygdx.honestmirror.R;
import com.mygdx.honestmirror.application.common.DebugLog;
import com.mygdx.honestmirror.view.activity.MainFeedbackActivity;
import com.mygdx.honestmirror.view.service.ForegroundService;

//Loading screen. This screen is visible when the app is performing the video analysis.
public class a_Loading extends AppCompatActivity {
    //Current progress of the loading bar. Should not exceed 100.
    int progress = 0;

    //Max value for the progress bar. Android wants big integers to animate smoothly.
    final int progressMax = 10000;

    //Layout of the activity.
    ConstraintLayout constraintLayout;

    // Handle for the animated circular progress bar.
    AnimationDrawable animationDrawable;

    //Progress text to display
    TextView progressText;

    ProgressBar progressBar;

    // Handler thread that updates the progress animation.
    Handler handler = new Handler();

    //Result button that appears when loading is done.
    Button b_Results;

    // Android default constructor.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_loading);
        AAL.setTitleBar(getWindow());
        constraintLayout = findViewById(R.id.constraint_loading);
        animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(1000);
        animationDrawable.setExitFadeDuration(1000);
        animationDrawable.start();
        b_Results = findViewById(R.id.resultsButton);
        b_Results.setOnClickListener(v -> {
            Context context = getApplicationContext();
            Intent intent = new Intent(context, MainFeedbackActivity.class);
            startActivity(intent);
        });
        b_Results.setVisibility(View.INVISIBLE);

        // get the progress bar and set its precision
        progressText = findViewById(R.id.progressTextView);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(progressMax);

        ForegroundService.setWork(() -> {
            while (progress < progressMax) {
                // use a handler to post the progress back to the UI thread as text
                handler.post(() -> {
                    String txt = progress / 100 + "%";
                    progressText.setText(txt);
                });
                // update the progress and sleep for 200 ns
                progressBar.setProgress(progress);
                try {
                    Thread.sleep(0, 4000);
                } catch (Exception e) {
                    //DebugLog.log(e.getMessage());
                }
                progress += 1;
            }
            handler.post(() -> {
                notifyUser();
                b_Results.setVisibility(View.VISIBLE);
            });


        });
        startService();


    }

    // Android function override, stops the service.
    @Override
    public void onStop() {
        super.onStop();
        finish();
    }

    //Notifies the user when it's done loading.
    public void notifyUser() {
        int importance = NotificationManager.IMPORTANCE_HIGH;
        final String CHANNEL_ID = "notifyUser";
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(getApplicationContext(), a_Results.class), PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "notifyUser", importance);
        notificationChannel.setDescription("Your video has been processed");
        notificationChannel.getLockscreenVisibility();
        notificationChannel.enableLights(true);
        notificationChannel.enableVibration(true);
        notificationChannel.shouldShowLights();
        notificationChannel.shouldVibrate();
        notificationManager.createNotificationChannel(notificationChannel);

        Notification notification = new NotificationCompat.Builder(this, "ForeGroundService")
                .setContentTitle("Honest Mirror")
                .setContentText("Neural Network is done.")
                .setSmallIcon(R.drawable.testplaatje)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setColorized(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setColor(0xff0000ff)
                .setLights(0xff0000ff, 1111, 1111)
                .setChannelId(CHANNEL_ID)
                .build();
        notificationManager.notify(12, notification);
    }

    //Starts the foreground service. Typically called when this activity starts.
    public void startService() {
        Toast toast = Toast.makeText(getApplicationContext(), "Started video analysis, this could take a while", Toast.LENGTH_LONG);
        toast.show();
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        serviceIntent.setData(getIntent().getData());
        ContextCompat.startForegroundService(this, serviceIntent);
    }

    public void setProgressBar(int i)
    {
        this.progress = i;
    }


}
