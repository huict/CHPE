package com.mygdx.honestmirror.application.nnanalysis.poseestimation;

import com.mygdx.honestmirror.data.persistance.AppDatabase;
import com.mygdx.honestmirror.data.persistance.Coordinate.NNCoordinate;

import java.util.List;

//The type Session container.
public class SessionContainer {

    private class FrameContainer {
        private final CoordinateContainer coordinateContainer;

        //Instantiates a new Frame container.
        public FrameContainer(CoordinateContainer coordinateContainer) {
            this.coordinateContainer = coordinateContainer;
        }
    }

    private class CoordinateContainer {
        private final List<NNCoordinate> nnCoordinateList;

        //Instantiates a new Coordinate container.
        public CoordinateContainer(List<NNCoordinate> nnCoordinateList) {
            this.nnCoordinateList = nnCoordinateList;
        }

        public void insertContainer(AppDatabase appDatabase) {
            for (NNCoordinate nnCoordinate : nnCoordinateList) {
                appDatabase.nnCoordinateDAO().insert(nnCoordinate);
            }
        }
    }
}
