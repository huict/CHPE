package com.mygdx.game.PoseEstimation.nn;

import com.mygdx.game.Exceptions.InvalidModelParse;
import com.mygdx.game.PoseEstimation.nn.PoseModels.NNModelCOCO;
import com.mygdx.game.PoseEstimation.nn.PoseModels.NNModelMPI;
import com.mygdx.game.PoseEstimation.nn.PoseModels.NNModelPosenet;
import com.mygdx.game.PoseEstimation.nn.PoseModels.PoseModel;

/**
 * The type Model factory.
 */
public class ModelFactory {

    /**
     * The constant COCO_MODEL.
     */
    public final static int COCO_MODEL = 1;
    /**
     * The constant MPI_MODEL.
     */
    public final static int MPI_MODEL = 2;
    /**
     * The constant POSENET_MODEL.
     */
    public final static int POSENET_MODEL = 3;

    /**
     * Gets model.
     *
     * @param modelId the model id
     * @return the model
     * @throws InvalidModelParse the invalid model parse
     */
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
