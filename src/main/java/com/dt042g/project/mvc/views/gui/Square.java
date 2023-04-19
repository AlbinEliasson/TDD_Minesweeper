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
    private State currentState;
    private JLabel squareContent;

    /**
     * Constructor which initializes the layout, border, colour, initial state and the
     * square content JLabel.
     */
    public Square() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createBevelBorder(1, Color.GRAY, Color.WHITE));
        setBackground(Color.LIGHT_GRAY);

        currentState = State.HIDDEN;

        initSquareContent();

        setVisible(true);
    }

    /**
     * Method for initializing the square content JLabel and adding the component to the square JPanel.
     */
    private void initSquareContent() {
        squareContent = new JLabel();

        // Force the JLabel text to be in the center of the square
        squareContent.setHorizontalAlignment(JLabel.CENTER);
        squareContent.setVerticalAlignment(JLabel.CENTER);

        squareContent.setVisible(true);
        add(squareContent, BorderLayout.CENTER);
    }

    /**
     * Text setter for the square content JLabel with an empty border to further center the text.
     * @param textContent the text to be added
     */
    private void setSquareText(final String textContent) {
        squareContent.setText(textContent);
        squareContent.setFont(new Font("Monaco", Font.BOLD, squareContent.getHeight() - 5));

        // As the square has a border, setting an empty border to the JLabel makes sure that the text looks centered
        squareContent.setBorder(BorderFactory.createEmptyBorder(0, 3, 3, 3));
    }

    /**
     * Icon setter for the square content JLabel, if the URL path to the image cant be found an
     * alternative string will be set.
     * @param path the path to the image
     * @param alt the alternative string
     */
    private void setSquareIcon(final URL path, final String alt) {
        try {
            squareContent.setBorder(null);
            squareContent.setIcon(path == null ? null : new ImageIcon(ImageIO.read(path).getScaledInstance(
                    squareContent.getWidth() - 5, squareContent.getHeight() - 5, Image.SCALE_SMOOTH)));

        } catch (IOException e) {
            squareContent.setIcon(null);
            setSquareText(alt);
        }
    }

    /**
     * Getter for the current state of the square.
     * @return the current state
     */
    public State getState() {
        return currentState;
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

        currentState = State.HIDDEN;
    }

    /**
     * Method for setting the square to flagged where the flag icon image is set to the square content JLabel and
     * the state is set to "flagged".
     */
    public void setFlagged() {
        setSquareIcon(getClass().getResource("/flag.png"), "F");

        validate();
        currentState = State.FLAGGED;
    }

    /**
     * Method for setting the square value with the provided number value where the value, if not zero,
     * is set to the square content JLabel. A new background colour/border is set to the square, different
     * foreground colours is set depending on the value and the state is set to "value".
     * @param value the provided number value
     */
    public void setValue(final int value) {
        switch (value) {
            case 1 -> squareContent.setForeground(Color.BLUE);
            case 2 -> squareContent.setForeground(new Color(0, 153, 0));
            case 3 -> squareContent.setForeground(Color.RED);
            case 4 -> squareContent.setForeground(new Color(102, 0, 153));
            case 5 -> squareContent.setForeground(new Color(153, 0, 18));
            case 6 -> squareContent.setForeground(new Color(0, 148, 153));
            case 7 -> squareContent.setForeground(Color.BLACK);
            case 8 -> squareContent.setForeground(new Color(73, 73, 73));
        }

        if (value != 0) {
            setSquareText(String.valueOf(value));
        }

        validate();
        setBackground(new Color(164, 164, 164));
        setBorder(BorderFactory.createLineBorder(Color.GRAY));

        currentState = State.VALUE;
    }

    /**
     * Method for setting the square to mine where the mine icon image is set to the square content JLabel, the
     * state is set to "value" and a new background colour/border is set to the square.
     */
    public void setMine() {
        setSquareIcon(getClass().getResource("/bomb.png"), "M");

        validate();
        setBackground(Color.RED);
        setBorder(BorderFactory.createLineBorder(Color.GRAY));

        currentState = State.VALUE;
    }
}
