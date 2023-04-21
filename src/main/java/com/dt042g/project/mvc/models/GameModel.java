package com.dt042g.project.mvc.models;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameModel extends Model {
    private final double MINE_CHANCE = 0.15;
    private final int _boardSize;
    private List<List<BackingSquare>> _board;

    public GameModel(int boardSize) {
        _boardSize = boardSize;
        _board = null;
    }

    /**
     * Method for (re)generating every square on the board.
     */
    private void generateSquares(Point firstLocation)  {
        _board = new ArrayList<>();
        Random random = new Random();

        // Add "rows" to board
        for(int i = _board.size(); i < _boardSize; i++)
            _board.add(i, new ArrayList<>());

        // Iterate through each "row"
        for(List<BackingSquare> row : _board) {
            // Iterate through each square in row
            for(int i = 0; i < _boardSize; i++) {
                // Create a new backing square
                row.add(i, new BackingSquare());

                // Randomize whether the square is a mine
                boolean isMine = random.nextDouble() < MINE_CHANCE;

                // Set whether it is a mine
                row.get(i).setMine(isMine);
            }
        }

        // Skip "first square protection" if first location is null
        if(firstLocation == null)
            return;

        // The first square clicked is a mine, make it not a mine.
        if(isMine(firstLocation)) {
            _board.get(firstLocation.x).get(firstLocation.y).setMine(false);
        }

        // Check each neighbour of the first clicked square.
        for(Point neighbor : getNeighbors(firstLocation)) {
            // If the neighbor is a mine, make it not a mine.
            if(isMine(neighbor)) {
                _board.get(neighbor.x).get(neighbor.y).setMine(false);
            }
        }
    }

    /**
     * Method for getting all the neighbor squares of a location.
     *
     * @param location The location of the subject.
     *
     * @return The neighbors.
     */
    private List<Point> getNeighbors(Point location) {
        List<Point> neighbors = new ArrayList<>();

        if(location == null)
            return neighbors;

        if(location.x > 0)
            neighbors.add(new Point(location.x - 1, location.y)); // West
        if(location.x < _boardSize - 1)
            neighbors.add(new Point(location.x + 1, location.y)); // East
        if(location.y > 0)
            neighbors.add(new Point(location.x, location.y - 1)); // North
        if(location.y < _boardSize - 1)
            neighbors.add(new Point(location.x, location.y + 1)); // South
        if(location.x > 0 && location.y > 0)
            neighbors.add(new Point(location.x - 1, location.y - 1)); // North-west
        if(location.x < _boardSize - 1 && location.y < _boardSize - 1)
            neighbors.add(new Point(location.x + 1, location.y + 1)); // South-east
        if(location.x > 0 && location.y < _boardSize - 1)
            neighbors.add(new Point(location.x - 1, location.y + 1)); // South-west
        if(location.x < _boardSize - 1 && location.y > 0)
            neighbors.add(new Point(location.x + 1, location.y - 1)); // North-east

        return neighbors;
    }

    /**
     * Method for getting the number of neighboring mines to a square.
     *
     * @param location The location of the square to check.
     * @return The number of neighboring mines.
     * @throws IndexOutOfBoundsException If the location is outside the size of the board.
     */
    @Override
    public int getSquareValue(Point location) {
        throw new UnsupportedOperationException("Not implemented!");
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
        throw new UnsupportedOperationException("Not implemented!");
    }

    /**
     * Method for checking if a square is a mine.
     *
     * @param location The location of the square to check.
     *
     * @return Whether the square is a mine.
     *
     * @throws IndexOutOfBoundsException If the location is outside the size of the board.
     * @throws NullPointerException If the location is null.
     */
    @Override
    public boolean isMine(Point location) {
        if(_board == null)
            return false;

        return _board.get(location.x).get(location.y).isMine();
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
        throw new UnsupportedOperationException("Not implemented!");
    }

    /**
     * Method for selecting a square to be revealed.
     *
     * @param location The location of the square to reveal.
     * @throws IndexOutOfBoundsException If the location is outside the size of the board.
     */
    @Override
    public void selectSquare(Point location) {
        throw new UnsupportedOperationException("Not implemented!");
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
        if(_board == null)
            return;

        _board.get(location.x).get(location.y).setFlagged(value);
    }

    /**
     * Method for resetting the backing game board; I.E. restarting the game.
     */
    @Override
    public void reset() {
        throw new UnsupportedOperationException("Not implemented!");
    }
}
