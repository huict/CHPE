package com.mygdx.honestmirror.application.nnanalysis.poseestimation;

import com.mygdx.honestmirror.Persistance.AppDatabase;
import com.mygdx.honestmirror.Persistance.Coordinate.NNCoordinate;
import com.mygdx.honestmirror.Persistance.Frame.NNFrame;
import com.mygdx.honestmirror.Persistance.Relations.NNFrameCoordinate;
import com.mygdx.honestmirror.Persistance.Relations.NNVideoFrame;
import com.mygdx.honestmirror.application.nnanalysis.poseestimation.nn.PoseNet.KeyPoint;
import com.mygdx.honestmirror.application.nnanalysis.poseestimation.nn.PoseNet.Person;

// TODO: Convert sessionId to videoId


/**
 * NNInsert is an example class used for inserting new videos to the database
 */
@SuppressWarnings("FieldMayBeFinal")
public class NNInserts {

    private AppDatabase appDatabase;
    private final String TAG = "NNInserts";

    /**
     * Instantiates a new NN inserts.
     *
     * @param appDatabase the app database
     */
    NNInserts(AppDatabase appDatabase) {
        this.appDatabase = appDatabase;

    }


    /**
     * Normalise coordinates.
     *
     * @param sessionId the session id
     */
    private double[] setNormaliseCoordinates(long sessionId) {
        double y_limit = this.appDatabase.nnVideoDAO().getMaxValuesX(sessionId);
        double x_limit = this.appDatabase.nnVideoDAO().getMaxValuesY(sessionId);

        double y_multiplier = 1 / y_limit;
        double x_multiplier = (y_limit / x_limit) * 1 / y_limit;
        return new double[]{y_multiplier, x_multiplier};
    }


    /**
     * Insert person.
     *
     * @param person     the Person object used to extract points from
     * @param videoId    the video id based on the database insert
     * @param frameCount the nth frame count to ensure it's placed in order.
     */
    public void insertPerson(Person person, long videoId, long frameCount) {

        // Creating new frame for the instance
        NNFrame nnFrame = new NNFrame(frameCount);
        long frameId = this.appDatabase.nnFrameDAO().insert(nnFrame);
        linkFrameIdToVideo(frameId, videoId);

        for (KeyPoint keyPoint : person.getKeyPoints()) {
            linkFrameToCoordinate(
                    frameId,
            this.appDatabase.nnCoordinateDAO().insert(new NNCoordinate(
                                            keyPoint.getPosition().getX(),
                                            keyPoint.getPosition().getY()
                                    )
                            )
            );
        }
    }


    /**
     * Normalising the data.
     *
     * @param videoId the video id. The primary key inserted upon entry.
     */
    void normalise(long videoId) {
        double[] normalised = setNormaliseCoordinates(22);
        this.appDatabase
                .nnCoordinateDAO()
                .normaliseCoordinates(videoId, normalised[0], normalised[1]);
    }

    /**
     * Links a coordinate to a video.
     *
     * @param frameId      the frame id. The primary key inserted upon entry.
     * @param coordinateId the video id. The primary key inserted upon entry.
     */
    private void linkFrameToCoordinate(long frameId, long coordinateId) {
        this.appDatabase
                .nnFrameCoordinateDAO()
                .insert(
                        new NNFrameCoordinate(frameId, coordinateId)
                );
    }

    /**
     * Links the frame to a video.
     *
     * @param frameId the frame id. The primary key inserted upon entry.
     * @param videoId the video id. The primary key inserted upon entry.
     */
    private void linkFrameIdToVideo(long frameId, long videoId) {
        // Linking to the video
        this.appDatabase
                .nnVideoFrameDAO()
                .insert(
                        new NNVideoFrame(videoId, frameId)
                );
    }
}
