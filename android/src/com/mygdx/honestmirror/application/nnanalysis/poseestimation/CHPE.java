package com.mygdx.honestmirror.application.nnanalysis.poseestimation;


import android.content.Context;
import android.util.Log;


import com.mygdx.honestmirror.application.common.exceptions.InvalidModelParse;
import com.mygdx.honestmirror.application.nnanalysis.poseestimation.nn.ModelFactory;
import com.mygdx.honestmirror.application.nnanalysis.poseestimation.nn.NNInterpreter;
import com.mygdx.honestmirror.application.nnanalysis.poseestimation.nn.PoseModels.PoseModel;
import com.mygdx.honestmirror.application.nnanalysis.poseestimation.nn.PoseNet.PoseNetHandler;


/**
 * The type Chpe.
 */
@SuppressWarnings("UnnecessaryLocalVariable")
public class CHPE {
    private final Resolution resolution;
    private final Context context;
    private PoseModel poseModel;
    private static final boolean BILINEAR_INTERPOLATION = true;

    /**
     * Instantiates a new CHPE.
     *
     * @param context    The context
     * @param resolution The resolution used for scaling
     * @param model      the model
     */
    public CHPE(Context context, Resolution resolution, PoseModel model) {
        this.context = context;
        this.resolution = resolution;
        this.poseModel = model;
    }

    /**
     * Instantiates a new Chpe.
     *
     * @param context    the context
     * @param resolution the resolution
     * @param model      the model
     */
    public CHPE(Context context, Resolution resolution, final int model) {
        this.context = context;
        this.resolution = resolution;
        parseModel(model);
    }

    private void parseModel(final int model) {
        try {
            this.poseModel = ModelFactory.getModel(model);
        }catch (InvalidModelParse invalidModelParse){
            Log.e(CHPE.class.getSimpleName(), invalidModelParse.getMessage());
        }
    }


    /**
     * Get pose model pose model.
     *
     * @return the pose model
     */
    PoseModel getPoseModel(){
        return this.poseModel;
    }

    /**
     * Process frame person based on the
     *
     * @param image         The supplied bitmap image
     * @param nnInterpreter The nnInterpreter type (i.e. CPU/GPU/NNAPI)
     * @return Instance of a Person found on the image
     */
//    Person ProcessFrame(Bitmap image, NNInterpreter nnInterpreter) {
//        long startTime = System.nanoTime();
//        PoseNetHandler posenetHandler = new PoseNetHandler(this.context,
//                this.poseModel.getModel(), // Instance of the model used
//                nnInterpreter, // Device on which the execution will take place
//                this.resolution); // Instance of resolution used for scaling
//
//        Person person = posenetHandler.estimateSinglePose(image);
//        long endTime = System.nanoTime();
//        DebugLog.log("Estimate Single pose took :" + ((endTime - startTime) / 1000000) + "ms");
//        return person; //
//    }

    /**
     * Over loader, uses GPU as default device
     *
     * //@param image The supplied bitmap image
     * @return Instance of a Person found on the image
     */
//    Person ProcessFrame(Bitmap image) {
//        return ProcessFrame(image, NNInterpreter.GPU);
//    }

    PoseNetHandler givePoseNetHandler(NNInterpreter nnInterpreter) {
        PoseNetHandler posenetHandler = new PoseNetHandler(this.context,
                this.poseModel.getModel(), // Instance of the model used
                nnInterpreter, // Device on which the execution will take place
                this.resolution); // Instance of resolution used for scaling
        return posenetHandler;
    }
}