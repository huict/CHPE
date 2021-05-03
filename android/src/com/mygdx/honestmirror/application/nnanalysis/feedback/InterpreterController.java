package com.mygdx.honestmirror.application.nnanalysis.feedback;

import android.content.Context;
import android.os.Debug;
import android.util.Log;

import com.mygdx.honestmirror.application.common.DebugLog;
import com.mygdx.honestmirror.application.nnanalysis.poseestimation.nn.PoseModels.NNModelPosenet;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.FileUtil;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
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
        float[][] outputArray = new float[1][6];

        if (PersonsAsJson == null)
            throw new IllegalArgumentException("input is null!");

        List<Float> floatArrayList = new ArrayList<>();
        String[] bodyParts = NNModelPosenet.bodyParts;

        for (String bodyPart : bodyParts){
            JsonArray JSonArrayWithCoordinates = PersonsAsJson.getJsonArray(bodyPart);

            BigDecimal bigDecimalX = JSonArrayWithCoordinates.getJsonNumber(0).bigDecimalValue();
            BigDecimal bigDecimalY = JSonArrayWithCoordinates.getJsonNumber(1).bigDecimalValue();

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
                DebugLog.log(Arrays.deepToString(outputArray));
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

    public float[][] testTouching_Hair(){
        float[] inputArray;
        float[][] outputArray = new float[1][6];
        List<Float> floatArrayList = new ArrayList<>();
        floatArrayList.add((float) 51.27184009552002);
        floatArrayList.add((float) 130.98658800125122);
        floatArrayList.add((float) 77.63010215759277);
        floatArrayList.add((float) 115.19560432434082);
        floatArrayList.add((float) 55.61896800994873);
        floatArrayList.add((float) 114.59652519226074);
        floatArrayList.add((float) 54.72090816497803);
        floatArrayList.add((float) 128.81183230876923);
        floatArrayList.add((float) 81.84581089019775);
        floatArrayList.add((float) 104.37448596954346);

        floatArrayList.add((float) 85.77149772644043);
        floatArrayList.add((float) 158.84040486812592);
        floatArrayList.add((float) 136.2244997024536);
        floatArrayList.add((float) 72.36149978637695);
        floatArrayList.add((float) 135.18316841125488);
        floatArrayList.add((float) 153.78929948806763);
        floatArrayList.add((float) 118.43409156799316);
        floatArrayList.add((float) 58.599379539489746);
        floatArrayList.add((float) 122.98944473266602);
        floatArrayList.add((float) 122.98944473266602);

        floatArrayList.add((float) 178.69256782531738);
        floatArrayList.add((float) 93.82057237625122);
        floatArrayList.add((float) 146.58090019226074);
        floatArrayList.add((float) 138.31378173828125);
        floatArrayList.add((float) 174.22069835662842);
        floatArrayList.add((float) 86.7153787612915);
        floatArrayList.add((float) 176.64463424682617);
        floatArrayList.add((float) 119.92428779602051);
        floatArrayList.add((float) 188.55237412452698);
        floatArrayList.add((float) 95.13518273830414);

        floatArrayList.add((float) 198.8670392036438);
        floatArrayList.add((float) 157.16115808486938);
        floatArrayList.add((float) 197.95399141311646);
        floatArrayList.add((float) 126.60192155838013);


        inputArray = new float[floatArrayList.size()];
        for (int index = 0; index < floatArrayList.size(); index++){
            inputArray[index] = floatArrayList.get(index);
        }
        interpreter.run(inputArray, outputArray);
        DebugLog.log("OutputArray touching hair: " + Arrays.deepToString(outputArray));
        return outputArray;
    }

    public float[][] testBodyWeightOneLeg(){
        float[] inputArray;
        float[][] outputArray = new float[1][6];
        List<Float> floatArrayList = new ArrayList<>();

        floatArrayList.add((float) 112.62297821044922);
        floatArrayList.add((float) 49.377668380737305);
        floatArrayList.add((float) 74.32021522521973);
        floatArrayList.add((float) 84.98075866699219);
        floatArrayList.add((float) 78.99310779571533);
        floatArrayList.add((float) 80.36751365661621);
        floatArrayList.add((float) 47.84467315673828);
        floatArrayList.add((float) 118.83786392211914);
        floatArrayList.add((float) 82.1274471282959);
        floatArrayList.add((float) 76.3248176574707);

        floatArrayList.add((float) 69.55124282836914);
        floatArrayList.add((float) 138.3743896484375);
        floatArrayList.add((float) 101.73267936706543);
        floatArrayList.add((float) 70.68048095703125);
        floatArrayList.add((float) 87.19325733184814);
        floatArrayList.add((float) 151.5379238128662);
        floatArrayList.add((float) 140.55152130126953);
        floatArrayList.add((float) 25.80533456802368);
        floatArrayList.add((float) 136.9531536102295);
        floatArrayList.add((float) 83.85749244689941);

        floatArrayList.add((float) 116.61385345458984);
        floatArrayList.add((float) 53.38039970397949);
        floatArrayList.add((float) 159.36824572086334);
        floatArrayList.add((float) 119.6358642578125);
        floatArrayList.add((float) 167.22476530075073);
        floatArrayList.add((float) 65.90655183792114);
        floatArrayList.add((float) 188.95105147361755);
        floatArrayList.add((float) 126.81961619853973);
        floatArrayList.add((float) 191.60147055983543);
        floatArrayList.add((float) 62.06202185153961);

        floatArrayList.add((float) 157.16115808486938);
        floatArrayList.add((float) 198.8670392036438);
        floatArrayList.add((float) 126.60192155838013);
        floatArrayList.add((float) 197.95399141311646);

        inputArray = new float[floatArrayList.size()];
        for (int index = 0; index < floatArrayList.size(); index++){
            inputArray[index] = floatArrayList.get(index);
        }
        interpreter.run(inputArray, outputArray);
        DebugLog.log("OutputArray body weight on one leg: " + Arrays.deepToString(outputArray));
        return outputArray;
    }

}
