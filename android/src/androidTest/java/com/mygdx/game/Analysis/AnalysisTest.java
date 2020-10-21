package com.mygdx.game.Analysis;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.mygdx.game.MockData;
import com.mygdx.game.persistance.AppDatabase;
import com.mygdx.game.persistance.Coordinate.NNCoordinate;
import com.mygdx.game.persistance.Video.NNVideoDAO;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;


/**
 * Tests the Analysis class and its methods.
 */
@RunWith(AndroidJUnit4.class)
public class AnalysisTest {

    private AppDatabase appDatabase;
    private String databaseName = "test";
    private int frameCount = 24 * 5;
    private int framesPerSecond = 24;
    private NNVideoDAO nnVideoDAO;
    private long insertId;

    /**
     * connects to the database and inserts the mock data.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        Context context = ApplicationProvider.getApplicationContext();

        // Ensure that the database name is NOT the actual database name
        this.appDatabase = Room.databaseBuilder(context, AppDatabase.class, databaseName)
                .allowMainThreadQueries() // TODO: Multi-threaded agent
                .build();

        MockData mockData = new MockData(this.appDatabase);
        this.nnVideoDAO = this.appDatabase.nnVideoDAO();


        // Initialising with mock data
    }

    /**
     * cleans up the database.
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        this.nnVideoDAO.nukeTable();
    }

    /**
     * Tests the if we're able to get coordinates.
     */
    @Test
    public void get_coordinates() {

        NNCoordinate coordinate = this.nnVideoDAO.get_coordinates(2,0,0);
        assertEquals(361, coordinate.x,0.0);
        assertEquals(62, coordinate.y, 0.0);
    }
}