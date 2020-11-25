package com.mygdx.honestmirror.application.nnanalysis.feedback;

import android.content.Context;
import android.util.Log;


import com.mygdx.honestmirror.application.nnanalysis.poseestimation.nn.PoseModels.NNModelPosenet;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.FileUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.MappedByteBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.json.JsonArray;
import javax.json.JsonObject;

public class InterpreterController {
    private JsonObject jsonInput = null;
    private final String modelFilePath = "model_2.tflite";

    float[] inputArray;
    float[][] outputArray;
    private Interpreter interpreter = null;




    private String output = "";
    private Context context;

    public InterpreterController(Context context){
        this.context = context;
        try{
            interpreter = new Interpreter(getModelAsMappedByteBuffer());
        }
        catch (IOException e){
            Log.e("InterpreterController", e.getMessage());
        }
    }

    public void runNN(){
        outputArray = new float[1][13];

        if (jsonInput == null)
            throw new IllegalArgumentException("imput is null!");



        List<Float> normalisedCoords = new ArrayList<>();
        String[] bodyParts = NNModelPosenet.bodyParts;


        for (String bodyPart : bodyParts){
            JsonArray coordsArray = jsonInput.getJsonArray(bodyPart);

            if (coordsArray == null)
                continue;

            BigDecimal bigDecimalX = coordsArray.getJsonNumber(0).bigDecimalValue();
            BigDecimal bigDecimalY = coordsArray.getJsonNumber(1).bigDecimalValue();

            float coordX = bigDecimalX.floatValue();
            float coordY = bigDecimalY.floatValue();

            normalisedCoords.add(coordX);
            normalisedCoords.add(coordY);
        }

        inputArray = new float[normalisedCoords.size()];

        for (int index = 0; index < normalisedCoords.size(); index++){
            inputArray[index] = normalisedCoords.get(index).floatValue();
        }

        if (interpreter != null){

            try{
                interpreter.run(inputArray, outputArray);
            }
            catch (Exception e){
                Log.e("InterpreterController", "Exception occurred when running the model:" + e.getMessage());
            }
        }

        Log.i("InterpreterController", "Output Length" + outputArray.length);
    }

    private Object getJsonInput(){
        return jsonInput.toString();
    }

    public void setJsonInput(JsonObject jsonObject){
        this.jsonInput = jsonObject;
    }

    private MappedByteBuffer getModelAsMappedByteBuffer() throws IOException {
        return FileUtil.loadMappedFile(context, modelFilePath);
    }

    public float[][]getInput(){
        return outputArray;
    }
}
