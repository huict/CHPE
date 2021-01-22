package com.mygdx.honestmirror.view.ui;

//Describes a single result card that the app shows as analysis result.
public class c_ResultCard {
    //Thumbnail image. Int because it's a view id.
    private int image;

    // Title and description of the result.
    private String title, description;

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
