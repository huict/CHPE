package com.mygdx.honestmirror.application.domain.analysis;

import com.mygdx.honestmirror.application.nnanalysis.poseestimation.nn.PoseModels.NNModelMPI;

import java.util.HashMap;


/**
 * @author Nico van Bentum
 * Main class that provides (and performs) the functionality for analyzing a data set
 * of vector coordinates for detecting human body language and actions.
 */
public class Analysis {
    /**
     * A handle to the detection class that handles detecting specific Actions.
     */
    private final Detection detection;

    /**
     * Object that can perform various filter techniques on the data object.
     */
    private final Filter filter;

    /**
     * Constructor. Initializes member fields.
     * @param data Object for interfacing with the data set.
     */
    public Analysis(final Data data) {
        detection = new Detection(data);
        filter = new Filter(data);
    }
    
    /**
     * Lets the Detection object perform it's operations.
     * @return A hash map containing every action and whether it was detected or not.
     * TODO: Implementation
     */
    public HashMap<Action, Boolean> detect() {
        HashMap<Action, Boolean> results = new HashMap<>();

        Action action = new Action("handAboveHead");
        action.setOccurrence(detection.handsAboveHead(1));
        results.put(action, action.occurred());

        action = new Action("handsIdle");
        action.setOccurrence(detection.HandsIdle(10, 0.01f));
        results.put(action, action.occurred());

        action = new Action("handsNotFound");
        action.setOccurrence(detection.handsFound(1.0f));
        results.put(action, action.occurred());

        return results;
    }
    
    /**
     * Filters and processes the data.
     * Most of this will be done in Python first because of faster - and visual - testing.
     */
    public void process() {
        filter.resolveZeros();
        filter.averageOf(NNModelMPI.body_part.waist, NNModelMPI.body_part.l_hip, NNModelMPI.body_part.r_hip);

        // TODO: random filter taken from the Python application for now, pls fix
        for(int i = 0; i < 10; i++) {
            double kernel[] = {3, 6, 9, 50, 9, 6, 3};
            filter.kernelFilter(kernel);
        }
    }
}