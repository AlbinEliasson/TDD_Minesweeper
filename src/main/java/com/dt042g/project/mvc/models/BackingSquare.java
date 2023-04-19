package com.dt042g.project.mvc.models;

/**
 * Class serving as the backing storage for squares in the model.
 *
 * @author Martin K. Herkules (makr1906) & Albin Eliasson (alel2104)
 */
public class BackingSquare {
    private boolean _mine = false;
    private boolean _flagged = false;
    private boolean _revealed = false;

    // NOTE: You could also store the value here, but that is not necessary since it can be calculated on the fly.

    /**
     * Method for checking if a square is currently revealed.
     *
     * @return Whether the square is revealed.
     */
    public boolean isRevealed() {
        return _revealed;
    }

    /**
     * Method for setting if a square is currently revealed.
     *
     * @param revealed Whether the square should be revealed.
     */
    public void setRevealed(boolean revealed) {
        _revealed = revealed;
    }

    /**
     * Method for checking if a square is currently flagged.
     *
     * @return Whether the square is flagged.
     */
    public boolean isFlagged() {
        return _flagged;
    }

    /**
     * Method for setting if a square is currently flagged.
     *
     * @param flagged Whether the square should be flagged.
     */
    public void setFlagged(boolean flagged) {
        _flagged = flagged;
    }

    /**
     * Method for checking if a square is a mine.
     *
     * @return Whether the square is a mine.
     */
    public boolean isMine() {
        return _mine;
    }

    /**
     * Method for setting if a square is a mine.
     *
     * @param mine Whether the square should be a mine.
     */
    public void setMine(boolean mine) {
        _mine = mine;
    }
}
