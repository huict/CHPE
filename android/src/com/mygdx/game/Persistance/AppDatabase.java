package com.mygdx.game.Persistance;

import com.mygdx.game.Persistance.Coordinate.NNCoordinate;
import com.mygdx.game.Persistance.Coordinate.NNCoordinateDAO;
import com.mygdx.game.Persistance.Frame.NNFrame;
import com.mygdx.game.Persistance.Frame.NNFrameDAO;
import com.mygdx.game.Persistance.Relations.NNFrameCoordinate;
import com.mygdx.game.Persistance.Relations.NNFrameCoordinateDAO;
import com.mygdx.game.Persistance.Relations.NNVideoFrame;
import com.mygdx.game.Persistance.Relations.NNVideoFrameDAO;
import com.mygdx.game.Persistance.Video.NNVideo;
import com.mygdx.game.Persistance.Video.NNVideoDAO;

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

