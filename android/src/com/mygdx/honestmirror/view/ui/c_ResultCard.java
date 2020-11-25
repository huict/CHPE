package com.mygdx.honestmirror.view.ui;

/**
 * Describes a single result card that the app shows as analysis result.
 */
public class c_ResultCard {
    /**
     * Thumbnail image. Int because it's a view id.
     */
    private int image;

    /**
     * Title and description of the result.
     */
    private String title, description;

    /**
     * Image getter.
     * @return returns the thumbnail.
     */
    public int getImage() {
        return image;
    }

    /**
     * sets the thumbnail.
     * @param image
     */
    public void setImage(int image) {
        this.image = image;
    }

    /**
     * Title getter.
     * @return card title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * sets card title.
     * @param title title to set.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the card description.
     * @return description string.
     */
    public String getDescription() {
        return description;
    }

    /**
     * sets the card description.
     * @param description description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
