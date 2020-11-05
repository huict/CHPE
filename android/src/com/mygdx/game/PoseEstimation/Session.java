package com.mygdx.game.PoseEstimation;


// Ensuring that sessions can be cancelled and continued later on.

import android.content.Context;
import android.graphics.Bitmap;

import com.mygdx.game.DebugLog;
import com.mygdx.game.Persistance.AppDatabase;
import com.mygdx.game.Persistance.PersistenceClient;
import com.mygdx.game.Persistance.Video.NNVideo;
import com.mygdx.game.PoseEstimation.nn.ModelFactory;
import com.mygdx.game.PoseEstimation.nn.NNInterpreter;
import com.mygdx.game.PoseEstimation.nn.PoseNet.Person;
import com.mygdx.game.PoseEstimation.nn.PoseNet.PoseNetHandler;
import com.mygdx.game.VideoHandler.VideoSplicer;
import java.util.ArrayList;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;

/**
 * The type Session.
 */
@SuppressWarnings({"FieldMayBeFinal"})
public class Session {

    private NNInserts nnInsert;
    private CHPE chpe;
    public VideoSplicer videoSplicer;
    private AppDatabase appDatabase;
    private long videoId;
    private Resolution resolution;
    private NNInterpreter nnInterpreter = NNInterpreter.CPU;
    List<Person> persons = new ArrayList<>();
    JsonArrayBuilder jsonArray = Json.createArrayBuilder();
    private JsonArray jsonFrames = null;

    public JsonArray getJsonFrames(){
        return jsonFrames;
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
        while (this.videoSplicer.isNextTimeAvailable()) {
            long totalStartTime = System.nanoTime();

            PoseNetHandler pnh = this.chpe.givePoseNetHandler(this.nnInterpreter);

            Bitmap bitmap = createBitmapThread();

            Person p = estimatePoseThread(pnh, bitmap);
            persons.add(p);

            long totalEndTime = System.nanoTime();
            DebugLog.log("total function Took: " + ((totalEndTime - totalStartTime) / 1000000) + "ms");

        }
        for(Person person: persons){
            jsonArray.add(person.toJson());
        }
        jsonFrames =  jsonArray.build();
    }
    public Bitmap createBitmapThread() {
        CreateBitmapThread object = new CreateBitmapThread(this.videoSplicer);
        object.start();
        return object.getBitmap();
    }

    public Person estimatePoseThread(PoseNetHandler pnh, Bitmap bitmap) {
        EstimatePoseThread object = new EstimatePoseThread(pnh, bitmap);
        object.start();
        return object.getPerson();
    }
    /**
     * The NormaliseData query.
     * Should run after the run video run to normalise the data.
     */
    public void normaliseData() {
        this.nnInsert.normalise(this.videoId);
    }
}
