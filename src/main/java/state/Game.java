package state;

import gui.ButtonSmall;
import main.Application;
import util.Level;
import util.Image;
import static util.Const.*;
import static util.Const.GUI.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/**
 * Game game state.
 * Represents game proccess.
 * NEW      - loading file from levels folder (stage).
 * LOAD     - loading file from savegames folder (stage).
 * PLAY     - direct playing (stage).
 * PAUSE    - pause screen (stage).
 * EXIT     - level save screen (stage).
 * WIN      - victory screen (stage).
 * LOSE     - defeat screen (stage).
 */
public class Game extends State implements StateInterface {
    private final BufferedImage[] popups = new BufferedImage[4]; // state submenus
    private final ButtonSmall[] buttons = new ButtonSmall[6]; // state submenu buttons

    /**
     * Constructor for the Game.
     *
     * @param app  associated Game object.
     */
    public Game(Application app) {
        super(app);
        loadPopups();
        loadDigits();
        createButtons();
    }

    /**
     * Loads all overlaping screens.
     */
    private void loadPopups() {
        popups[0] = Image.loadImage(PAUSE_1);
        popups[1] = Image.loadImage(PAUSE_2);
        popups[2] = Image.loadImage(PAUSE_4);
        popups[3] = Image.loadImage(PAUSE_5);
    }

    /**
     * Creates all state buttons.
     */
    private void createButtons() {
        buttons[0] = new ButtonSmall(Buttons.Small.RESUME, Buttons.Pause.X_POS_2, Buttons.Pause.Y_POS_2_1, States.GAME, Stages.Game.PLAY);
        buttons[1] = new ButtonSmall(Buttons.Small.QUIT, Buttons.Pause.X_POS_2, Buttons.Pause.Y_POS_2_2, States.GAME, Stages.Game.EXIT);
        buttons[2] = new ButtonSmall(Buttons.Small.YES, Buttons.Pause.X_POS_1_1, Buttons.Pause.Y_POS_1, States.LOAD, Stages.Load.UPDATE);
        buttons[3] = new ButtonSmall(Buttons.Small.NO, Buttons.Pause.X_POS_1_2, Buttons.Pause.Y_POS_1, States.LOAD, Stages.Load.UPDATE);
        buttons[5] = new ButtonSmall(Buttons.Small.QUIT, Buttons.Pause.X_POS_2, Buttons.Pause.Y_POS_2_2, States.MENU, Stages.Menu.START);
    }

    /**
     * Updates the state.
     * Respects actual game stage.
     */
    @Override
    public void update() {
        switch (States.stage) {
            case Stages.Game.NEW, Stages.Game.LOAD -> {
                if (States.stage == Stages.Game.NEW) { // Load new game to play
                    level = new Level(this.getApp().getLoad().getActive(), "lvl", app);
                    buttons[4] = new ButtonSmall(Buttons.Small.AGAIN, Buttons.Pause.X_POS_2, Buttons.Pause.Y_POS_2_1, States.GAME, Stages.Game.NEW);
                    level.getApp().getLogger().game("New game was loaded from level '" + this.getApp().getLoad().getActive() + "'."); // Logging
                } else { // Load saved game to play
                    level = new Level(this.getApp().getLoad().getActive(), "svd", app);
                    buttons[4] = new ButtonSmall(Buttons.Small.AGAIN, Buttons.Pause.X_POS_2, Buttons.Pause.Y_POS_2_1, States.GAME, Stages.Game.LOAD);
                    level.getApp().getLogger().game("Saved game was loaded from '" + this.getApp().getLoad().getActive() + "'."); // Logging
                }
                States.stage = 0;
                level.getApp().getLogger().game("Enemies alive: " + level.getEnemiesCount() + ". Documents to collect: " + level.getDocsCount() + "."); // Logging
            }
            case Stages.Game.PLAY -> { // Gaming process
                level.update();
                checkFinish();
            }
            default -> { // Stop player movement when the game is paused
                for (ButtonSmall button: buttons) if (button != null) button.update(); // Update buttons
                level.getPlayer().setUp(false);
                level.getPlayer().setDown(false);
                level.getPlayer().setLeft(false);
                level.getPlayer().setRight(false);
            }
        }
    }

