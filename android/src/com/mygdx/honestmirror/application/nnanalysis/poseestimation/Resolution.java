package com.mygdx.honestmirror.application.nnanalysis.poseestimation;

import android.graphics.Bitmap;

//The type Resolution.
public class Resolution {
    private int modelWidth = 257;
    private int modelHeight = 257;
    private final int screenWidth;
    private final int screenHeight;

    public int getModelWidth() {
        return modelWidth;
    }

    public int getModelHeight() {
        return modelHeight;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }


    //Instantiates a new Resolution.
    public Resolution(Bitmap bitmap) {
        this.screenWidth = bitmap.getWidth();
        this.screenHeight = bitmap.getHeight();
    }

    //Instantiates a new Resolution.
    public Resolution(int width, int height, int modelWidth, int modelHeight) {
        this.screenWidth = width;
        this.screenHeight = height;
        this.modelWidth = modelWidth;
        this.modelHeight = modelHeight;
    }

    //Instantiates a new Resolution.
    Resolution(int width, int height, int modelRes) {
        this.screenWidth = width;
        this.screenHeight = height;
        this.modelWidth = modelRes;
        this.modelHeight = modelRes;
    }

    int getWidthByRatio(float width) throws NumberFormatException {

        if (width > this.screenWidth) {
            throw new NumberFormatException("Width higher than image");
        }

        if (width < 0) {
            throw new NumberFormatException("Width lower than 0");
        }

        return (int) (width * ((float) this.screenWidth / this.modelWidth));
    }

    int getHeightByRatio(float height) {
        if (height > this.screenHeight) {
            throw new NumberFormatException("Height higher than image");
        }

        if (height < 0) {
            throw new NumberFormatException("Height lower than 0");
        }
        return (int) (height * ((float) this.screenHeight / this.modelHeight));
    }
}
