package com.mygdx.honestmirror.application.domain.feedback.settings;

import com.mygdx.honestmirror.application.domain.feedback.EstimatedPose;

import java.util.ArrayList;
import java.util.List;

public class SettingsLoader {
    public static List<FeedbackSetting> GetDefaultSettings(double frameRate){

        List<FeedbackSetting> feedbackSettings = new ArrayList<>();

        for (EstimatedPose pose : EstimatedPose.values()){
            OccurrenceOverTime occurrenceOverTime = new OccurrenceOverTime(1, 1, frameRate);
            FeedbackSetting newSetting = new FeedbackSetting(pose, 3,  occurrenceOverTime);
        }

        return feedbackSettings;

    }
}
