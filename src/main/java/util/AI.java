package util;

import obj.soldier.Enemy;
import obj.Obstacle;
import obj.soldier.Player;
import static util.Const.*;
import static util.Const.Soldier.*;

import java.util.Random;

/**
 * Artificial Intelligence for the game.
 * Controls enemies.
 */
public class AI {
    private final Level level; // associated level
    private final Enemy enemy; // associated enemy
    private int direction; // movement direction
    private int frame; // animation frame

    /**
     * Constructor for the AI.
     *
     * @param level  Level instance where AI exists.
     * @param enemy  enemy controlled by AI.
     */
    public AI(Level level, Enemy enemy) {
        this.level = level;
        this.enemy = enemy;
        setRandomDirection();
        frame = 0;
    }

    /**
     * Controls enemy.
     */
    public void update() {
        if (isPlayerVisible(enemy.getDirection()) && isPlayerNear()) {
            // Attack player if he is nearby and visible
            attackPlayer();
        } else {
            // Stop attacking and start patrolling area
            enemy.setShooting(false);
            patrol();
        }
    }

    /**
     * Determines if player can be seen by this enemy.
     *
     * @param direction  current direction of enemy's view.
     * @return true if player can be seen by enemy, false otherwise.
     */
    private boolean isPlayerVisible(int direction) {
        Player player = level.getPlayer();

        // Player is visible above (only if enemy don't go down)
        if (direction != DOWN && player.getX() - 15 <= enemy.getX() && enemy.getX() <= player.getX() + 15 && player.getY() < enemy.getY()) {
            boolean visible = true;
            for (Obstacle obstacle : level.getObstacles()) { // Control if some obstacle is blocking the view
                boolean betweenX = obstacle.getX() - 32 <= enemy.getX() && enemy.getX() <= obstacle.getX() + 32 ;
                boolean betweenY = obstacle.getY() > player.getY() && obstacle.getY() < enemy.getY();
                if (betweenX && betweenY) {visible = false; break;}
            }
            for (Enemy enemy : level.getEnemies()) { // Control if some other alive enemy is blocking the view
                boolean betweenX = enemy.getX() - 32 <= this.enemy.getX() && this.enemy.getX() <= enemy.getX() + 32 ;
                boolean betweenY = enemy.getY() > player.getY() && this.enemy.getY() < enemy.getY();
                if (!enemy.isDead() && betweenX && betweenY) {visible = false; break;}
            }
            if (visible) {enemy.setDirection(UP); return true;} // Nothing blocks the view - player is finally visible
        }

        // Player is visible below (only if enemy don't go up)
        if (direction != UP && player.getX() - 5 <= enemy.getX() && enemy.getX() <= player.getX() + 25 && player.getY() > enemy.getY()) {
            boolean visible = true;
            for (Obstacle obstacle : level.getObstacles()) { // Control if some obstacle blocking the view
                boolean betweenX = obstacle.getX() - 32 <= enemy.getX() && enemy.getX() <= obstacle.getX() + 32 ;
                boolean betweenY = obstacle.getY() > enemy.getY() && obstacle.getY() < player.getY();
                if (betweenX && betweenY) {visible = false; break;}
            }
            for (Enemy enemy : level.getEnemies()) { // Control if some other alive enemy is blocking the view
                boolean betweenX = enemy.getX() - 32 <= this.enemy.getX() && this.enemy.getX() <= enemy.getX() + 32 ;
                boolean betweenY = enemy.getY() > this.enemy.getY() && enemy.getY() < player.getY();
                if (!enemy.isDead() && betweenX && betweenY) {visible = false; break;}
            }
            if (visible) {enemy.setDirection(DOWN); return true;} // Nothing blocks the view - player is finally visible
        }

        // Player is visible on the left (only if enemy don't go right)
        if (direction != RIGHT && player.getY() - 5 <= enemy.getY() && enemy.getY() <= player.getY() + 25 && player.getX() < enemy.getX()) {
            boolean visible = true;
            for (Obstacle obstacle : level.getObstacles()) { // Control if some obstacle blocking the view
                boolean betweenX = obstacle.getX() > player.getX() && obstacle.getX() < enemy.getX();
                boolean betweenY = obstacle.getY() - 32 <= enemy.getY() && enemy.getY() <= obstacle.getY() + 32 ;
                if (betweenX && betweenY) {visible = false; break;}
            }
            for (Enemy enemy : level.getEnemies()) { // Control if some other alive enemy is blocking the view
                boolean betweenX = enemy.getX() > player.getX() && enemy.getX() < this.enemy.getX();
                boolean betweenY = enemy.getY() - 32 <= this.enemy.getY() && this.enemy.getY() <= enemy.getY() + 32 ;
                if (!enemy.isDead() && betweenX && betweenY) {visible = false; break;}
            }
            if (visible) {enemy.setDirection(LEFT); return true;} // Nothing blocks the view - player is finally visible
        }

        // Player is visible on the right (only if enemy don't go left)
        if (direction != LEFT && player.getY() - 15 <= enemy.getY() && enemy.getY() <= player.getY() + 15 && player.getX() > enemy.getX()) {
            boolean visible = true;
            for (Obstacle obstacle : level.getObstacles()) { // Control if some obstacle blocking the view
                boolean betweenX = obstacle.getX() > enemy.getX() && obstacle.getX() < player.getX();
                boolean betweenY = obstacle.getY() - 32 <= enemy.getY() && enemy.getY() <= obstacle.getY() + 32 ;
                if (betweenX && betweenY) {visible = false; break;}
            }
            for (Enemy enemy : level.getEnemies()) { // Control if some other alive enemy is blocking the view
                boolean betweenX = enemy.getX() > this.enemy.getX() && enemy.getX() < player.getX();
                boolean betweenY = enemy.getY() - 32 <= this.enemy.getY() && this.enemy.getY() <= enemy.getY() + 32 ;
                if (!enemy.isDead() && betweenX && betweenY) {visible = false; break;}
            }
            if (visible) {enemy.setDirection(RIGHT); return true;} // Nothing blocks the view - player is finally visible
        }

        return false; // player isn't visible
    }

