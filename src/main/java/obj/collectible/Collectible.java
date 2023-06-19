package obj.collectible;

import obj.GameObj;
import obj.Takeable;
import util.Image;
import util.Level;
import static util.Const.*;
import static util.Const.GUI.*;

import org.json.JSONException;
import org.json.JSONObject;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * The abstract class for collectible objects.
 * Extends abstract Game object class.
 * Implements Takeable interface, because objects can be taken.
 */
public abstract class Collectible extends GameObj implements Takeable {
    protected boolean active; // is item active at the moment
    protected int animFrame, animIndex; // animation variables

    /**
     * Constructor for the Collectible.
     *
     * @param x         x-coordinate of collectible.
     * @param y         y-coordinate of collectible.
     */
    public Collectible(int x, int y, Level level) {
        super(x, y, level);
        level = null;
        active = true;
        hitbox = new Rectangle((int)(x-0.25*SPRITE), (int)(y-0.25*SPRITE), (int)(1.5*SPRITE), (int)(1.5*SPRITE));;
    }

    /**
     * Constructor for the Collectible.
     *
     * @param json  collectible object from JSON file.
     * @param level associated Level object.
     */
    public Collectible(JSONObject json, Level level) {
        super(json.getInt(JSON.X), json.getInt(JSON.Y), level);
        active = json.getInt(JSON.ACTIVE) == 1;
        hitbox = new Rectangle((int)(x-0.25*SPRITE), (int)(y-0.25*SPRITE), (int)(1.5*SPRITE), (int)(1.5*SPRITE));
    }

    /**
     * Renders the collectible on the screen.
     *
     * @param graphics Graphics object used to draw the collectible.
     */
    public void render(Graphics graphics) {
        if (active) graphics.drawImage(sprites[0][0][animIndex], (int)x, (int)y, SPRITE, SPRITE,null);
    }

    /**
     * Loads images of collectible object.
     *
     * @param type type of object (position in image file).
     */
    protected void loadSprites(int type) {
        BufferedImage tmp = Image.loadImage(ITEMS);
        sprites = new BufferedImage[1][1][4];
        for (int i = 0; i < sprites[0][0].length; ++i) {
            sprites[0][0][i] = tmp.getSubimage(i*GameObject.W,(type - 2) * GameObject.H, GameObject.W, GameObject.H);
        }
    }

    /**
     * Processes collectible animation.
     */
    protected void updateAnim() {
        animFrame++;
        if (animFrame >= FPS) {
            animFrame = 0;
            animIndex = animIndex == 3 ? 0 : animIndex + 1;
        }
    }

    /**
     * Controls if the object can be taken.
     *
     * @return true if object can be taken, false otherwise.
     */
    @Override
    public boolean isTaken() {
        Rectangle playerHitbox = level.getPlayer().getHitbox();
        double w = playerHitbox.getWidth(); // Player's hitbox width
        double h = playerHitbox.getHeight(); // Player's hitbox height

        boolean topL = hitbox.contains(playerHitbox.getX(), playerHitbox.getY()); // Player's top left corner is inside item hitbox
        boolean topR = hitbox.contains(playerHitbox.getX() + w, playerHitbox.getY()); // Player's top right corner is inside item hitbox
        boolean botL = hitbox.contains(playerHitbox.getX(), playerHitbox.getY() + h);  // Player's bottom left corner is inside item hitbox
        boolean botR = hitbox.contains(playerHitbox.getX() + w, playerHitbox.getY() + h);  // Player's bottom right corner is inside item hitbox

        return topR && topL && botR && botL;
    }

    /**
     * Returns if the collectible is active at the moment.
     *
     * @return true if collectible is active, false otherwise.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Makes JSON object from the collectible to write into the file.
     *
     * @return JSON object for writing
     */
    public JSONObject getJSON() {
        JSONObject json = new JSONObject();
        try {
            json.put(JSON.X, x);
            json.put(JSON.Y, y);
            if (active) json.put(JSON.ACTIVE, 1); else json.put(JSON.ACTIVE, 0);
        } catch (JSONException e) {
            level.getApp().getLogger().error("Unable to write JSON object!"); // Logging
        }

        return json;
    }
}
