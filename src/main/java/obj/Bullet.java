package obj;

import static util.Const.*;
import static util.Const.Soldier.*;
import static util.Const.GUI.*;
import obj.soldier.Enemy;
import org.json.JSONException;
import org.json.JSONObject;
import util.Level;
import util.Image;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Class for bullet object.
 * Extends abstract Game object class.
 */
public class Bullet extends GameObj implements Movable {
    private final int direction; // bullet movement direction
    private boolean active; // is bullet active at the moment
    private int limit; // bullet range

    /**
     * Constructor for the Bullet.
     *
     * @param x         x-coordinate of bullet.
     * @param y         y-coordinate of bullet.
     * @param direction bullet movement direction.
     * @param level     associated Level object.
     */
    public Bullet(int x, int y, int direction, Level level) {
        super(x, y, level);
        this.direction = direction;
        limit = (int)(Limits.RANGE * 0.75);
        active = true;
        loadSprites();
    }

    /**
     * Constructor for the Bullet.
     *
     * @param json  bullet object from JSON file.
     * @param level associated Level object.
     */
    public Bullet(JSONObject json, Level level) {
        super(json.getInt(JSON.X), json.getInt(JSON.Y), level);
        direction = json.getInt(JSON.DIRECTION);
        limit = json.getInt(JSON.LIMIT);
        active = true;
        loadSprites();
    }

    /**
     * Loads images of bullet object.
     */
    private void loadSprites() {
        BufferedImage tmp = Image.loadImage(BULLET);
        sprites = new BufferedImage[4][1][1];
        for (int i = 0; i < sprites.length; ++i) {
            sprites[i][0][0] = tmp.getSubimage(0,i * GameObject.BULLET, GameObject.BULLET, GameObject.BULLET);
        }
    }

    /**
     * Updates bullet position and its state.
     */
    public void update() {
        if (active) {
            switch (direction) {
                case UP -> moveUp();
                case DOWN -> moveDown();
                case LEFT -> moveLeft();
                case RIGHT -> moveRight();
            }
        }
    }

    /**
     * Renders the bullet on the screen.
     *
     * @param graphics Graphics object used to draw the bullet.
     */
    public void render(Graphics graphics) {
        if (active) graphics.drawImage(sprites[direction][0][0], (int)x, (int)y, GameObject.BULLET, GameObject.BULLET,null);
    }

    /**
     * Moves bullet to the left.
     */
    @Override
    public void moveLeft() {
        if (x > 0) { // Bullet doesn't cross left screen border
            if (limit == 0) active = false; // Range was exhausted
            x -= Limits.BULLET;
            limit--;
            affect();
        } else {
            active = false;
        }
    }

    /**
     * Moves bullet to the right.
     */
    @Override
    public void moveRight() {
        if (x < GAME_WIDTH) { // Bullet doesn't cross right screen border
            if (limit == 0) active = false; // Range was exhausted
            x += Limits.BULLET;
            limit--;
            affect();
        } else {
            active = false;
        }
    }

    /**
     * Moves bullet upwards.
     */
    @Override
    public void moveUp() {
        if (y > 3*SPRITE) { // Bullet doesn't cross upper screen border
            if (limit == 0) active = false; // Range was exhausted
            y -= Limits.BULLET;
            limit--;
            affect();
        } else {
            active = false;
        }
    }

    /**
     * Moves bullet downwards.
     */
    @Override
    public void moveDown() {
        if (y < GAME_HEIGHT) { // Bullet doesn't cross bottom screen border
            if (limit == 0) active = false; // Range was exhausted
            y += Limits.BULLET;
            limit--;
            affect();
        } else {
            active = false;
        }
    }

    /**
     * Processes effect of the bullet's interaction with other objects.
     */
    private void affect() {
        for (Obstacle obstacle : level.getObstacles()) {
            if (obstacle.getHitbox().contains(x, y)) { // Bullet hit obstacle
                active = false;
                break;
            }
        }

        for (Enemy enemy : level.getEnemies()) {
            if (enemy.getHitbox().contains(x, y) && !enemy.isDead()) { // Bullet hit enemy
                active = false;
                enemy.setHealth(enemy.getHealth()-1);
                if (level.getApp() != null) level.getApp().getAudio().playSound(Sounds.HIT);
                level.getApp().getLogger().game("Enemy " + enemy + " was hit. Health remained: " + enemy.getHealth() + "."); // Logging
                break;
            }
        }

        if (level.getPlayer().getHitbox().contains(x, y)) { // Bullet hit player
            active = false;
            level.getPlayer().setHealth(level.getPlayer().getHealth()-1);
            if (level.getApp() != null) level.getApp().getAudio().playSound(Sounds.HIT);
            level.getApp().getLogger().game("Player " + level.getPlayer() + " was hit. Health remained: " + level.getPlayer().getHealth() + "."); // Logging
        }
    }

    /**
     * Makes JSON object from the bullet to write into the file.
     *
     * @return JSON object for writing
     */
    public JSONObject getJSON() {
        JSONObject json = new JSONObject();
        try {
            json.put(JSON.X, x);
            json.put(JSON.Y, y);
            json.put(JSON.DIRECTION, direction);
            json.put(JSON.LIMIT, limit);
        } catch (JSONException e) {
            level.getApp().getLogger().error("Unable to write JSON object!"); // Logging
        }

        return json;
    }
}
