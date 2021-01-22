package com.mygdx.honestmirror.data.persistance.Coordinate;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

//Entity NNCoordinate.
@Entity(tableName = "coordinate")
public class NNCoordinate {

    @PrimaryKey(autoGenerate = true)
    public long id;

    //The actual pixel value found in an image
    //Will always be between the ranges of the video resolution
    public int raw_x;

    //The actual pixel value found in an image
    //Will always be between the ranges of the video resolution
    public int raw_y;

    //Normalised value based upon the raw_x and the range of the video resolution
    //Value will always be between 0 and 1
    public double x;

    //Normalised value based upon the raw_y and the range of the video resolution
    //Value will always be between 0 and 1
    public double y;

    //Instantiates a new NNCoordinate.
    public NNCoordinate() {
    }

    //Instantiates a new NNCoordinate.
    //Useful when inserting new coordinates.
    //raw_x the raw x coordinate
    //raw_y the raw y coordinate

    @Ignore
    public NNCoordinate(int raw_x, int raw_y) {
        this.raw_x = raw_x;
        this.raw_y = raw_y;
    }

    //Instantiates a new NNCoordinate based on it's id.
    //Useful when values need to be gathered based on the ID.
    @Ignore
    public NNCoordinate(int id) {
        this.id = id;
    }

    // Instantiates a new NNCoordinate.
    // Useful for updating the x and y values.
    //Primary use case is when a smoothing occurs in the analysis.
    @Ignore
    public NNCoordinate(int id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }
}
