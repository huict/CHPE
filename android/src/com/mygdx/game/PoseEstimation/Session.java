package com.mygdx.game.PoseEstimation;


// Ensuring that sessions can be cancelled and continued later on.

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.mygdx.game.DebugLog;
import com.mygdx.game.Exceptions.InvalidFrameAccess;
import com.mygdx.game.Exceptions.InvalidVideoSplicerType;
import com.mygdx.game.Persistance.AppDatabase;
import com.mygdx.game.Persistance.PersistenceClient;
import com.mygdx.game.Persistance.Video.NNVideo;
import com.mygdx.game.PoseEstimation.nn.ModelFactory;
import com.mygdx.game.PoseEstimation.nn.NNInterpreter;
import com.mygdx.game.PoseEstimation.nn.PoseNet.Person;
import com.mygdx.game.VideoHandler.VideoSplicer;
import com.mygdx.game.VideoHandler.VideoSplicerFactory;

/**
 * The type Session.
 */
public class Session {

    private NNInserts nnInsert;
    private CHPE chpe;
    private VideoSplicer videoSplicer;
    private AppDatabase appDatabase;
    private long videoId;
    private Resolution resolution;
    private NNInterpreter nnInterpreter = NNInterpreter.CPU;

    /**
     * Instantiates a new Session.
     *
     * @param uri     the uri
     * @param context the context
     */
// TODO: Run benchmark configuration
    public Session(String uri, Context context) {

        try {
            this.videoSplicer = VideoSplicerFactory.getVideoSplicer(uri);
        } catch (InvalidVideoSplicerType invalidVideoSplicerType) {
            Log.e("SESSION", invalidVideoSplicerType.toString());
        }
        this.appDatabase = PersistenceClient.getInstance(context).getAppDatabase();
        this.resolution = new Resolution(this.videoSplicer.getNextFrame(1));
        this.chpe = new CHPE(context, this.resolution, ModelFactory.POSENET_MODEL);

        this.initialiseDatabase(); // Preparing database for person entry
    }

    /**
     * Instantiates a new Session.
     *
     * @param uri     the uri
     * @param context the context
     */
    public Session(Uri uri, Context context) {
        try {
            this.videoSplicer = VideoSplicerFactory.getVideoSplicer(uri, context);
        } catch (InvalidVideoSplicerType invalidVideoSplicerType) {
            Log.e("SESSION", invalidVideoSplicerType.toString());
        }
        this.appDatabase = PersistenceClient.getInstance(context).getAppDatabase();
        this.resolution = new Resolution(this.videoSplicer.getNextFrame(0));
        this.chpe = new CHPE(context, this.resolution, ModelFactory.POSENET_MODEL);

        this.initialiseDatabase(); // Preparing database for person entry
    }


    /**
     * Instantiates a new Session.
     *
     * @param context      the context
     * @param videoSplicer the video splicer
     */
    public Session(Context context, VideoSplicer videoSplicer) {

        this.videoSplicer = videoSplicer;
        this.appDatabase = PersistenceClient.getInstance(context).getAppDatabase();
        this.resolution = new Resolution(1920, 1080, 257);
        this.chpe = new CHPE(context, this.resolution, ModelFactory.POSENET_MODEL);

        this.initialiseDatabase(); // Preparing database for person entry
    }

    private void initialiseDatabase() {

        this.videoId = this.appDatabase.nnVideoDAO().insert(new NNVideo(
                24.54f,
                this.videoSplicer.getFrameCount(),
                this.resolution.getScreenWidth(),
                this.resolution.getScreenHeight()
        ));
        this.nnInsert = new NNInserts(this.appDatabase);
    }

    /**
     * Loops through a video and stores it continuously
     */
    public void runVideo() {
        while (this.videoSplicer.isNextFrameAvailable()) {
            try {
                this.PersonToFrame(this.chpe.ProcessFrame(this.videoSplicer.getNextFrame(),this.nnInterpreter));
            }
            catch (InvalidFrameAccess invalidFrameAccess) {
                Log.e("runVideo -> PoseNet - Iterator", "runVideo: ", invalidFrameAccess);
            }
        }
    }


    private void PersonToFrame(Person person) {
        this.nnInsert.insertPerson(person, this.videoId, this.videoSplicer.getFramesProcessed());
    }

    /**
     * The NormaliseData query.
     * Should run after the run video run to normalise the data.
     */
    public void normaliseData() {
        this.nnInsert.normalise(this.videoId);
    }
}
