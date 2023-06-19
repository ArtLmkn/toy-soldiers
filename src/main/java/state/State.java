package state;

import gui.Button;
import gui.RadioButton;
import main.Application;
import util.Const;
import util.Image;
import util.Level;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static util.Const.GUI.DIGITS;

/**
 * The abstract base class for all game states. Each state represents a different screen in the game.
 * All game states have access to the game application object.
 * Some states (e.g. Editor,Game) have access to the current level.
 */
public abstract class State {
    protected Application app; // associated game application
    protected Level level = null; // associated level
    protected BufferedImage[] digits; // digit images

    /**
     * Constructor for the State.
     *
     * @param app  associated Application object.
     */
    public State(Application app) {
        this.app = app;
    }

    /**
     * Loads the digit images used to render level information (e.g. player's health, ammo count)
     */
    protected void loadDigits() {
        digits = new BufferedImage[10];
        BufferedImage tmp = Image.loadImage(DIGITS);
        for (int i = 0; i < digits.length; ++i) {
            digits[i] = tmp.getSubimage( i* Const.GUI.Digits.W, 0, Const.GUI.Digits.W, Const.GUI.Digits.H);
        }
    }

    /**
     * Renders the digits on the screen.
     *
     * @param graphics Graphics object used to draw the digit.
     */
    protected void renderDigits(Graphics graphics) {
        // Player health
        graphics.drawImage(digits[level.getPlayer().getHealth() / 10], Const.GUI.Digits.X_POS_1_1, Const.GUI.Digits.Y_POS_1, null);
        graphics.drawImage(digits[level.getPlayer().getHealth() % 10], Const.GUI.Digits.X_POS_1_2, Const.GUI.Digits.Y_POS_1, null);

        // Player ammo
        graphics.drawImage(digits[level.getPlayer().getAmmo() / 10], Const.GUI.Digits.X_POS_1_1, Const.GUI.Digits.Y_POS_2, null);
        graphics.drawImage(digits[level.getPlayer().getAmmo() % 10], Const.GUI.Digits.X_POS_1_2, Const.GUI.Digits.Y_POS_2, null);

        // Enemies count
        graphics.drawImage(digits[level.getEnemiesCount() / 10], Const.GUI.Digits.X_POS_2_1, Const.GUI.Digits.Y_POS_1, null);
        graphics.drawImage(digits[level.getEnemiesCount() % 10], Const.GUI.Digits.X_POS_2_2, Const.GUI.Digits.Y_POS_1, null);

        // Documents count
        graphics.drawImage(digits[level.getDocsCount() / 10], Const.GUI.Digits.X_POS_2_1, Const.GUI.Digits.Y_POS_2, null);
        graphics.drawImage(digits[level.getDocsCount() % 10], Const.GUI.Digits.X_POS_2_2, Const.GUI.Digits.Y_POS_2, null);
    }

    /**
     * Checks if the given string contains only valid characters.
     * Used for saving current game/level to binary file.
     * Accepts only digits and latin symbols.
     *
     * @param str string to check.
     * @return true if the string is alphanumeric, false otherwise.
     */
    protected boolean checkText(String str) {
        if (str.length() == 0) return false;
        return str.matches("^[a-zA-Z0-9]*$");
    }

    /**
     * Checks if the mouse interacts with the given button.
     *
     * @param e         mouse event to check.
     * @param button    button to check (Button - Small, Big, Editor)
     * @return true if mouse interacts with the button, false otherwise.
     */
    public boolean isButton(MouseEvent e, Button button) {
        return button.getHitbox().contains(e.getX(), e.getY());
    }

    /**
     * Checks if the mouse interacts with the given button.
     *
     * @param e         mouse event to check.
     * @param button    button to check (Button - Radio)
     * @return true if mouse interacts with the button, false otherwise.
     */
    public boolean isButton(MouseEvent e, RadioButton button) {
        return button.getHitbox().contains(e.getX(), e.getY());
    }

    /**
     * Returns the associated game application object.
     *
     * @return associated game Application object.
     */
    public Application getApp() {
        return app;
    }
}
