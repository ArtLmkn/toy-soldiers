package obj;

import util.Level;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * The abstract base class for all game objects.
 */
public abstract class GameObj {
    protected final Level level; // associated level
    protected float x, y; // object coordinates
    protected BufferedImage[][][] sprites; // object images (with animation)
    protected Rectangle hitbox; // interaction area

    /**
     * Constructor for the game object.
     *
     * @param x  x-coordinate of object.
     * @param y  y-coordinate of object.
     */
    public GameObj(int x, int y, Level level) {
        setPos(x, y);
        this.level = level;
    }


    /**
     * Sets new position for the game object.
     *
     * @param x  x-coordinate of object.
     * @param y  y-coordinate of object.
     */
    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns horizontal coordinate of object.
     *
     * @return  x-coordinate of object.
     */
    public float getX() {
        return x;
    }

    /**
     * Returns vertical coordinate of object.
     *
     * @return  y-coordinate of adding object.
     */
    public float getY() {
        return y;
    }

    /**
     * Returns the object interaction hitbox.
     *
     * @return object interaction hitbox.
     */
    public Rectangle getHitbox() {
        return hitbox;
    }

    /**
     * Sets new interaction area for object.
     *
     * @param hitbox interaction area.
     */
    public void setHitbox(Rectangle hitbox) {
        this.hitbox = hitbox;
    }

    /**
     * Returns string representation of object.
     *
     * @return  string representation of object.
     */
    @Override
    public String toString() {
        return Integer.toHexString(hashCode());
    }
}
