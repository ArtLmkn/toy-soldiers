package obj.collectible;

import util.Level;
import util.Image;
import static util.Const.GUI.*;

import org.json.JSONObject;
import java.awt.image.BufferedImage;

/**
 * Class for finish position.
 * Extends abstract Collectible object class.
 * Must be visited to finish game.
 */
public class Finish extends Collectible {
    /**
     * Constructor for the Finish.
     *
     * @param x         x-coordinate of finish position.
     * @param y         y-coordinate of finish position.
     */
    public Finish(int x, int y, Level level) {
        super(x, y, level);
        animIndex = 0;
        sprites = new BufferedImage[1][1][1];
        sprites[0][0][0] = Image.loadImage(BLOCKS).getSubimage(0, 1*GameObject.W, GameObject.W, GameObject.H);
    }

    /**
     * Constructor for the Finish.
     *
     * @param json  finish position object from JSON file.
     * @param level associated Level object.
     */
    public Finish(JSONObject json, Level level) {
        super(json, level);
        animIndex = 0;
        sprites = new BufferedImage[1][1][1];
        sprites[0][0][0] = Image.loadImage(BLOCKS).getSubimage(0, 1*GameObject.W, GameObject.W, GameObject.H);
    }

    @Override
    public void affect() {
        // Effect of standing on finish position calculates in Game state
    }
}
