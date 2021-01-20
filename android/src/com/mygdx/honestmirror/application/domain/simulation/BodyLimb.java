package com.mygdx.honestmirror.application.domain.simulation;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * This class gives you a cylinder between two given coordinates.
 * The created BodyLimb objects in the Body class are used as body limbs for the representation of the body.
 */
public class BodyLimb {
    Vector2 coords1_ , coords2_;
    float limbDiameter_;
    float length_;
    float rotation_;
    float z_;
    float scale_;
    Color color_;
    ModelInstance cylinder_;
    ModelBuilder modelBuilder_ = new ModelBuilder();
    Model model_;

    /**
     * This is the BodyLimb constructor.
     * @param coords1
     * @param coords2
     * @param limbdiameter
     * @param z
     * @param color
     */
    public BodyLimb(Vector2 coords1, Vector2 coords2, float limbdiameter, float z, Color color){
        coords1_ = coords1;
        coords2_ = coords2;
        z_ = z;
        limbDiameter_ = limbdiameter;
        length_ = HelperClass.PythagorasTheorem(coords1_, coords2_);
        rotation_ = HelperClass.getAngle(coords1_, coords2_);
        color_ = color;
        model_ = modelBuilder_.createCylinder(limbDiameter_, length_, limbDiameter_, 20,
                new Material(),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);
        cylinder_ = new ModelInstance(model_);
        cylinder_.transform.setToTranslation(coords1_.x - (0.5f * (coords1_.x - coords2_.x)), coords1_.y - (0.5f * (coords1_.y - coords2_.y)), z_);
        cylinder_.transform.rotate(Vector3.Z, rotation_);
        cylinder_.materials.get(0).set(ColorAttribute.createDiffuse(color_));
    }

    /**
     * This function returns the actual ModelInstance of the BodyLimb object.
     * @return Cylinder ModelInstance.
     */
    public ModelInstance getInstance(){
        return cylinder_;
    }

    /**
     * This function updates the BodyLimb object.
     * The object (cylinder) will be translated, rotated and scaled according to the given coordinates.
     * @param coords1
     * @param coords2
     * @param z
     */
    public void update(Vector2 coords1, Vector2 coords2, float z){
        coords1_ = coords1;
        coords2_ = coords2;
        z_ = z;
        float new_length = HelperClass.PythagorasTheorem(coords1, coords2);
        scale_ = new_length / length_;
        length_ = new_length;
        rotation_ = HelperClass.getAngle(coords1_, coords2_);
        cylinder_.transform.setToTranslation(coords1_.x - (0.5f * (coords1_.x - coords2_.x)), coords1_.y - (0.5f * (coords1_.y - coords2_.y)), z_);
        cylinder_.transform.rotate(Vector3.Z, rotation_);
        cylinder_.transform.scale(1f,scale_, 1f);
    }
}
