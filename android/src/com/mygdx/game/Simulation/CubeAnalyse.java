package com.mygdx.game.Simulation;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Analysis.Data;
import com.mygdx.game.PoseEstimation.nn.PoseModels.NNModelMPI;

public class CubeAnalyse {
    float data_scale = -25;
    public Cube cube_left_hip = new Cube(new Vector3(-0.5555555f,0.2734378f,0.0f), 1f, 1f, 1f, Color.RED);
    public Cube cube_right_hip = new Cube(new Vector3(0.5555555f,0.2734378f,0.0f), 1f, 1f, 1f, Color.RED);
    public Cube cube_left_head = new Cube(new Vector3(-2.5555555f,4.5f,0.0f), 1f, 1f, 1f, Color.RED);
    public Cube cube_right_head = new Cube(new Vector3(2.5555555f + 1,4.5f,0.0f), 1f, 1f, 1f, Color.RED);

    public CubeAnalyse(){}

    public Array<ModelInstance> renderables(){
        Array<ModelInstance> result = new Array<>();
        result.add(cube_left_hip.getInstance());
        result.add(cube_right_hip.getInstance());
        result.add(cube_left_head.getInstance());
        result.add(cube_right_head.getInstance());
        return result;
    }

    public boolean collision(Cube cube, Data data, NNModelMPI.body_part bp, int frame){
        Vector3 joint_data = new Vector3((data.getCoord(frame, bp).x * -data_scale - (data.getCoord(frame, NNModelMPI.body_part.values()[NNModelMPI.body_part.waist.ordinal()]).x * -data_scale)),
                (data.getCoord(frame, bp).y * data_scale - (data.getCoord(frame, NNModelMPI.body_part.values()[NNModelMPI.body_part.waist.ordinal()]).y * data_scale)),
                (data.getCoord(frame, bp).z * data_scale - (data.getCoord(frame, NNModelMPI.body_part.values()[NNModelMPI.body_part.waist.ordinal()]).z * data_scale)));

        //todo separate =)
        return(cube.coords_.x < joint_data.x + 1f &&
                cube.coords_.x + cube.width_ > joint_data.x &&
                cube.coords_.y < joint_data.y + 1f &&
                cube.coords_.y + cube.height_ > joint_data.y);


    }

    public void update(Data data, int frame){
        // left hand head collision
        if(collision(cube_left_head, data, NNModelMPI.body_part.values()[NNModelMPI.body_part.l_wrist.ordinal()], frame)){
            cube_left_head.setColor(Color.GREEN);
        } else {
            cube_left_head.setColor(Color.RED);
        }
        // Right hand head collision
        if(collision(cube_right_head, data, NNModelMPI.body_part.values()[NNModelMPI.body_part.r_wrist.ordinal()], frame)){
            cube_right_head.setColor(Color.GREEN);
        } else {
            cube_right_head.setColor(Color.RED);
        }
        // Left hand hip collision
        if(collision(cube_left_hip, data, NNModelMPI.body_part.values()[NNModelMPI.body_part.l_wrist.ordinal()], frame)){
            cube_left_hip.setColor(Color.GREEN);
        } else {
            cube_left_hip.setColor(Color.RED);
        }
        // Right hand hip collision
        if(collision(cube_left_head, data, NNModelMPI.body_part.values()[NNModelMPI.body_part.r_wrist.ordinal()], frame)){
            cube_right_hip.setColor(Color.GREEN);
        } else {
            cube_right_hip.setColor(Color.RED);
        }
    }
}
