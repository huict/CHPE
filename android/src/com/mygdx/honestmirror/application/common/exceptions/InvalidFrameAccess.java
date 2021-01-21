package com.mygdx.honestmirror.application.common.exceptions;

import androidx.annotation.NonNull;

// The type Invalid frame access.Used in the UriLegacy.
// Thrown when a frame is accessed by time and no frame is found.

public class InvalidFrameAccess extends Exception {

    private String message;

    private Throwable cause;


    //Instantiates a new Invalid frame access.
    public InvalidFrameAccess() {
        super();
    }


    //Instantiates a new Invalid frame access.
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
