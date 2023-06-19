package obj.soldier;

import util.AI;
import util.Level;
import static util.Const.*;
import static util.Const.GUI.*;

import org.json.JSONObject;

/**
 * Class for enemies.
 * Extends abstract Soldier object class.
 * Controlled by Artificial Intelligence.
 * Must be killed to finish game.
 */
public class Enemy extends Soldier {
    private final AI ai;

    /**
     * Constructor for the Enemy.
     *
     * @param x         x-coordinate of enemy.
     * @param y         y-coordinate of enemy.
     */
    public Enemy(int x, int y, Level level) {
        super(x,y,level);
        ai = null;
        health = Limits.HEALTH_ENEMY;
        loadSprites(GameObject.ENEMY);
    }

    /**
     * Constructor for the Enemy.
     *
     * @param json  enemy object from JSON file.
     * @param level associated Level object.
     */
    public Enemy(JSONObject json, Level level) {
        super(json, level);
        ai = new AI(level, this);
        loadSprites(GameObject.ENEMY);
    }

    /**
     * Updates player position and its state.
     */
    public void update() {
        if (!dead) {
            ai.update();
            if (health == 0) { // Enemy was killed
                walking = false;
                shooting = false;
                dead = true;
                level.setEnemiesCount(level.getEnemiesCount()-1);
                if (level.getApp() != null) level.getApp().getAudio().playSound(Sounds.DIE);
                if (level.getApp() != null) level.getApp().getLogger().game("Enemy " + this + " was killed. Enemies remained: " + level.getEnemiesCount() + "."); // Logging
            }
        }
        updateAnim();
    }
}
