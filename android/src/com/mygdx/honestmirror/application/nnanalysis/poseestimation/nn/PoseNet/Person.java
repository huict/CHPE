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

    public Float score = 0.0f;
    public List<KeyPoint> keyPoints = new ArrayList<>();

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
        Float[] heatmapArray = new Float[1377];
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line;
        float f;

        for(int i = 0; i < 1377; i++) {
            line = reader.readLine();
            f = Float.parseFloat(line);
            heatmapArray[i] = f;
        }
        return heatmapArray;
    }

    public Float[] readOffsetFile(Context context) throws IOException{
        InputStream is = context.getAssets().open("output_details[1].txt");
        Float[] offsetArray = new Float[2754];
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line;
        float f;

        for(int i = 0; i < 2754; i++) {
            line = reader.readLine();
            f = Float.parseFloat(line);
            offsetArray[i] = f;
        }
        return offsetArray;
    }

    public ArrayList<String> readFeedbackMessagesNL(Context context) throws IOException {
        InputStream is = context.getAssets().open("feedbackmessages(NL).txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        ArrayList<String> feedbackmessages = new ArrayList<>();
        String line;
        int i = 1;
        while(reader.readLine() != null){
            line = reader.readLine();
            feedbackmessages.add(line);
            DebugLog.log(line);
            i++;
        }
        return feedbackmessages;
    }
}





