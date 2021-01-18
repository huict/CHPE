package com.mygdx.honestmirror.application.domain.feedback;

import java.util.ArrayList;
import java.util.List;

public class FeedbackFactory {
    private List<EstimatedPose> estimatedPose;
    private FeedbackDataContainer feedbackDataContainer;

    public FeedbackFactory(FeedbackDataContainer dataContainer) {
        this.estimatedPose = new ArrayList<>();
    }

    public void addFeedback(EstimatedPose estimatedPose, int secondsStart, int secondsEnd){

    }

    public void addFeedbackList(List<EstimatedPose> estimatedPoseList){
        if (estimatedPoseList == null)
            return;
    }




    public String getSummary(){
        return "";
    }

    public List<FeedbackItem> getFeedbackItems(){
        return null;
    }
}
