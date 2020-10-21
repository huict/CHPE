package com.mygdx.game.Analysis;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This class describes the tests for the Action class.
 */
public class ActionTest {

    /**
     * Tests the setters and getters for the Action name.
     */
    @Test
    public void NameTest() {
        Action action = new Action("action");
        String action_name = action.getName();
        assertEquals(action.getName(), action_name);
    }

    /**
     * Tests the setters and getters for the Action occurrence.
     */
    @Test
    public void OccurrenceTest() {
        Action action = new Action("action");
        action.setOccurrence(true);
        assertTrue(action.occurred());
    }
}
