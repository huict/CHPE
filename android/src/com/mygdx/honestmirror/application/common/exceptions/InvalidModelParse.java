package com.mygdx.honestmirror.application.common.exceptions;


import androidx.annotation.NonNull;

//The type Invalid model parse.
//Thrown when an invalid ID is given to ModelFactory
public class InvalidModelParse extends Exception {
    String message;
    Throwable cause;

     //Instantiates a new Invalid model parse.
     public InvalidModelParse() {
        super();
    }


    //Instantiates a new Invalid model parse.
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

