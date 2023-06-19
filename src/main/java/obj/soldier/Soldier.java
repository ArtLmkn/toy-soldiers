package obj.soldier;

import obj.GameObj;
import obj.Movable;
import obj.Obstacle;
import org.json.JSONException;
import util.Image;
import util.Level;
import static util.Const.*;
import static util.Const.GUI.*;
import static util.Const.Soldier.*;

import org.json.JSONObject;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * The abstract class for alive game entities.
 * Extends abstract Game object class.
 * Implements Mavable interface, because objects can move.
 */
public abstract class Soldier extends GameObj implements Movable {
    protected int health; // soldier health amount
    protected int ammo; // soldier ammo count
    protected int direction; // curren movement direction
    protected int animFrame, animIndex; // animation variables
    protected boolean walking, shooting, dead; // soldier activity states
    protected boolean up, down, left, right; // is soldier moving in specified direction

    /**
     * Constructor for the Soldier.
     *
     * @param x     x-coordinate of soldier.
     * @param y     y-coordinate of soldier.
     */
    public Soldier(int x, int y, Level level) {
        super(x, y, level);
        direction = UP;
        walking = false;
        shooting = false;
        dead = false;
        hitbox = new Rectangle(x + 13, y + 13, 40, 40);
    }

    /**
     * Constructor for the Soldier.
     *
     * @param json  soldier object from JSON file.
     * @param level associated Level object.
     */
    public Soldier(JSONObject json, Level level) {
        super(json.getInt(JSON.X), json.getInt(JSON.Y), level);
        direction = json.getInt(JSON.DIRECTION);
        health = json.getInt(JSON.HEALTH);
        walking = false;
        shooting = false;
        dead = health == 0;
        hitbox = new Rectangle((int) x + 13, (int) y + 13, 40, 40);
    }

    /**
     * Loads images and animations of soldier.
     *
     * @param type type of soldier (position in image file).
     */
    protected void loadSprites(int type) {
        BufferedImage tmp = Image.loadImage(SOLDIERS);
        sprites = new BufferedImage[4][3][4];
        for (int k = 0; k < sprites.length; ++k) { // Load all direction images
            for (int j = 0; j < sprites[k].length; ++j) { // Load all action images
                for (int i = 0; i < sprites[k][j].length; ++i) { // Load all animation frames
                    sprites[k][j][i] = tmp.getSubimage((j * 4 + i) * GameObject.W, (k + type * 4) * GameObject.H, GameObject.W, GameObject.H);
                }
            }
        }
    }

    /**
     * Renders the soldier on the screen.
     *
     * @param graphics Graphics object used to draw the soldier.
     */
    public void render(Graphics graphics) {
        if (shooting) { // Draw shooting action
            graphics.drawImage(sprites[direction][SHOOT][animIndex], (int) x, (int) y, SPRITE, SPRITE, null);
        } else if (walking) {  // Draw walking action
            graphics.drawImage(sprites[direction][WALK][animIndex], (int) x, (int) y, SPRITE, SPRITE, null);
        } else if (dead) {  // Draw dying action
            graphics.drawImage(sprites[direction][DIE][animIndex], (int) x, (int) y, SPRITE, SPRITE, null);
        } else {  // Draw standing action
            graphics.drawImage(sprites[direction][WALK][0], (int) x, (int) y, SPRITE, SPRITE, null);
        }
    }

    /**
     * Processes soldier animation.
     */
    protected void updateAnim() {
        animFrame++;
        if (animFrame >= FPS / 2) {
            animFrame = 0;
            if (!dead) {animIndex = animIndex == 3 ? 0 : animIndex + 1;} else {animIndex = 3;}
        }
    }

    /**
     * Updates soldiers interaction hitbox.
     */
    protected void updateHitbox() {
        hitbox = new Rectangle((int) x + 13, (int) y + 13, hitbox.width, hitbox.height);
    }

    /**
     * Makes a shot.
     * Method creates Bullet object in the actual level.
     */
    public void shoot() {
        shooting = true;

        switch (direction) {
            case UP -> level.createBullet((int) x + 32, (int) y, direction, level); // Shoot up
            case DOWN -> level.createBullet((int) x + 22, (int) y + 55, direction, level); // Shoot down
            case LEFT -> level.createBullet((int) x, (int) y + 22, direction, level); // Shoot left
            case RIGHT -> level.createBullet((int) x + 55, (int) y + 32, direction, level); // Shoot right
        }

        if (level.getApp() != null) level.getApp().getAudio().playSound(Sounds.SHOOT);
    }

