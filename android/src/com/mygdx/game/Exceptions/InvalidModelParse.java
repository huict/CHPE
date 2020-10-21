package com.mygdx.game.Exceptions;


import androidx.annotation.NonNull;

/**
 * The type Invalid model parse.
 * Thrown when an invalid ID is given to ModelFactory
 */
public class InvalidModelParse extends Exception {

    /**
     * The Message.
     */
    String message;
    /**
     * The Cause.
     */
    Throwable cause;

    /**
     * Instantiates a new Invalid model parse.
     */
    public InvalidModelParse() {
        super();
    }

    /**
     * Instantiates a new Invalid model parse.
     *
     * @param message the message
     * @param cause   the cause
     */
    public InvalidModelParse(String message, Throwable cause) {
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

