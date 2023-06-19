package state;

/**
 * Enumeration represents different states of the game.
 * Holds actual game state (global state) and game stage (local state).
 * MENU     - main game menu (state).
 * TUTORIAL - information about the game (state).
 * LOAD     - load screen to choose levels (state).
 * GAME     - direct gaming (state).
 * EDITOR   - built-in level editor (state).
 */
public enum States {
    MENU, TUTORIAL, LOAD, GAME, EDITOR;

    public static States state = MENU; // current game state (global state)
    public static int stage = 0; // current game stage (local state)
}
