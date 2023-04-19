package com.dt042g.project.mvc.models;

import com.dt042g.project.mvc.observer.Observed;

import java.awt.Point;
import java.util.Arrays;
import java.util.List;

/**
 * Class serving as the superclass of any model.
 *
 * @author Martin K. Herkules (makr1906) & Albin Eliasson (alel2104)
 */
public abstract class Model extends Observed {
    public static final String MODEL_REVEAL_SQUARE_EVENT = "MODEL_REVEAL_SQUARE_EVENT";
    public static final String MODEL_MINE_HIT_EVENT = "MODEL_MINE_HIT_EVENT";
    public static final String MODEL_WIN_EVENT = "MODEL_WIN_EVENT";

    /**
     * Helper method for triggering a "Reveal Square" event.
     *
     * @param locations The squares which should be revealed.
     */
    public void pushRevealSquareEvent(List<Point> locations) {
        pushEvent(MODEL_REVEAL_SQUARE_EVENT, locations);
    }

    /**
     * Helper method for triggering a "Mine Hit" event.
     *
     * @param location The location of the mine which was hit.
     */
    public void pushMineHitEvent(Point location) {
        pushEvent(MODEL_MINE_HIT_EVENT, Arrays.asList(location));
    }

    /**
     * Helper method for triggering a "Win" event.
     */
    public void pushWinEvent() {
        pushEvent(MODEL_WIN_EVENT, null);
    }

    /* ******************* */

    /**
     * Method for getting the number of neighboring mines to a square.
     *
     * @param location The location of the square to check.
     *
     * @return The number of neighboring mines.
     *
     * @throws IndexOutOfBoundsException If the location is outside the size of the board.
     */
    abstract public int getSquareValue(Point location);

    /**
     * Method for checking if a square is currently flagged.
     *
     * @param location The location of the square to check.
     *
     * @return Whether the square is flagged.
     *
     * @throws IndexOutOfBoundsException If the location is outside the size of the board.
     */
    abstract public boolean isFlagged(Point location);

    /**
     * Method for checking if a square is a mine.
     *
     * @param location The location of the square to check.
     *
     * @return Whether the square is a mine.
     *
     * @throws IndexOutOfBoundsException If the location is outside the size of the board.
     */
    abstract public boolean isMine(Point location);

    /**
     * Method for checking if a square is revealed.
     *
     * @param location The location of the square to check.
     *
     * @return Whether the square is revealed.
     *
     * @throws IndexOutOfBoundsException If the location is outside the size of the board.
     */
    abstract public boolean isRevealed(Point location);

    /**
     * Method for selecting a square to be revealed.
     *
     * @param location The location of the square to reveal.
     *
     * @throws IndexOutOfBoundsException If the location is outside the size of the board.
     */
    abstract public void selectSquare(Point location);

    /**
     * Method for setting the flagged value of a square.
     *
     * @param location The location of the square to flag.
     *
     * @param value The flag value; true = flagged, false = not flagged.
     *
     * @throws IndexOutOfBoundsException If the location is outside the size of the board.
     */
    abstract public void setSquareFlag(Point location, boolean value);

    /**
     * Method for resetting the backing game board; I.E. restarting the game.
     */
    abstract public void reset();
}
