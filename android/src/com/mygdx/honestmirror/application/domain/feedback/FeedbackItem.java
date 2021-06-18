package com.mygdx.honestmirror.application.domain.feedback;

/**
 * holds the information needed for the feedback UI element
 */
public class FeedbackItem {
    private EstimatedPose estimatedPose;
    private String name;
    private String description;
    private int startSeconds = 0;
    private int endSeconds = 0;

    public FeedbackItem(EstimatedPose estimatedPose, String name, String description) {
        this.estimatedPose = estimatedPose;
        this.name = name;
        this.description = description;
    }

    public EstimatedPose getEstimatedPose() {
        return estimatedPose;
    }

    public void setStartSeconds(int startSeconds) {
        this.startSeconds = startSeconds;
    }

    public void setEndSeconds(int endSeconds) {
        this.endSeconds = endSeconds;
    }

    /**
     * function to get name and timestamp if end seconds is > 0
     * @return name and if endseconds are set returns name + timestamp
     */
    public String getName()
    {
        if (endSeconds != 0){
            return name + " " + getTimeFormat(startSeconds) + " - " + getTimeFormat(endSeconds);
        }

        return name;
    }

    public String getDescription() {
        return description;
    }

    /**
     * translates a given amount of seconds in minutes and second
     * @param seconds amount of seconds to translate
     * @return timestamp string 00:00
     */
    private String getTimeFormat(int seconds){

        int timeSeconds = seconds;
        int timeMinutes = 0;


        while (timeSeconds > 60){
            timeMinutes++;
            timeSeconds -= 60;
        }

        if (timeSeconds < 10)
            return timeMinutes + ":0" + timeSeconds;


        return timeMinutes + ":" + timeSeconds;
    }

    /**
     * returns the saved name asosiated whit the estimated pose
     * @return name of the estimated pose
     */
    public String getFeedback() {
       return name;
    }

    public String getShortFeedback() {
        return description;
    }

    @Override
    public String toString() {
        return "estimatedPose = " + estimatedPose;
    }
}
