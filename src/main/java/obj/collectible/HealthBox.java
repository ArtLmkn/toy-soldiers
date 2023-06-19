package obj.collectible;

import util.Level;
import static util.Const.*;
import static util.Const.GUI.*;

import org.json.JSONObject;

/**
 * Class for health boxes.
 * Extends abstract Collectible object class.
 * Gives additional health to the player.
 */
public class HealthBox extends Collectible {
    /**
     * Constructor for the HealthBox.
     *
     * @param x         x-coordinate of health box.
     * @param y         y-coordinate of health box.
     */
    public HealthBox(int x, int y, Level level) {
        super(x, y, level);
        loadSprites(GameObject.HEALTH);
    }

    /**
     * Constructor for the HealthBox.
     *
     * @param json  health box position object from JSON file.
     * @param level associated Level object.
     */
    public HealthBox(JSONObject json, Level level) {
        super(json, level);
        loadSprites(GameObject.HEALTH);
    }

    /**
     * Updates health box's state.
     */
    public void update() {
        if (active && isTaken()) affect();
        updateAnim();
    }

    /**
     * Processes effect from health box taking.
     */
    @Override
    public void affect() {
        int tmpHealth = level.getPlayer().getHealth();  // Current player health amount
        if (tmpHealth != Limits.HEALTH_MAX) { // Player doesn't take box if he doesn't need it
            tmpHealth = Math.min(tmpHealth + Limits.HEALTHBOX_EFFECT, Limits.HEALTH_MAX); // Add box capacity (respectfully to max count)
            level.getPlayer().setHealth(tmpHealth);
            active = false;
            if (level.getApp() != null) level.getApp().getAudio().playSound(Sounds.HEALTH);
            if (level.getApp() != null) level.getApp().getLogger().game("Health box " + this + " was taken. Current health amount: " + level.getPlayer().getHealth() + "."); // Logging
        }
    }
}
