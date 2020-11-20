package com.mygdx.honestmirror.data.persistance.Coordinate;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


/**
 * The interface NNCoordinateDao.
 */
@Dao
public interface NNCoordinateDAO {

    /**
     * Returns the amount of entity's in the coordinate table.
     * Used when estimating the size of the database.
     *
     * @return the count as an int
     */
    @Query("SELECT COUNT(*) FROM coordinate")
    int getCount();

    /**
     * Inserting an entity and returning the insert ID
     *
     * @param nnCoordinate the nn coordinate
     * @return insert ID
     */
    @Insert
    long insert(NNCoordinate nnCoordinate);

    /**
     * Updating an entity by entity
     *
     * @param nnCoordinate the nn coordinate
     */
    @Update
    void update(NNCoordinate nnCoordinate);

    /**
     * Deleting entity via entity
     *
     * @param nnCoordinate the nn coordinate
     */
    @Delete
    void delete(NNCoordinate nnCoordinate);

    /**
     * Get all available coordinates in the database.
     *
     * @return ArrayList of NNCoordinate objects.
     */
    @Query("SELECT * FROM coordinate")
    NNCoordinate[] getAllCoordinates();

    /**
     * Get an entity by primary key
     *
     * @param id of the coordinate entity as int
     * @return NNCoordinate entry, filled if found empty if not.
     */
    @Query("SELECT * FROM coordinate WHERE id == :id LIMIT 1")
    NNCoordinate getById(int id);

    /**
     * Get an entity by primary key
     *
     * @param id of the coordinate entity as long
     * @return NNCoordinate entry, filled if found empty if not.
     */
    @Query("SELECT * FROM coordinate WHERE id == :id LIMIT 1")
    NNCoordinate getById(long id);

    /**
     * Nuke table.
     */
    @Query("DELETE FROM coordinate")
    void nukeTable(); // Naming is about as clear as it can be.


    /**
     * Normalize coordinates.
     * Range coordinates between 0 and 1
     *
     * @param video_id     the video id
     * @param x_multiplier the x multiplier used for calculating x values
     * @param y_multiplier the y multiplier used for calculating y values
     * @return int affected rows
     */
    @Query("UPDATE coordinate SET  " +
            "x = raw_x * :x_multiplier, " +
            "y = raw_y * :y_multiplier  " +
            "WHERE coordinate.id IN (SELECT coordinate.id " +
            "FROM video, video_frame, frame, frame_coordinate, coordinate " +
            "WHERE video_id = :video_id " +
            "AND video.id = video_frame.video_id " +
            "AND video_frame.frame_id = frame.id " +
            "AND frame.id = frame_coordinate.frame_id " +
            "AND frame_coordinate.coordinate_id = coordinate.id)")
    int normaliseCoordinates(long video_id, double x_multiplier, double y_multiplier);
}

