package com.mygdx.game;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.mygdx.game.Exceptions.InvalidVideoSplicerType;
import com.mygdx.game.PoseEstimation.Session;
import com.mygdx.game.UI.a_Loading;
import com.mygdx.game.VideoHandler.VideoSplicer;
import com.mygdx.game.VideoHandler.VideoSplicerFactory;
import com.mygdx.game.nnanalysis.InterpreterController;

import javax.json.JsonArray;
import javax.json.JsonObject;

/**
 * Class where the neural network will analyze the video footage
 * @author Gianluca Piccardo
 */
public class ForegroundService extends Service {
    /**
     * Declaration of the name inside the notification
     */
    public String CHANNEL_ID = "ForegroundService";
    static Thread thread;
    static Runnable work;
    private VideoSplicer videoSplicer;

    /**
     * This function sets the work that the foreground service will perform on start command.
     * @param r Android runnable that contains the work.
     */
    public static void setWork(Runnable r) {
        work = r;
    }

    /**
     * Constructor
     */
    @Override
    public void onCreate() {
        super.onCreate();
    }
    /**
     * Function that starts as soon the Service is called upon
     * Notificationchannel and Notication is created
     * Uri's are copied and service gets started
     * @author Gianluca Piccardo
     * @param intent what intents to use
     * @param flags what permissions have been set
     * @param startID A unique integer representing this specific request to start
     * @return STICKY_NOT_STICKY The return value indicates what semantics the system should use for the
     * service's current started state.
     */
    @SuppressLint("NewApi")
    @Override
    public int onStartCommand(Intent intent, int flags, int startID) {
        /**
         * Creating Uri for MediaMetadataRetriever
         */


        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(getApplicationContext(), a_Loading.class), PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "JointFinder", importance);
        notificationChannel.setDescription("Channel for neural network");
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(Color.RED);
        notificationManager.createNotificationChannel(notificationChannel);
        Notification notification = new NotificationCompat.Builder(this, "ForeGroundService")
                .setContentTitle("Honest Mirror")
                .setContentText("Processing video in neural network")
                .setSmallIcon(R.drawable.testplaatje)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .setChannelId(CHANNEL_ID)
                .build();
        /**
         * Starting actual notification on Foreground
         */
        startForeground(7, notification);
        /**
         Launch thread that performs the actual work
         */

        final Uri otherUri = intent.getData();
        thread = new Thread(() -> {

            MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever();
            metadataRetriever.setDataSource(getApplicationContext(), otherUri);
            try {
                VideoSplicer videoSplicer = VideoSplicerFactory.getVideoSplicer(metadataRetriever);
                Session session = new Session(getApplicationContext(), videoSplicer);
                session.runVideo();
                //session.normaliseData();
                JsonArray jsonFrames = session.getJsonFrames();

                InterpreterController interpreterController = new InterpreterController(getApplicationContext());
                interpreterController.setInput(jsonFrames);
                interpreterController.LoadData();

                Log.println(Log.INFO, "", interpreterController.writeFeedback());
            }catch (InvalidVideoSplicerType splicerType){
                Log.e(splicerType.getClass().toGenericString(), splicerType.toString());
                throw new RuntimeException("InvalidVideoSplicer");
            }catch (Exception e){
                if (e.getClass() == RuntimeException.class)
                    throw e;

                Log.e("InterpreterController:", e.getMessage());
            }
            work.run();
            stopForeground(true);
            stopSelf();
        });
        thread.start();

        return START_NOT_STICKY;
    }

    /**
     * Necessary function overrides
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    /**
     * Necessary function overrides
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
