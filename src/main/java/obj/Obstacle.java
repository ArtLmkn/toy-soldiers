package obj;

import util.Image;
import static util.Const.*;
import static util.Const.GUI.*;

import org.json.JSONException;
import org.json.JSONObject;
import util.Level;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Class for obstacles (boxes) object.
 * Extends abstract Game object class.
 */
public class Obstacle extends GameObj {

    /**
     * Constructor for the Obstacle.
     *
     * @param x         x-coordinate of bullet.
     * @param y         y-coordinate of bullet.
     */
    public Obstacle(int x, int y, Level level) {
        super(x, y, level);
        sprites = new BufferedImage[1][1][1];
        sprites[0][0][0] = Image.loadImage(BLOCKS).getSubimage(0, 0, GameObject.W, GameObject.H);
        hitbox = new Rectangle(x, y, SPRITE, SPRITE);
    }

    /**
     * Constructor for the Obstacle.
     *
     * @param json  obstacle object from JSON file.
     */
    public Obstacle(JSONObject json, Level level) {
        super(json.getInt(JSON.X), json.getInt(JSON.Y), level);
        hitbox = new Rectangle((int)x, (int)y, SPRITE, SPRITE);
        sprites = new BufferedImage[1][1][1];
        sprites[0][0][0] = Image.loadImage(BLOCKS).getSubimage(0, 0, GameObject.W, GameObject.H);
    }

    /**
     * Renders the obstacle on the screen.
     *
     * @param graphics Graphics object used to draw the obstaclet.
     */
    public void render(Graphics graphics) {
        graphics.drawImage(sprites[0][0][0], (int)x, (int)y, SPRITE, SPRITE, null);
    }

    /**
     * Makes JSON object from the obstacle to write into the file.
     *
     * @return JSON object for writing
     */
    public JSONObject getJSON() {
        JSONObject json = new JSONObject();
        try {
            json.put(JSON.X, x);
            json.put(JSON.Y, y);
        } catch (JSONException e) {
            level.getApp().getLogger().error("Unable to write JSON object!"); // Logging
        }

        return json;
    }
}
