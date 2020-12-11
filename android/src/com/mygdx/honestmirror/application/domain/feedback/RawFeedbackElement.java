package com.mygdx.honestmirror.application.domain.feedback;

public class RawFeedbackElement {
    private Feedback feedback;
    private String writtenFeedback;
    private String shortFeedback;
    public RawFeedbackElement(Feedback feedback) {
        this.feedback = feedback;
        setAttributes();
    }



    private void setAttributes(){

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
