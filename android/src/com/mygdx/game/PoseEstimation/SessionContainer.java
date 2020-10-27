package com.mygdx.game.PoseEstimation;

import com.mygdx.game.Persistance.AppDatabase;
import com.mygdx.game.Persistance.Coordinate.NNCoordinate;

import java.util.List;

/**
 * The type Session container.
 */
public class SessionContainer {

    private class FrameContainer {
        private CoordinateContainer coordinateContainer;

        /**
         * Instantiates a new Frame container.
         *
         * @param coordinateContainer the coordinate container
         */
        public FrameContainer(CoordinateContainer coordinateContainer) {
            this.coordinateContainer = coordinateContainer;
        }
    }

    private class CoordinateContainer {
        private List<NNCoordinate> nnCoordinateList;

        /**
         * Instantiates a new Coordinate container.
         *
         * @param nnCoordinateList the nn coordinate list
         */
        public CoordinateContainer(List<NNCoordinate> nnCoordinateList) {
            this.nnCoordinateList = nnCoordinateList;
        }

        /**
         * Insert container.
         *
         * @param appDatabase the app database
         */
        public void insertContainer(AppDatabase appDatabase) {
            for (NNCoordinate nnCoordinate : nnCoordinateList) {
                appDatabase.nnCoordinateDAO().insert(nnCoordinate);
            }
        }
    }
}
