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

public class DesignTimeFeedbackDataContainer implements FeedbackDataContainer {

    @Override
    public String getName(EstimatedPose pose) {
        String name = "";

        switch (pose) {
            case giving_the_back_to_the_audience:
                name = "Giving the back to the audience";
                break;
            case standing_with_the_bodyweight_on_one_leg:
                name = "Standing with the bodyweight on one leg";
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
            case crossing_legs:
                name = "Crossing legs";
                break;
            case feet_between_shoulders_and_waist_width_firmly_on_the_ground:
                name = "Upright position";
                break;
            case hands_touching_hair:
                name = "Hands touching hair";
                break;
            case hands_behind_the_back:
                name = "Hands behind the back";
                break;
            case chin_up:
                name = "Chin up";
                break;
            case neck_forward:
                name = "Neck forward";
                break;
            case empty:
                name = "EMPTY_NAME";
                break;
        }

        return name;
    }

    @Override
    public String getDescription(EstimatedPose pose) throws IOException {
        String description = "";

        Context context = GlobalApplication.getAppContext();
        InputStream is = context.getAssets().open("feedbackmessages(NL).txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        ArrayList<String> feedbackmessages = new ArrayList<>();
        String line;
        int i = 1;
        while (reader.readLine() != null) {
            line = reader.readLine();
            feedbackmessages.add(line);
            DebugLog.log(line);
            i++;
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
            case crossing_legs:
                description = feedbackmessages.get(6);
                break;
            case feet_between_shoulders_and_waist_width_firmly_on_the_ground:
                description = feedbackmessages.get(7);
                break;
            case hands_touching_hair:
                description = feedbackmessages.get(8);
                break;
            case hands_behind_the_back:
                description = feedbackmessages.get(9);
                break;
            case chin_up:
                description = feedbackmessages.get(10);
                break;
            case neck_forward:
                description = feedbackmessages.get(11);
                break;
            case empty:
                description = feedbackmessages.get(12);
                break;
        }

        return description;
    }
}
