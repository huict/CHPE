package com.mygdx.game.Analysis;

import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Persistance.AppDatabase;
import com.mygdx.game.Persistance.Coordinate.NNCoordinateDAO;
import com.mygdx.game.Persistance.Video.NNVideo;
import com.mygdx.game.Persistance.Video.NNVideoDAO;
import com.mygdx.game.PoseEstimation.NN.PoseModels.NNModelMPI.body_part;
import com.mygdx.game.Persistance.Coordinate.NNCoordinate;


/**
 * @author Nico van Bentum
 * This class implements the Data interface, it retrieves the vector data
 * from a JSON file generated by a neural network based Python application.
 */
public class DatabaseData implements Data {
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
    public Vector3 getCoord(long frame, body_part bp) {
        NNCoordinate nnCoordinate = this.nnVideoDAO.getCoordinates(frame,bp.ordinal(),
                this.currentSession.id);
        return new Vector3((float) nnCoordinate.x, (float) nnCoordinate.y, 0);
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
    public long getFrameCount() {
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
    public void serialize() {}

    /**
     * Implements Data's interface for setting the x component of a coordinate.
     */
    public void setX(long frame, body_part bp, double x) {
        NNCoordinate current = this.nnVideoDAO.getCoordinates(frame,bp.ordinal(),
                this.currentSession.id);
        current.x = x;
        NNCoordinateDAO dao = this.appDatabase.nnCoordinateDAO();
        dao.update(current);
    }

    /**
     * Implements Data's interface for setting the y component of a coordinate.
     */
    public void setY(long frame, body_part bp, double  y) {
        NNCoordinate current = this.nnVideoDAO.getCoordinates(frame,bp.ordinal(),
                this.currentSession.id);
        current.y = y;
        NNCoordinateDAO dao = this.appDatabase.nnCoordinateDAO();
        dao.update(current);
    }
}