package com.mygdx.honestmirror.application.nnanalysis.feedback;

import android.util.Log;

import com.mygdx.honestmirror.application.domain.feedback.DesignTimeFeedbackDataContainer;
import com.mygdx.honestmirror.application.domain.feedback.EstimatedPose;
import com.mygdx.honestmirror.application.domain.feedback.FeedbackDataContainer;
import com.mygdx.honestmirror.application.domain.feedback.FeedbackFactory;
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

        for (int currentPoseOccurrenceIndex = 0; currentPoseOccurrenceIndex < poseData.size(); currentPoseOccurrenceIndex++){
            PoseData poseDataItem = poseData.get(currentPoseOccurrenceIndex);

            if (lastPose == null){
                lastPose = poseDataItem.getPose();
                poseOccurrenceCount = 1;
                continue;
            }

            if (lastPose.equals(poseDataItem.getPose()))
                poseOccurrenceCount++;
            else{
                if ((poseOccurrenceCount / framerate) > settings.getMaxPersistSeconds(lastPose)){
                    double firstOccurenceTimeSeconds = firstOccurrenceTimeMs / 1000;
                    double lastOccurrenceTimeSeconds = (double) poseDataItem.getTimeMilliseconds() / 1000;
                    feedbackItems.add(feedbackItemBuilder.make(lastPose, (int) firstOccurenceTimeSeconds, (int) lastOccurrenceTimeSeconds));
                }

                //firstOccurrenceIndex = currentPoseOccurrenceIndex;
                firstOccurrenceTimeMs = poseDataItem.getTimeMilliseconds();
            }
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

        return "Despite you not moving your hands the whole time this presentation was very very good";
    }





    private int getTimeInMilliseconds(int frameCount){
        double milliseconds = frameCount * 3.333;

        return (int) milliseconds;
    }
}
