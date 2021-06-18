package com.mygdx.honestmirror.application.domain.simulation;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class HelperClass {
//    private static ShapeRenderer debugRenderer = new ShapeRenderer();
//
//    public static void DrawDebugLine(Vector2 start, Vector2 end, int lineWidth, Color color, Matrix4 projectionMatrix)
//    {
//        Gdx.gl.glLineWidth(lineWidth);
//        debugRenderer.setProjectionMatrix(projectionMatrix);
//        debugRenderer.begin(ShapeRenderer.ShapeType.Line);
//        debugRenderer.setColor(color);
//        debugRenderer.line(start, end);
//        debugRenderer.end();
//        Gdx.gl.glLineWidth(1);
//    }

    //This function returns the distance between two coordinates(Vector2).
    public static float PythagorasTheorem(Vector2 coords1, Vector2 coords2){
        double side1 = Math.abs(coords1.x-coords2.x);
        double side2 = Math.abs(coords1.y-coords2.y);
        double length = Math.sqrt(Math.pow(side1, 2) + Math.pow(side2, 2));
        return (float)length;
    }

    //This function returns the angle between two coordinates(Vector2).
    public static float getAngle(Vector2 coords1, Vector2 coords2) {
        float angle = (float) Math.toDegrees(Math.atan2(coords1.y - coords2.y, coords1.x - coords2.x));

        if(angle < 0){
            angle += 360;
        }

        return angle - 90;
    }

    //This function returns the subtraction of two Vector3.
    public static Vector3 vec3Subtraction(Vector3 vec_1, Vector3 vec_2){
        Vector3 result = new Vector3();
        result.x = vec_1.x - vec_2.x;
        result.y = vec_1.y - vec_2.y;
        result.z = vec_1.z - vec_2.z;
        return result;
    }
}
