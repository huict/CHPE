package com.mygdx.honestmirror.application.domain.feedback;

import android.content.Context;

import com.mygdx.honestmirror.GlobalApplication;
import com.mygdx.honestmirror.application.common.DebugLog;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

/**
 * translates the found pose to text for the UI.
 */
public class DesignTimeFeedbackDataContainer implements FeedbackDataContainer {

    /**
     * translates the estimated pose to its corsponding string info
     * @param pose estimated pose
     * @return the string information for the pose
     */
    @Override
    public String getName(EstimatedPose pose) {
        String name = "";

        switch (pose) {
            case giving_the_back_to_the_audience:
                name = "Giving the back to the audience";
                break;
            case standing_with_the_bodyweight_on_one_leg:
                name = "Standing with the body weight on one leg";
                break;
            case hands_touching_face:
                name = "Hands touching face";
                break;
            case crossed_arms:
                name = "Crossed arms";
                break;
            case hands_in_pockets:
                name = "Hands in pockets";
                break;
            case delivered_gestures:
                name = "Delivered gestures";
                break;
        }

        return name;
    }

    /**
     * translates the esrimated pose to its corsponding detailt information
     * @param pose estimated pose that you want in detailt information on
     * @return detailt information for the pose information
     * @throws IOException
     */
    @Override
    public String getDescription(EstimatedPose pose) throws IOException {
        String description = "";

        Context context = GlobalApplication.getAppContext();
        InputStream is = context.getAssets().open("feedbackmessages(NL).txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        ArrayList<String> feedbackmessages = new ArrayList<>();
        String line;
        while (reader.readLine() != null) {
            line = reader.readLine();
            feedbackmessages.add(line);
        }
        switch (pose) {
            case giving_the_back_to_the_audience:
                description = feedbackmessages.get(0);
                break;
            case standing_with_the_bodyweight_on_one_leg:
                description = feedbackmessages.get(1);
                break;
            case hands_touching_face:
                description = feedbackmessages.get(2);
                break;
            case crossed_arms:
                description = feedbackmessages.get(3);
                break;
            case hands_in_pockets:
                description = feedbackmessages.get(4);
                break;
            case delivered_gestures:
                description = feedbackmessages.get(5);
                break;
        }

        return description;
    }
}
