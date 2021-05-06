package com.mygdx.honestmirror.application.domain.analysis;

import android.content.Context;
import android.content.res.AssetManager;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.mygdx.honestmirror.application.common.DebugLog;

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
 * Tests the Detection class.
 */
@RunWith(AndroidJUnit4.class)
public class DetectionTest {
    Data data;
    Context context;
    Detection detect;

    /**
     * Reads the joint data from a json file.
     */
    @Before
    public void before() {
        // initialize the json data
        context = ApplicationProvider.getApplicationContext();
        AssetManager am = context.getAssets();
        InputStream is = null;
        try {
            is = am.open("data/wave.json");
        } catch (IOException e) {
            //DebugLog.log("Unable to load asset norm json");
        }
        Reader r = new InputStreamReader(is);
        JSONLoader loader = new JSONLoader(r);
        data = new JSONData(loader);

        detect = new Detection(data);
    }

    /**
     * Checks if handsfound returns the right result for the data set.
     */
    @Test
    public void handsFoundTest() {
        boolean happened = detect.handsFound(1.0f);
        assertEquals(happened, false);
    }

    /**
     * Checks if handsidle returns the right result for the data set.
     */
    @Test
    public void handsIdle() {
        boolean happened = detect.HandsIdle(1, 0.1);
        assertEquals(happened, false);
    }

    /**
     * Checks if handsabovehead returns the right result for the data set.
     */
    @Test
    public void handsAboveHead() {
        boolean happened = detect.handsAboveHead(1.0f);
        assertEquals(happened, true);
    }

    @After
    public void after() {}
}
