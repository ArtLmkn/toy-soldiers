package main;

import util.Log;

/**
 * The Main class.
 * Initializes and starts game.
 */
public class Main {
    /**
     * The entry point of the game.
     * Creates a new instance of the Application class and starts it.
     *
     * @param args command-line arguments.
     */
    public static void main(String[] args) {
        boolean logging = false;

        for (String arg : args) {
            if (arg.equals("-log")) {
                logging = true;
                break;
            }
        }

        Log logger = new Log(logging);
        new Application(logger);
    }
}
