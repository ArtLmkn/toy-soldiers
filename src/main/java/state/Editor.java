package state;

import gui.ButtonSmall;
import gui.ButtonEdit;
import main.Application;
import obj.Obstacle;
import obj.collectible.AmmoBox;
import obj.collectible.Doc;
import obj.collectible.HealthBox;
import obj.soldier.Enemy;
import util.Level;
import util.Image;
import static util.Const.*;
import static util.Const.GUI.*;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/**
 * Editor game state.
 * Represents level editor.
 * NEW      - creating new level (stage).
 * LOAD     - loading existing level to edit (stage).
 * MAIN     - direct editing (stage).
 * PAUSE    - pause screen (stage).
 * EXIT     - level save screen (stage).
 * OBJECTS  - screen with game object choosing (stage).
 */
public class Editor extends State implements StateInterface {
    private final BufferedImage[] popups = new BufferedImage[3]; // state submenus
    private final ButtonSmall[] buttons = new ButtonSmall[4]; // state submenu buttons
    private final ButtonEdit[] buttonsEdit = new ButtonEdit[7]; // state object buttons
    private int[][] mask; // level mask (where objects can be placed)
    private int active; // active object to place at the level

    /**
     * Constructor for the Editor.
     *
     * @param app  associated Game object.
     */
    public Editor(Application app) {
        super(app);
        loadPopups();
        loadDigits();
        createButtons();
        active = GameObject.PLAYER;
    }

    /**
     * Loads all overlaping screens.
     */
    private void loadPopups() {
        popups[0] = Image.loadImage(PAUSE_1);
        popups[1] = Image.loadImage(PAUSE_2);
        popups[2] = Image.loadImage(PAUSE_3);
    }

    /**
     * Creates all state buttons.
     */
    private void createButtons() {
        // Creating submenu buttons
        buttons[0] = new ButtonSmall(Buttons.Small.RESUME, Buttons.Pause.X_POS_2, Buttons.Pause.Y_POS_2_1, States.EDITOR, Stages.Editor.MAIN);
        buttons[1] = new ButtonSmall(Buttons.Small.QUIT, Buttons.Pause.X_POS_2, Buttons.Pause.Y_POS_2_2, States.EDITOR, Stages.Editor.EXIT);
        buttons[2] = new ButtonSmall(Buttons.Small.YES, Buttons.Pause.X_POS_1_1, Buttons.Pause.Y_POS_1, States.LOAD, Stages.Load.UPDATE);
        buttons[3] = new ButtonSmall(Buttons.Small.NO, Buttons.Pause.X_POS_1_2, Buttons.Pause.Y_POS_1, States.LOAD, Stages.Load.CREATE);

        // Creating object buttons
        buttonsEdit[0] = new ButtonEdit(Buttons.Edit.PLAYER, Buttons.Edit.X_POS_1, Buttons.Edit.Y_POS_1, States.EDITOR, GameObject.PLAYER);
        buttonsEdit[1] = new ButtonEdit(Buttons.Edit.ENEMY, Buttons.Edit.X_POS_2, Buttons.Edit.Y_POS_1, States.EDITOR, GameObject.ENEMY);
        buttonsEdit[2] = new ButtonEdit(Buttons.Edit.DOC, Buttons.Edit.X_POS_3, Buttons.Edit.Y_POS_2, States.EDITOR, GameObject.DOC);
        buttonsEdit[3] = new ButtonEdit(Buttons.Edit.HEALTH, Buttons.Edit.X_POS_4, Buttons.Edit.Y_POS_2, States.EDITOR, GameObject.HEALTH);
        buttonsEdit[4] = new ButtonEdit(Buttons.Edit.AMMO, Buttons.Edit.X_POS_5, Buttons.Edit.Y_POS_2, States.EDITOR, GameObject.AMMO);
        buttonsEdit[5] = new ButtonEdit(Buttons.Edit.OBSTACLE, Buttons.Edit.X_POS_1, Buttons.Edit.Y_POS_3, States.EDITOR, GameObject.OBSTACLE);
        buttonsEdit[6] = new ButtonEdit(Buttons.Edit.FINISH, Buttons.Edit.X_POS_2, Buttons.Edit.Y_POS_3, States.EDITOR, GameObject.FINISH);
    }

