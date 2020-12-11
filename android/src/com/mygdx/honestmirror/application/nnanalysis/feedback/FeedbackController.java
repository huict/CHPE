package com.mygdx.honestmirror.application.nnanalysis.feedback;

import java.util.ArrayList;
import java.util.List;

public class FeedbackController {
    private static FeedbackController instance;

    List<FeedbackElement> feedbackElements;
    private int maxFloatIndex;

    private FeedbackController() {
        feedbackElements = new ArrayList<>();
    }

    public static FeedbackController getInstance(){
        if (instance == null)
            instance = new FeedbackController();


        return instance;
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

    public List<FeedbackElement> getFeedbackElements(){
        //return this.feedbackElements;

        List<FeedbackElement> items = new ArrayList<>();

        items.add(new FeedbackElement(Feedback.hands_down));
        items.add(new FeedbackElement(Feedback.hands_up));
        items.add(new FeedbackElement(Feedback.left_down_right_up));

        return items;
    }

    public String getSummary(){
        return "Despite you not moving your hands the whole time this presentation was very very good";
    }
}
