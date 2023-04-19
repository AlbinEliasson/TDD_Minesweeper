package com.dt042g.project.mvc.views;

import com.dt042g.project.mvc.observer.Observed;

import java.awt.Point;
import java.util.Arrays;

/**
 * Class serving as the common super class for any view.
 *
 * @author Martin K. Herkules (makr1906) & Albin Eliasson (alel2104)
 */
public abstract class View extends Observed {
    public static final String VIEW_SELECT_SQUARE_EVENT = "VIEW_SELECT_SQUARE_EVENT";
    public static final String VIEW_FLAG_SQUARE_EVENT = "VIEW_FLAG_SQUARE_EVENT";
    public static final String VIEW_RESET_GAME_EVENT = "VIEW_RESET_GAME_EVENT";

    /**
     * Helper method for triggering a "select square" event.
     *
     * @param location The square location.
     */
    public void pushSelectEvent(Point location) {
        pushEvent(VIEW_SELECT_SQUARE_EVENT, Arrays.asList(location));
    }

    /**
     * Helper method for triggering a "flag square" event.
     *
     * @param location The square location.
     */
    public void pushFlagEvent(Point location) {
        pushEvent(VIEW_FLAG_SQUARE_EVENT, Arrays.asList(location));
    }

    /**
     * Helper method for triggering a "reset" event.
     */
    public void pushResetGameEvent() {
        pushEvent(VIEW_RESET_GAME_EVENT, null);
    }

    /* *************** */

    /**
     * Method for hiding a square.
     *
     * @param location The square location.
     */
    abstract public void setHidden(Point location);

    /**
     * Method for flagging a square.
     *
     * @param location The square location.
     */
    abstract public void setFlagged(Point location);

    /**
     * Method for setting the value of a square.
     *
     * @param location The square location.
     * @param value The square's value.
     */
    abstract public void setValue(Point location, int value);

    /**
     * Method for setting a square to a mine.
     *
     * @param location The square location.
     */
    abstract public void setMine(Point location);

    /**
     * Method for ending the game.
     *
     * @param location The location of a clicked mine which caused the game over.
     */
    abstract public void gameOver(Point location);

    /**
     * Method for ending the game by winning.
     */
    abstract public void win();

    /**
     * Method for resetting the board to start the game over.
     */
    abstract public void reset();
}
