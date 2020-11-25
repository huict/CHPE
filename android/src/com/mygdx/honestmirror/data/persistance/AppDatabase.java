package com.mygdx.honestmirror.data.persistance;

import com.mygdx.honestmirror.data.persistance.Coordinate.NNCoordinate;
import com.mygdx.honestmirror.data.persistance.Coordinate.NNCoordinateDAO;
import com.mygdx.honestmirror.data.persistance.Frame.NNFrame;
import com.mygdx.honestmirror.data.persistance.Frame.NNFrameDAO;
import com.mygdx.honestmirror.data.persistance.Relations.NNFrameCoordinate;
import com.mygdx.honestmirror.data.persistance.Relations.NNFrameCoordinateDAO;
import com.mygdx.honestmirror.data.persistance.Relations.NNVideoFrame;
import com.mygdx.honestmirror.data.persistance.Relations.NNVideoFrameDAO;
import com.mygdx.honestmirror.data.persistance.Video.NNVideo;
import com.mygdx.honestmirror.data.persistance.Video.NNVideoDAO;

import androidx.room.Database;
import androidx.room.RoomDatabase;


/**
 * The type App database.
 */
@Database(
        entities = {
                NNFrame.class,
                NNVideo.class,
                NNCoordinate.class,

                // Many-to-many
                NNVideoFrame.class,
                NNFrameCoordinate.class,
        },

        version = 2,
        exportSchema = false)

public abstract class AppDatabase extends RoomDatabase {

    /**
     * Nn frame dao nn frame dao.
     *
     * @return the nn frame dao
     */
    public abstract NNFrameDAO nnFrameDAO();

    /**
     * Nn video dao nn video dao.
     *
     * @return the nn video dao
     */
    public abstract NNVideoDAO nnVideoDAO();

    /**
     * Nn coordinate dao nn coordinate dao.
     *
     * @return the nn coordinate dao
     */
    public abstract NNCoordinateDAO nnCoordinateDAO();

    /**
     * Nn session frame dao nn video frame dao.
     *
     * @return the nn video frame dao
     */
    public abstract NNVideoFrameDAO nnVideoFrameDAO();

    /**
     * Nn frame coordinate dao nn frame coordinate dao.
     *
     * @return the nn frame coordinate dao
     */
    public abstract NNFrameCoordinateDAO nnFrameCoordinateDAO();
}