    /**
     * Moves soldier to the left.
     */
    @Override
    public void moveLeft() {
        boolean canMove = true;

        if (hitbox.x - Limits.SPEED > 0) { // Soldier doesn't cross left screen border
            for (Obstacle obstacle : level.getObstacles()) { // Control of going through obstacle
                boolean cornerOne = obstacle.getHitbox().contains(hitbox.getX() - 1, hitbox.getY());
                boolean cornerTwo = obstacle.getHitbox().contains(hitbox.getX() - 1, hitbox.getY() + 40);
                if (cornerOne || cornerTwo) {canMove = false; break;}
            }

            for (Enemy enemy : level.getEnemies()) { // Control of going through enemy
                boolean cornerOne = enemy.getHitbox().contains(hitbox.getX() - 1, hitbox.getY());
                boolean cornerTwo = enemy.getHitbox().contains(hitbox.getX() - 1, hitbox.getY() + 40);
                if ((cornerOne || cornerTwo) && !enemy.isDead()) {canMove = false; break;}
            }

            // Control of going through player
            boolean cornerOne = level.getPlayer().getHitbox().contains(hitbox.getX() - 1, hitbox.getY());
            boolean cornerTwo = level.getPlayer().getHitbox().contains(hitbox.getX() - 1, hitbox.getY() + 40);
            if (cornerOne || cornerTwo) canMove = false;
        } else {canMove = false;}

        if (canMove) { // Nothing stands on the way left
            walking = true;
            direction = LEFT;
            x -= Limits.SPEED;
            updateHitbox();
        }
    }

    /**
     * Moves soldier to the right.
     */
    @Override
    public void moveRight() {
        boolean canMove = true;

        if (hitbox.x + Limits.SPEED < GAME_WIDTH - 40) {  // Soldier doesn't cross right screen border
            for (Obstacle obstacle : level.getObstacles()) { // Control of going through obstacle
                boolean cornerOne = obstacle.getHitbox().contains(hitbox.getX() + 41, hitbox.getY());
                boolean cornerTwo = obstacle.getHitbox().contains(hitbox.getX() + 41, hitbox.getY() + 40);
                if (cornerOne || cornerTwo) {canMove = false; break;}
            }

            for (Enemy enemy : level.getEnemies()) { // Control of going through enemy
                boolean cornerOne = enemy.getHitbox().contains(hitbox.getX() + 41, hitbox.getY());
                boolean cornerTwo = enemy.getHitbox().contains(hitbox.getX() + 41, hitbox.getY() + 40);
                if ((cornerOne || cornerTwo) && !enemy.isDead()) {canMove = false;break;}
            }

            // Control of going through player
            boolean cornerOne = level.getPlayer().getHitbox().contains(hitbox.getX() + 41, hitbox.getY());
            boolean cornerTwo = level.getPlayer().getHitbox().contains(hitbox.getX() + 41, hitbox.getY() + 40);
            if (cornerOne || cornerTwo) canMove = false;
        } else { canMove = false;}

        if (canMove) { // Nothing stands on the way right
            walking = true;
            direction = RIGHT;
            x += Limits.SPEED;
            updateHitbox();
        }
    }

    /**
     * Moves soldier upwards.
     */
    @Override
    public void moveUp() {
        boolean canMove = true;

        if (hitbox.y - Limits.SPEED > 3 * SPRITE) { // Soldier doesn't cross upper screen border
            for (Obstacle obstacle : level.getObstacles()) { // Control of going through obstacle
                boolean cornerOne = obstacle.getHitbox().contains(hitbox.getX(), hitbox.getY() - 1);
                boolean cornerTwo = obstacle.getHitbox().contains(hitbox.getX() + 40, hitbox.getY() - 1);
                if (cornerOne || cornerTwo) {canMove = false; break;}
            }

            for (Enemy enemy : level.getEnemies()) { // Control of going through enemy
                boolean cornerOne = enemy.getHitbox().contains(hitbox.getX(), hitbox.getY() - 1);
                boolean cornerTwo = enemy.getHitbox().contains(hitbox.getX() + 40, hitbox.getY() - 1);
                if ((cornerOne || cornerTwo) && !enemy.isDead()) {canMove = false; break;}
            }

            // Control of going through player
            boolean cornerOne = level.getPlayer().getHitbox().contains(hitbox.getX(), hitbox.getY() - 1);
            boolean cornerTwo = level.getPlayer().getHitbox().contains(hitbox.getX() + 40, hitbox.getY() - 1);
            if (cornerOne || cornerTwo) canMove = false;
        } else {canMove = false;}

        if (canMove) { // Nothing stands on the way up
            walking = true;
            direction = UP;
            y -= Limits.SPEED;
            updateHitbox();
        }
    }

