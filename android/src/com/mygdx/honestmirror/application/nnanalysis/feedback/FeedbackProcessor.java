package com.mygdx.honestmirror.application.nnanalysis.feedback;

import com.mygdx.honestmirror.application.domain.feedback.FeedbackItem;
import com.mygdx.honestmirror.application.domain.feedback.settings.FeedbackSettings;

import java.io.IOException;
import java.util.List;

public interface FeedbackProcessor {
    void resetData();

    void addData(float[][] data);

    void addData(float[][] data, Integer frameIndex);

    FeedbackSettings getSettings();

    void setSettings(FeedbackSettings settings);

    List<FeedbackItem> getFeedbackItems() throws IOException;

    String getSummary() throws IOException;
}
