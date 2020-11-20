package com.mygdx.honestmirror.application.nnanalysis.feedback;

public class FeedbackElement {
    private Feedback feedback;
    private String writtenFeedback;
    private String shortFeedback;

    public FeedbackElement(Feedback feedback) {
        this.feedback = feedback;
        setAttributes();
    }



    private void setAttributes(){
        switch (feedback){
            case hands_down:
                writtenFeedback = "You have been holding your hands down.";
                shortFeedback = "Hands Down";
                break;
            case hands_up:
                writtenFeedback = "You have been holding your hands up";
                shortFeedback = "Hands up";
                break;
            case left_down_right_up:
                writtenFeedback = "you have been holding your right hand up";
                shortFeedback = "Right hand up";
                break;
            case left_up_right_down:
                writtenFeedback = "you have been holding your left hand up";
                shortFeedback = "Left hand up";
                break;
            default:
                break;
        }
    }

    public Feedback getFeedback() {
        return feedback;
    }


    public String getWrittenFeedback() {
        return writtenFeedback;
    }

    public String getShortFeedback() {
        return shortFeedback;
    }
}
