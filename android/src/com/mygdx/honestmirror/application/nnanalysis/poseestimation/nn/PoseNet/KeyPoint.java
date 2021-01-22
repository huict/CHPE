package com.mygdx.honestmirror.application.nnanalysis.poseestimation.nn.PoseNet;

import com.mygdx.honestmirror.application.nnanalysis.poseestimation.nn.PoseModels.NNModelPosenet;

//The type Key point.
public class KeyPoint {
    //The Body part.
    public NNModelPosenet.bodyPart bodyPart;
    //The Position.
    public Position position = new Position();
    //The Score.
    public Float score = 0.0f;

    //Gets body part.
    public NNModelPosenet.bodyPart getBodyPart() {
        return bodyPart;
    }

    //Sets body part.
    public void setBodyPart(NNModelPosenet.bodyPart bodyPart) {
        this.bodyPart = bodyPart;
    }

    //Gets position.
    public Position getPosition() {
        return position;
    }

    //Sets position.
    public void setPosition(Position position) {
        this.position = position;
    }

    //Gets score.
    public Float getScore() {
        return score;
    }

    //Sets score.
    public void setScore(Float score) {
        this.score = score;
    }
}
