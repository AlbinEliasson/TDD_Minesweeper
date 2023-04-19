package com.dt042g.project.testinghelpers;

import com.dt042g.project.mvc.views.View;

import java.awt.Point;

/**
 * A concrete class for the *View* abstract class.
 *
 * This class is used to test the default implementations
 * provided in the methods in the *View* class. It is
 * needed since the *View* class is abstract and therefore
 * cannot be instantiated. And since the purpose is to test
 * the default methods, any method implemented directly by
 * this class are irrelevant; and their outputs don-t matter
 * since they are not used, and only exist to make the concrete
 * class valid.
 */
public class TestingConcreteView extends View {
    /**
     * Method for hiding a square.
     *
     * @param location The square location.
     */
    @Override
    public void setHidden(Point location) {

    }

    /**
     * Method for flagging a square.
     *
     * @param location The square location.
     */
    @Override
    public void setFlagged(Point location) {

    }

    /**
     * Method for setting the value of a square.
     *
     * @param location The square location.
     * @param value    The square's value.
     */
    @Override
    public void setValue(Point location, int value) {

    }

    /**
     * Method for setting a square to a mine.
     *
     * @param location The square location.
     */
    @Override
    public void setMine(Point location) {

    }

    /**
     * Method for ending the game.
     *
     * @param location The location of a clicked mine which caused the game over.
     */
    @Override
    public void gameOver(Point location) {

    }

    /**
     * Method for ending the game by winning.
     */
    @Override
    public void win() {

    }

    /**
     * Method for resetting the board to start the game over.
     */
    @Override
    public void reset() {

    }
}
