package com.mygdx.honestmirror.application.domain.feedback;

import com.mygdx.honestmirror.application.common.DebugLog;

public class FeedbackItemBuilder {
    private final double framerate;
    private FeedbackDataContainer dataContainer;

    public FeedbackItemBuilder(double framerate) {
        this.framerate = framerate;
    }

    public void SetDataContainer(FeedbackDataContainer dataContainer){
        this.dataContainer = dataContainer;
    }


    public FeedbackItem make(EstimatedPose pose, int startTimeSeconds, int endTimeSeconds){
        if (dataContainer == null)
            dataContainer = new DesignTimeFeedbackDataContainer();

//        DebugLog.log("Ints: " + startTimeSeconds + " , " + endTimeSeconds);
        FeedbackItem newFeedbackItem = new FeedbackItem(pose, dataContainer.getName(pose), dataContainer.getDescription(pose));

        newFeedbackItem.setStartSeconds(startTimeSeconds);
        newFeedbackItem.setEndSeconds(endTimeSeconds);

        return newFeedbackItem;
    }


}
