package com.mygdx.honestmirror.application.domain.feedback;

public class DesignTimeFeedbackDataContainer implements FeedbackDataContainer{

    @Override
    public String getName(EstimatedPose pose) {
        String name = "";

        switch (pose){
            case giving_the_back_to_the_audience:
                name = "Giving the back to the audience";
                break;
            case standing_with_the_bodyweight_on_one_leg:
                name = "Standing with the bodyweight on on e leg";
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
                name = "Deliverd gestures";
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
    public String getDescription(EstimatedPose pose) {
        String description = "";

        switch (pose){
            case giving_the_back_to_the_audience:
                description = "You were giving your back to the audience";
                break;
            case standing_with_the_bodyweight_on_one_leg:
                description = "You were standing with your bodyweight on one leg";
                break;
            case hands_touching_face:
                description = "You were touching your face with your hands";
                break;
            case crossed_arms:
                description = "You had your arms crossed";
                break;
            case hands_in_pockets:
                description = "You held your hands in your pockets";
                break;
            case delivered_gestures:
                description = "You delivered gestures";
                break;
            case crossing_legs:
                description = "You had your legs crossed";
                break;
            case feet_between_shoulders_and_waist_width_firmly_on_the_ground:
                description = "You held an upright position with your feet between your shoulders and waist width firmly on the ground";
                break;
            case hands_touching_hair:
                description = "You were touching your hair.";
                break;
            case hands_behind_the_back:
                description = "You held your hands behind your back";
                break;
            case chin_up:
                description = "You held your chin upward.";
                break;
            case neck_forward:
                description = "You held your neck forward";
                break;
            case empty:
                description = "EMPTY_DESCRIPTION";
                break;
        }

        return description;
    }
}