    /**
     * Moves soldier downwards.
     */
    @Override
    public void moveDown() {
        boolean canMove = true;

        if (hitbox.y + Limits.SPEED < GAME_HEIGHT - 40) { // Soldier doesn't cross bottom screen border
            for (Obstacle obstacle : level.getObstacles()) {  // Control of going through obstacle
                boolean cornerOne = obstacle.getHitbox().contains(hitbox.getX(), hitbox.getY() + 41);
                boolean cornerTwo = obstacle.getHitbox().contains(hitbox.getX() + 40, hitbox.getY() + 41);
                if (cornerOne || cornerTwo) {canMove = false; break;}
            }

            for (Enemy enemy : level.getEnemies()) { // Control of going through enemy
                boolean cornerOne = enemy.getHitbox().contains(hitbox.getX(), hitbox.getY() + 41);
                boolean cornerTwo = enemy.getHitbox().contains(hitbox.getX() + 40, hitbox.getY() + 41);
                if ((cornerOne || cornerTwo) && !enemy.isDead()) {canMove = false; break;}
            }

            // Control of going through player
            boolean cornerOne = level.getPlayer().getHitbox().contains(hitbox.getX(), hitbox.getY() + 41);
            boolean cornerTwo = level.getPlayer().getHitbox().contains(hitbox.getX() + 40, hitbox.getY() + 41);
            if (cornerOne || cornerTwo) canMove = false;
        } else {canMove = false;}

        if (canMove) { // Nothing stands on the way down
            walking = true;
            direction = DOWN;
            y += Limits.SPEED;
            updateHitbox();
        }
    }

    /**
     * Makes JSON object from the soldier to write into the file.
     *
     * @return JSON object for writing
     */
    public JSONObject getJSON(int type) {
        JSONObject json = new JSONObject();
        try {
            json.put(JSON.X, x);
            json.put(JSON.Y, y);
            json.put(JSON.DIRECTION, direction);
            json.put(JSON.HEALTH, health);
            if (type == GameObject.PLAYER) json.put(JSON.AMMO, ammo);
        } catch (JSONException e) {
            level.getApp().getLogger().error("Unable to write JSON object!"); // Logging
        }

        return json;
    }

    /**
     * Returns soldier's health amount.
     *
     * @return  health amount.
     */
    public int getHealth() {
        return health;
    }

    /**
     * Returns soldier's ammo count.
     *
     * @return  ammo count.
     */
    public int getAmmo() {
        return ammo;
    }

    /**
     * Sets new health amount to the soldier.
     *
     * @param health  health amount.
     */
    public void setHealth(int health) {
        this.health = health;
    }

    /**
     * Sets new ammo count to the soldier.
     *
     * @param ammo  ammo count.
     */
    public void setAmmo(int ammo) {
        this.ammo = ammo;
    }

    /**
     * Returns if soldier is dead.
     *
     * @return  true if soldier is dead, false otherwise.
     */
    public boolean isDead() {
        return dead;
    }

    /**
     * Returns if soldier is walking.
     *
     * @return  true if soldier is walking, false otherwise.
     */
    public boolean isWalking() {
        return walking;
    }

    /**
     * Returns if soldier is shooting.
     *
     * @return  true if soldier is shooting, false otherwise.
     */
    public boolean isShooting() {
        return shooting;
    }

    /**
     * Returns soldier's movement direction.
     *
     * @return  movement direction.
     */
    public int getDirection() {
        return direction;
    }

    /**
     * Sets movement direction to the soldier.
     *
     * @param direction  new direction.
     */
    public void setDirection(int direction) {
        this.direction = direction;
    }

    /**
     * Sets if soldier is walking.
     *
     * @param walking  true if soldier is walking, false otherwise.
     */
    public void setWalking(boolean walking) {
        this.walking = walking;
    }

    /**
     * Sets if soldier is shooting.
     *
     * @param shooting  true if soldier is shooting, false otherwise.
     */
    public void setShooting(boolean shooting) {
        if (!shooting) animFrame = 0;
        this.shooting = shooting;
    }

    /**
     * Sets if soldier is walking upwards.
     *
     * @param up  true if soldier is walking upwards, false otherwise.
     */
    public void setUp(boolean up) {
        walking = up || down || left || right; // Calculate walking state
        if (!walking) animFrame = 0; // Reset animation frame
        this.up = up;
    }

    /**
     * Sets if soldier is walking downwards.
     *
     * @param down  true if soldier is walking downwards, false otherwise.
     */
    public void setDown(boolean down) {
        walking = up || down || left || right; // Calculate walking state
        if (!walking) animFrame = 0; // Reset animation frame
        this.down = down;
    }

    /**
     * Sets if soldier is walking to the left.
     *
     * @param left  true if soldier is walking to the left, false otherwise.
     */
    public void setLeft(boolean left) {
        walking = up || down || left || right; // Calculate walking state
        if (!walking) animFrame = 0; // Reset animation frame
        this.left = left;
    }

    /**
     * Sets if soldier is walking to the right.
     *
     * @param right  true if soldier is walking to the right, false otherwise.
     */
    public void setRight(boolean right) {
        walking = up || down || left || right; // Calculate walking state
        if (!walking) animFrame = 0; // Reset animation frame
        this.right = right;
    }
}
