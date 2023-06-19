import util.Level;
import static util.Const.Limits.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests Level class.
 */
public class LevelTest {
    private Level level; // level to test

    /**
     * Set up before tests.
     * Creates level.
     */
    @BeforeEach
    void setUp() {
        level = new Level(null);
    }

    /**
     * Tests correct creation of enemiesCount.
     */
    @Test
    void enemiesCountInit() {
        assertEquals(0, level.getEnemiesCount());
    }

    /**
     * Tests correct creation of docsCount.
     */
    @Test
    void docsCountInit() {
        assertEquals(0, level.getDocsCount());
    }

    /**
     * Tests correct creation of object.
     */
    @Test
    void objectCreation() {
        level.createEnemy(640, 640);
        boolean enemyCountOK = level.getEnemies().size() == 1;
        boolean xOK = level.getEnemies().get(0).getX() == 640;
        boolean yOK = level.getEnemies().get(0).getY() == 640;

        assertTrue(enemyCountOK && xOK && yOK);
    }

    /**
     * Tests correct behaviour when trying to create object above limit.
     */
    @Test
    void objectLimitCreation() {
        // Creating more objects than level can handle
        for (int i = 0; i < 100; ++i) {
            level.createEnemy(640, 640);
        }

        assertEquals(level.getEnemies().size(), ENEMIES);
    }

    /**
     * Tests correct deleting of object.
     */
    @Test
    void objectDeleting() {
        level.createEnemy(640, 640);
        level.deleteObject(640, 640);
        assertEquals(0, level.getEnemies().size());
    }

    /**
     * Tests correct behaviour that level doesn't delete another objects.
     */
    @Test
    void objectNotDeleting() {
        level.createEnemy(0, 0);
        level.deleteObject(640, 640);
        assertEquals(1, level.getEnemies().size());
    }
}
