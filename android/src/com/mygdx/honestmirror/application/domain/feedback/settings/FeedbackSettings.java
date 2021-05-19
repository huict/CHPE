package com.mygdx.honestmirror.application.domain.feedback.settings;

import com.mygdx.honestmirror.application.domain.feedback.EstimatedPose;

import java.util.List;

/**
 * holds a list of settings for the poses.
 */

public class FeedbackSettings {
    private List<FeedbackSetting> settingList;
    private final double framerate;

    /**
     * sets the video frame rate.
     * @param framerate frame rate of the video.
     */
    public FeedbackSettings(double framerate) {
        this.framerate = framerate;
    }

    /**
     * loads default settings defined in settingsloader.java.
     */
    public void loadDefaults(){
        settingList = SettingsLoader.GetDefaultSettings(framerate);
    }

    /**
     * get the max amount of seconds a pose can persist.
     * @param pose the pose to get the max persist seconds on.
     * @return the max amount seconds of the pose.
     */
    public int getMaxPersistSeconds(EstimatedPose pose){
        for (FeedbackSetting setting : settingList){
            if (setting.getPose().equals(pose))
                return (setting.getMaxPersistSeconds());
        }

        return 0;
    }
}
