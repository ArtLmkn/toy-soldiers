package obj.collectible;

import util.Level;
import static util.Const.*;
import static util.Const.GUI.*;

import org.json.JSONObject;

/**
 * Class for documents.
 * Extends abstract Collectible object class.
 * Must be collected to finish game.
 */
public class Doc extends Collectible {
    /**
     * Constructor for the Doc.
     *
     * @param x         x-coordinate of document.
     * @param y         y-coordinate of document.
     */
    public Doc(int x, int y, Level level) {
        super(x, y, level);
        loadSprites(GameObject.DOC);
    }

    /**
     * Constructor for the Doc.
     *
     * @param json  document object from JSON file.
     * @param level associated Level object.
     */
    public Doc(JSONObject json, Level level) {
        super(json, level);
        loadSprites(GameObject.DOC);
    }

    /**
     * Updates document's state.
     */
    public void update() {
        if (active && isTaken()) affect();
        updateAnim();
    }

    /**
     * Processes effect from document taking.
     */
    @Override
    public void affect() {
        if (isTaken()) {
            level.setDocsCount(level.getDocsCount()-1);
            active = false;
            if (level.getApp() != null) level.getApp().getAudio().playSound(Sounds.DOC);
            if (level.getApp() != null) level.getApp().getLogger().game("Document " + this + " was taken. Documents remained: " + level.getDocsCount() + "."); // Logging
        }
    }
}
