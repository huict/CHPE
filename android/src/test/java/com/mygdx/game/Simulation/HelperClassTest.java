package com.mygdx.honestmirror.application.domain.simulation;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HelperClassTest {

    @Test
    public void test() {
        Vector2 a = new Vector2(0f, 3f);
        Vector2 b = new Vector2(4f, 0f);
        float c = HelperClass.PythagorasTheorem(a, b);
        assertEquals(c, 5f, 0f);
    }

    public void before() {
    }

    @After
    public void after() {
    }

    /**
     * This function is a unit test for the PythagorasTheorem() function from the HelperClass Class.
     */
    @Test
    public void test_PythagorasTheorem() {
        Vector2 a = new Vector2(0f, 3f);
        Vector2 b = new Vector2(4f, 0f);
        float c = HelperClass.PythagorasTheorem(a, b);
        assertEquals(5f, c, 0f);
    }

    /**
     * This function is a unit test for the getAngle() function from the HelperClass Class.
     */
    @Test
    public void test_getAngle() {
        Vector2 a = new Vector2(200f, 300f);
        Vector2 b = new Vector2(300f, 200f);
        float c = HelperClass.getAngle(a, b);
        assertEquals(45, c, 0f);
    }

    /**
     * This function is a unit test for the vec3Subtraction() function from the HelperClass Class.
     */
    @Test
    public void test_vec3Subtraction() {
        Vector3 a = new Vector3(100f, 200f, 300f);
        Vector3 b = new Vector3(300f, 200f, 100f);
        Vector3 c = HelperClass.vec3Subtraction(a, b);
        assertEquals(new Vector3(-200f, 0f, 200), c);
    }
}