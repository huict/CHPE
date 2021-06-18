package com.mygdx.honestmirror.data.persistance.Frame;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

//The type Nn frame.
@Entity(tableName = "frame")
public class NNFrame {

    @PrimaryKey(autoGenerate = true)
    public long id;
    public long frame_count;


    //Empty constructor, Instantiates a new NNFrame without data.
    NNFrame() {
    }


    // Instantiates a new Nn frame.
    @Ignore
    public NNFrame(long frame_count) {
        this.frame_count = frame_count;
    }

}
