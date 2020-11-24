package com.mygdx.honestmirror.data.persistance.Frame;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * The type Nn frame.
 */
@Entity(tableName = "frame")
public class NNFrame {

    /**
     * The Id.
     */
    @PrimaryKey(autoGenerate = true)
    public long id;
    /**
     * The Frame count.
     */
    public long frame_count;


    /**
     * Empty constructor, Instantiates a new NNFrame without data.
     */
    NNFrame() {
    }


    /**
     * Instantiates a new Nn frame.
     *
     * @param frame_count the frame count
     */
    @Ignore
    public NNFrame(long frame_count) {
        this.frame_count = frame_count;
    }

}
