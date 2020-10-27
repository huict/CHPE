package com.mygdx.game.Analysis;

import com.mygdx.game.DebugLog;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.PoseEstimation.nn.PoseModels.NNModelMPI;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author Nico van Bentum
 * This class can perform various filtering actions on a Data class.
 */
class Filter {
    /**
     * Data you want to filter.
     */
    private final Data data;

    /**
     * Constructor, sets the data.
     * @param pData external data object.
     */
    Filter(final Data pData) {
        data = pData;
    }

    /**
     * Looks for zeros in the entire data set and changes them to previous entry.
     */
    void resolveZeros() {
        for(NNModelMPI.body_part bp : NNModelMPI.body_part.values()) {
            for(int f = 1; f < data.getFrameCount(); f++) {
                Vector3 previous = data.getCoord(f-1, bp);
                Vector3 current = data.getCoord(f, bp);
                if(!previous.isZero() && current.isZero()) {
                    data.setX(f, bp, previous.x);
                    data.setY(f, bp, previous.y);
                }
            }
        }
    }

    /**
     * Calculates the absolute average between two numbers.
     * @param a first number
     * @param b second number
     * @return resulting absolute average.
     */
    double absAverage(double a, double b) {
        return a + Math.abs((a - b) / 2);
    }

    /**
     * Updates every coordinate for a given body part with the average of two other body parts.
     * Mainly used for changing the waist coordinates to the average of both hips.
     * @param toUpdate which body part you want to give the average to
     * @param left first body part
     * @param right second body part
     */
    void averageOf(NNModelMPI.body_part toUpdate, NNModelMPI.body_part left, NNModelMPI.body_part right) {
        for(int f = 0; f < data.getFrameCount(); f++) {
            double x = absAverage(data.getCoord(f, left).x, data.getCoord(f, right).x);
            double y = absAverage(data.getCoord(f, left).x, data.getCoord(f, right).x);
            data.setX(f, toUpdate, x);
            data.setY(f, toUpdate, y);
        }
    }

    /**
     * Performs a linear performed weighted average kernel filtering over the data set.
     * @param kernel array of doubles to use as kernel
     */
    void kernelFilter(double kernel[]) {
        int offset = kernel.length / 2;
        int sum = 0;
        for(double weight : kernel) {
            sum += weight;
        }

        for(NNModelMPI.body_part bp : NNModelMPI.body_part.values()) {
            // create a vector for every coordinate of the body part
            ArrayList<Vector2> coords = new ArrayList<Vector2>();

            for(int f = offset; f < data.getFrameCount()-offset; f++) {
                Vector2 acc = new Vector2(0, 0);
                for(int i = 0; i < kernel.length; i++) {
                    acc.x += kernel[i] * data.getCoord(f + (i - offset), bp).x;
                    acc.y += kernel[i] * data.getCoord(f + (i - offset), bp).y;
                }
                // add the smoothed vector to the ArrayList of coordinates
                coords.add(new Vector2(acc.x / sum, acc.y / sum));
            }
            // write the entire vector of coordinates for this specific  body part to
            // the data object
            for(int f = 0; f < coords.size(); f++) {
                data.setX(f + offset, bp, coords.get(f).x);
                data.setY(f + offset, bp, coords.get(f).y);
            }
        }
    }

    
}
