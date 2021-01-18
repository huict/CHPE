package com.mygdx.honestmirror.application.domain.feedback.settings;

public class OccurrenceOverTime {
    private int timeSeconds;
    private int occurrence;
    private double framerate;

    public OccurrenceOverTime(int timeSeconds, int occurrence, double framerate) {
        this.timeSeconds = timeSeconds;
        this.occurrence = occurrence;
        this.framerate = framerate;
    }

    public int getTimeSeconds() {
        return timeSeconds;
    }

    public int getOccurrence() {
        return occurrence;
    }

    public double getFramerate() {
        return framerate;
    }
}
