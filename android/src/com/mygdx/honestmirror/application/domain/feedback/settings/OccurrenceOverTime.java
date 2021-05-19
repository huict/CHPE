package com.mygdx.honestmirror.application.domain.feedback.settings;

/**
 * keeps track of how often a pose occurs... seems unused?
 */
public class OccurrenceOverTime {
    private final int timeSeconds;
    private final int occurrence;
    private final double framerate;

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
