package main;

import state.*;
import util.Audio;
import util.Log;
import static util.Const.*;

import java.awt.Graphics;

/**
 * The main application class that runs the game.
 */
public class Application {
    private final Window window; // associated game window
    private final Audio audio; // associated audio player
    private final Menu menu; // associated menu state
    private final Load load;  // associated load state
    private final Tutorial tutorial; // associated tutorial state
    private final Editor editor;  // associated editor state
    private final Game game;  // associated game state
    Thread update, render; // parallel threads
    Log logger; // application logger


    /**
     * Constructor for the Application.
     * Initializes all game states, Java Swing Window and audio player.
     * Starts threads for game update and game render.
     */
    public Application(Log logger) {
        // Connect logger
        this.logger = logger;

        // Create game states
        menu = new Menu(this);
        load = new Load(this);
        tutorial = new Tutorial(this);
        editor = new Editor(this);
        game = new Game(this);

        // Connect video and audio outputs
        window = new Window(this);
        audio = new Audio();

        // Start new thread for game updating
        update = new Thread(new Update());
        update.start();

        // Start new thread for game rendering
        render = new Thread(new Render());
        render.start();
    }

    /**
     * Method to update the current game state.
     * Calls the update method of the current state object based on the current state.
     */
    public void update() {
        switch (States.state) {
            case MENU -> menu.update();
            case TUTORIAL -> tutorial.update();
            case LOAD -> load.update();
            case GAME -> game.update();
            case EDITOR -> editor.update();
        }
    }

    /**
     * Method to render the current game state.
     * Calls the render method of the current state object based on the current state.
     * @param graphics Graphics object to use for rendering.
     */
    public void render(Graphics graphics) {
        switch (States.state) {
            case MENU -> menu.render(graphics);
            case TUTORIAL -> tutorial.render(graphics);
            case LOAD -> load.render(graphics);
            case GAME -> game.render(graphics);
            case EDITOR -> editor.render(graphics);
        }
    }

    /**
     * Returns Window (JFrame + JPanel) used in the application.
     *
     * @return game window.
     */
    public Window getWindow() {
        return window;
    }

    /**
     * Returns Menu state.
     *
     * @return Menu state.
     */
    public Menu getMenu() {
        return menu;
    }

    /**
     * Returns Load state.
     *
     * @return Load state.
     */
    public Load getLoad() {
        return load;
    }

    /**
     * Returns Tutorial state.
     *
     * @return Tutorial state.
     */
    public Tutorial getTutorial() {
        return tutorial;
    }

    /**
     * Returns Editor state.
     *
     * @return Editor state.
     */
    public Editor getEditor() {
        return editor;
    }

    /**
     * Returns Game state.
     *
     * @return Game state.
     */
    public Game getGame() {
        return game;
    }

    /**
     * Returns audio player.
     *
     * @return audio player used in game.
     */
    public Audio getAudio() {
        return audio;
    }

    /**
     * Returns application logger.
     *
     * @return application logger.
     */
    public Log getLogger() {
        return logger;
    }

    /**
     * Runnable class for the render thread.
     * Renders the game at the specified frame rate (FPS).
     */
    private class Render implements Runnable {
        @Override
        public void run() {
            double fpsTime = 1000000000d / FPS; // Time per frames (nanosec)
            double fpsDelta = 0;
            long prevTime = System.nanoTime();

            do {
                long curTime = System.nanoTime();
                fpsDelta += (curTime - prevTime) / fpsTime;
                prevTime = curTime;
                if (fpsDelta >= 1) {window.repaint(); fpsDelta--;} // Time to draw
            } while (States.state != States.MENU || States.stage != Stages.Menu.EXIT); // Stop thread on exit
        }
    }

    /**
     * Runnable class for the update thread.
     * Updatess the game at the specified update rate (UPS).
     */
    private class Update implements Runnable {
        @Override
        public void run() {
            double upsTime = 1000000000d / UPS; // Time per updates (nanosec)
            double upsDelta = 0;
            long prevTime = System.nanoTime();

            do {
                long curTime = System.nanoTime();
                upsDelta += (curTime - prevTime) / upsTime;
                prevTime = curTime;
                if (upsDelta >= 1) {update(); upsDelta--;} // Time to make update
            } while (States.state != States.MENU || States.stage != Stages.Menu.EXIT); // Stop thread on exit

            System.exit(0); // Stop program execution
        }
    }
}
