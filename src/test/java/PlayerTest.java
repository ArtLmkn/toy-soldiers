import obj.soldier.Player;
import util.Level;
import static util.Const.*;
import static util.Const.Limits.*;
import static util.Const.Soldier.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.awt.Rectangle;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests Player class.
 */
public class PlayerTest {
    private Player player; // player to test
    private Level level; // associated level

    /**
     * Set up before tests.
     * Creates level construction and adds player there.
     */
    @BeforeEach
    void setUp() {
        level = new Level(null);
        player = new Player(640, 640, level);
        level.setPlayer(player);
    }

    /**
     * Tests correct initialisation of x coordinate.
     */
    @Test
    void xInit() {
        assertEquals(640, player.getX());
    }

    /**
     * Tests correct initialisation of y coordinate.
     */
    @Test
    void yInit() {
        assertEquals(640, player.getY());
    }

    /**
     * Tests correct initialisation of player's health.
     */
    @Test
    void healthInit() {
        assertEquals(3, player.getHealth());
    }

    /**
     * Tests correct initialisation of player's ammo.
     */
    @Test
    void ammoInit() {
        assertEquals(30, player.getAmmo());
    }

    /**
     * Tests correct initialisation of walking action.
     */
    @Test
    void walkingInit() {
        assertFalse(player.isWalking());
    }

    /**
     * Tests correct initialisation of shooting action.
     */
    @Test
    void shootingInit() {
        assertFalse(player.isShooting());
    }

    /**
     * Tests correct initialisation if player alive.
     */
    @Test
    void deadInit() {
        assertFalse(player.isDead());
    }

    /**
     * Tests correct player's death.
     */
    @Test
    void deadZeroHealth() {
        player.setHealth(0);
        player.update();
        assertTrue(player.isDead());
    }

    /**
     * Tests correct setting "up" direction.
     */
    @Test
    void directionUp() {
        player.setUp(true);
        player.update();
        assertEquals(UP, player.getDirection());
    }

    /**
     * Tests correct setting "down" direction.
     */
    @Test
    void directionDown() {
        player.setDown(true);
        player.update();
        assertEquals(DOWN, player.getDirection());
    }

    /**
     * Tests correct setting "left" direction.
     */
    @Test
    void directionLeft() {
        player.setLeft(true);
        player.update();
        assertEquals(LEFT, player.getDirection());
    }

    /**
     * Tests correct setting "right" direction.
     */
    @Test
    void directionRight() {
        player.setRight(true);
        player.update();
        assertEquals(RIGHT, player.getDirection());
    }

    /**
     * Tests correct ammo decreasing when player is shooting.
     */
    @Test
    void shootDecreasesAmmo() {
        int ammoCount = player.getAmmo();
        player.shoot();
        assertEquals(ammoCount - 1, player.getAmmo());
    }

    /**
     * Tests correct behaviour when shooting with no ammo.
     */
    @Test
    void shootZeroAmmo() {
        player.setAmmo(0);
        player.shoot();
        assertEquals(0, player.getAmmo());
    }

    /**
     * Tests correct "up" movement.
     */
    @Test
    void movingUp() {
        // Coordinates before making step
        float xBefore = player.getX();
        float yBefore = player.getY();

        player.moveUp();

        // Coordinates after making step
        float xAfter = player.getX();
        float yAfter = player.getY();

        // Checks if step up was made
        boolean xEquals = xBefore == xAfter;
        boolean yEquals = yBefore - SPEED == yAfter;
        assertTrue(xEquals && yEquals);
    }

    /**
     * Tests correct "down" movement.
     */
    @Test
    void movingDown() {
        // Coordinates before making step
        float xBefore = player.getX();
        float yBefore = player.getY();

        player.moveDown();

        // Coordinates after making step
        float xAfter = player.getX();
        float yAfter = player.getY();

        // Checks if step down was made
        boolean xEquals = xBefore == xAfter;
        boolean yEquals = yBefore + SPEED == yAfter;
        assertTrue(xEquals && yEquals);
    }

    /**
     * Tests correct "left" movement.
     */
    @Test
    void movingLeft() {
        // Coordinates before making step
        float xBefore = player.getX();
        float yBefore = player.getY();

        player.moveLeft();

        // Coordinates after making step
        float xAfter = player.getX();
        float yAfter = player.getY();

        // Checks if step left was made
        boolean xEquals = xBefore - SPEED == xAfter;
        boolean yEquals = yBefore == yAfter;
        assertTrue(xEquals && yEquals);
    }

    /**
     * Tests correct "right" movement.
     */
    @Test
    void movingRight() {
        // Coordinates before making step
        float xBefore = player.getX();
        float yBefore = player.getY();

        player.moveRight();

        // Coordinates after making step
        float xAfter = player.getX();
        float yAfter = player.getY();

        // Checks if step right was made
        boolean xEquals = xBefore + SPEED == xAfter;
        boolean yEquals = yBefore == yAfter;
        assertTrue(xEquals && yEquals);
    }

    /**
     * Tests correct behaviour when player is trying to go our of the screen.
     */
    @Test
    void notMovingOutScreen() {
        // Moving player out of the screen
        player.setPos(GAME_WIDTH, GAME_HEIGHT);
        player.setHitbox(new Rectangle((int)player.getX() + 13, (int)player.getY() + 13, 40, 40));

        // Coordinates before making step
        float xBefore = player.getX();
        float yBefore = player.getY();

        player.moveDown(); // Trying to make a step

        // Coordinates after making step
        float xAfter = player.getX();
        float yAfter = player.getY();

        // Checks that no step was made
        boolean xEquals = xBefore == xAfter;
        boolean yEquals = yBefore == yAfter;
        assertTrue(xEquals && yEquals);
    }

    /**
     * Tests correct behaviour when player is trying to go through enemy.
     */
    @Test
    void notMovingThroughEnemy() {
        // Creating enemy near player
        level.createEnemy((int)player.getX(), (int)player.getY()-10);

        // Coordinates before making step
        float xBefore = player.getX();
        float yBefore = player.getY();

        player.moveUp(); // Trying to make a step

        // Coordinates after making step
        float xAfter = player.getX();
        float yAfter = player.getY();

        // Checks that no step was made
        boolean xEquals = xBefore == xAfter;
        boolean yEquals = yBefore == yAfter;
        assertTrue(xEquals && yEquals);
    }

    /**
     * Tests correct behaviour when player is trying to go through obstacle.
     */
    @Test
    void notMovingThroughObstacle() {
        // Creating obstacle near player
        level.createObstacle((int)player.getX(), (int)player.getY()-10);

        // Coordinates before making step
        float xBefore = player.getX();
        float yBefore = player.getY();

        player.moveUp(); // Trying to make a step

        // Coordinates after making step
        float xAfter = player.getX();
        float yAfter = player.getY();

        // Checks that no step was made
        boolean xEquals = xBefore == xAfter;
        boolean yEquals = yBefore == yAfter;
        assertTrue(xEquals && yEquals);
    }
}
