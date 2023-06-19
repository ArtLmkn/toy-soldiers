package state;

import gui.ButtonLarge;
import main.Application;
import util.Image;
import static util.Const.*;
import static util.Const.GUI.*;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/**
 * Menu game state.
 * Represents main game menus.
 * MAIN     - main menu (stage).
 * START    - menu with choice between playing and editor (stage).
 * PLAY     - menu with choice between new game and load one (stage).
 * EXIT     - exiting from game application (stage).
 */
public class Menu extends State implements StateInterface {
    private final BufferedImage background; // state background image
    private final ButtonLarge[] buttons; // state menu buttons
    private int stage; // local stage

    /**
     * Constructor for the Menu.
     *
     * @param app  associated Game object.
     */
    public Menu(Application app) {
        super(app);
        background = Image.loadImage(MENU_1);
        buttons = new ButtonLarge[3];
        createButtons();
        stage = 0;
    }

    private void createButtons() {
        switch (States.stage) {
            case Stages.Menu.MAIN -> {
                buttons[0] = new ButtonLarge(Buttons.Large.PLAY, Buttons.Large.X, Buttons.Large.Y_POS_1, States.MENU, Stages.Menu.START);
                buttons[1] = new ButtonLarge(Buttons.Large.TUTORIAL, Buttons.Large.X, Buttons.Large.Y_POS_2, States.TUTORIAL, Stages.Tutorial.FIRST);
                buttons[2] = new ButtonLarge(Buttons.Large.QUIT, Buttons.Large.X, Buttons.Large.Y_POS_3, States.MENU, Stages.Menu.EXIT);
            }
            case Stages.Menu.START -> {
                buttons[0] = new ButtonLarge(Buttons.Large.PLAYMISSION, Buttons.Large.X, Buttons.Large.Y_POS_1, States.MENU, Stages.Menu.PLAY);
                buttons[1] = new ButtonLarge(Buttons.Large.CREATEMISSION, Buttons.Large.X, Buttons.Large.Y_POS_2, States.LOAD, Stages.Load.CREATE);
                buttons[2] = new ButtonLarge(Buttons.Large.BACK, Buttons.Large.X, Buttons.Large.Y_POS_3, States.MENU, Stages.Menu.MAIN);
            }
            case Stages.Menu.PLAY -> {
                buttons[0] = new ButtonLarge(Buttons.Large.NEWGAME, Buttons.Large.X, Buttons.Large.Y_POS_1, States.LOAD, Stages.Load.NEW);
                buttons[1] = new ButtonLarge(Buttons.Large.LOADGAME, Buttons.Large.X, Buttons.Large.Y_POS_2, States.LOAD, Stages.Load.LOAD);
                buttons[2] = new ButtonLarge(Buttons.Large.BACK, Buttons.Large.X, Buttons.Large.Y_POS_3, States.MENU, Stages.Menu.START);
            }
        }
    }

    /**
     * Updates the state.
     * Respects actual game stage.
     */
    @Override
    public void update() {
        if (stage != States.stage) { // Remake buttons
            stage = States.stage;
            createButtons();
        }

        for (ButtonLarge button: buttons) button.update(); // Update buttons
    }

    /**
     * Renders the state on the screen.
     * Respects actual game stage.
     *
     * @param graphics Graphics object used to draw the state.
     */
    @Override
    public void render(Graphics graphics) {
        graphics.drawImage(background, 0, 0, null); // Render background
        for (ButtonLarge button: buttons) button.render(graphics); // Render buttons
    }


    /**
     * Handles mouse movement.
     * Respects actual game stage.
     *
     * @param e the MouseEvent object representing the mouse movement event.
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        for (ButtonLarge button : buttons) { // Check if mouse is over buttons
            button.setMouseOver(false);
            if (isButton(e, button)) {
                button.setMouseOver(true);
                break;
            }
        }
    }

    /**
     * Handles mouse presses.
     * Respects actual game stage.
     *
     * @param e the MouseEvent object representing the mouse press event.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        for (ButtonLarge button : buttons) { // Check if mouse presses on buttons
            if (isButton(e, button)) {
                button.setMousePressed(true);
                break;
            }
        }
    }

    /**
     * Handles mouse releases.
     * Respects actual game stage.
     *
     * @param e the MouseEvent object representing the mouse release event.
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        for (ButtonLarge button : buttons) { // Check if buttons pressed
            if (isButton(e, button) && button.isMousePressed()) button.buttonAction();
            button.setMouseOver(false);
            button.setMousePressed(false);
        }
    }

    /**
     * Handles key presses.
     * Isn't used by this state.
     *
     * @param e the KeyEvent object representing the key press.
     */
    @Override
    public void keyPressed(KeyEvent e) {
    }

    /**
     * Handles key releases.
     * Isn't used by this state.
     *
     * @param e the KeyEvent object representing the key release.
     */
    @Override
    public void keyReleased(KeyEvent e) {
    }
}
