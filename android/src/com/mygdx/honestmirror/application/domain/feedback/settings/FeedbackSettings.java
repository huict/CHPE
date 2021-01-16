package com.mygdx.honestmirror.application.domain.feedback.settings;

import com.mygdx.honestmirror.application.domain.feedback.EstimatedPose;

import java.util.List;

public class FeedbackSettings {
    private List<FeedbackSetting> settingList;
    private double framerate;

    public FeedbackSettings(double framerate) {
        this.framerate = framerate;
    }

    public void loadDefaults(){
        settingList = SettingsLoader.GetDefaultSettings(framerate);
    }

    public OccurrenceOverTime getMaxOccurrence(EstimatedPose pose){
        for (FeedbackSetting setting : settingList){
            if (setting.getPose().equals(pose))
                return (setting.getMaxOccurrence());
        }

        return null;
    }

    public int getMaxPersistSeconds(EstimatedPose pose){
        for (FeedbackSetting setting : settingList){
            if (setting.getPose().equals(pose))
                return (setting.getMaxPersistSeconds());
        }

        return 0;
    }
}
