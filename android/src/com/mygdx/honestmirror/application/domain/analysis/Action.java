package com.mygdx.honestmirror.application.domain.analysis;

//Class used for defining actions we want to detect.
public class Action { 
    // Name of the action.
    // Constructor.
    Action(final String name) {
        this.name = name;
    }


    //Requests if the Action has occurred or not.
    boolean occurred() {
        return occurred;
    }


    //sets the occurrence.
    void setOccurrence(boolean occ) {
        occurred = occ;
    }

    //Getter for the Action's name.
    public String getName() { return name; }

    //Setter for the Action's name.
    public void setName(String new_name) { name = new_name; }

    //Name of the action.
    private String name;

    //If the action occurred or not.
    private Boolean occurred;
}