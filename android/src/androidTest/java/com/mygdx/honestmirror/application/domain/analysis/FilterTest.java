package com.mygdx.honestmirror.application.domain.analysis;

import android.content.Context;
import android.content.res.AssetManager;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.badlogic.gdx.math.Vector3;
import com.mygdx.honestmirror.application.common.DebugLog;
import com.mygdx.honestmirror.application.nnanalysis.poseestimation.nn.PoseModels.NNModelMPI;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class FilterTest {
    Data data;
    Filter filter;
    Context context;

    /**
     * Read in a JSON file to filter.
     */
    @Before
    public void before() {
        // initialize the json data
        context = ApplicationProvider.getApplicationContext();
        AssetManager am = context.getAssets();
        InputStream is = null;
        try {
            is = am.open("data/tedtalk.json");
        } catch (IOException e) {
            //DebugLog.log("Unable to load asset norm json");
        }
        Reader r = new InputStreamReader(is);
        JSONLoader loader = new JSONLoader(r);
        data = new JSONData(loader);

        filter = new Filter(data);
    }

    /**
     * Test the absolute average method of Filter.
     */
    @Test
    public void absAverageTest() {
        double a = 50;
        double b = 100;
        double result = filter.absAverage(a, b);
        assertEquals(result, 75, 0);
    }

    /**
     * Tests if the resolvezeros method actually resolves all zeros.
     */
    @Test
    public void TestZeros() {
        filter.resolveZeros();
        for(int f = 0; f < data.getFrameCount(); f++) {
            for(NNModelMPI.body_part bp : NNModelMPI.body_part.values()) {
                Vector3 coord = data.getCoord(f, bp);
                assertEquals(coord.isZero(), false);
            }
        }
    }

    /**
     * Test if kernel has the right result.
     * TODO: Fix error, has to do with getCoord not giving the correct frame
     */
    @Test
    public void testKernel() {
        // store the unfiltered vector
        Vector3 before = data.getCoord(data.getFrameCount()-2, NNModelMPI.body_part.l_wrist);

        // simple average filter
        double[] kernel = {1, 1, 1};

        // calculate expected
        Vector3 v1 = data.getCoord(data.getFrameCount()-1, NNModelMPI.body_part.l_wrist);
        Vector3 v2 = data.getCoord(data.getFrameCount()-3, NNModelMPI.body_part.l_wrist);
        Vector3 expected = new Vector3((v1.x + before.x + v2.x) / 3, (v1.y + before.y + v2.y) / 3, 0);

        // apply filter
        filter.kernelFilter(kernel);

        // get the filtered vector
        Vector3 actual = data.getCoord(data.getFrameCount()-2, NNModelMPI.body_part.l_wrist);

        assertEquals(expected, actual);
    }

    /**
     * Cleanup.
     */
    @After
    public void after() {}
}
