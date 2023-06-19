package util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Game logger manager.
 * Writes game, editor and error messages to toysoldiers.log.
 * Uses Log4J logger.
 */
public class Log {
    private final boolean active; // is logging active
    private static final Logger LOGGER = LogManager.getLogger(); //

    /**
     * Constructor for the Logger manager.
     *
     * @param active is logging active.
     */
    public Log(boolean active) {
        this.active = active;
    }

    /**
     * Writes logging messages for Game state.
     *
     * @param message message to log.
     */
    public void game(String message) {
        if (active) LOGGER.info("[GAME] " + message);
    }

    /**
     * Writes logging messages for Editor state.
     *
     * @param message message to log.
     */
    public void editor(String message) {
        if (active) LOGGER.info("[EDITOR] " + message);
    }

    /**
     * Writes error logging messages.
     *
     * @param message message to log.
     */
    public void error(String message) {
        if (active) LOGGER.error("[ERROR] " + message);
    }
}