    /**
     * Updates the state.
     * Respects actual game stage.
     */
    @Override
    public void update() {
        if (States.stage == Stages.Editor.NEW || States.stage == Stages.Editor.LOAD) {
            if (States.stage == Stages.Editor.NEW) { // Create new level to edit
                level = new Level(app);
                level.getApp().getLogger().editor("Created new level for edit."); // Logging
            } else { // Open existing level to edit
                level = new Level(this.getApp().getLoad().getActive(), "lvl", app);
                level.getApp().getLogger().editor("Opened level '" + this.getApp().getLoad().getActive() + "' for edit."); // Logging
            }
            createMask();
            States.stage = 0;
        }

        // Update buttons
        for (ButtonSmall button: buttons) button.update();
        for (ButtonEdit button: buttonsEdit) button.update();
    }

    /**
     * Renders the state on the screen.
     * Respects actual game stage.
     *
     * @param graphics Graphics object used to draw the state.
     */
    @Override
    public void render(Graphics graphics) {
        if (level != null) {
            level.render(graphics); // Render level with all objects
            renderDigits(graphics); // Render info digits

            switch (States.stage) {
                case Stages.Editor.MAIN -> renderGrid(graphics); // Render game grid
                case Stages.Editor.PAUSE -> { // Render buttons
                    graphics.drawImage(popups[0], 0, 0, null);
                    buttons[0].render(graphics);
                    buttons[1].render(graphics);
                }
                case Stages.Editor.EXIT -> { // Render buttons
                    graphics.drawImage(popups[1], 0, 0, null);
                    buttons[2].render(graphics);
                    buttons[3].render(graphics);
                    this.getApp().getWindow().setTextField(true); // Enable text field
                }
                case Stages.Editor.OBJECTS -> { // Render buttons
                    graphics.drawImage(popups[2], 0, 0, null);
                    for (ButtonEdit button : buttonsEdit) button.render(graphics);
                }
            }
        }
    }

    /**
     * Updates current active object to place at level.
     */
    private void updateActive() {
        switch (States.stage) {
            case GameObject.PLAYER -> active = GameObject.PLAYER;
            case GameObject.ENEMY -> active = GameObject.ENEMY;
            case GameObject.DOC -> active = GameObject.DOC;
            case GameObject.HEALTH -> active = GameObject.HEALTH;
            case GameObject.AMMO -> active = GameObject.AMMO;
            case GameObject.OBSTACLE -> active = GameObject.OBSTACLE;
            case GameObject.FINISH -> active = GameObject.FINISH;
        }
    }

