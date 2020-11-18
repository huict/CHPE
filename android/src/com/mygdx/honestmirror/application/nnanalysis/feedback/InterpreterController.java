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
    private JsonArray input = null;
    private final String modelFilePath = "model.tflite";

    private String output = "";
    private Context context;

    public InterpreterController(Context context){
        this.context = context;
    }

    public String writeFeedback(){
        return output;
    }

    public void LoadData(){
        float[] inputArray;
        float[][] outputObject = new float[1][16];

        if (input == null)
            throw new IllegalArgumentException("imput is null!");

        Interpreter interpreter = null;

        try{
            interpreter = new Interpreter(getModelAsMappedByteBuffer());
        }
        catch (IOException e){
            Log.e("InterpreterController", e.getMessage());
        }


        JsonObject jsonObject = input.getJsonObject(0);

        List<Float> normalisedCoords = new ArrayList<>();
        String[] bodyParts = NNModelPosenet.bodyParts;


        for (String bodyPart : bodyParts){
            JsonArray coordsArray = jsonObject.getJsonArray(bodyPart);

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

                interpreter.run(inputArray, outputObject);
            }
            catch (Exception e){
                Log.e("InterpreterController", "Exception occurred when running the model:" + e.getMessage());
            }
        }



        Log.i("InterpreterController", "Output Length" + outputObject.length);
    }

    private Object getInput(){
        return input.toString();
    }

    public void setInput(JsonArray jsonArray){
        this.input = jsonArray;
    }

    public MappedByteBuffer getModelAsMappedByteBuffer() throws IOException {
        return FileUtil.loadMappedFile(context, modelFilePath);
    }
}
