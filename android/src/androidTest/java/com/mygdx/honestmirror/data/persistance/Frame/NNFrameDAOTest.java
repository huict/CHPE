package com.mygdx.honestmirror.data.persistance.Frame;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.mygdx.honestmirror.data.persistance.AppDatabase;
import com.mygdx.honestmirror.data.persistance.PersistenceClient;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;


/**
 * The NNFrameDAO tester.
 */
@RunWith(AndroidJUnit4.class)
public class NNFrameDAOTest {
    private NNFrameDAO nnFrameDAO;
    private AppDatabase appDatabase;

    /**
     * Creates the db instance.
     * Cleared for later usage.
     */
    @Before
    public void createDb() {
        this.appDatabase = PersistenceClient.getInstance(ApplicationProvider.getApplicationContext(), "debugDB").getAppDatabase();
        this.appDatabase.clearAllTables();
        this.nnFrameDAO = appDatabase.nnFrameDAO();
    }

    /**
     * Clear db.
     * Required to allow different tests to use debugDB
     */
    @After
    public void clearDB() {
        this.appDatabase.clearAllTables();
    }

    /**
     * Insert NNFrameDAOTest. Validates if deleting works as expected.
     */
    @Test
    public void InsertAmountCounter() {

        this.appDatabase.clearAllTables();
        // Random integer to validate insert statements
        int randomInt = new Random().nextInt();

        // Creating a new frame instance
        NNFrame nnFrame = new NNFrame();
        nnFrame.frame_count = randomInt;
        NNFrame insertedFrame = nnFrameDAO.getById(nnFrameDAO.insert(nnFrame));
        // Validating if the random frame_count value equals the actual frame count value
        assertEquals(insertedFrame.frame_count, randomInt);
    }

    /**
     * Update NNFrameDAOTest test. Validates if updating works as expected.
     */
    @Test
    public void UpdateAmountCounter() {

        this.appDatabase.clearAllTables();
        // Random integer to validate insert statements
        int randomInt = new Random().nextInt();
        int secondRandomInt = new Random().nextInt();
        // Creating a new frame instance
        NNFrame nnFrame = new NNFrame();
        nnFrame.frame_count = randomInt;


        NNFrame insertedFrame = nnFrameDAO.getById(nnFrameDAO.insert(nnFrame));
        insertedFrame.frame_count = secondRandomInt;
        nnFrameDAO.update(insertedFrame);

        NNFrame updatedFrame = nnFrameDAO.getById(insertedFrame.id);
        // Validating if the random frame_count value equals the actual frame count value
        assertEquals(updatedFrame.frame_count, secondRandomInt);
    }

}