    /**
     * Creates array for level with information of empty cells where object can be placed.
     */
    private void createMask(){
        mask = new int[GAME_HEIGHT/SPRITE][GAME_WIDTH/SPRITE];

        // Occupy game header cells
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < mask[i].length; ++j) {
                mask[i][j]--;
            }
        }

        mask[(int)level.getPlayer().getY()/SPRITE][(int)level.getPlayer().getX()/SPRITE]--; // Occupy cell with player
        mask[(int)level.getFinish().getY()/SPRITE][(int)level.getFinish().getX()/SPRITE]--; // Occupy cell with finish position
        for (Enemy enemy : level.getEnemies()) mask[(int)enemy.getY()/SPRITE][(int)enemy.getX()/SPRITE]++; // Occupy cell with enemy
        for (Doc doc : level.getDocs()) mask[(int)doc.getY()/SPRITE][(int)doc.getX()/SPRITE]++; // Occupy cell with document
        for (HealthBox healthBox : level.getHealthBoxes()) mask[(int)healthBox.getY()/SPRITE][(int)healthBox.getX()/SPRITE]++; // Occupy cell with health box
        for (AmmoBox ammoBox : level.getAmmoBoxes()) mask[(int)ammoBox.getY()/SPRITE][(int)ammoBox.getX()/SPRITE]++; // Occupy cell with ammo box
        for (Obstacle obstacle : level.getObstacles()) mask[(int)obstacle.getY()/SPRITE][(int)obstacle.getX()/SPRITE]++; // Occupy cell with obstacle
    }

    /**
     * Places game object on the level map.
     * Adds active object if choosed cell is empty.
     * Deletes placed object if choosed cell isn't empty.
     * Method rounds input values respective the game grid.
     *
     * @param x x-coordinate of adding/deleting object.
     * @param y y-coordinate of adding/deleting object.
     */
    private void createObject(int x, int y) {
        if (mask[y/SPRITE][x/SPRITE] == 0) { // Cell is empty - place object
            switch (active) {
                case GameObject.PLAYER -> { // Place player (can be only one)
                    mask[(int)level.getPlayer().getY()/SPRITE][(int)level.getPlayer().getX()/SPRITE]++;
                    level.getPlayer().setPos(x / SPRITE * SPRITE, y / SPRITE * SPRITE);
                    mask[y/SPRITE][x/SPRITE]--;
                    level.getApp().getLogger().editor("Player " + level.getPlayer() + " was placed at [" + x + "," + y + "]."); // Logging
                }
                case GameObject.FINISH -> { // Place finish position (can be only one)
                    mask[(int)level.getFinish().getY()/SPRITE][(int)level.getFinish().getX()/SPRITE]++;
                    level.getFinish().setPos(x / SPRITE * SPRITE, y / SPRITE * SPRITE);
                    mask[y/SPRITE][x/SPRITE]--;
                    level.getApp().getLogger().editor("Finish position " + level.getFinish() + " was placed at [" + x + "," + y + "]."); // Logging
                }
                case GameObject.ENEMY -> { // Place enemy (can't be more than maximum amount)
                    level.createEnemy(x / SPRITE * SPRITE, y / SPRITE * SPRITE);
                    level.setEnemiesCount(level.getEnemies().size());
                    mask[y/SPRITE][x/SPRITE]++;
                }
                case GameObject.DOC -> { // Place document (can't be more than maximum amount)
                    level.createDoc(x / SPRITE * SPRITE, y / SPRITE * SPRITE);
                    level.setDocsCount(level.getDocs().size());
                    mask[y/SPRITE][x/SPRITE]++;
                }
                case GameObject.HEALTH -> { // Place health box (can't be more than maximum amount)
                    level.createHealthBox(x / SPRITE * SPRITE, y / SPRITE * SPRITE);
                    mask[y/SPRITE][x/SPRITE]++;
                }
                case GameObject.AMMO -> { // Place ammo box (can't be more than maximum amount)
                    level.createAmmoBox(x / SPRITE * SPRITE, y / SPRITE * SPRITE);
                    mask[y/SPRITE][x/SPRITE]++;
                }
                case GameObject.OBSTACLE -> { // Place obstacle
                    level.createObstacle(x / SPRITE * SPRITE, y / SPRITE * SPRITE);
                    mask[y/SPRITE][x/SPRITE]++;
                }
            }
        } else if (mask[y/SPRITE][x/SPRITE] == 1) { // Cell is occupied - delete object
            level.deleteObject(x / SPRITE * SPRITE , y / SPRITE * SPRITE);
            mask[y/SPRITE][x/SPRITE]--;
        }
    }

    /**
     * Draws game grid.
     *
     * @param graphics Graphics object used to draw.
     */
    private void renderGrid(Graphics graphics) {
        graphics.setColor(Colors.ASPHALT);
        for (int i = 0; i < GAME_WIDTH / SPRITE; ++i) {
            for (int j = 3; j < GAME_HEIGHT / SPRITE; ++j) {
                graphics.drawRect(i*SPRITE, j*SPRITE, SPRITE, SPRITE);
            }
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
            case Stages.Editor.PAUSE -> {
                for (int i = 0; i < 2; ++i) { // Check if mouse is over buttons
                    buttons[i].setMouseOver(false);
                    if (isButton(e, buttons[i])) buttons[i].setMouseOver(true);
                }
            }
            case Stages.Editor.EXIT -> {
                for (int i = 2; i < 4; ++i) { // Check if mouse is over buttons
                    buttons[i].setMouseOver(false);
                    if (isButton(e, buttons[i])) buttons[i].setMouseOver(true);
                }
            }
            case Stages.Editor.OBJECTS -> {
                for (ButtonEdit button : buttonsEdit) { // Check if mouse is over buttons
                    button.setMouseOver(false);
                    if (isButton(e, button)) button.setMouseOver(true);
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
            case Stages.Editor.PAUSE -> {
                for (int i = 0; i < 2; ++i) { // Check if mouse presses on buttons
                    if (isButton(e, buttons[i])) {
                        buttons[i].setMousePressed(true);
                        break;
                    }
                }
            }
            case Stages.Editor.EXIT -> {
                for (int i = 2; i < 4; ++i) { // Check if mouse presses on buttons
                    if (isButton(e, buttons[i])) {
                        buttons[i].setMousePressed(true);
                        break;
                    }
                }
            }
            case Stages.Editor.OBJECTS -> { // Check if mouse presses on buttons
                for (ButtonEdit button : buttonsEdit) {
                    if (isButton(e, button)) {
                        button.setMousePressed(true);
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
            case Stages.Editor.MAIN -> createObject(e.getX(), e.getY()); // Place object
            case Stages.Editor.PAUSE -> {
                for (int i = 0; i < 2; ++i) { // Check if buttons pressed
                    if (isButton(e, buttons[i]) && buttons[i].isMousePressed()) buttons[i].buttonAction();
                    buttons[i].setMouseOver(false);
                    buttons[i].setMousePressed(false);
                }
            }
            case Stages.Editor.EXIT -> {
                for (int i = 2; i < 4; ++i) { // Check if buttons pressed
                    if (isButton(e, buttons[i]) && buttons[i].isMousePressed()) {
                        if (i == 3 || checkText(this.getApp().getWindow().getTextField())) { // Not saving or filename is ok
                            if (i == 2) {
                                level.saveLevel(this.getApp().getWindow().getTextField(), "lvl");
                                level.getApp().getLogger().editor("Level was saved as '" + this.getApp().getWindow().getTextField() + "'."); // Logging
                            } else {
                                level.getApp().getLogger().editor("Exit without saving."); // Logging
                            }
                            this.getApp().getLoad().setActive(null);
                            buttons[i].buttonAction();
                            if (States.state == States.MENU || States.state == States.LOAD) {
                                level = null;
                                this.getApp().getLoad().setActive(null);
                                this.getApp().getAudio().playSong(Sounds.MENU); // Play menu song if returned to menu
                            }
                        } else { // Saving with invalid filename
                            this.getApp().getAudio().playSound(Sounds.ERROR);
                        }
                    }
                    buttons[i].setMouseOver(false);
                    buttons[i].setMousePressed(false);
                }
            }
            case Stages.Editor.OBJECTS -> { // Check if buttons pressed
                for (ButtonEdit button : buttonsEdit) {
                    if (isButton(e, button) && button.isMousePressed()) {
                        button.buttonAction();
                        updateActive();
                        States.stage = 0;
                    }
                    button.setMouseOver(false);
                    button.setMousePressed(false);
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
        if (States.stage == Stages.Editor.MAIN) { // Change player attributes only in main editor state
            int tmpHealth = level.getPlayer().getHealth();
            int tmpAmmo = level.getPlayer().getAmmo();

            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP -> {if (tmpHealth != Limits.HEALTH_MAX) level.getPlayer().setHealth(tmpHealth + 1);} // Increase health
                case KeyEvent.VK_DOWN -> {if (tmpHealth != Limits.HEALTH_MIN) level.getPlayer().setHealth(tmpHealth - 1);} // Decrease health
                case KeyEvent.VK_LEFT -> {if (tmpAmmo != Limits.AMMO_MIN) level.getPlayer().setAmmo(tmpAmmo - 1);} // Increase ammo
                case KeyEvent.VK_RIGHT -> {if (tmpAmmo != Limits.AMMO_MAX) level.getPlayer().setAmmo(tmpAmmo + 1);} // Decrease ammo
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
        if (States.stage == Stages.Editor.MAIN) { // "Pause" or "choose object" only from main editor state
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP, KeyEvent.VK_DOWN -> level.getApp().getLogger().editor("Player's health was set up to " + level.getPlayer().getHealth() + "."); // Logging
                case KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT -> level.getApp().getLogger().editor("Player's ammo was set up to " + level.getPlayer().getAmmo() + "."); // Logging
                case KeyEvent.VK_ESCAPE -> States.stage = Stages.Editor.PAUSE; // Pause game
                case KeyEvent.VK_SPACE -> States.stage = Stages.Editor.OBJECTS; // Change an active object to place
            }
        } else {
            switch (e.getKeyCode()) { // Return to main editor state
                case KeyEvent.VK_ESCAPE -> {if (States.stage == Stages.Editor.PAUSE) States.stage = Stages.Editor.MAIN;}
                case KeyEvent.VK_SPACE -> {if (States.stage == Stages.Editor.OBJECTS) States.stage = Stages.Editor.MAIN;}
            }
        }
    }
}
