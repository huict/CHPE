package com.mygdx.honestmirror.application.domain.simulation;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

//This class gives you a sphere on a given coordinate.
//The created BodyPart objects in the Body class are used as body parts for the representation of the body.
public class BodyPart {
    Vector3 coords_;
    float size_, rotation_, data_scale_;
    Color color_;
    ModelInstance sphere_;
    ModelBuilder modelBuilder_ = new ModelBuilder();
    Model model_;

    //This is the BodyPart constructor.
    public BodyPart(Vector3 coords, float size, Color color){
        coords_ = coords;
        size_ = size;
        color_ = color;
        model_ = modelBuilder_.createSphere(size, size, size, 20, 20,
                new Material(),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);
        sphere_ = new ModelInstance(model_);
        sphere_.transform.setToTranslation(coords_);
        sphere_.transform.rotate(Vector3.Z, rotation_);
        sphere_.materials.get(0).set(ColorAttribute.createDiffuse(color_));
    }

    //This function can be used to change the BodyPart object color.
    public void change_color(Color color){
        color_ = color;
        sphere_.materials.get(0).set(ColorAttribute.createDiffuse(color_));
    }

    //This function can be used to change the object scale.
    //IMPORTANT: Do NOT use scale before translating and rotating.
    //Scaling must always be done as last.
    //The order of transformations is important, translate -> rotate -> scale.
    public void set_scale(float data_scale){
        data_scale_ = data_scale;
        sphere_.transform.scale(data_scale_, data_scale_, data_scale_);
    }

    //his function returns the actual ModelInstance of the BodySphere object.
    //return Sphere ModelInstance
    public ModelInstance getInstance(){
        return sphere_;
    }

    //The object (sphere) will be translated to the given coordinate.
    public void update(Vector3 coords){
        coords_ = coords;
        sphere_.transform.setToTranslation(coords_);
    }
}
