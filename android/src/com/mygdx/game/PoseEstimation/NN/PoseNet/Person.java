package com.mygdx.game.PoseEstimation.nn.PoseNet;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Person.
 */
public class Person {
    /**
     * Instantiates a new Person.
     */
    public Person() {
    }


    /**
     * The Score.
     */
    public Float score = 0.0f;

    /**
     * The Key points.
     */
    public List<KeyPoint> keyPoints = new ArrayList<>();

    /**
     * Gets key points.
     *
     * @return the key points
     */
    public List<KeyPoint> getKeyPoints() {
        return keyPoints;
    }
}