    /**
     * Renders the state on the screen.
     * Respects actual game stage.
     *
     * @param graphics Graphics object used to draw the state.
     */
    @Override
    public void render(Graphics graphics) {
        if (level != null) { // Draw level and its objects
            level.render(graphics);
            renderDigits(graphics);
        }

        switch (States.stage) {
            case Stages.Game.PAUSE -> { // Render buttons
                graphics.drawImage(popups[0], 0, 0, null);
                buttons[0].render(graphics);
                buttons[1].render(graphics);
            }
            case Stages.Game.EXIT -> { // Render buttons
                graphics.drawImage(popups[1], 0, 0, null);
                buttons[2].render(graphics);
                buttons[3].render(graphics);
                this.getApp().getWindow().setTextField(true); // Enable text field
            }
            case Stages.Game.WIN -> { // Render buttons
                graphics.drawImage(popups[2], 0, 0, null);
                buttons[4].render(graphics);
                buttons[5].render(graphics);
            }
            case Stages.Game.LOSE -> { // Render buttons
                graphics.drawImage(popups[3], 0, 0, null);
                buttons[4].render(graphics);
                buttons[5].render(graphics);
            }
        }
    }

    /**
     * Controls if the current level is over.
     * Stops gaming stage.
     */
    private void checkFinish() {
        if (level.getEnemiesCount() == 0 && level.getDocsCount() == 0 && level.getFinish().isTaken()) {
            // Mission complete
            States.stage = Stages.Game.WIN;
            this.getApp().getAudio().playSound(Sounds.WIN);
            level.getApp().getLogger().game("Level '" + this.getApp().getLoad().getActive() + "' was successfully finished."); // Logging
        } else if (level.getPlayer().isDead()) {
            // Mission failed
            States.stage = Stages.Game.LOSE;
            this.getApp().getAudio().playSound(Sounds.DEFEAT);
            level.getApp().getLogger().game("Level '" + this.getApp().getLoad().getActive() + "' was failed."); // Logging
        }
    }

