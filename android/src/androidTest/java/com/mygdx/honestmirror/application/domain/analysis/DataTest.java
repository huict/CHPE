package com.mygdx.honestmirror.application.domain.analysis;

import android.content.Context;
import android.content.res.AssetManager;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.badlogic.gdx.math.Vector3;
import com.mygdx.honestmirror.application.common.DebugLog;
import com.mygdx.honestmirror.application.nnanalysis.poseestimation.nn.PoseModels.NNModelMPI;
import com.mygdx.honestmirror.data.MockData;
import com.mygdx.honestmirror.data.persistance.AppDatabase;
import com.mygdx.honestmirror.data.persistance.PersistenceClient;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import static org.junit.Assert.assertEquals;

/**
 * Tests the Data interface for performing analysis and filtering.
 */
@RunWith(AndroidJUnit4.class)
public class DataTest {
    Data data;
    Data dbData;
    Data jsonData;
    Context context;
    AppDatabase appDatabase;
    JSONLoader loader;

    /**
     * Sets up the database and the right data.
     */
    @Before
    public void before() {
        // initialize the json data
        context = ApplicationProvider.getApplicationContext();
        AssetManager am = context.getAssets();
        InputStream is = null;
        try {
            is = am.open("data/norm.json");
        } catch (IOException e) {
            //DebugLog.log("Unable to load asset norm json");
        }
        Reader r = new InputStreamReader(is);
        JSONLoader loader = new JSONLoader(r);
        jsonData = new JSONData(loader);

        // initialize the database
        this.appDatabase = PersistenceClient.getInstance(ApplicationProvider.getApplicationContext(), "debugDB").getAppDatabase();
        this.appDatabase.clearAllTables();
        MockData mockData = new MockData(this.appDatabase, loader.getArray());
        mockData.executeInserts();
        dbData = new DatabaseData(this.appDatabase);
    }

    /**
     * Runs the interface from a JSONData object.
     */
    @Test
    public void JSONTest() {
        data = jsonData;
        runAll();
    }

    /**
     * Runs the interface from a DatabaseData object.
     */
    @Test
    public void DBTest() {
        data = dbData;
        runAll();
    }

    /**
     * Tests all the interface functions
     */
    public void runAll() {
        setTest();
        getCoordTest();
        getBodyPartCountTest();
        getFrameCountTest();
        getFramerateTest();
    }

    public void getCoordTest() {
        Vector3 first_coord = data.getCoord(0, NNModelMPI.body_part.head);
        assertEquals(first_coord.x, 5.0f,0.0f);
        assertEquals(first_coord.y, 10.0f, 0.0f);
        assertEquals(first_coord.z, 0.0f, 0.0f);
    }


    public void setTest() {
        Vector3 to_set = new Vector3(5,10,0);
        data.setX(0, NNModelMPI.body_part.head, to_set.x);
        data.setY(0, NNModelMPI.body_part.head, to_set.y);
        Vector3 coord = data.getCoord(0, NNModelMPI.body_part.head);
        assertEquals(to_set, coord);
    }

    public void getBodyPartCountTest() {
        assertEquals(data.getBodyPartCount(), 15);
    }

    public void getFrameCountTest() {
        assertEquals(data.getFrameCount(), 99);
    }

    public void getFramerateTest() {
        assertEquals(data.getFps(), 24, 0);
    }

    @After
    public void after() {
        this.appDatabase.close();
    }

}
