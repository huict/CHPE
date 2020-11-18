package com.mygdx.game.nnanalysis;

import java.util.ArrayList;
import java.util.List;

public class FeedbackController {
    List<FeedbackElement> feedbackElements;
    private int maxFloatIndex;
    public FeedbackController() {
        feedbackElements = new ArrayList<>();
    }

    public void addData(float[][] data){
        float[] probabilityArray = data[0];

        float maxFloat = 0f;
        maxFloatIndex = 0;

        for (int index=0; index < probabilityArray.length; index++){
            if (probabilityArray[index] > maxFloat){
                maxFloatIndex = index;
                maxFloat = probabilityArray[index];
            }
        }

        float[] probabilities = data[0];

        for (int index=0; index < probabilities.length; index++){

        }
        switch (maxFloatIndex){
            case 0:
                feedbackElements.add(new FeedbackElement(Feedback.left_down_right_up));
                break;
            case 1:
                feedbackElements.add(new FeedbackElement(Feedback.left_up_right_down));
                break;
            case 2:
                feedbackElements.add(new FeedbackElement(Feedback.hands_down));
                break;
            case 3:
                feedbackElements.add(new FeedbackElement(Feedback.hands_up));
                break;
            default:
                feedbackElements.add(new FeedbackElement(Feedback.empty));
        }
    }

    public String getFeedback(){
        if (feedbackElements == null || feedbackElements.size() == 0)
            return "No feedback available";


        return feedbackElements.get(0).getShortFeedback();
    }
}
