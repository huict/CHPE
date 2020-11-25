package com.mygdx.honestmirror.view.service;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
/**
 * Class NeuralService where a optional service is called on the background
 * @deprecated NeuralService
 * @auteur Gianluca Piccardo
 */
public class NeuralService extends JobIntentService {
    /**
     * Tag for channel and debug purposes
     */
    private static final String TAG = "AnalysisService";
    /**
     * Enqueue's the work needed to be done by the main thread
     * @author Gianluca Piccardo
     * @param context context where the service starts from
     * @param work work that needs to be enqueued
     * @return void
     */
    static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, NeuralService.class, 117, work);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Log.d(TAG, "onHandleWork: ");

        String input = intent.getStringExtra("inputExtra");
        for (int i = 0; i < 10; i++) {
            Log.d(TAG, input + " - " + i);
            Log.d(TAG, "IK WERK");
            if(isStopped()) {
                return;
            }
            SystemClock.sleep(1000);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    @Override
    public boolean onStopCurrentWork() {
        Log.d(TAG, "onStopCurrentWork: ");
        return super.onStopCurrentWork();
    }
}
