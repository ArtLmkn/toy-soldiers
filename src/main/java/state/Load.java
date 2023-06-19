package state;

import gui.ButtonSmall;
import gui.RadioButton;
import main.Application;
import static util.Const.*;
import static util.Const.GUI.*;
import util.Image;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Objects;

/**
 * Load game state.
 * Represents screens to oad levels.
 * NEW      - choosing level to start new game (stage).
 * LOAD     - choosing savefile to continue game (stage).
 * CREATE   - choosing level to edit in editor (stage).
 * UPDATE   - updating levels list (stage).
 */
public class Load extends State implements StateInterface {
    private final BufferedImage background; // state background image
    private final ButtonSmall[] buttons; // state menu buttons
    private final ArrayList<RadioButton> radioButtons; // radio buttons with levels
    private String active; // selected level to load
    private int stage; // local stage

    /**
     * Constructor for the Load.
     *
     * @param app  associated Game object.
     */
    public Load(Application app) {
        super(app);
        background = Image.loadImage(MENU_2);
        buttons = new ButtonSmall[3];
        createButtons();
        radioButtons = new ArrayList<>();
        createRadioButtons();
        active = null;
        stage = 0;
    }

    /**
     * Creates all state buttons.
     */
    private void createButtons() {
        switch (States.stage) {
            case Stages.Load.NEW -> {
                buttons[0] = new ButtonSmall(Buttons.Small.START, Buttons.Small.X, Buttons.Small.Y_POS_1, States.GAME, Stages.Game.NEW);
                buttons[1] = new ButtonSmall(Buttons.Small.BACK, Buttons.Small.X, Buttons.Small.Y_POS_2, States.MENU, Stages.Menu.PLAY);
                buttons[2] = new ButtonSmall(Buttons.Small.BACK, GAME_WIDTH, GAME_HEIGHT, States.MENU, Stages.Menu.PLAY);
            }
            case Stages.Load.LOAD -> {
                buttons[0] = new ButtonSmall(Buttons.Small.LOAD, Buttons.Small.X, Buttons.Small.Y_POS_1, States.GAME, Stages.Game.LOAD);
                buttons[1] = new ButtonSmall(Buttons.Small.BACK, Buttons.Small.X, Buttons.Small.Y_POS_2, States.MENU, Stages.Menu.PLAY);
                buttons[2] = new ButtonSmall(Buttons.Small.BACK, GAME_WIDTH, GAME_HEIGHT, States.MENU, Stages.Menu.PLAY);
            }
            case Stages.Load.CREATE -> {
                buttons[0] = new ButtonSmall(Buttons.Small.CREATE, Buttons.Small.X, Buttons.Small.Y_POS_1, States.EDITOR, Stages.Editor.NEW);
                buttons[1] = new ButtonSmall(Buttons.Small.EDIT, Buttons.Small.X, Buttons.Small.Y_POS_2, States.EDITOR, Stages.Editor.LOAD);
                buttons[2] = new ButtonSmall(Buttons.Small.BACK, Buttons.Small.X, Buttons.Small.Y_POS_3, States.MENU, Stages.Menu.START);
            }
        }
    }

    /**
     * Creates all state radio buttons.
     */
    private void createRadioButtons() {
        // Read level files in folder
        ArrayList<String> lvl = new ArrayList<>();
        String dir = States.stage == 1 ? "svd" : "lvl";
        ArrayList<String> files = new ArrayList<>(Arrays.asList(Objects.requireNonNull(new File(dir).list())));
        for (String file : files) if (file.contains(".bin")) lvl.add(file.replace(".bin", ""));

        // Create radio buttons for level files
        for (int i = 0; i < lvl.size(); ++i) {
            if (i == Limits.LEVELS) break;
            radioButtons.add(new RadioButton(Buttons.Radio.X, Buttons.Radio.Y, i, lvl.get(i)));
        }
    }

    /**
     * Updates the state.
     * Respects actual game stage.
     */
    @Override
    public void update() {
        if (States.stage == Stages.Load.UPDATE) { // Update list of levels (radio buttons)
            States.stage = stage;
            radioButtons.clear();
            createRadioButtons();
            active = null;
        }

        if (States.stage != stage) { // Remake buttons
            stage = States.stage;
            createButtons();
            radioButtons.clear();
            createRadioButtons();
            active = null;
        }

        this.getApp().getWindow().setTextField(false); // Disable text field
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
        for (ButtonSmall button: buttons) button.render(graphics); // Render buttons

        try {
            for (RadioButton button: radioButtons) if (button != null) button.render(graphics); // Render radio buttons
        } catch (ConcurrentModificationException e) {
            // Avoiding error when new radio button was created with active render
        }
    }

    /**
     * Returns text from the actual active radio button.
     *
     * @return text from the active radio button.
     */
    public String getActive() {
        return active;
    }

    /**
     * Sets active radio buttons.
     *
     * @param active active radio buttons.
     */
    public void setActive(String active) {
        this.active = active;
    }

    /**
     * Handles mouse movement.
     * Respects actual game stage.
     *
     * @param e the MouseEvent object representing the mouse movement event.
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        for (ButtonSmall button : buttons) {  // Check if mouse is over buttons
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
        for (ButtonSmall button : buttons) {  // Check if mouse presses on buttons
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
        boolean nothingPressed = true;

        for (ButtonSmall button : buttons) { // Check if buttons pressed
            if (isButton(e, button) && button.isMousePressed()) {
                if ((button.getStage() != -1 && button.getStage() != -2) || active != null) { // Not editing or file to edit was selected
                    nothingPressed = false;
                    button.buttonAction();
                    if (States.state == States.GAME ||States.state == States.EDITOR ) this.getApp().getAudio().playSong(Sounds.GAME); // Play game song if entered to game/editor
                } else { // Nothing was selected to edit
                    this.getApp().getAudio().playSound(Sounds.ERROR);
                }
            }
            button.setMouseOver(false);
            button.setMousePressed(false);
        }

        for (RadioButton button : radioButtons) { // Check if radio buttons pressed
            button.setMousePressed(false);
            if (isButton(e, button)) { // Select this level to edit
                nothingPressed = false;
                active = button.getName();
                button.setMousePressed(true);
            }
        }

        if (nothingPressed) active = null;
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
