package com.mygdx.honestmirror.data.persistance.Relations;

import com.mygdx.honestmirror.data.persistance.Frame.NNFrame;
import com.mygdx.honestmirror.data.persistance.Video.NNVideo;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

/**
 * The type Nn video frame.
 */
@Entity(
        primaryKeys = {
                "video_id",
                "frame_id"
        },
        foreignKeys = {
                @ForeignKey(
                        entity = NNVideo.class,
                        parentColumns = "id",
                        childColumns = "video_id"
                ),
                @ForeignKey(
                        entity = NNFrame.class,
                        parentColumns = "id",
                        childColumns = "frame_id"
                )
        },
        tableName = "video_frame"
)
public class NNVideoFrame {

    /**
     * The Video id.
     * The primary key of the video record.
     */
    @ColumnInfo(index = true)
    public long video_id;
    /**
     * The Frame id.
     * The primary key of the video record.
     */
    @ColumnInfo(index = true)
    public long frame_id;

    /**
     * Instantiates a new NNVideoFrame.
     * The only way to instantiate this object is with both ID's
     *
     * @param video_id the video id
     * @param frame_id the frame id
     */
    public NNVideoFrame(long video_id, long frame_id) {
        this.video_id = video_id;
        this.frame_id = frame_id;
    }
}