    /**
     * Determines if player is somewhere near to enemy.
     *
     * @return true if player is near, false otherwise.
     */
    private boolean isPlayerNear() {
        float x = enemy.getX() - level.getPlayer().getX(); // horizontal difference
        float y = enemy.getY() - level.getPlayer().getY(); // vertical difference
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)) < Limits.RANGE;
    }

    /**
     * Method to attack player by enemy.
     */
    private void attackPlayer() {
        enemy.setWalking(false); // Stop moving

        // Prevents too fast shooting
        if (frame > Limits.REALOAD) {
            enemy.shoot();
            frame = 0;
        }
        frame++;
    }

    /**
     * Method to patrolling area when enemy don't see player.
     * Direction of movement and duration are randomly determined.
     * Prevents enemy to going "into the obstacle".
     */
    private void patrol() {
        float pos;
        switch (direction) {
            case UP -> { // Control if still can move upwards
                pos = enemy.getY();
                enemy.moveUp();
                if (pos == enemy.getY()) setRandomDirection(UP);
            }
            case DOWN -> { // Control if still can move downwards
                pos = enemy.getY();
                enemy.moveDown();
                if (pos == enemy.getY()) setRandomDirection(DOWN);
            }
            case LEFT -> { // Control if still can move to the left
                pos = enemy.getX();
                enemy.moveLeft();
                if (pos == enemy.getX()) setRandomDirection(LEFT);
            }
            case RIGHT -> { // Control if still can move to the right
                pos = enemy.getX();
                enemy.moveRight();
                if (pos == enemy.getX()) setRandomDirection(RIGHT);
            }
        }

        // Randomly chooses duration of the movement in this direction
        if (frame > new Random().nextInt(1500) + 500) {
            switch (direction) { // Prevents sudden 180-degree turns
                case UP -> setRandomDirection(DOWN);
                case DOWN -> setRandomDirection(UP);
                case LEFT -> setRandomDirection(RIGHT);
                case RIGHT -> setRandomDirection(LEFT);
            }
            frame = 0;
        }
        frame++;
    }

    /**
     * Randomly chooses movement direction.
     */
    private void setRandomDirection() {
        this.direction = new Random().nextInt(4);
    }

    /**
     * Randomly chooses movement direction different from the previous one.
     *
     * @param except previous movement direction.
     */
    private void setRandomDirection(int except) {
        int direction = new Random().nextInt(4);
        while (direction == except) {direction = new Random().nextInt(4);}
        this.direction = direction;
    }
}
