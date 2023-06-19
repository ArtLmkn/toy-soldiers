package util;

import main.Window;
import state.States;

import java.awt.event.*;

/**
 * Class for handling mouse and keyboard inputs for the game.
 * Implements KeyListener, MouseListener, and MouseMotionListener interfaces.
 */
public class Input implements KeyListener, MouseListener, MouseMotionListener {
    private final Window window; // associated game window

    /**
     * Constructor for the Input object.
     *
     * @param window  associated game window to listen inputs.
     */
    public Input(Window window) {
        this.window = window;
    }

    // Keyboard inputs

    /**
     * Handles key presses.
     * Forwards the event to the appropriate state.
     *
     * @param e the KeyEvent object representing the key press.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        switch (States.state) {
            case MENU -> window.getApp().getMenu().keyPressed(e);
            case TUTORIAL -> window.getApp().getTutorial().keyPressed(e);
            case LOAD -> window.getApp().getLoad().keyPressed(e);
            case GAME -> window.getApp().getGame().keyPressed(e);
            case EDITOR -> window.getApp().getEditor().keyPressed(e);
        }
    }

    /**
     * Handles key releases.
     * Forwards the event to the appropriate state.
     *
     * @param e the KeyEvent object representing the key release.
     */
    @Override
    public void keyReleased(KeyEvent e) {
        switch (States.state) {
            case MENU -> window.getApp().getMenu().keyReleased(e);
            case TUTORIAL -> window.getApp().getTutorial().keyReleased(e);
            case LOAD -> window.getApp().getLoad().keyReleased(e);
            case GAME -> window.getApp().getGame().keyReleased(e);
            case EDITOR -> window.getApp().getEditor().keyReleased(e);
        }
    }

    /**
     * Handles key typing (press and release).
     * Isn't used by game.
     *
     * @param e the KeyEvent object representing the key typing event.
     */
    @Override
    public void keyTyped(KeyEvent e) {
    }

    // Mouse inputs

    /**
     * Handles mouse movement.
     * Forwards the event to the appropriate state.
     *
     * @param e the MouseEvent object representing the mouse movement event.
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        switch (States.state) {
            case MENU -> window.getApp().getMenu().mouseMoved(e);
            case TUTORIAL -> window.getApp().getTutorial().mouseMoved(e);
            case LOAD -> window.getApp().getLoad().mouseMoved(e);
            case GAME -> window.getApp().getGame().mouseMoved(e);
            case EDITOR -> window.getApp().getEditor().mouseMoved(e);
        }
    }

    /**
     * Handles mouse presses.
     * Forwards the event to the appropriate state.
     *
     * @param e the MouseEvent object representing the mouse press event.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        switch (States.state) {
            case MENU -> window.getApp().getMenu().mousePressed(e);
            case TUTORIAL -> window.getApp().getTutorial().mousePressed(e);
            case LOAD -> window.getApp().getLoad().mousePressed(e);
            case GAME -> window.getApp().getGame().mousePressed(e);
            case EDITOR -> window.getApp().getEditor().mousePressed(e);
        }
    }

    /**
     * Handles mouse releases.
     * Forwards the event to the appropriate state.
     *
     * @param e the MouseEvent object representing the mouse release event.
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        switch (States.state) {
            case MENU -> window.getApp().getMenu().mouseReleased(e);
            case TUTORIAL -> window.getApp().getTutorial().mouseReleased(e);
            case LOAD -> window.getApp().getLoad().mouseReleased(e);
            case GAME -> window.getApp().getGame().mouseReleased(e);
            case EDITOR -> window.getApp().getEditor().mouseReleased(e);
        }
    }

    /**
     * Handles mouse clicks.
     * Isn't used by game.
     *
     * @param e the MouseEvent object representing the mouse click event.
     */
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    /**
     * Handles mouse enters to game window.
     * Isn't used by game.
     *
     * @param e the MouseEvent object representing the mouse enters.
     */
    @Override
    public void mouseEntered(MouseEvent e) {
    }

    /**
     * Handles mouse exits from game window.
     * Isn't used by game.
     *
     * @param e the MouseEvent object representing the mouse exits.
     */
    @Override
    public void mouseExited(MouseEvent e) {
    }

    /**
     * Handles mouse dragging (click and movement).
     * Isn't used by game.
     *
     * @param e the MouseEvent object representing the mouse dragging.
     */
    @Override
    public void mouseDragged(MouseEvent e) {
    }
}
