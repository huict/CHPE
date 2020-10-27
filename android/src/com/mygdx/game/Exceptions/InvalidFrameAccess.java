package com.mygdx.game.Exceptions;

import androidx.annotation.NonNull;

/**
 * The type Invalid frame access.
 * Used in the UriLegacy.
 * Thrown when a frame is accessed by time and no frame is found.
 */
public class InvalidFrameAccess extends Exception {

    /**
     * The Message.
     */
    private String message;
    /**
     * The Cause.
     */
    private Throwable cause;

    /**
     * Instantiates a new Invalid frame access.
     */
    public InvalidFrameAccess() {
        super();
    }

    /**
     * Instantiates a new Invalid frame access.
     *
     * @param message the message
     * @param cause   the cause for the exception.
     */
    public InvalidFrameAccess(String message, Throwable cause) {
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
