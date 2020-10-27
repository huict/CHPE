package com.mygdx.game.PoseEstimation.nn.PoseNet;

import com.mygdx.game.PoseEstimation.nn.PoseNet.Position;
import com.mygdx.game.PoseEstimation.nn.PoseModels.NNModelPosenet;

/**
 * The type Key point.
 */
public class KeyPoint {
    /**
     * The Body part.
     */
    public NNModelPosenet.bodyPart bodyPart;
    /**
     * The Position.
     */
    public Position position = new Position();
    /**
     * The Score.
     */
    public Float score = 0.0f;

    /**
     * Gets body part.
     *
     * @return the body part
     */
    public NNModelPosenet.bodyPart getBodyPart() {
        return bodyPart;
    }

    /**
     * Sets body part.
     *
     * @param bodyPart the body part
     */
    public void setBodyPart(NNModelPosenet.bodyPart bodyPart) {
        this.bodyPart = bodyPart;
    }

    /**
     * Gets position.
     *
     * @return the position
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Sets position.
     *
     * @param position the position
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * Gets score.
     *
     * @return the score
     */
    public Float getScore() {
        return score;
    }

    /**
     * Sets score.
     *
     * @param score the score
     */
    public void setScore(Float score) {
        this.score = score;
    }
}
