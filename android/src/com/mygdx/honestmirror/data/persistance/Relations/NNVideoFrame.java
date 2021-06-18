package com.mygdx.honestmirror.data.persistance.Relations;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import com.mygdx.honestmirror.data.persistance.Frame.NNFrame;
import com.mygdx.honestmirror.data.persistance.Video.NNVideo;

//The type Nn video frame.
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

    @ColumnInfo(index = true)
    public long video_id;
    @ColumnInfo(index = true)
    public long frame_id;

    //Instantiates a new NNVideoFrame.
    //The only way to instantiate this object is with both ID's
    public NNVideoFrame(long video_id, long frame_id) {
        this.video_id = video_id;
        this.frame_id = frame_id;
    }
}
