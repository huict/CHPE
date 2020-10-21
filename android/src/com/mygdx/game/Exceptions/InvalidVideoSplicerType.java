package com.mygdx.game.Exceptions;

import androidx.annotation.NonNull;

/**
 * The type Invalid video splicer type.
 * Thrown when the VideoSplicerFactory encounters an SDK version that is unsupported.
 */
public class InvalidVideoSplicerType extends Exception {
    /**
     * The Message.
     */
    String message;
    /**
     * The Cause.
     */
    Throwable cause;

    /**
     * Instantiates a new Invalid video splicer type.
     */
    public InvalidVideoSplicerType() {
        super();
    }

    /**
     * Instantiates a new Invalid video splicer type.
     *
     * @param message the message
     * @param cause   the cause
     */
    public InvalidVideoSplicerType(String message, Throwable cause) {
        super(message, cause);

        this.cause = cause;
        this.message = message;
    }
    @NonNull
    @Override
    public String toString() {
        return cause.toString() + " " + message;
    }
}


