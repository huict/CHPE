package com.mygdx.honestmirror.application.nnanalysis.poseestimation.nn;

import com.mygdx.honestmirror.application.common.exceptions.InvalidModelParse;

import org.junit.Test;

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
