package gui;

import state.States;
import util.Image;

import static util.Const.GUI.*;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * Small graphical button.
 * Extends Button class.
 */
public class ButtonSmall extends Button {

    /**
     * Constructor for the Small Button.
     *
     * @param type  specific type of button (e.g. play, back, quit).
     * @param x     x-coordinate of the button on the screen.
     * @param y     the y-coordinate of the button on the screen.
     * @param state state of the game associated with the button action.
     * @param stage stage of the game associated with the button action.
     */
    public ButtonSmall(int type, int x, int y, States state, int stage) {
        super(type, x, y, state, stage);
        hitbox = new Rectangle(this.x, this.y, Buttons.Small.W, Buttons.Small.H);

        BufferedImage tmp = Image.loadImage(BUTTONS_S);
        for (int i = 0; i < buttonImg.length; ++i) { // Loads every state of this button
            buttonImg[i] = tmp.getSubimage(i * Buttons.Small.W, type * Buttons.Small.H, Buttons.Small.W, Buttons.Small.H);
        }
    }
}
