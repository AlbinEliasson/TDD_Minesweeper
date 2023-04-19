package com.dt042g.project.testinghelpers;

import com.dt042g.project.mvc.models.Model;

import java.awt.Point;

/**
 * A concrete class for the *Model* abstract class.
 *
 * This class is used to test the default implementations
 * provided in the methods in the *Model* class. It is
 * needed since the *Model* class is abstract and therefore
 * cannot be instantiated. And since the purpose is to test
 * the default methods, any method implemented directly by
 * this class are irrelevant; and their outputs don-t matter
 * since they are not used, and only exist to make the concrete
 * class valid.
 */
public class TestingConcreteModel extends Model {
    /**
     * Method for getting the number of neighboring mines to a square.
     *
     * @param location The location of the square to check.
     * @return The number of neighboring mines.
     * @throws IndexOutOfBoundsException If the location is outside the size of the board.
     */
    @Override
    public int getSquareValue(Point location) {
        return 0;
    }

    /**
     * Method for checking if a square is currently flagged.
     *
     * @param location The location of the square to check.
     * @return Whether the square is flagged.
     * @throws IndexOutOfBoundsException If the location is outside the size of the board.
     */
    @Override
    public boolean isFlagged(Point location) {
        return false;
    }

    /**
     * Method for checking if a square is a mine.
     *
     * @param location The location of the square to check.
     * @return Whether the square is a mine.
     * @throws IndexOutOfBoundsException If the location is outside the size of the board.
     */
    @Override
    public boolean isMine(Point location) {
        return false;
    }

    /**
     * Method for checking if a square is revealed.
     *
     * @param location The location of the square to check.
     * @return Whether the square is revealed.
     * @throws IndexOutOfBoundsException If the location is outside the size of the board.
     */
    @Override
    public boolean isRevealed(Point location) {
        return false;
    }

    /**
     * Method for selecting a square to be revealed.
     *
     * @param location The location of the square to reveal.
     * @throws IndexOutOfBoundsException If the location is outside the size of the board.
     */
    @Override
    public void selectSquare(Point location) {

    }

    /**
     * Method for setting the flagged value of a square.
     *
     * @param location The location of the square to flag.
     * @param value    The flag value; true = flagged, false = not flagged.
     * @throws IndexOutOfBoundsException If the location is outside the size of the board.
     */
    @Override
    public void setSquareFlag(Point location, boolean value) {

    }

    /**
     * Method for resetting the backing game board; I.E. restarting the game.
     */
    @Override
    public void reset() {

    }
}
