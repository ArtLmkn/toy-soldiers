package gui;

import state.States;
import util.Image;

import static util.Const.GUI.*;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * Large graphical button.
 * Extends Button class.
 */
public class ButtonLarge extends Button {

    /**
     * Constructor for the Large Button.
     *
     * @param type  specific type of button (e.g. play, back, quit).
     * @param x     x-coordinate of the button on the screen.
     * @param y     the y-coordinate of the button on the screen.
     * @param state state of the game associated with the button action.
     * @param stage stage of the game associated with the button action.
     */
    public ButtonLarge(int type, int x, int y, States state, int stage) {
        super(type, x, y, state, stage);
        hitbox = new Rectangle(this.x, this.y, Buttons.Large.W, Buttons.Large.H);

        BufferedImage tmp = Image.loadImage(BUTTONS_L);
        for (int i = 0; i < buttonImg.length; ++i) { // Loads every state of this button
            buttonImg[i] = tmp.getSubimage(i * Buttons.Large.W, type * Buttons.Large.H, Buttons.Large.W, Buttons.Large.H);
        }
    }
}
