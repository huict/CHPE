package com.mygdx.honestmirror.application.nnanalysis.poseestimation.nn.PoseNet;

public class Position {
    private float x = 0;
    private float y = 0;
    public float rawX = 0;
    public float rawY = 0;

    public float getRawX() {
        return rawX;
    }

    public float getRawY() {
        return rawY;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setX(float x, int horizontalRes){
        this.rawX = (float)x / (float) horizontalRes;
        setX(x);

//        if (this.rawX > 1)
//            DebugLog.log((Float.toString(rawX)));
    }

    public void setY(float y, int verticalRes){
        this.rawY = (float) y / (float) verticalRes;
        setY(y);

//        if (this.rawY > 1)
//            DebugLog.log((Float.toString(rawY)));
    }


}
