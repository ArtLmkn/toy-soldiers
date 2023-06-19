package state;

import gui.ButtonSmall;
import main.Application;
import static util.Const.*;
import static util.Const.GUI.*;
import util.Image;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/**
 * Tutorial game state.
 * Represents information about game.
 * FIRST    - first page (stage).
 * ...      - middle pages (stage).
 * LAST     - last page (stage).
 */
public class Tutorial extends State implements StateInterface {
    private final BufferedImage background; // state background image
    private final ButtonSmall[] buttons; // state menu buttons
    private BufferedImage[] pages; // all tutorial pages
    private BufferedImage page; // current tutorial page to read
    private int stage; // local stage

    /**
     * Constructor for the Tutorial.
     *
     * @param app  associated Game object.
     */
    public Tutorial(Application app) {
        super(app);
        background = Image.loadImage(MENU_2);
        loadPages();
        buttons = new ButtonSmall[3];
        createButtons();
        stage = 0;
        page = pages[stage];
    }

    /**
     * Creates buttons for Tutorial state.
     */
    private void createButtons() {
        buttons[0] = new ButtonSmall(Buttons.Small.PREV, Buttons.Small.X, Buttons.Small.Y_POS_1, States.TUTORIAL, States.stage-1);
        buttons[1] = new ButtonSmall(Buttons.Small.NEXT, Buttons.Small.X, Buttons.Small.Y_POS_2, States.TUTORIAL, States.stage+1);
        buttons[2] = new ButtonSmall(Buttons.Small.BACK, Buttons.Small.X, Buttons.Small.Y_POS_3, States.MENU, Stages.Menu.MAIN);
    }

    /**
     * Creates pages with information about game.
     */
    private void loadPages() {
        pages = new BufferedImage[5];
        BufferedImage tmp = Image.loadImage(TUTOR);
        for (int i = 0; i < pages.length; ++i) {
            pages[i] = tmp.getSubimage(0, i * 800, 800, 800);
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

        // Prevent going out of tutorial book
        if (States.stage < Stages.Tutorial.FIRST) States.stage = Stages.Tutorial.FIRST;
        if (States.stage > Stages.Tutorial.LAST) States.stage = Stages.Tutorial.LAST;

        page = pages[States.stage];

        for (ButtonSmall button: buttons) button.update(); // Update buttons
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
        graphics.drawImage(page, 80, 90, null); // Render actual tutorial page
        for (ButtonSmall button: buttons) button.render(graphics); // Render buttons
    }

    /**
     * Handles mouse movement.
     * Respects actual game stage.
     *
     * @param e the MouseEvent object representing the mouse movement event.
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        for (ButtonSmall button : buttons) { // Check if mouse is over buttons
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
        for (ButtonSmall button : buttons) { // Check if mouse presses on buttons
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
        for (ButtonSmall button : buttons) { // Check if buttons pressed
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
