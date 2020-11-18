package com.mygdx.honestmirror.Persistance.Relations;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


/**
 * The interface Nn frame coordinate dao.
 */
@Dao
public interface NNFrameCoordinateDAO {

    /**
     * Insert entity by entity
     *
     * @param nnFrameCoordinate the entity
     */
    @Insert
    void insert(NNFrameCoordinate nnFrameCoordinate);

    /**
     * Update entity by entity
     *
     * @param nnFrameCoordinate the entity
     */
    @Update
    void update(NNFrameCoordinate nnFrameCoordinate);

    /**
     * Delete entity by entity
     *
     * @param nnFrameCoordinate the entity
     */
    @Delete
    void delete(NNFrameCoordinate nnFrameCoordinate);


    /**
     * Nuke table.
     */
    @Query("DELETE FROM frame_coordinate")
    void nukeTable(); // Naming is about as clear as it can be.

}
