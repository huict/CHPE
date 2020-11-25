package com.mygdx.honestmirror.application.domain.analysis;

/**
 * @author Nico van Bentum
 * Class used for defining actions we want to detect.
 * TODO: Should become a more sophisticated object people can implement and tie attributes to.
 */
public class Action { 
    /**
     * @param name Name of the action.
     * Constructor.
     */
    Action(final String name) {
        this.name = name;
    }

    /**
     * Requests if the Action has occurred or not.
     * @return Whether the Action occurred or not.
     */
    boolean occurred() {
        return occurred;
    }

    /**
     * Sets the occurrence.
     * @param occ New occurrence to set.
     */
    void setOccurrence(boolean occ) {
        occurred = occ;
    }

    /**
     * Getter for the Action's name.
     * @return String containing the Action's name.
     */
    public String getName() { return name; }

    /**
     * Setter for the Action's name.
     * @param new_name new name
     */
    public void setName(String new_name) { name = new_name; }

    /**
     * Name of the action.
     */
    private String name;

    /**
     * If the action occurred or not.
     */
    private Boolean occurred;
}