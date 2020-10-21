package com.mygdx.game.PoseEstimation.NN;

import com.mygdx.game.Exceptions.InvalidModelParse;
import com.mygdx.game.PoseEstimation.nn.ModelFactory;
import com.mygdx.game.PoseEstimation.nn.PoseModels.PoseModel;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


/**
 * The ModelFactoryTest.
 * Validates whether the correct NN Model returns.
 */
public class ModelFactoryTest {


    /**
     * Exception parser.
     * Testing if an exception is thrown under the right circumstances
     */
    @Test
    public void ExceptionParser(){
        try {
            ModelFactory.getModel(10);
            fail();
        }catch (InvalidModelParse invalidModelParse){
            // Body left empty on purpose.
        }

    }
}
