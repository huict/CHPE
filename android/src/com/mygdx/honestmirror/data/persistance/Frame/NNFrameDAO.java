package com.mygdx.honestmirror.Persistance.Frame;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

/**
 * The interface Nn frame dao.
 */
@Dao
public interface NNFrameDAO {

    /**
     * Insert entity and receive the insert ID
     *
     * @param nnFrame the entity
     * @return the insert ID
     */
    @Insert
    long insert(NNFrame nnFrame);

    /**
     * Delete entity by entity
     *
     * @param nnFrame the entity
     */
    @Delete
    void delete(NNFrame nnFrame);

    /**
     * Update entity by entity
     *
     * @param nnFrame the entity
     */
    @Update
    void update(NNFrame nnFrame);

    /**
     * Nuke table.
     */
    @Query("DELETE FROM frame")
    void nukeTable(); // Naming is about as clear as it can be.

    /**
     * Gets a frame entity by id
     *
     * @param id the id
     * @return the by id
     */
    @Query("SELECT * FROM frame where id=:id")
    NNFrame getById(long id);

}
