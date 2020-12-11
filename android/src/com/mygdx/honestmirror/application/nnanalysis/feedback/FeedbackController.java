package com.mygdx.honestmirror.application.nnanalysis.feedback;

import android.util.Log;

import com.mygdx.honestmirror.application.domain.feedback.Feedback;
import com.mygdx.honestmirror.application.domain.feedback.RawFeedbackElement;

import java.util.ArrayList;
import java.util.List;

public class FeedbackController {
    private static FeedbackController instance;

    List<RawFeedbackElement> feedbackElements;
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


        Feedback feedback = Feedback.empty;

        try{
            feedback = Feedback.values()[maxFloatIndex];
        }
        catch (Exception e){
            Log.e(this.getClass().getCanonicalName(), e.getMessage());
        }


        feedbackElements.add(new RawFeedbackElement(feedback));
    }

    public String getFeedback(){
        if (feedbackElements == null || feedbackElements.size() == 0)
            return "No feedback available";


        return feedbackElements.get(0).getShortFeedback();
    }

    public List<RawFeedbackElement> getFeedbackElements(){
        return this.feedbackElements;
    }

    public String getSummary(){
        return "Despite you not moving your hands the whole time this presentation was very very good";
    }
}
