package com.mygdx.honestmirror.application.nnanalysis.feedback;

import android.util.Log;

import com.mygdx.honestmirror.application.domain.feedback.EstimatedPose;
import com.mygdx.honestmirror.application.domain.feedback.FeedbackItem;
import com.mygdx.honestmirror.application.domain.feedback.FeedbackItemBuilder;
import com.mygdx.honestmirror.application.domain.feedback.PoseData;
import com.mygdx.honestmirror.application.domain.feedback.settings.FeedbackSettings;

import com.mygdx.honestmirror.application.common.DebugLog;

import java.util.ArrayList;
import java.util.List;

public class FeedbackController implements FeedbackProcessor {
    private static FeedbackController instance;

    private List<FeedbackItem> feedbackItems;
    private List<PoseData> poseData;
    private int currentFrameCount = 0;
    private int maxFloatIndex;
    private boolean feedbackGenerated;
    private double framerate = 24.23;
    private FeedbackSettings settings  = new FeedbackSettings(framerate);;
    private FeedbackItemBuilder feedbackItemBuilder;


    private FeedbackController() {
        resetData();
    }

    public static FeedbackController getInstance(){
        if (instance == null)
            instance = new FeedbackController();

        return instance;
    }

    @Override
    public void resetData(){
        feedbackItems = new ArrayList<>();
        poseData = new ArrayList<>();
        currentFrameCount = 0;
        feedbackGenerated = false;
        framerate = 24.23;
        feedbackItemBuilder = new FeedbackItemBuilder(framerate);
    }

    @Override
    public void addData(float[][] data){
        addData(data, null);
    }

    @Override
    public void addData(float[][] data, Integer frameIndex){
        currentFrameCount++;


        float[] probabilityArray = data[0];

        float maxFloat = 0f;
        maxFloatIndex = 0;

        for (int index=0; index < probabilityArray.length; index++){
            if (probabilityArray[index] > maxFloat){
                maxFloatIndex = index;
                maxFloat = probabilityArray[index];
            }
        }

        EstimatedPose estimatedPose = EstimatedPose.empty;

        try{
            estimatedPose = EstimatedPose.values()[maxFloatIndex];
        }
        catch (Exception e){
            Log.e(this.getClass().getCanonicalName(), e.getMessage());
        }

        int frameCount = currentFrameCount;
        if (frameIndex != null)
            frameCount = frameIndex + 1;

        poseData.add(new PoseData(estimatedPose, getTimeInMilliseconds(currentFrameCount)));
    }

    @Override
    public FeedbackSettings getSettings() {
        return settings;
    }

    @Override
    public void setSettings(FeedbackSettings settings) {
        this.settings = settings;
    }

    private void generateFeedback(){
 //     DebugLog.log("--- generate Feedback items ---");
        settings.loadDefaults();
        if (feedbackGenerated)
            return;

        if (poseData == null)
            return;

        // detect pose persisting over time

        EstimatedPose lastPose = null;
        int poseOccurrenceCount = 0;
        int firstOccurrenceIndex = 0;
        double firstOccurrenceTimeMs = 0;
        double lastOccurrenceTimeMs = 0;

        for (int currentPoseOccurrenceIndex = 0; currentPoseOccurrenceIndex < poseData.size(); currentPoseOccurrenceIndex++){
 //         DebugLog.log("--- generate Feedback items start for loop---");
            PoseData poseDataItem = poseData.get(currentPoseOccurrenceIndex);

            if (lastPose == null){
  //            DebugLog.log("--- generate Feedback items first if---");
                firstOccurrenceIndex = currentPoseOccurrenceIndex;
                firstOccurrenceTimeMs = poseDataItem.getTimeMilliseconds();

                lastPose = poseDataItem.getPose();
                poseOccurrenceCount = 1;

                continue;
            }
            DebugLog.log("pose " + poseDataItem.getPose());
//            DebugLog.log("pose size " + poseData.size());
            if (lastPose.equals(poseDataItem.getPose()) && (currentPoseOccurrenceIndex + 1) != poseData.size()){
  //              DebugLog.log("--- generate Feedback items second if---" + poseOccurrenceCount);
                poseOccurrenceCount++;
                lastOccurrenceTimeMs = poseDataItem.getTimeMilliseconds();
 //               DebugLog.log("--- generate Feedback items second if---" + lastOccurrenceTimeMs);
            }

            else{
       //         DebugLog.log("--- generate Feedback items else---");
                DebugLog.log("--- generate Feedback items else--- settings.getMaxPersistSeconds(lastPose)" + settings.getMaxPersistSeconds(lastPose) );
                DebugLog.log("--- generate Feedback items else---poseOccurrenceCount / framerate" + poseOccurrenceCount / framerate );
           //     if ((poseOccurrenceCount / framerate) > settings.getMaxPersistSeconds(lastPose)){
                    DebugLog.log("--- generate Feedback items else ---" + lastPose);
                    double firstOccurenceTimeSeconds = firstOccurrenceTimeMs / 1000;
                    double lastOccurrenceTimeSeconds = lastOccurrenceTimeMs / 1000;
                    feedbackItems.add(feedbackItemBuilder.make(lastPose, (int) firstOccurenceTimeSeconds, (int) lastOccurrenceTimeSeconds));
                    DebugLog.log("Feedback items " + feedbackItems);
                    firstOccurrenceIndex = currentPoseOccurrenceIndex;
                    firstOccurrenceTimeMs = poseDataItem.getTimeMilliseconds();
            //    }
            }
            lastPose = poseDataItem.getPose();
        }


        // detect pose recurring often over time

        // detect pose not changing over time

        feedbackGenerated = true;
    }

    @Override
    public List<FeedbackItem> getFeedbackItems(){
        generateFeedback();

        return this.feedbackItems;
    }

    @Override
    public String getSummary(){
        generateFeedback();

        return "Despite delivering gestures for 13 consecutive seconds this presentation was very very good";
    }


    public void generateMockData(){
        //this.resetData();
        settings = new FeedbackSettings(5);
        settings.loadDefaults();

        this.framerate = 5;

        for (int index = 0; index < 50; index++ ){
            int counter = (index + 1) * 200;
            this.poseData.add(new PoseData(EstimatedPose.feet_between_shoulders_and_waist_width_firmly_on_the_ground, counter));
        }

        for (int index = 50; index < 120; index++ ){
            int counter = (index + 1) * 200;
            this.poseData.add(new PoseData(EstimatedPose.delivered_gestures, counter));
        }
    }

    private int getTimeInMilliseconds(int frameCount){
        double milliseconds = frameCount * (1000 / this.framerate);

        return (int) milliseconds;
    }
}
