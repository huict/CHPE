package com.mygdx.honestmirror.application.domain.analysis;

import com.badlogic.gdx.math.Vector3;
import com.mygdx.honestmirror.application.nnanalysis.poseestimation.nn.PoseModels.NNModelMPI.body_part;

//This class handles checking the vector data for certain FUTURE: Actions
//and FUTURE: Patterns. For now these are just single functions with descriptive names.
class Detection {
    //Interface to the vector data.
    private final Data data;

    //Constructor, initializes class fields.
    Detection(final Data data) {
        this.data = data;
    }

    //Helper function for getting the absolute values of a Vector3.
    //returns Vector with absolute values.
    private Vector3 abs(Vector3 v) {
        return new Vector3(
            Math.abs(v.x),
            Math.abs(v.y),
            Math.abs(v.z)
        );
    }

    //Checks if the person's hands can not be found for a given amount of time.
    // dt Given amount of time for it to be true.
    //returns if the Action was detected or not.
    boolean handsFound(float dt) {
        boolean inAction = false;
        float action_time = 0;
        for(long i = 0; i < data.getFrameCount(); i++) {
            if(action_time >= dt) {
                return true;
            }
            Vector3 r_handCoord = data.getCoord(i, body_part.r_wrist);
            Vector3 l_handCoord = data.getCoord(i, body_part.l_wrist);
            if(!inAction) {
                if(r_handCoord.isZero() &&
                        l_handCoord.isZero()) {
                    action_time = 0;
                    inAction = true;
                }
            }
            if(inAction) {
                if(!r_handCoord.isZero()  &&
                        !l_handCoord.isZero()) {
                    inAction = false;
                    continue;
                }
                action_time += 1.0f / data.getFps();
            }
        }

        return false;
    }

    //Checks if the person keeps his/her hands still for too long.
    //dt How long the condition has to be true for (in seconds).
    //returns If the Action was detected or not.
    boolean HandsIdle(float dt, double threshold) {
        boolean inAction = false;
        float actionTime = 0;

        Vector3[] deltas = new Vector3[2];
        Vector3[] lastHandPos = {
                data.getCoord(0, body_part.l_wrist),
                data.getCoord(0, body_part.r_wrist)
        };

        // check for delta movement every second
        for(long i = 1; i < data.getFrameCount(); i += data.getFps()) {
            if(actionTime >= dt) {
                return true;
            }
            deltas[0] = abs(data.getCoord(i, body_part.l_wrist).sub(lastHandPos[0]));
            deltas[1] = abs(data.getCoord(i, body_part.r_wrist).sub(lastHandPos[1]));
            boolean movedHands = deltas[0].x > threshold && deltas[1].x > threshold &&
                    deltas[0].y > threshold && deltas[1].y > threshold;

            if(!inAction) {
                if(!movedHands) {
                    actionTime = 0;
                    inAction = true;
                }
            }
            // if we didnt detect any movement last time but hands moved,
            // cancel action timing and move on
            if(inAction) {
                if(movedHands) {
                    inAction = false;
                } else {
                    // add a second
                    actionTime += 1.0f / data.getFps();
                }
            }
        }
        return false;
    }

    //Checks if the person has his hand higher than his head for too long.
    //param dt How long the condition has to be true for (in seconds).
    //returns If the Action was detected or not.
    boolean handsAboveHead(float dt) {
        boolean inAction = false;
        float action_time = 0;
        for(long i = 0; i < data.getFrameCount(); i++) {
            if(action_time >= dt) {
                return true;
            }
            Vector3 headCoord = data.getCoord(i, body_part.l_shoulder);
            Vector3 r_handCoord = data.getCoord(i, body_part.r_wrist);
            Vector3 l_handCoord = data.getCoord(i, body_part.l_wrist);
            if(!inAction) {
                if((!l_handCoord.isZero() && l_handCoord.y < headCoord.y) ||
                        (!r_handCoord.isZero() && r_handCoord.y < headCoord.y)) {
                    action_time = 0;
                    inAction = true;
                }
            }
            if(inAction) {
                if((!l_handCoord.isZero() && l_handCoord.y > headCoord.y) &&
                        (!r_handCoord.isZero() && r_handCoord.y > headCoord.y)) {
                    inAction = false;
                    continue;
                }
                action_time += 1.0f / data.getFps();
            }
        }
        return false;
    }
}