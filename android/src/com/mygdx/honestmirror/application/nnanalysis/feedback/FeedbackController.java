package com.mygdx.honestmirror.application.nnanalysis.feedback;

import android.util.Log;

import com.mygdx.honestmirror.application.domain.feedback.EstimatedPose;
import com.mygdx.honestmirror.application.domain.feedback.FeedbackItem;
import com.mygdx.honestmirror.application.domain.feedback.FeedbackItemBuilder;
import com.mygdx.honestmirror.application.domain.feedback.PoseData;
import com.mygdx.honestmirror.application.domain.feedback.settings.FeedbackSettings;

import java.util.ArrayList;
import java.util.List;

public class FeedbackController implements FeedbackProcessor {
    private static FeedbackController instance;

    private List<FeedbackItem> feedbackItems;
    private List<PoseData> poseData;
    private int currentFrameCount = 0;
    private int maxFloatIndex;
    private boolean feedbackGenerated;
    private FeedbackSettings settings;
    private FeedbackItemBuilder feedbackItemBuilder;
    private double framerate;


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
        framerate = 30;
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
            PoseData poseDataItem = poseData.get(currentPoseOccurrenceIndex);

            if (lastPose == null){
                firstOccurrenceIndex = currentPoseOccurrenceIndex;
                firstOccurrenceTimeMs = poseDataItem.getTimeMilliseconds();

                lastPose = poseDataItem.getPose();
                poseOccurrenceCount = 1;
                continue;
            }

            if (lastPose.equals(poseDataItem.getPose()) && (currentPoseOccurrenceIndex + 1) != poseData.size()){
                poseOccurrenceCount++;
                lastOccurrenceTimeMs = poseDataItem.getTimeMilliseconds();
            }

            else{
                if ((poseOccurrenceCount / framerate) > settings.getMaxPersistSeconds(lastPose)){
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
        double milliseconds = frameCount * 3.333;

        return (int) milliseconds;
    }
}
