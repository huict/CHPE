package com.mygdx.honestmirror.application.nnanalysis.poseestimation.nn.PoseNet;

import android.content.Context;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;
import com.mygdx.honestmirror.application.common.DebugLog;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

//The type Person.
public class Person {
    //Instantiates a new Person.
    public Person() {
    }

    //The Score.
    public Float score = 0.0f;

    //The Key points.
    public List<KeyPoint> keyPoints = new ArrayList<>();

    //Gets key points.
    public List<KeyPoint> getKeyPoints() {
        return keyPoints;
    }

    public JsonObject toJson(){

        JsonObjectBuilder jsonObjectBuilderRoot = Json.createObjectBuilder();
        JsonArrayBuilder jsonObjectBuilderCoords = null;

        StringWriter sw = new StringWriter();

        for (KeyPoint keyPoint : keyPoints) {
            if (keyPoint.bodyPart != null || keyPoint.bodyPart.name() != null){
               jsonObjectBuilderCoords = Json.createArrayBuilder();
               jsonObjectBuilderCoords.add(keyPoint.getPosition().rawX);
               jsonObjectBuilderCoords.add(keyPoint.getPosition().rawY);
               jsonObjectBuilderRoot.add(keyPoint.bodyPart.name(), jsonObjectBuilderCoords);
            }
        }

        return jsonObjectBuilderRoot.build();
    }

    @NotNull
    @Override
    public String toString() {
        return "Person{" +
                "score=" + score +
                ", keyPoints=" + keyPoints.toString() +
                '}';
    }

    public Float[] readHeatmapFile(Context context) throws IOException {
        InputStream is = context.getAssets().open("output_details[0].txt");
        //Float[][][][] mLines = new Float[1][9][9][17];
        Float[] heatmapArray = new Float[1377];
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line;
        float f;

        for(int i = 0; i < 1377; i++) {
            line = reader.readLine();
            f = Float.parseFloat(line);
            heatmapArray[i] = f;
            //DebugLog.log("O: " + f);

//            for(int j = 0; j < 9; j++){
//                for(int k = 0; k < 9; k++){
//                    for(int l = 0; l < 17; l++){
//                        mLines[0][j][k][l] = f;
//                    }
//                }
//            }
        }
        return heatmapArray;
    }

    public Float[] readOffsetFile(Context context) throws IOException{
        InputStream is = context.getAssets().open("output_details[1].txt");

        //Array<Array<Array<FloatArray>>> mLines = new Array<Array<Array<FloatArray>>>();
        Float[] offsetArray = new Float[1377];
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line;
        float f;

        for(int i = 0; i < 1377; i++) {
            line = reader.readLine();
            f = Float.parseFloat(line);
            offsetArray[i] = f;
            //DebugLog.log("O: " + f);

//            for(int j = 0; j < 9; j++){
//                for(int k = 0; k < 9; k++){
//                    for(int l = 0; l < 17; l++){
//                        mLines[0][j][k][l] = f;
//                    }
//                }
//            }
        }
        return offsetArray;
    }
}





