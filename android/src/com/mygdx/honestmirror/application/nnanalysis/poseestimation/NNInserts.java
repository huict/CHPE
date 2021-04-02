package com.mygdx.honestmirror.application.nnanalysis.poseestimation;


import com.mygdx.honestmirror.application.nnanalysis.poseestimation.nn.PoseNet.KeyPoint;
import com.mygdx.honestmirror.application.nnanalysis.poseestimation.nn.PoseNet.Person;
import com.mygdx.honestmirror.data.persistance.AppDatabase;
import com.mygdx.honestmirror.data.persistance.Coordinate.NNCoordinate;
import com.mygdx.honestmirror.data.persistance.Frame.NNFrame;
import com.mygdx.honestmirror.data.persistance.Relations.NNFrameCoordinate;
import com.mygdx.honestmirror.data.persistance.Relations.NNVideoFrame;



//NNInsert is an example class used for inserting new videos to the database
@SuppressWarnings("FieldMayBeFinal")
public class NNInserts {

    private AppDatabase appDatabase;
    private final String TAG = "NNInserts";

    //Instantiates a new NN inserts.
    NNInserts(AppDatabase appDatabase) {
        this.appDatabase = appDatabase;

    }


    //Normalise coordinates.
    private double[] setNormaliseCoordinates(long sessionId) {
        double y_limit = this.appDatabase.nnVideoDAO().getMaxValuesX(sessionId);
        double x_limit = this.appDatabase.nnVideoDAO().getMaxValuesY(sessionId);

        double y_multiplier = 1 / y_limit;
        double x_multiplier = (y_limit / x_limit) * 1 / y_limit;
        return new double[]{y_multiplier, x_multiplier};
    }


    //Insert person.
    public void insertPerson(Person person, long videoId, long frameCount) {

        // Creating new frame for the instance
        NNFrame nnFrame = new NNFrame(frameCount);
        long frameId = this.appDatabase.nnFrameDAO().insert(nnFrame);
        linkFrameIdToVideo(frameId, videoId);

//        for (KeyPoint keyPoint : person.getKeyPoints()) {
//            linkFrameToCoordinate(
//                    frameId,
//            this.appDatabase.nnCoordinateDAO().insert(new NNCoordinate(
//                                            keyPoint.getPosition().getX(),
//                                            keyPoint.getPosition().getY()
//                                    )
//                            )
//            );
//        }
    }


    //Normalising the data.
    void normalise(long videoId) {
        double[] normalised = setNormaliseCoordinates(22);
        this.appDatabase
                .nnCoordinateDAO()
                .normaliseCoordinates(videoId, normalised[0], normalised[1]);
    }

    //Links a coordinate to a video.
    private void linkFrameToCoordinate(long frameId, long coordinateId) {
        this.appDatabase
                .nnFrameCoordinateDAO()
                .insert(
                        new NNFrameCoordinate(frameId, coordinateId)
                );
    }

    //Links the frame to a video.
    private void linkFrameIdToVideo(long frameId, long videoId) {
        // Linking to the video
        this.appDatabase
                .nnVideoFrameDAO()
                .insert(
                        new NNVideoFrame(videoId, frameId)
                );
    }
}
