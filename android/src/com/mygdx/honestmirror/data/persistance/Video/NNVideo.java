package com.mygdx.honestmirror.Persistance.Video;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


/**
 * The type Nn video.
 */
@Entity(tableName = "video")
public class NNVideo {
     /*
    TODO: Future: Create date field
    TODO: Future: Add Dynamic model type
    */


    /**
     * The Id.
     */
    @PrimaryKey(autoGenerate = true)
    public long id;
    /**
     * The Frames per second.
     */
    public float frames_per_second;
    /**
     * The Frame count.
     */
    public long frame_count;
    /**
     * The Width.
     */
    public int width;
    /**
     * The Height.
     */
    public int height;

    /**
     * Instantiates a new Nn video.
     */
    public NNVideo() {
    }

    /**
     * Instantiates a new Nn video.
     *
     * @param id the id
     */
    @Ignore
    public NNVideo(long id) {
        this.id = id;
    }

    /**
     * Instantiates a new Nn video.
     *
     * @param frames_per_second the frames per second
     * @param frame_count       the frame count
     * @param width             the width
     * @param height            the height
     */
    @Ignore
    public NNVideo(float frames_per_second, long frame_count, int width, int height) {
        this.frames_per_second = frames_per_second;
        this.frame_count = frame_count;
        this.width = width;
        this.height = height;
    }

}
