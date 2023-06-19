package state;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Definition of methods which every game state should have.
 */
public interface StateInterface {
    /**
     * Updates the state.
     * Respects actual game stage.
     */
    void update();

    /**
     * Renders the state on the screen.
     * Respects actual game stage.
     *
     * @param graphics Graphics object used to draw the state.
     */
    void render(Graphics graphics);

    /**
     * Handles mouse presses.
     * Respects actual game stage.
     *
     * @param e the MouseEvent object representing the mouse press event.
     */
    void mousePressed(MouseEvent e);

    /**
     * Handles mouse releases.
     * Respects actual game stage.
     *
     * @param e the MouseEvent object representing the mouse release event.
     */
    void mouseReleased(MouseEvent e);

    /**
     * Handles mouse movement.
     * Respects actual game stage.
     *
     * @param e the MouseEvent object representing the mouse movement event.
     */
    void mouseMoved(MouseEvent e);

    /**
     * Handles key presses.
     * Respects actual game stage.
     *
     * @param e the KeyEvent object representing the key press.
     */
    void keyPressed(KeyEvent e);

    /**
     * Handles key releases.
     * Respects actual game stage.
     *
     * @param e the KeyEvent object representing the key release.
     */
    void keyReleased(KeyEvent e);
}
