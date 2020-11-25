package com.mygdx.honestmirror.data.persistance.Video;

import com.mygdx.honestmirror.data.persistance.Coordinate.NNCoordinate;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

/**
 * The interface Nn video dao.
 */
@Dao
public interface NNVideoDAO {
    /**
     * Insert long.
     *
     * @param nnVideo the nn session
     * @return the long
     */
    @Insert
    long insert(NNVideo nnVideo);

    /**
     * Update.
     *
     * @param nnVideo the nn session
     */
    @Update
    void update(NNVideo nnVideo);

    /**
     * Delete.
     *
     * @param nnVideo the nn session
     */
    @Delete
    void delete(NNVideo nnVideo);

    /**
     * Gets last video.
     *
     * @return the last session in NNVideo
     */
    @Query("SELECT * FROM video ORDER BY id DESC LIMIT 1")
    NNVideo getLastSession();

    /**
     * Gets frames per second.
     *
     * @param id the id
     * @return the frames per second
     */
    @Query("SELECT frames_per_second FROM video WHERE :id")
    float getFramesPerSecond(int id);

    /**
     * Gets frame count.
     *
     * @param id the id
     * @return the frame count
     */
    @Query("SELECT frame_count from video WHERE :id")
    int getFrameCount(int id);

    /**
     * Gets width.
     *
     * @param id the id
     * @return the width
     */
    @Query("SELECT width from video WHERE :id")
    int getWidth(int id);

    /**
     * Gets height.
     *
     * @param id the id
     * @return the height
     */
    @Query("SELECT height from video WHERE :id")
    int getHeight(int id);

    /**
     * Gets coordinates.
     *
     * @param frameCount the frame count
     * @param bodyPart   the body part
     * @param videoId    the video id
     * @return the coordinates
     */
    @Query("SELECT coordinate.id, coordinate.x, coordinate.y, coordinate.raw_x, coordinate.raw_y from coordinate, frame, frame_coordinate, video_frame, video WHERE video.id = :videoId AND video.id = video_frame.video_id AND video_frame.frame_id = frame.id AND frame.frame_count = :frameCount AND frame_coordinate.frame_id = frame.id AND frame_coordinate.coordinate_id = coordinate.id LIMIT 1 OFFSET :bodyPart")
    NNCoordinate getCoordinates(long frameCount, int bodyPart, long videoId);

    /**
     * Nuke table.
     */
    @Query("DELETE FROM video")
    void nukeTable();

    /**
     * Gets max values x.
     *
     * @param videoId the video id
     * @return the max values x
     */
    @Query("SELECT max(coordinate.raw_x) FROM frame, coordinate, frame_coordinate, video, video_frame WHERE coordinate.id == frame_coordinate.coordinate_id and frame_coordinate.frame_id == frame.id and video_frame.frame_id == frame.id and video_frame.video_id = :videoId")
    long getMaxValuesX(long videoId);

    /**
     * Gets max values y.
     *
     * @param videoId the video id
     * @return the max values y
     */
    @Query("SELECT max(coordinate.raw_y) FROM frame, coordinate, frame_coordinate, video, video_frame WHERE coordinate.id == frame_coordinate.coordinate_id and frame_coordinate.frame_id == frame.id and video_frame.frame_id == frame.id and video_frame.video_id = :videoId")
    long getMaxValuesY(long videoId);

}
