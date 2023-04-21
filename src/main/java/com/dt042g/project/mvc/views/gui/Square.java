package com.dt042g.project.mvc.views.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

/**
 * The square GUI component which represents a single square in the minesweeper board.
 *
 * @author Martin K. Herkules (makr1906) & Albin Eliasson (alel2104)
 */
public class Square extends JPanel {

    /**
     * The State enum represents the three states the square can possess.
     */
    public enum State {
        HIDDEN,
        FLAGGED,
        VALUE
    }
    private State _currentState;
    private JLabel _squareContent;

    /**
     * Constructor which initializes the layout, border, colour, initial state and the
     * square content JLabel.
     */
    public Square() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createBevelBorder(1, Color.GRAY, Color.WHITE));
        setBackground(Color.LIGHT_GRAY);

        _currentState = State.HIDDEN;

        initSquareContent();

        setVisible(true);
    }

    /**
     * Method for initializing the square content JLabel and adding the component to the square JPanel.
     */
    private void initSquareContent() {
        _squareContent = new JLabel();

        // Force the JLabel text to be in the center of the square
        _squareContent.setHorizontalAlignment(JLabel.CENTER);
        _squareContent.setVerticalAlignment(JLabel.CENTER);

        _squareContent.setVisible(true);
        add(_squareContent, BorderLayout.CENTER);
    }

    /**
     * Text setter for the square content JLabel with an empty border to further center the text.
     * @param textContent the text to be added
     */
    private void setSquareText(final String textContent) {
        _squareContent.setText(textContent);
        _squareContent.setFont(new Font("Monaco", Font.BOLD, _squareContent.getHeight() - 5));

        // As the square has a border, setting an empty border to the JLabel makes sure that the text looks centered
        _squareContent.setBorder(BorderFactory.createEmptyBorder(0, 3, 3, 3));
    }

    /**
     * Icon setter for the square content JLabel, if the URL path to the image cant be found an
     * alternative string will be set.
     * @param path the path to the image
     * @param alt the alternative string
     */
    private void setSquareIcon(final URL path, final String alt) {
        try {
            _squareContent.setBorder(null);
            _squareContent.setIcon(path == null ? null : new ImageIcon(ImageIO.read(path).getScaledInstance(
                    _squareContent.getWidth() - 5, _squareContent.getHeight() - 5, Image.SCALE_SMOOTH)));

        } catch (IOException e) {
            _squareContent.setIcon(null);
            setSquareText(alt);
        }
    }

    /**
     * Getter for the current state of the square.
     * @return the current state
     */
    public State getState() {
        return _currentState;
    }

    /**
     * Method for setting the square to hidden where the text and icon is removed from the square content JLabel,
     * the state is set to "hidden" and a new background colour/border is set.
     */
    public void setHidden() {
        setBorder(BorderFactory.createBevelBorder(1, Color.GRAY, Color.WHITE));
        setBackground(Color.LIGHT_GRAY);
        setSquareText("");
        setSquareIcon(null, "");
        validate();

        _currentState = State.HIDDEN;
    }

    /**
     * Method for setting the square to flagged where the flag icon image is set to the square content JLabel and
     * the state is set to "flagged".
     */
    public void setFlagged() {
        setSquareIcon(getClass().getResource("/flag.png"), "F");

        validate();
        _currentState = State.FLAGGED;
    }

    /**
     * Method for setting the square value with the provided number value where the value, if not zero,
     * is set to the square content JLabel. A new background colour/border is set to the square, different
     * foreground colours is set depending on the value and the state is set to "value".
     * @param value the provided number value
     */
    public void setValue(final int value) {
        switch (value) {
            case 1 -> _squareContent.setForeground(Color.BLUE);
            case 2 -> _squareContent.setForeground(new Color(0, 153, 0));
            case 3 -> _squareContent.setForeground(Color.RED);
            case 4 -> _squareContent.setForeground(new Color(102, 0, 153));
            case 5 -> _squareContent.setForeground(new Color(153, 0, 18));
            case 6 -> _squareContent.setForeground(new Color(0, 148, 153));
            case 7 -> _squareContent.setForeground(Color.BLACK);
            case 8 -> _squareContent.setForeground(new Color(73, 73, 73));
        }

        if (value != 0) {
            setSquareText(String.valueOf(value));
        }

        setBackground(new Color(164, 164, 164));
        setBorder(BorderFactory.createLineBorder(Color.GRAY));
        validate();

        _currentState = State.VALUE;
    }

    /**
     * Method for setting the square to mine where the mine icon image is set to the square content JLabel, the
     * state is set to "value" and a new background colour/border is set to the square.
     */
    public void setMine() {
        setSquareIcon(getClass().getResource("/bomb.png"), "M");

        setBackground(Color.RED);
        setBorder(BorderFactory.createLineBorder(Color.GRAY));
        validate();

        _currentState = State.VALUE;
    }
}
