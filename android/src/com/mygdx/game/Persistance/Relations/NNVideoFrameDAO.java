package com.mygdx.game.Persistance.Relations;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

/**
 * The interface Nn video frame dao.
 */
@Dao
public interface NNVideoFrameDAO {

    /**
     * Insert entity by entity
     *
     * @param nnVideoFrame the entity
     */
    @Insert
    void insert(NNVideoFrame nnVideoFrame);

    /**
     * Update entity by entity
     *
     * @param nnVideoFrame the entity
     */
    @Update
    void update(NNVideoFrame nnVideoFrame);

    /**
     * Delete entity by entity
     *
     * @param nnVideoFrame the entity
     */
    @Delete
    void delete(NNVideoFrame nnVideoFrame);

    /**
     * Nuke table.
     */
    @Query("DELETE FROM video_frame")
    void nukeTable(); // Naming is about as clear as it can be.

}
