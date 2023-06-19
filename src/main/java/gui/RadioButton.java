package gui;

import util.Const;
import static util.Const.GUI.*;

import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;

/**
 * Class for a graphical radio button.
 * Contains text of the button and position on the screen.
 * Only one can be active.
 * Activates by a mouse click.
 */
public class RadioButton {
    private final int x, y; // button coordinates
    private final String name;  // name of target file
    protected Rectangle hitbox;  // clickable area
    private boolean mousePressed; // interaction with mouse

    /**
     * Constructor for the Radio Button.
     *
     * @param x     x-coordinate of the button.
     * @param y     y-coordinate of the button.
     * @param pos   position of the button in the list.
     * @param name  text of the button.
     */
    public RadioButton(int x, int y, int pos, String name) {
        this.x = x;
        this.y = y + pos * Buttons.Radio.OFFSET;
        this.name = name;
        mousePressed = false;
        hitbox = new Rectangle(this.x - 5, this.y - Buttons.Radio.OFFSET + 5, Buttons.Radio.W, Buttons.Radio.H);
    }

    /**
     * Renders the radio button on the screen.
     *
     * @param graphics the Graphics object used to draw the button.
     */
    public void render(Graphics graphics) {
        graphics.setFont(VETERAN);
        if (mousePressed) { // Draw active button
            graphics.setColor(Colors.OLIVE_A);
            graphics.fillRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
            graphics.setColor(Colors.KHAKI);
        } else {  // Draw inactive button
            graphics.setColor(Colors.OLIVE);
        }
        graphics.drawString(name, x, y);
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
     * Gets the text of the radio button.
     *
     * @return the text of the radio button.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the clickable area of this button.
     *
     * @return the clickable area of this button.
     */
    public Rectangle getHitbox() {
        return hitbox;
    }
}
