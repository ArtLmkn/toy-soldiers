package gui;

import state.States;
import util.Image;

import static util.Const.GUI.*;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * Editor graphical button.
 * Extends Button class.
 */
public class ButtonEdit extends Button {

    /**
     * Constructor for the Edit Button.
     *
     * @param type  specific type of button (e.g. play, back, quit).
     * @param x     x-coordinate of the button on the screen.
     * @param y     the y-coordinate of the button on the screen.
     * @param state state of the game associated with the button action.
     * @param stage stage of the game associated with the button action.
     */
    public ButtonEdit(int type, int x, int y, States state, int stage) {
        super(type, x, y, state, stage);
        hitbox = new Rectangle(this.x, this.y, Buttons.Edit.W, Buttons.Edit.H);

        BufferedImage tmp = Image.loadImage(BUTTONS_E);
        for (int i = 0; i < buttonImg.length; ++i) { // Loads every state of this button
            buttonImg[i] = tmp.getSubimage(i * Buttons.Edit.W, type * Buttons.Edit.H, Buttons.Edit.W, Buttons.Edit.H);
        }
    }
}
