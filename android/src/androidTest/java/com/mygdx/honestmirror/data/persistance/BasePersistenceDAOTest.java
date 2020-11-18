package com.mygdx.honestmirror.Persistance;



import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.junit.Assert.assertTrue;


/**
 * The type Base persistence dao test.
 */
@RunWith(AndroidJUnit4.class)
public class BasePersistenceDAOTest {
    private AppDatabase appDatabase;


    /**
     * Creates the db instance.
     * Cleared for later usage.
     */
    @Before
    public void createDb() {
        this.appDatabase = PersistenceClient.getInstance(ApplicationProvider.getApplicationContext(), "debugDB").getAppDatabase();
        this.appDatabase.clearAllTables();
    }

    /**
     * Clear db.
     */
    @After
    // Ensures that other tasks are able to access a clear DB
    public void clearDb() {
        this.appDatabase.clearAllTables();
    }

    /**
     * Example test.
     */
    @Test
    public void exampleTest() {
        assertTrue(true);
    }

}
