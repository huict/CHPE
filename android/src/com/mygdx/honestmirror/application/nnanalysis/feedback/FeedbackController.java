package com.mygdx.honestmirror.application.nnanalysis.feedback;

import android.util.Log;

import com.mygdx.honestmirror.application.domain.feedback.EstimatedPose;
import com.mygdx.honestmirror.application.domain.feedback.FeedbackItem;
import com.mygdx.honestmirror.application.domain.feedback.FeedbackItemBuilder;
import com.mygdx.honestmirror.application.domain.feedback.PoseData;
import com.mygdx.honestmirror.application.domain.feedback.settings.FeedbackSettings;

import com.mygdx.honestmirror.application.common.DebugLog;
import com.mygdx.honestmirror.view.ui.a_Loading;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mygdx.honestmirror.GlobalApplication.resetProgress;

/**
 * runs the feedback network and adds the pose data
 * generates feedback items accordingly.
 */
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

    /**
     * creats new feedback controller if non exists otherwise returns existing one
     * @return the new or old feedback controller
     */
    public static FeedbackController getInstance(){
        if (instance == null)
            instance = new FeedbackController();

        return instance;
    }

    /**
     * resets the data stored from feedback generation
     * use to insure clean start for new feedback
     */
    @Override
    public void resetData(){
        feedbackItems = new ArrayList<>();
        poseData = new ArrayList<>();
        currentFrameCount = 0;
        feedbackGenerated = false;
        framerate = 24;
        feedbackItemBuilder = new FeedbackItemBuilder(framerate);
        resetProgress();
    }

    /**
     *  finds the estimated pose.
     * @param data the probability's array from the feedback array
     */
    @Override
    public void addData(float[][] data){
        addData(data, null);
    }

    /**
     * fill's the poseData array of estimated poses from given probability array
     * @param data the probability's array from the feedback network
     * @param frameIndex the index of the frame used for timestamp
     */
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

        EstimatedPose estimatedPose = null;
        //filers all estimated poses where the max surety of the pose is below 0.55 (-55%)
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

    }

    @Override
    public FeedbackSettings getSettings() {
        return settings;
    }

    @Override
    public void setSettings(FeedbackSettings settings) {
        this.settings = settings;
    }

    /**
     * generates feedback items from the pose data array
     * also sets the timestamp for the feedback ui element.
     * @throws IOException
     */
    private void generateFeedback() throws IOException {
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
            PoseData poseDataItem = poseData.get(currentPoseOccurrenceIndex);


            if (lastPose == null) {
                firstOccurrenceIndex = currentPoseOccurrenceIndex;
                firstOccurrenceTimeMs = poseDataItem.getTimeMilliseconds();

                lastPose = poseDataItem.getPose();
                poseOccurrenceCount = 1;

                continue;
            }

            if (lastPose.equals(poseDataItem.getPose()) && (currentPoseOccurrenceIndex + 1) != poseData.size()) {
                poseOccurrenceCount++;
                lastOccurrenceTimeMs = poseDataItem.getTimeMilliseconds();
            }
            else {

                if ((poseOccurrenceCount / framerate) > settings.getMaxPersistSeconds(lastPose)) {
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
    public List<FeedbackItem> getFeedbackItems() throws IOException {
        generateFeedback();
        return this.feedbackItems;
    }

    @Override
    public String getSummary() throws IOException {
        generateFeedback();

        return "Despite delivering gestures for 13 consecutive seconds this presentation was very very good";
    }

    /**
     * generates mock data for to be drawn on the ui
     */

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
        return (float) ((frameCount * (1000 / this.framerate))*3);
    }
}
