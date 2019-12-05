package com.mygdx.game.Analysis;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;

import com.mygdx.game.PoseEstimation.nn.MPI.body_part;
import com.badlogic.gdx.math.Vector3;

/**
 * @author Nico van Bentum
 * This class implements the Data interface, it retrieves the vector data
 * from a JSON file generated by a neural network based Python application.
 */
public class JSONData implements Data {
    /**
     * A JSON specific array that holds every frame's data
     */
    private JSONArray frames;
    /**
     * Location of the JSON file on disk.
     */
    private String filepath;

    /**
     * Constructor that inits member fields thus loading the data from disk.
     * @param fp File path to the JSON file on disk.
     */
    JSONData(String fp) {
        filepath = fp;
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader(filepath))
        {
            //Read JSON file
            Object obj = jsonParser.parse(reader);
            frames = (JSONArray) obj;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Constructor that grabs the frame data from a JSONLoader.
     * @param loader External JSONLoader object.
     */
    JSONData(JSONLoader loader) {
        frames = loader.getArray();
    }

    /**
     * Implements Data's interface function for getting a single coordinate using Java's JSON library.
     */
    public Vector3 getCoord(int frame, body_part bp) {
        JSONObject bodyparts = (JSONObject) frames.get(frame);
        JSONArray coords = (JSONArray) bodyparts.get(bp.toString());

        Number x = (Number) coords.get(0);
        Number y = (Number) coords.get(1);
        return new Vector3(x.floatValue(), y.floatValue(), 0.0f);
    }

    /**
     * Implements Data's interface for setting the x component of a coordinate.
     */
    public void setX(int frame, body_part bp, double x) {
        JSONObject bodyparts = (JSONObject) frames.get(frame);
        JSONArray coords = (JSONArray) bodyparts.get(bp.toString());
        coords.set(0, x);
    }

    /**
     * Implements Data's interface for setting the y component of a coordinate.
     */
    public void setY(int frame, body_part bp, double y) {
        JSONObject bodyparts = (JSONObject) frames.get(frame);
        JSONArray coords = (JSONArray) bodyparts.get(bp.toString());
        coords.set(1, y);
    }

    /**
     * Implements Data's interface function for retrieving the number of body parts
     * using a hardcoded enum.
     */
    public int getBodyPartCount() {
        return body_part.values().length;
    }

    /**
     * Implements Data's interface function for getting the frame count using the read JSON data.
     */
    public int getFrameCount() {
        return frames.size();
    }
    
    /**
     * Implements Data's interface function for getting the number of frames per second used.
     * hardcoded for now.
     */
    public float getFps() {
        return 24;
    }

    /**
     * Implements Data's interface for writing the data back to the data structure.
     * Does nothing for now.
     */
    public void serialize() {}

}