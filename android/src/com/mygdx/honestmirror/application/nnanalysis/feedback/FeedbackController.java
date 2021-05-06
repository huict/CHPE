package com.mygdx.honestmirror.application.nnanalysis.feedback;

import android.util.Log;

import com.mygdx.honestmirror.application.domain.feedback.EstimatedPose;
import com.mygdx.honestmirror.application.domain.feedback.FeedbackItem;
import com.mygdx.honestmirror.application.domain.feedback.FeedbackItemBuilder;
import com.mygdx.honestmirror.application.domain.feedback.PoseData;
import com.mygdx.honestmirror.application.domain.feedback.settings.FeedbackSettings;

import com.mygdx.honestmirror.application.common.DebugLog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FeedbackController implements FeedbackProcessor {
    private static FeedbackController instance;

    private List<FeedbackItem> feedbackItems;
    private List<PoseData> poseData;
    private float currentFrameCount = 0;
    private int maxFloatIndex;
    private boolean feedbackGenerated;
    private double framerate = 24;
    private FeedbackSettings settings  = new FeedbackSettings(framerate);;
    private FeedbackItemBuilder feedbackItemBuilder;
    private int firstOccurrenceIndex = 0;

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
        framerate = 24;
        feedbackItemBuilder = new FeedbackItemBuilder(framerate);
    }

    @Override
    public void addData(float[][] data){
        addData(data, null);
    }

    @Override
    public void addData(float[][] data, Integer frameIndex){


      //  DebugLog.log("----data frameIndex----- " + frameIndex   );
        currentFrameCount++;


        float[] probabilityArray = data[0];

        float maxFloat = 0f;
        maxFloatIndex = 0;

        //DebugLog.log(Arrays.toString(probabilityArray));
        for (int index=0; index < probabilityArray.length; index++){
            if (probabilityArray[index] > maxFloat){
                maxFloatIndex = index;
                maxFloat = probabilityArray[index];
            }
        }
        //TODO: ADD MINIMUM LIMIT, SO THAT THE APPLICATION DOESN'T ADD UNRELIABLE FEEDBACK


        EstimatedPose estimatedPose = null;

        DebugLog.log("-----maxFloat " + maxFloat   );
//        DebugLog.log("-----maxFloatIndex " + maxFloatIndex   );


        if(maxFloat >= 0.55) {
            try {
                estimatedPose = EstimatedPose.values()[maxFloatIndex];
            } catch (Exception e) {
                Log.e(this.getClass().getCanonicalName(), e.getMessage());
            }
        }

        float frameCount = currentFrameCount;
        if (frameIndex != null)
            frameCount = frameIndex + 1;

        poseData.add(new PoseData(estimatedPose, getTimeInMilliseconds(currentFrameCount)));
        //DebugLog.log("*******-----data added ------****** " );
    }

    @Override
    public FeedbackSettings getSettings() {
        return settings;
    }

    @Override
    public void setSettings(FeedbackSettings settings) {
        this.settings = settings;
    }

    private void generateFeedback() throws IOException {
             //DebugLog.log("--- generate Feedback items ---");
        settings.loadDefaults();
        if (feedbackGenerated)
            return;

        if (poseData == null)
            return;

        // detect pose persisting over time
        EstimatedPose lastPose = null;
        int poseOccurrenceCount = 0;
        double firstOccurrenceTimeMs = 0;
        double lastOccurrenceTimeMs = 0;

        for (int currentPoseOccurrenceIndex = 0; currentPoseOccurrenceIndex < poseData.size(); currentPoseOccurrenceIndex++) {
            //DebugLog.log("--- generate Feedback items start for loop---");
            PoseData poseDataItem = poseData.get(currentPoseOccurrenceIndex);
            //DebugLog.log("pose " + poseDataItem.getPose());

            if (lastPose == null) {
                //DebugLog.log("--- generate Feedback items first if---");
                firstOccurrenceIndex = currentPoseOccurrenceIndex;
                firstOccurrenceTimeMs = poseDataItem.getTimeMilliseconds();

                lastPose = poseDataItem.getPose();
                poseOccurrenceCount = 1;

                continue;
            }

            if (lastPose.equals(poseDataItem.getPose()) && (currentPoseOccurrenceIndex + 1) != poseData.size()) {
                //DebugLog.log("--- generate Feedback items second if---" + poseOccurrenceCount);
                poseOccurrenceCount++;
                lastOccurrenceTimeMs = poseDataItem.getTimeMilliseconds();
            }
            else {
                //DebugLog.log("--- generate Feedback items ELSE---");
                //if ((poseOccurrenceCount / framerate) > settings.getMaxPersistSeconds(lastPose)) {
                    double firstOccurenceTimeSeconds = firstOccurrenceTimeMs / 1000;
                    double lastOccurrenceTimeSeconds = lastOccurrenceTimeMs / 1000;
                    feedbackItems.add(feedbackItemBuilder.make(lastPose, (int) firstOccurenceTimeSeconds, (int) lastOccurrenceTimeSeconds));
                    firstOccurrenceIndex = currentPoseOccurrenceIndex;
                    firstOccurrenceTimeMs = poseDataItem.getTimeMilliseconds();
                }
            }
                lastPose = poseDataItem.getPose();
        }


            // detect pose recurring often over time

            // detect pose not changing over time
            
            //DebugLog.log("Feedback items " + feedbackItems);
            feedbackGenerated = true;
    }


    @Override
    public List<FeedbackItem> getFeedbackItems() throws IOException {
        generateFeedback();
        return this.feedbackItems;
    }

    @Override
    public String getSummary() throws IOException {
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
            this.poseData.add(new PoseData(EstimatedPose.crossed_arms, counter));
        }

        for (int index = 50; index < 120; index++ ){
            int counter = (index + 1) * 200;
            this.poseData.add(new PoseData(EstimatedPose.delivered_gestures, counter));
        }
    }

    private float getTimeInMilliseconds(float frameCount){
        //        DebugLog.log("----milliseconds " + milliseconds + "-----------");
        return (float) ((frameCount * (1000 / this.framerate))*3);
    }
}
