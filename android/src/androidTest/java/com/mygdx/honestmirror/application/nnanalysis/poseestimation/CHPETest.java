package com.mygdx.honestmirror.application.nnanalysis.poseestimation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.mygdx.honestmirror.application.nnanalysis.poseestimation.nn.PoseModels.*;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import androidx.test.platform.app.InstrumentationRegistry;

import static org.junit.Assert.assertEquals;


/**
 * Testing the workings of the CHPE based on images
 */
public class CHPETest {
    /**
     * The Collector is used to enable multiple asserts
     */
    @Rule
    public ErrorCollector collector = new ErrorCollector();

    private Context context = InstrumentationRegistry.getInstrumentation().getContext();
    private Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
    private String examplePhoto = "example-human-pose.jpg";
    private Bitmap bitmap;

    /**
     * Sets up.
     *
     * @throws Exception the exception
     */
    @Before
    public void setUp() throws Exception {
        // Importing the Example image
        this.bitmap = BitmapFactory.decodeStream(this.context.getAssets().open(examplePhoto));
    }


    /**
     * Validate settings.
     */
    @Test
    public void validateSettings() {
        // Using multiple asserts are not good practice
        // because if the first one fail and the remaining asserts will not
        collector.checkThat(this.bitmap.getByteCount(), CoreMatchers.is(4894720));
        collector.checkThat(this.bitmap.getHeight(), CoreMatchers.is(1280));
        collector.checkThat(this.bitmap.getWidth(), CoreMatchers.is(956));
    }


    /**
     * Tear down.
     * Currently serves no purpose, because all manipulated data is accessed and modified locally.
     *
     * @throws Exception the exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Parse constructor, checking if the Factory is working as expected.
     */
    @Test
    public void ParseConstructor() {
        CHPE chpePoseNet = new CHPE(this.context, new Resolution(this.bitmap), 3);
        CHPE chpeMPI = new CHPE(this.context, new Resolution(this.bitmap), 2);
        CHPE chpeCOCO = new CHPE(this.context, new Resolution(this.bitmap), 1);


        collector.checkThat(chpeMPI.getPoseModel().getModel(),
                CoreMatchers.is("pose_iter_160000.caffemodel")
        );

        collector.checkThat(chpePoseNet.getPoseModel().getModel(),
                CoreMatchers.is("posenet_model.tflite")
        );

        collector.checkThat(chpeCOCO.getPoseModel().getModel(),
                CoreMatchers.is("pose/coco/pose_iter_440000.caffemodel")
        );
    }

    /**
     * A static constructor. Checking if the static constructor is working as expected.
     */
    @Test
    public void StaticConstructor() {

        CHPE chpePoseNet = new CHPE(this.context, new Resolution(this.bitmap), new NNModelPosenet());
        CHPE chpeMPI = new CHPE(this.context, new Resolution(this.bitmap), new NNModelMPI());
        CHPE chpeCOCO = new CHPE(this.context, new Resolution(this.bitmap), new NNModelCOCO());


        collector.checkThat(chpeMPI.getPoseModel().getModel(),
                CoreMatchers.is("pose_iter_160000.caffemodel")
        );

        collector.checkThat(chpePoseNet.getPoseModel().getModel(),
                CoreMatchers.is("posenet_model.tflite")
        );

        collector.checkThat(chpeCOCO.getPoseModel().getModel(),
                CoreMatchers.is("pose/coco/pose_iter_440000.caffemodel")
        );
    }


    /**
     * Process frame a frame with the GPU as NNInterpreter
     */
//    @Test
//    public void ProcessFrameGPU() {
//        CHPE chpe = new CHPE(this.targetContext, new Resolution(this.bitmap), new NNModelPosenet());
//        Person p = chpe.ProcessFrame(this.bitmap, NNInterpreter.GPU);
//        assertEquals(new NNModelPosenet().points, p.getKeyPoints().size());
//    }

    /**
     * Process frame a frame with the CPU as NNInterpreter
     */
//    @Test
//    public void ProcessFrameCPU() {
//        CHPE chpe = new CHPE(this.context, new Resolution(this.bitmap), new NNModelPosenet());
//        Person p = chpe.ProcessFrame(this.bitmap, NNInterpreter.CPU);
//        assertEquals(new NNModelPosenet().points, p.getKeyPoints().size());
//    }

    /**
     * Process frame a frame with the neural network API as NNInterpreter
     */
//    @Test
//    public void ProcessFrameNNAPI() {
//        CHPE chpe = new CHPE(this.context, new Resolution(this.bitmap), new NNModelPosenet());
//        Person p = chpe.ProcessFrame(this.bitmap, NNInterpreter.NNAPI);
//        assertEquals(new NNModelPosenet().points, p.getKeyPoints().size());
//    }
}