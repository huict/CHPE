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

import com.mygdx.honestmirror.application.common.DebugLog;

public class InterpreterController {
    private JsonObject jsonInput = null;
    private final String modelFilePath = "feedback_model.tflite";

    float[] inputArray;
    float[][] outputArray;
    private Interpreter interpreter = null;

    private final String output = "";
    private final Context context;

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
            //normalised? voor zo ver ik zie word er niets met de data gedaan...
            normalisedCoords.add(coordX);
            normalisedCoords.add(coordY);
        }

        inputArray = new float[normalisedCoords.size()];
       // DebugLog.log("inputArray" + inputArray);
        //why? waarom word data overgeschreven van een list<float> naar een float[]?
        for (int index = 0; index < normalisedCoords.size(); index++){
            inputArray[index] = normalisedCoords.get(index).floatValue();
            DebugLog.log("inputArray i = ["+index + "] value = ["+ inputArray[index] + "]");
        }

        if (interpreter != null){

            try{
                interpreter.run(inputArray, outputArray);
            }
            catch (Exception e){
                Log.e("InterpreterController", "Exception occurred when running the model:" + e.getMessage());
            }
        }
        
        for(int i = 0; i < outputArray.length; i++)
        {
            for(int j = 0; j < outputArray[i].length; j++)
            {
              //  DebugLog.log("output array i =[" + i + "] j = [" + j +"] value = " + outputArray[i][j]   );
            }
        }
 //       Log.i("InterpreterController", "Output Length" + outputArray);
    }

    private Object getJsonInput(){
 //       DebugLog.log("jsonInput.toString() " +jsonInput.toString());
        return jsonInput.toString();
    }

    public void setJsonInput(JsonObject jsonObject){
        this.jsonInput = jsonObject;
    }

    private MappedByteBuffer getModelAsMappedByteBuffer() throws IOException {
        return FileUtil.loadMappedFile(context, modelFilePath);
    }

    public float[][] getOutput(){
//        DebugLog.log("****outputArray[0].length****" + outputArray[0].length);
//        DebugLog.log("****outputArray.length****" + outputArray.length);

        return outputArray;
    }
}
