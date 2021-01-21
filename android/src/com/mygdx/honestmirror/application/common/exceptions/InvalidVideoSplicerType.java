package com.mygdx.honestmirror.application.common.exceptions;

import androidx.annotation.NonNull;


//The type Invalid video splicer type.
//Thrown when the VideoSplicerFactory encounters an SDK version that is unsupported.
public class InvalidVideoSplicerType extends Exception {
    String message;
    Throwable cause;


    //Instantiates a new Invalid video splicer type.
    public InvalidVideoSplicerType() {
        super();
    }

    //Instantiates a new Invalid video splicer type.
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


