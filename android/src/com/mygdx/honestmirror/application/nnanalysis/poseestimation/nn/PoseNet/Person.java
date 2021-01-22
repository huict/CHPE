package com.mygdx.honestmirror.application.nnanalysis.poseestimation.nn.PoseNet;

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

        for (KeyPoint keyPoint : keyPoints){
            if (keyPoint.bodyPart == null || keyPoint.bodyPart.name() == null)
                continue;

            jsonObjectBuilderCoords = Json.createArrayBuilder();

            jsonObjectBuilderCoords.add(keyPoint.getPosition().rawX);
            jsonObjectBuilderCoords.add(keyPoint.getPosition().rawY);
            jsonObjectBuilderRoot.add(keyPoint.bodyPart.name(), jsonObjectBuilderCoords);
        }

        return jsonObjectBuilderRoot.build();
    }
}





