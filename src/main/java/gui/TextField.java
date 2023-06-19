package gui;

import util.Const;
import static util.Const.GUI.*;

import javax.swing.JTextField;
import java.awt.GraphicsEnvironment;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

/**
 * Class for a customized text field.
 * Extends JTextFIeld.
 */
public class TextField extends JTextField {

    /**
     * Constructor for the Text Field.
     *
     * @param str initial text of the text field.
     */
    public TextField(String str) {
        super(str);
        setField();
    }

    /**
     * Sets the predefined settings for the text field.
     */
    private void setField() {
        this.setMinimumSize(new Dimension(Text.W,Text.H));
        this.setMaximumSize(new Dimension(Text.W,Text.H));
        this.setPreferredSize(new Dimension(Text.W,Text.H));
        this.setSelectionColor(Colors.KHAKI_A);
        this.setBackground(Colors.TRANSPARENT);
        this.setSelectedTextColor(Colors.OLIVE);
        this.setForeground(Colors.OLIVE);
        this.setBorder(null);
        this.setFont(VETERAN);
        this.setToolTipText("Filename can contain only english letters and digits. Filename can't be empty for saving.");
        this.setVisible(false);
    }
}
