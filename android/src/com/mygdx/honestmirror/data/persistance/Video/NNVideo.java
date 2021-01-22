package com.mygdx.honestmirror.data.persistance.Video;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


//The type Nn video.
@Entity(tableName = "video")
public class NNVideo {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public float frames_per_second;
    public long frame_count;
    public int width;
    public int height;

    //Instantiates a new Nn video.
    public NNVideo() {
    }

    //Instantiates a new Nn video.
    @Ignore
    public NNVideo(long id) {
        this.id = id;
    }

    //Instantiates a new Nn video.
    @Ignore
    public NNVideo(float frames_per_second, long frame_count, int width, int height) {
        this.frames_per_second = frames_per_second;
        this.frame_count = frame_count;
        this.width = width;
        this.height = height;
    }

}
