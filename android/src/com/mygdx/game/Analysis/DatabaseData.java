package com.mygdx.game.Analysis;

import com.mygdx.game.persistance.*;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.persistance.Video.NNVideo;
import com.mygdx.game.persistance.Video.NNVideoDAO;
import com.mygdx.game.PoseEstimation.NN.MPI.body_part;
import com.mygdx.game.persistance.Coordinate.NNCoordinate;

/**
 * This class implements the Data interface, it retrieves the vector data
 * from a JSON file generated by a neural network based Python application.
 */
public class DatabaseData implements Data {

    /**
     * Constructor that inits member fields thus loading the data from disk.
     * @param appDatabase File path to the JSON file on disk.
     */
    public DatabaseData(AppDatabase appDatabase) {
        this.appDatabase=appDatabase;
        this.nnVideoDAO = this.appDatabase.nnVideoDAO();
        this.currentSession = this.nnVideoDAO.getLastSession();

    }

    /**
     * Implements Data's interface function for getting a single coordinate using Java's JSON library.
     */
    public Vector3 getCoord(int frame, body_part bp) {
        NNCoordinate nnCoordinate = this.nnVideoDAO.get_coordinates(frame,bp.ordinal(),
                this.currentSession.id);

        if(bp.ordinal() == 4) {
        }
        return new Vector3((float)0.0f, (float)  0.0f, 0);
    }

    /**
     * Implements Data's interface function for retrieving the number of body parts
     * using a hardcoded enum.
     */
    public int getBodyPartCount() {
        return body_part.values().length;
    }

    /**
     * Implements Data's interface function for getting the frame count using the read JSON data.
     */
    public int getFrameCount() {
        return this.currentSession.frame_count;
    }

    /**
     * Implements Data's interface function for getting the number of frames per second used.
     * hardcoded for now.
     */
    public float getFps() {
        return this.currentSession.frames_per_second;
    }


    /**
     * Implements Data's interface for writing the data back to the data structure.
     * Does nothing for now.
     */
    public void serialize() {
        return;
    }

    /**
     * Connection to the Android room database.
     */
    private AppDatabase appDatabase;

    /**
     * Data access object for getting data out of the database.
     */
    private NNVideoDAO nnVideoDAO;

    /**
     * Identifier that specifies which session's data we want.
     */
    private NNVideo currentSession;

}