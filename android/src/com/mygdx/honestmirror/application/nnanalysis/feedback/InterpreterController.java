package com.mygdx.honestmirror.application.nnanalysis.feedback;

import android.content.Context;
import android.util.Log;

import com.mygdx.honestmirror.application.nnanalysis.poseestimation.nn.PoseModels.NNModelPosenet;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.FileUtil;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.json.JsonArray;
import javax.json.JsonObject;

public class InterpreterController {


    private Interpreter interpreter = null;

    public InterpreterController(Context context){
        try{
            String modelFilePath = "feedback_model.tflite";
            interpreter = new Interpreter(FileUtil.loadMappedFile(context, modelFilePath));
        }
        catch (IOException e){
            Log.e("InterpreterController", e.getMessage());
        }
    }

    public float[][] runNN(JsonObject PersonsAsJson){
        float[] inputArray;
        float[][] outputArray = new float[1][13];

        if (PersonsAsJson == null)
            throw new IllegalArgumentException("input is null!");

        List<Float> floatArrayList = new ArrayList<>();
        String[] bodyParts = NNModelPosenet.bodyParts;

        for (String bodyPart : bodyParts){
            JsonArray JSonArrayWithCoordinates = PersonsAsJson.getJsonArray(bodyPart);

            //X and Y coordinates switched due to PoseNet Json, as it sets the Y first, then the X
            BigDecimal bigDecimalX = JSonArrayWithCoordinates.getJsonNumber(1).bigDecimalValue();
            BigDecimal bigDecimalY = JSonArrayWithCoordinates.getJsonNumber(0).bigDecimalValue();

            float coordX = bigDecimalX.floatValue();
            float coordY = bigDecimalY.floatValue();
            floatArrayList.add(coordX);
            floatArrayList.add(coordY);
        }

        inputArray = new float[floatArrayList.size()];
       // DebugLog.log("inputArray" + inputArray);
        //why? waarom word data overgeschreven van een list<float> naar een float[]?
        for (int index = 0; index < floatArrayList.size(); index++){
            inputArray[index] = floatArrayList.get(index);
//            DebugLog.log("inputArray i = ["+index + "] value = ["+ inputArray[index] + "]");
        }

        if (interpreter != null){

            try{
                interpreter.run(inputArray, outputArray);
            }
            catch (Exception e){
                Log.e("InterpreterController", "Exception occurred when running the model:" + e.getMessage());
            }
        }
//
//        for(int i = 0; i < outputArray.length; i++)
//        {
//            for(int j = 0; j < outputArray[i].length; j++)
//            {
//                DebugLog.log("output array i =[" + i + "] j = [" + j +"] value = " + outputArray[i][j]   );
//            }
//        }
//        Log.i("InterpreterController", "Output Length" + Arrays.deepToString(outputArray));
        return outputArray;
    }

    public float[][] testChin_Up(){
        float[] inputArray;
        float[][] outputArray = new float[1][13];
        List<Float> floatArrayList = new ArrayList<>();

        floatArrayList.add((float) 122.90178871154785);
        floatArrayList.add((float) 26.7021427154541);
        floatArrayList.add((float) 122.11682987213135);
        floatArrayList.add((float) 27.366634845733643);
        floatArrayList.add((float) 120.34361267089844);
        floatArrayList.add((float) 27.206862449645996);
        floatArrayList.add((float) 136.81968212127686);
        floatArrayList.add((float) 56.82061052322388);
        floatArrayList.add((float) 141.21107006072998);
        floatArrayList.add((float) 55.47068500518799);

        floatArrayList.add((float) 141.52256202697754);
        floatArrayList.add((float) 56.828299045562744);
        floatArrayList.add((float) 138.329833984375);
        floatArrayList.add((float) 57.38221740722656);
        floatArrayList.add((float) 95.31030905246735);
        floatArrayList.add((float) 97.78847634792328);
        floatArrayList.add((float) 129.29709041118622);
        floatArrayList.add((float) 72.26025199890137);
        floatArrayList.add((float) 112.75652885437012);
        floatArrayList.add((float) 140.38017082214355);

        floatArrayList.add((float) 98.45100450515747);
        floatArrayList.add((float) 111.78064060211182);
        floatArrayList.add((float) 160.71825122833252);
        floatArrayList.add((float) 103.51701927185059);
        floatArrayList.add((float) 141.43992233276367);
        floatArrayList.add((float) 91.6095199584961);
        floatArrayList.add((float) 137.42376708984375);
        floatArrayList.add((float) 162.2060854434967);
        floatArrayList.add((float) 139.38702201843262);
        floatArrayList.add((float) 161.08633995056152);

        floatArrayList.add((float) 119.8869457244873);
        floatArrayList.add((float) 225.2100374698639);
        floatArrayList.add((float) 123.78856229782104);
        floatArrayList.add((float) 224.93200063705444);

        inputArray = new float[floatArrayList.size()];
        for (int index = 0; index < floatArrayList.size(); index++){
            inputArray[index] = floatArrayList.get(index);
        }
        interpreter.run(inputArray, outputArray);
        return outputArray;
    }
}
