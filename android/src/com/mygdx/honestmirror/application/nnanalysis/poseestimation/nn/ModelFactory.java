package com.mygdx.honestmirror.application.nnanalysis.poseestimation.nn;

import com.mygdx.honestmirror.application.common.exceptions.InvalidModelParse;
import com.mygdx.honestmirror.application.nnanalysis.poseestimation.nn.PoseModels.NNModelCOCO;
import com.mygdx.honestmirror.application.nnanalysis.poseestimation.nn.PoseModels.NNModelMPI;
import com.mygdx.honestmirror.application.nnanalysis.poseestimation.nn.PoseModels.NNModelPosenet;
import com.mygdx.honestmirror.application.nnanalysis.poseestimation.nn.PoseModels.PoseModel;

//The type Model factory.
public class ModelFactory {

    //The constant COCO_MODEL.
    public final static int COCO_MODEL = 1;
    //The constant MPI_MODEL.
    public final static int MPI_MODEL = 2;
    //The constant POSENET_MODEL.

    public final static int POSENET_MODEL = 3;

    //Gets model
    public static PoseModel getModel(int modelId) throws InvalidModelParse {
        PoseModel model;
        switch (modelId) {
            case 1:
                model = (PoseModel) new NNModelCOCO();
                break;
            case 2:
                model = (PoseModel) new NNModelMPI();
                break;
            case 3:
                model = (PoseModel) new NNModelPosenet();
                break;
            default:
                throw new InvalidModelParse(Integer.toString(modelId), new Throwable());
        }
        return model;
    }
}
