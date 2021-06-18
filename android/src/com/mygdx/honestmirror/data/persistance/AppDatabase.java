package com.mygdx.honestmirror.data.persistance;

import androidx.room.Database;
import androidx.room.RoomDatabase;

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


//The type App database.
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

    public abstract NNFrameDAO nnFrameDAO();

    public abstract NNVideoDAO nnVideoDAO();

    public abstract NNCoordinateDAO nnCoordinateDAO();

    public abstract NNVideoFrameDAO nnVideoFrameDAO();

    public abstract NNFrameCoordinateDAO nnFrameCoordinateDAO();
}

