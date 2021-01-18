package com.mygdx.honestmirror.application.domain.feedback;

public class FeedbackItemBuilder {
    private double framerate;
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

        return new FeedbackItem(pose, dataContainer.getName(pose), dataContainer.getDescription(pose));
    }


}
