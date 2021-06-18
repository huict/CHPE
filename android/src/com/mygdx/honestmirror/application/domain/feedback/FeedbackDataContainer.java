package com.mygdx.honestmirror.application.domain.feedback;

import android.content.Context;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public interface FeedbackDataContainer {
    String getName(EstimatedPose pose);
    String getDescription(EstimatedPose pose) throws IOException;
}
