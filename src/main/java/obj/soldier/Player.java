package obj.soldier;

import util.Level;
import static util.Const.*;
import static util.Const.Soldier.*;
import static util.Const.GUI.*;

import org.json.JSONObject;

/**
 * Class for player.
 * Extends abstract Soldier object class.
 * Controlled by user.
 */
public class Player extends Soldier {

    /**
     * Constructor for the Player.
     *
     * @param x         x-coordinate of player.
     * @param y         y-coordinate of player.
     */
    public Player(int x, int y, Level level) {
        super(x, y, level);
        health = 3;
        ammo = 30;
        loadSprites(GameObject.PLAYER);
    }

    /**
     * Constructor for the Player.
     *
     * @param json  player object from JSON file.
     * @param level associated Level object.
     */
    public Player(JSONObject json, Level level) {
        super(json, level);
        ammo = json.getInt(JSON.AMMO);
        loadSprites(GameObject.PLAYER);
    }

    /**
     * Updates player position and its state.
     */
    public void update() {
        if (health == 0) { // Player was killed
            dead = true;
            if (level.getApp() != null) level.getApp().getLogger().game("Player " + level.getPlayer() + " was killed."); // Logging
        }

        if (walking) {
            if (up) moveUp();
            else if (down) moveDown();
            else if (left) moveLeft();
            else if (right) moveRight();
        }
        updateAnim();
    }

    /**
     * Makes a shot.
     * Method creates Bullet object in the actual level.
     * Respects actual ammo amount.
     */
    public void shoot() {
        if (ammo != 0) {
            super.shoot();
            ammo--;
        } else {
            if (level.getApp() != null) level.getApp().getAudio().playSound(Sounds.EMPTY);
        }
    }
}
