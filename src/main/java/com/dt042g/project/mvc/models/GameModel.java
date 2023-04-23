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
        if(_board == null || isMine(location))
            return -1;

        int value = 0;

        for (Point neighbor : getNeighbors(location)) {
            value += _board.get(neighbor.x).get(neighbor.y).isMine() ? 1 : 0;
        }

        return value;
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
        if(_board == null)
            return false;

        return _board.get(location.x).get(location.y).isFlagged();
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
        return _board != null && _board.get(location.x).get(location.y).isRevealed();
    }

    /**
     * Method for selecting a square to be revealed.
     *
     * @param location The location of the square to reveal.
     * @throws IndexOutOfBoundsException If the location is outside the size of the board.
     */
    @Override
    public void selectSquare(Point location) {
        if(_board == null)
            generateSquares(location);

        if(isRevealed(location) || isFlagged(location))
            return;

        if(isMine(location)) {
            pushMineHitEvent(location);
            return;
        }

        List<Point> locations = findAndRevealZeroValueNeighbors(location);
        pushRevealSquareEvent(locations);
    }

    /**
     * Method for getting (and revealing) all "zero-value" related (recursive)
     * neighbors of a square.
     *
     * This method checks the provided location to see if it is zero. If it is,
     * then all of its neighbors are included. Then it checks each of the
     * neighbors for zero-value squares. If one is found, the method recurses to
     * also include its neighbors and to again look for zero-value neighbors.
     *
     * The result being the original location, plus any "zero-value" related
     * squares. This is used when revealing squares, to give the player extra
     * vision by automatically revealing zero-value squares.
     *
     * @param location The original location.
     *
     * @return All zero-value related squares.
     */
    private List<Point> findAndRevealZeroValueNeighbors(Point location) {
        List<Point> locations = new ArrayList<>();

        // Don't check already revealed squares, flagged squares and mines.
        if(isRevealed(location) || isFlagged(location) || isMine(location))
            return locations;

        // Set the current square to revealed, since it is to be shown. But also
        // to avoid infinite recursion loops. So be careful if you are making
        // modifications to this.
        _board.get(location.x).get(location.y).setRevealed(true);
        locations.add(location);

        // Check if the current square is zero
        if(getSquareValue(location) == 0) {
            // If so recurse for each neighbor, and check for their zero value
            // neighbors. Note that this also adds all the current squares
            // neighbors to the "locations" list, since this method at a minimum
            // returns the original provided location.
            for(Point neighbor : getNeighbors(location))
                locations.addAll(findAndRevealZeroValueNeighbors(neighbor));
        }

        return locations;
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
        _board = null;
    }
}
