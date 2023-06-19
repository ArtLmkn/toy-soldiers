package obj;

/**
 * Definition of methods which should have every game object that can move.
 */
public interface Movable {
    /**
     * Moves object to the left.
     */
    void moveLeft();

    /**
     * Moves object to the right.
     */
    void moveRight();

    /**
     * Moves object upwards.
     */
    void moveUp();

    /**
     * Moves object downwards.
     */
    void moveDown();
}
