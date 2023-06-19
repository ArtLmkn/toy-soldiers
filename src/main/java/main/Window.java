package main;

import state.States;
import util.Const;
import util.Input;
import gui.TextField;
import static util.Const.*;
import static util.Const.Stages.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.IOException;
import java.io.InputStream;

/**
 * Class for a game window.
 * Window is JFrame and JPanel at the same time.
 */
public class Window extends JPanel {
    private final Application app; // associated game application
    private final TextField textField; // text field for inputs (game saving)

    /**
     * Constructor for the Window.
     *
     * @param app   Application instance associated with this Window.
     */
    public Window(Application app) {
        this.app = app;
        loadFont();

        // Connect keyboard and mouse inputs
        Input input = new Input(this);
        setFocusable(true);
        addKeyListener(input);
        addMouseListener(input);
        addMouseMotionListener(input);

        // Create frame and add canvas
        setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
        setLayout(new OverlayLayout(this));
        JFrame frame = new JFrame("Toy Soldiers");
        frame.add(this);
        textField = new TextField("");
        add(textField);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        frame.addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowLostFocus(WindowEvent e) {if (States.state == States.GAME && States.stage == Game.PLAY) States.stage = Game.PAUSE;}

            @Override
            public void windowGainedFocus(WindowEvent e) {}
        });
    }

    /**
     * Method to repaint game screen.
     *
     * @param graphics Graphics object to use for rendering
     */
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        app.render(graphics);
    }

    /**
     * Sets the visibility of text field for writing.
     *
     * @param value true to show text field component, false otherwise.
     */
    public void setTextField(boolean value) {
        textField.setVisible(value);
    }

    /**
     * Returns text written in text field.
     *
     * @return text written in text field.
     */
    public String getTextField() {
        return textField.getText();
    }

    /**
     * Returns Application instance associated with this Window.
     *
     * @return Application instance associated with this Window.
     */
    public Application getApp() {
        return app;
    }

    /**
     * Loads the font used for game.
     */
    private void loadFont() {
        try {
            InputStream is = getClass().getResourceAsStream(Const.GUI.FONT);
            assert is != null;
            Font font = Font.createFont(Font.TRUETYPE_FONT, is);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);
        } catch (IOException | FontFormatException e) {
            app.getLogger().error("Font was not found!"); // Logging
        }
    }
}
