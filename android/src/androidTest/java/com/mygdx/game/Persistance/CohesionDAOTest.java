package com.mygdx.game.Persistance;

import android.content.Context;
import android.content.res.AssetManager;

import com.mygdx.game.Analysis.JSONLoader;
import com.mygdx.game.DebugLog;
import com.mygdx.game.MockData;
import com.mygdx.game.Persistance.Coordinate.NNCoordinate;
import com.mygdx.game.Persistance.Video.NNVideo;
import com.mygdx.game.Persistance.Video.NNVideoDAO;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.junit.Assert.assertEquals;


/**
 * The type Cohesion dao test.
 */
@RunWith(AndroidJUnit4.class)
public class CohesionDAOTest {

    /**
     * The Collector enables multiple asserts
     */
    @Rule
    public ErrorCollector collector = new ErrorCollector();
    private AppDatabase appDatabase;
    private Context context = ApplicationProvider.getApplicationContext();

    /**
     * Sets up.
     */
    @Before
    public void setUp() {
        // Ensure that the database name is NOT the actual database name
        this.appDatabase = Room.databaseBuilder(context, AppDatabase.class, "test")
                .allowMainThreadQueries() // TODO: Multi-threaded agent
                .build();
        this.appDatabase.clearAllTables();

        // Importing DEMO data
        importDB();
    }

    // Imports the example database
    private void importDB() {
        // Initializing with mock data
        AssetManager am = this.context.getAssets();
        InputStream is = null;
        try {
            is = am.open("data/wave.json");
        } catch (IOException e) {
            DebugLog.log("Unable to load asset norm json");
        }
        Reader r = new InputStreamReader(is);
        JSONLoader loader = new JSONLoader(r);

        MockData mockData1 = new MockData(this.appDatabase, loader.getArray());
        mockData1.executeInserts();

    }

    /**
     * Tear down. Done when the unit tests of this specific class finished.
     */

    @After
    public void tearDown()  {
        this.appDatabase.clearAllTables();
    }

    /**
     * Validating of the getCoordinates function call works
     * Function used to test across different tables and columns.
     */
    @Test
    public void getCoordinates() {

        NNVideo nnVideo = this.appDatabase.nnVideoDAO().getLastSession();
        NNCoordinate coordinate = this.appDatabase.nnVideoDAO().getCoordinates(0, 0, nnVideo.id);

        // Normalized values
        collector.checkThat(0.6083333333333334, CoreMatchers.is(coordinate.x));
        collector.checkThat(0.34765625, CoreMatchers.is(coordinate.y));
    }
}