import obj.collectible.HealthBox;
import obj.soldier.Player;
import util.Level;
import static util.Const.Limits.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests HealthBox class.
 */
public class HealthBoxTest {
    private HealthBox healthBox; // health box to test
    private Level level; // associated level

    /**
     * Set up before tests.
     * Creates level construction and adds healthBox there.
     */
    @BeforeEach
    void setUp() {
        level = new Level(null);
        healthBox = new HealthBox(640, 640, level);
        level.createHealthBox(640, 640);
    }

    /**
     * Tests correct initialisation of x coordinate.
     */
    @Test
    void xInit() {
        assertEquals(640, healthBox.getX());
    }

    /**
     * Tests correct initialisation of y coordinate.
     */
    @Test
    void yInit() {
        assertEquals(640, healthBox.getY());
    }

    /**
     * Tests correct initialisation is health box active.
     */
    @Test
    void activeInit() {
        assertTrue(healthBox.isActive());
    }

    /**
     * Tests correct behaviour when health box was taken.
     */
    @Test
    void playerTake() {
        // Creating player near health box
        Player player = new Player(640, 640, level);
        level.setPlayer(player);

        assertTrue(healthBox.isTaken());
    }

    /**
     * Tests correct behaviour when health box wasn't taken.
     */
    @Test
    void playerNotTake() {
        // Creating player far away from health box
        Player player = new Player(0, 0, level);
        level.setPlayer(player);

        assertFalse(healthBox.isTaken());
    }

    /**
     * Tests correct deactivation of health box after being taken.
     */
    @Test
    void activeAfterTake() {
        // Creating player that takes health box
        Player player = new Player(640, 640, level);
        level.setPlayer(player);

        healthBox.affect();
        assertFalse(healthBox.isActive());
    }

    /**
     * Tests correct health increasing.
     */
    @Test
    void increaseHealth() {
        // Creating player with 1 health
        Player player = new Player(640, 640, level);
        level.setPlayer(player);
        player.setHealth(1);

        healthBox.affect();
        assertEquals(1 + HEALTHBOX_EFFECT, player.getHealth());
    }

    /**
     * Tests correct health increasing when player have almost full health.
     */
    @Test
    void increaseHealthToMax() {
        // Creating player with 8 health
        Player player = new Player(640, 640, level);
        level.setPlayer(player);
        player.setHealth(8);

        healthBox.affect();
        assertEquals(HEALTH_MAX, player.getHealth());
    }

    /**
     * Tests correct behaviour when player has maximum health.
     */
    @Test
    void playerMaxHealth() {
        // Creating player with maximum health
        Player player = new Player(640, 640, level);
        level.setPlayer(player);
        player.setHealth(HEALTH_MAX);

        healthBox.affect();
        assertTrue(healthBox.isActive());
    }
}
