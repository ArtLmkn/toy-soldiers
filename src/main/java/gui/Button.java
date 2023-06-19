package gui;

import state.*;

import static util.Const.GUI.Buttons.*;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * Class for a graphical button.
 * Contains image of button, position on the screen and game state + stage where button leads.
 * Action triggers by a mouse click.
 */
public abstract class Button {
    protected final int x, y; // button coordinates
    protected final int type; // type (name) of button
    private int phase; // current button phase (active, pressed,..) for button animation
    protected Rectangle hitbox; // clickable area
    protected final BufferedImage[] buttonImg; // button images
    protected final States state; // target game state
    protected int stage; // target game stage
    protected boolean mouseOver, mousePressed; // interactions with mouse

    /**
     * Constructor for the Button.
     *
     * @param type  specific type of button (e.g. play, back, quit).
     * @param x     x-coordinate of the button on the screen.
     * @param y     y-coordinate of the button on the screen.
     * @param state state of the game associated with the button action.
     * @param stage stage of the game associated with the button action.
     */
    public Button(int type, int x, int y, States state, int stage) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.state = state;
        this.stage = stage;
        buttonImg = new BufferedImage[3];
        phase = INACTIVE;
    }

    /**
     * Updates the button phase based on user interactions with the mouse.
     */
    public void update() {
        phase = INACTIVE;
        if (mouseOver) phase = ACTIVE;
        if (mousePressed) phase = PRESSED;
    }

    /**
     * Renders the button on the screen.
     *
     * @param graphics Graphics object used to draw the button.
     */
    public void render(Graphics graphics) {
        graphics.drawImage(buttonImg[phase], x, y, null);
    }

    /**
     * Performs the button action associated with this button.
     */
    public void buttonAction() {
        States.state = state;
        States.stage = stage;
    }

    /**
     * Sets if the mouse is currently over this button.
     *
     * @param mouseOver true if the mouse is currently over this button, false otherwise.
     */
    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    /**
     * Returns if the mouse is currently pressed on this button.
     *
     * @return true if the mouse is currently pressed on this button, false otherwise.
     */
    public boolean isMousePressed() {
        return mousePressed;
    }

    /**
     * Sets if the mouse is currently pressed on this button.
     *
     * @param mousePressed true if the mouse is currently pressed on this button, false otherwise.
     */
    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }

    /**
     * Returns the clickable area of this button.
     *
     * @return the clickable area of this button.
     */
    public Rectangle getHitbox() {
        return hitbox;
    }

    /**
     * Returns the stage of the game associated with this button action.
     *
     * @return the stage of the game associated with this button action.
     */
    public int getStage() {
        return stage;
    }

    /**
     * Sets the stage of the game associated with this button action.
     *
     * @param stage the new stage of the game associated with this button action.
     */
    public void setStage(int stage) {
        this.stage = stage;
    }
}