    /**
     * Handles mouse movement.
     * Respects actual game stage.
     *
     * @param e the MouseEvent object representing the mouse movement event.
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        switch (States.stage) {
            case Stages.Game.PAUSE -> {
                for (int i = 0; i < 2; ++i) { // Check if mouse is over buttons
                    buttons[i].setMouseOver(false);
                    if (isButton(e, buttons[i])) buttons[i].setMouseOver(true);
                }
            }
            case Stages.Game.EXIT -> {
                for (int i = 2; i < 4; ++i) { // Check if mouse is over buttons
                    buttons[i].setMouseOver(false);
                    if (isButton(e, buttons[i])) buttons[i].setMouseOver(true);
                }
            }
            case Stages.Game.WIN, Stages.Game.LOSE -> {
                for (int i = 4; i < 6; ++i) { // Check if mouse is over buttons
                    buttons[i].setMouseOver(false);
                    if (isButton(e, buttons[i])) buttons[i].setMouseOver(true);
                }
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
        switch (States.stage) {
            case Stages.Game.PAUSE -> {
                for (int i = 0; i < 2; ++i) { // Check if mouse presses on buttons
                    if (isButton(e, buttons[i])) {
                        buttons[i].setMousePressed(true);
                        break;
                    }
                }
            }
            case Stages.Game.EXIT -> {
                for (int i = 2; i < 4; ++i) {
                    if (isButton(e, buttons[i])) { // Check if mouse presses on buttons
                        buttons[i].setMousePressed(true);
                        break;
                    }
                }
            }
            case Stages.Game.WIN, Stages.Game.LOSE -> {
                for (int i = 4; i < 6; ++i) {
                    if (isButton(e, buttons[i])) { // Check if mouse presses on buttons
                        buttons[i].setMousePressed(true);
                        break;
                    }
                }
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
        switch (States.stage) {
            case Stages.Game.PAUSE -> {
                for (int i = 0; i < 2; ++i) { // Check if buttons pressed
                    if (isButton(e, buttons[i]) && buttons[i].isMousePressed()) buttons[i].buttonAction();
                    buttons[i].setMouseOver(false);
                    buttons[i].setMousePressed(false);
                }
            }
            case Stages.Game.EXIT -> {
                for (int i = 2; i < 4; ++i) { // Check if buttons pressed
                    if (isButton(e, buttons[i]) && buttons[i].isMousePressed()) {
                        if (i == 3 || checkText(this.getApp().getWindow().getTextField())) { // Not saving or filename is ok
                            if (i == 2) {
                                level.saveLevel(this.getApp().getWindow().getTextField(), "svd");
                                level.getApp().getLogger().game("Level was saved as '" + this.getApp().getWindow().getTextField() + "'."); // Logging
                            } else {
                                level.getApp().getLogger().game("Exit without saving."); // Logging
                            }
                            this.getApp().getLoad().setActive(null);
                            buttons[i].buttonAction();
                            if (States.state == States.MENU || States.state == States.LOAD) this.getApp().getAudio().playSong(Sounds.MENU); // Play menu song if returned to menu
                        } else { // Saving with invalid filename
                            this.getApp().getAudio().playSound(Sounds.ERROR);
                        }
                    }
                    buttons[i].setMouseOver(false);
                    buttons[i].setMousePressed(false);
                }
            }
            case Stages.Game.WIN, Stages.Game.LOSE -> {
                for (int i = 4; i < 6; ++i) { // Check if buttons pressed
                    if (isButton(e, buttons[i]) && buttons[i].isMousePressed()) buttons[i].buttonAction();
                    buttons[i].setMouseOver(false);
                    buttons[i].setMousePressed(false);
                    if (States.state == States.MENU) {
                        level = null;
                        this.getApp().getLoad().setActive(null);
                        this.getApp().getAudio().playSong(Sounds.MENU); // Play menu song if exited to menu
                    }
                }
            }
        }
    }

    /**
     * Handles key presses.
     *
     * @param e the KeyEvent object representing the key press.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if (States.stage == Stages.Game.PLAY) { // Control player only in main gaming state
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP -> level.getPlayer().setUp(true); // Go up
                case KeyEvent.VK_DOWN -> level.getPlayer().setDown(true); // Go down
                case KeyEvent.VK_LEFT -> level.getPlayer().setLeft(true); // Go to the left
                case KeyEvent.VK_RIGHT -> level.getPlayer().setRight(true); // Go to the right
                case KeyEvent.VK_SPACE -> level.getPlayer().shoot(); // Start shooting
            }
        }
    }

    /**
     * Handles key releases.
     *
     * @param e the KeyEvent object representing the key release.
     */
    @Override
    public void keyReleased(KeyEvent e) {
        if (States.stage == Stages.Game.PLAY) {  // Control player only in main gaming state
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP -> level.getPlayer().setUp(false); // Stop going up
                case KeyEvent.VK_DOWN -> level.getPlayer().setDown(false); // Stop going down
                case KeyEvent.VK_LEFT -> level.getPlayer().setLeft(false); // Stop going to the left
                case KeyEvent.VK_RIGHT -> level.getPlayer().setRight(false); // Stop going to the right
                case KeyEvent.VK_SPACE -> level.getPlayer().setShooting(false); // Stop shooting
                case KeyEvent.VK_ESCAPE -> States.stage = Stages.Game.PAUSE; // Pause game
            }
        } else {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE && States.stage == Stages.Game.PAUSE) States.stage = Stages.Game.PLAY; // Return to main gaming state
        }
    }
}
