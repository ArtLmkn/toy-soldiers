package obj.collectible;

import util.Level;
import static util.Const.*;
import static util.Const.GUI.*;

import org.json.JSONObject;

/**
 * Class for ammo boxes.
 * Extends abstract Collectible object class.
 * Gives additional ammo to the player.
 */
public class AmmoBox extends Collectible {
    /**
     * Constructor for the AmmoBox.
     *
     * @param x         x-coordinate of ammo box.
     * @param y         y-coordinate of ammo box.
     */
    public AmmoBox(int x, int y, Level level) {
        super(x, y, level);
        loadSprites(GameObject.AMMO);
    }

    /**
     * Constructor for the AmmoBox.
     *
     * @param json  ammo box position object from JSON file.
     * @param level associated Level object.
     */
    public AmmoBox(JSONObject json, Level level) {
        super(json, level);
        loadSprites(GameObject.AMMO);
    }

    /**
     * Updates ammo box's state.
     */
    public void update() {
        if (active && isTaken()) affect();
        updateAnim();
    }

    /**
     * Processes effect from ammo box taking.
     */
    @Override
    public void affect() {
        int tmpAmmo = level.getPlayer().getAmmo(); // Current player ammo count
        if (tmpAmmo != Limits.AMMO_MAX) { // Player doesn't take box if he doesn't need it
            tmpAmmo = Math.min(tmpAmmo + Limits.AMMOBOX_EFFECT, Limits.AMMO_MAX); // Add box capacity (respectfully to max count)
            level.getPlayer().setAmmo(tmpAmmo);
            active = false;
            if (level.getApp() != null) level.getApp().getAudio().playSound(Sounds.AMMO);
            if (level.getApp() != null) level.getApp().getLogger().game("Ammo box " + this + " was taken. Current ammo count: " + level.getPlayer().getAmmo() + "."); // Logging
        }
    }
}
