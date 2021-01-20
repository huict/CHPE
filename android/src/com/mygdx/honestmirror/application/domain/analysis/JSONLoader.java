package com.mygdx.honestmirror.application.domain.analysis;

import com.mygdx.honestmirror.application.common.DebugLog;

import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;

/**
 * @author Nico van Bentum
 * This class implements the Data interface, it retrieves the vector data
 * from a JSON file generated by a neural network based Python application.
 */
public class JSONLoader {
    /**
     * A JSON specific array that holds every frame's data
     */
    private JSONArray frames;

    /**
     * Constructor that loads the json data from a reader object.
     * this is mostly used for reading a json file through the android filesystem.
     * @param r Reader object.
     */
    public JSONLoader(Reader r) {
        JSONParser jsonParser = new JSONParser();

        try {
            //Read JSON file
            Object obj = jsonParser.parse(r);
            frames = (JSONArray) obj;
        } catch (FileNotFoundException e) {
            DebugLog.log("Unable to process Reader");
            e.printStackTrace();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

    }

    /**
     * Converts the data back to the original json string.
     * @return json string.
     */
    @NotNull
    public String toString() {
        return frames.toString();
    }

    /**
     * Returns the number of total frames.
     * @return The number of total frames.
     */
    public int getFrameCount() { return frames.size(); }

    /**
     * Getter for the underlying JSON array object.
     * @return json array object.
     */
    public JSONArray getArray() { return frames; }

}