package com.mygdx.honestmirror.data.persistance.Relations;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import com.mygdx.honestmirror.data.persistance.Coordinate.NNCoordinate;
import com.mygdx.honestmirror.data.persistance.Frame.NNFrame;

//The type Nn frame coordinate.
@Entity(
        primaryKeys = {
                "frame_id",
                "coordinate_id"
        },
        foreignKeys = {
                @ForeignKey(
                        entity = NNFrame.class,
                        parentColumns = "id",
                        childColumns = "frame_id"
                ),
                @ForeignKey(
                        entity = NNCoordinate.class,
                        parentColumns = "id",
                        childColumns = "coordinate_id"
                )
        },
        tableName = "frame_coordinate"
)


public class NNFrameCoordinate {

    @ColumnInfo(index = true)
    public long frame_id;
    @ColumnInfo(index = true)
    public long coordinate_id;


    //Instantiates a new NNFrame coordinate.
    //The only way to instantiate this object is with both ID's
    public NNFrameCoordinate(long frame_id, long coordinate_id) {
        this.frame_id = frame_id;
        this.coordinate_id = coordinate_id;
    }
}
