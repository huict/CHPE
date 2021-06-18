package com.mygdx.honestmirror.view.ui;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.mygdx.honestmirror.GlobalApplication;
import com.mygdx.honestmirror.R;
import com.mygdx.honestmirror.application.common.DebugLog;
import com.mygdx.honestmirror.view.activity.MainFeedbackActivity;
import com.mygdx.honestmirror.view.service.ForegroundService;

import static com.mygdx.honestmirror.GlobalApplication.getProgress;

//Loading screen. This screen is visible when the app is performing the video analysis.
public class a_Loading extends AppCompatActivity {
    //Current progress of the loading bar. Should not exceed 100.
    static int progress = 0;

    //Max value for the progress bar. Android wants big integers to animate smoothly.
    final int progressMax = 10000;

    //Layout of the activity.
    ConstraintLayout constraintLayout;

    // Handle for the animated circular progress bar.
    AnimationDrawable animationDrawable;

    ProgressBar progressBar;

    // Handler thread that updates the progress animation.
    Handler handler = new Handler();

    //Result button that appears when loading is done.
    Button b_Results;

    // globalApplication retrieval, so we get access to the language files in this class
    GlobalApplication globalApplication = a_Home.getGlobalApplication();

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
        
        progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(progressMax);

        ForegroundService.setWork(() -> {
             handler.post(() -> {
                notifyUser();
                b_Results.setVisibility(View.VISIBLE);
            });


        });
        setProgressValue();
    }

    private void setProgressValue() {

          // thread is used to change the progress value
          Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    int i = getProgress();
                    while(i < progressMax) {
                        i = getProgress();
                        int finalI = i;
                        runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            TextView progressText;
                            progressText = findViewById(R.id.progressTextView);
                            String txt = finalI / 100 + "%";
                            progressText.setText(txt);
                            progressBar.setProgress(getProgress());
                        }
                    });

                    try {

                       Thread.sleep(1500);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                    }

                }
          });

            thread.start();
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

        String finishedAnalysis = globalApplication.getLayoutMessages().get(11);

        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "notifyUser", importance);
        notificationChannel.setDescription(finishedAnalysis);
        notificationChannel.getLockscreenVisibility();
        notificationChannel.enableLights(true);
        notificationChannel.enableVibration(true);
        notificationChannel.shouldShowLights();
        notificationChannel.shouldVibrate();
        notificationManager.createNotificationChannel(notificationChannel);

        Notification notification = new NotificationCompat.Builder(this, "ForeGroundService")
                .setContentTitle("Honest Mirror")
                .setContentText(finishedAnalysis)
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

        String string = globalApplication.getLayoutMessages().get(9);
        Toast toast = Toast.makeText(getApplicationContext(), string, Toast.LENGTH_LONG);
        toast.show();
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        serviceIntent.setData(getIntent().getData());
        ContextCompat.startForegroundService(this, serviceIntent);
    }

}
