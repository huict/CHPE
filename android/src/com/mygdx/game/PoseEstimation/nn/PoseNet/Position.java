package com.mygdx.game.PoseEstimation.nn.PoseNet;

import com.mygdx.game.DebugLog;


public class Position {
    private int x = 0;
    private int y = 0;
    public float rawX = 0;
    public float rawY = 0;

    public float getRawX() {
        return rawX;
    }

    public float getRawY() {
        return rawY;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setX(int x, int horizontalRes){
        this.rawX = (float)x / (float) horizontalRes;
        setX(x);

        if (this.rawX > 1)
            DebugLog.log((Float.toString(rawX)));
    }

    public void setY(int y, int verticalRes){
        this.rawY = (float) y / (float) verticalRes;
        setY(y);

        if (this.rawY > 1)
            DebugLog.log((Float.toString(rawY)));
    }
}
