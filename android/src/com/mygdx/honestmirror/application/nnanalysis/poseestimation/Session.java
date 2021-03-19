package com.mygdx.honestmirror.application.nnanalysis.poseestimation;


// Ensuring that sessions can be cancelled and continued later on.

import android.content.Context;

import com.mygdx.honestmirror.application.common.DebugLog;
import com.mygdx.honestmirror.application.common.videohandler.VideoSplicer;
import com.mygdx.honestmirror.application.nnanalysis.feedback.FeedbackController;
import com.mygdx.honestmirror.application.nnanalysis.feedback.InterpreterController;
import com.mygdx.honestmirror.application.nnanalysis.poseestimation.nn.ModelFactory;
import com.mygdx.honestmirror.application.nnanalysis.poseestimation.nn.NNInterpreter;
import com.mygdx.honestmirror.application.nnanalysis.poseestimation.nn.PoseNet.Person;
import com.mygdx.honestmirror.application.nnanalysis.poseestimation.nn.PoseNet.PoseNetHandler;
import com.mygdx.honestmirror.data.persistance.AppDatabase;
import com.mygdx.honestmirror.data.persistance.PersistenceClient;
import com.mygdx.honestmirror.data.persistance.Video.NNVideo;

import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;

//The type Session.
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

    private InterpreterController interpreterController;
    private FeedbackController feedbackController;

    //Instantiates a new Session.
    public Session(Context context, VideoSplicer videoSplicer){
        this(context, videoSplicer, null, null);
    }

    public Session(Context context, VideoSplicer videoSplicer, InterpreterController interpreterController, FeedbackController feedbackController) {
        this.videoSplicer = videoSplicer;
        this.appDatabase = PersistenceClient.getInstance(context).getAppDatabase();
        this.resolution = new Resolution(1920, 1080, 257);
        this.chpe = new CHPE(context, this.resolution, ModelFactory.POSENET_MODEL);

        this.initialiseDatabase(); // Preparing database for person entry
        this.interpreterController = interpreterController;
        this.feedbackController = feedbackController;
    }

    private void initialiseDatabase() {

        this.videoId = this.appDatabase.nnVideoDAO().insert(new NNVideo(
                24.0f,
                this.videoSplicer.getFrameCount(),
                this.resolution.getScreenWidth(),
                this.resolution.getScreenHeight()
        ));
        this.nnInsert = new NNInserts(this.appDatabase);
    }

    //Loops through a video and stores it continuously
    public void runVideo() {
   //   int temptestInt = 0;
        PoseNetHandler pnh = this.chpe.givePoseNetHandler(this.nnInterpreter);
        List<Person> persons = this.videoSplicer.performAnalyse(pnh);
     //   DebugLog.log("*******interpreterController persons.size()********* " + persons.size() );

        if (interpreterController != null){
            for(Person person: persons){
            //    DebugLog.log("*******Person********* " + persons.toString() );
            //    DebugLog.log("*******Person********* " + person.getKeyPoints() );

                //DebugLog.log("keypoints " + person.getKeyPoints().toString());


                if (person != null){

            //      DebugLog.log("*******interpreterController person.toString()********* " + person.toString() );
                    interpreterController.setJsonInput(person.toJson());
                    interpreterController.runNN();
                    feedbackController.addData(interpreterController.getOutput());//no frameIndex given so allways null why?
         //         temptestInt++;
            //      DebugLog.log("*******interpreterController.getOutput()********* " + interpreterController.getOutput() );
                }
            }
        }

//        feedbackController.generateMockData();
    }
    //The NormaliseData query.
    public void normaliseData() {
        this.nnInsert.normalise(this.videoId);
    }
}
