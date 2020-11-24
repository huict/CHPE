package com.mygdx.honestmirror.application.domain.simulation;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

public class Cube {
    Vector3 coords_;
    public float width_, height_, depth_;
    public Color color_;
    ModelInstance cube_;
    ModelBuilder modelBuilder_ = new ModelBuilder();
    Model model_;

    public Cube(Vector3 coords, float width, float height, float depth, Color color){
        coords_ = coords;
        width_ = width;
        height_ = height;
        depth_ = depth;
        color_ = color;
        model_ = modelBuilder_.createBox(width_, height_, depth_, new Material(),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);
        cube_ = new ModelInstance(model_);
        cube_.transform.setToTranslation(coords_);
        cube_.materials.get(0).set(ColorAttribute.createDiffuse(color_));
    }

    public void setColor(Color color){
        color_ = color;
        cube_.materials.get(0).set(ColorAttribute.createDiffuse(color_));
    }

    public ModelInstance getInstance(){return cube_;}

    public void update(Vector3 coords){
        coords_ = coords;
        cube_.transform.setToTranslation(coords_);
    }
}
