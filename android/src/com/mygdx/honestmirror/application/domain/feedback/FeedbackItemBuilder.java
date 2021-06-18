package com.mygdx.honestmirror.application.domain.feedback;

import com.mygdx.honestmirror.application.common.DebugLog;

import java.io.IOException;

/**
 * build the feedback items to be drawn on the screen
 */
public class FeedbackItemBuilder {
    private final double framerate;
    private FeedbackDataContainer dataContainer;

    public FeedbackItemBuilder(double framerate) {
        this.framerate = framerate;
    }

    public void SetDataContainer(FeedbackDataContainer dataContainer){
        this.dataContainer = dataContainer;
    }

    /**
     * proses the pose information for proper displaying
     * @param pose the estemated pose
     * @param startTimeSeconds first frame the pose is found
     * @param endTimeSeconds last frame the pose is found
     * @return a feedback item ready for display
     * @throws IOException
     */
    public FeedbackItem make(EstimatedPose pose, int startTimeSeconds, int endTimeSeconds) throws IOException {
        if (dataContainer == null)
            dataContainer = new DesignTimeFeedbackDataContainer();

        FeedbackItem newFeedbackItem = new FeedbackItem(pose, dataContainer.getName(pose), dataContainer.getDescription(pose));

        newFeedbackItem.setStartSeconds(startTimeSeconds);
        newFeedbackItem.setEndSeconds(endTimeSeconds);

        return newFeedbackItem;
    }


}
