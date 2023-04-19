package com.dt042g.project.mvc.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Class containing unit-tests for the *BackingSquare* class.
 *
 * @author Martin K. Herkules (makr1906) & Albin Eliasson (alel2104)
 */
public class BackingSquareTests {
    private BackingSquare square;

    /**
     * Method which is triggered before each individual test method is executed.
     */
    @BeforeEach
    public void setupAll() {
        square = new BackingSquare();
    }

    /**
     * Method for testing that getting and setting the "revealed" value works
     * as indented. Tests all combinations before -> after values:
     *     - false -> false
     *     - false -> true
     *     - true  -> true
     *     - true - > false
     */
    @Test
    public void test_BackingSquare_GetSetRevealed() {
        // {DEFAULT} -> false
        square.setRevealed(false);
        Assertions.assertFalse(square.isRevealed());

        // false -> false
        square.setRevealed(false);
        Assertions.assertFalse(square.isRevealed());

        // false -> true
        square.setRevealed(true);
        Assertions.assertTrue(square.isRevealed());

        // true -> true
        square.setRevealed(true);
        Assertions.assertTrue(square.isRevealed());

        // true -> false
        square.setRevealed(false);
        Assertions.assertFalse(square.isRevealed());
    }

    /**
     * Method for testing the default "revealed" value of the BackingSquares.
     */
    @Test
    public void test_BackingSquare_RevealedDefaultValue() {
        Assertions.assertFalse(square.isRevealed());
    }

    /**
     * Method for testing that getting and setting the "flagged" value works
     * as indented. Tests all combinations before -> after values:
     *     - false -> false
     *     - false -> true
     *     - true  -> true
     *     - true - > false
     */
    @Test
    public void test_BackingSquare_GetSetFlagged() {
        // {DEFAULT} -> false
        square.setFlagged(false);
        Assertions.assertFalse(square.isFlagged());

        // false -> false
        square.setFlagged(false);
        Assertions.assertFalse(square.isFlagged());

        // false -> true
        square.setFlagged(true);
        Assertions.assertTrue(square.isFlagged());

        // true -> true
        square.setFlagged(true);
        Assertions.assertTrue(square.isFlagged());

        // true -> false
        square.setFlagged(false);
        Assertions.assertFalse(square.isFlagged());
    }

    /**
     * Method for testing the default "flagged" value of the BackingSquares.
     */
    @Test
    public void test_BackingSquare_FlaggedDefaultValue() {
        Assertions.assertFalse(square.isFlagged());
    }

    /**
     * Method for testing that getting and setting the "mine" value works
     * as indented. Tests all combinations before -> after values:
     *     - false -> false
     *     - false -> true
     *     - true  -> true
     *     - true - > false
     */
    @Test
    public void test_BackingSquare_GetSetMine() {
        // {DEFAULT} -> false
        square.setMine(false);
        Assertions.assertFalse(square.isMine());

        // false -> false
        square.setMine(false);
        Assertions.assertFalse(square.isMine());

        // false -> true
        square.setMine(true);
        Assertions.assertTrue(square.isMine());

        // true -> true
        square.setMine(true);
        Assertions.assertTrue(square.isMine());

        // true -> false
        square.setMine(false);
        Assertions.assertFalse(square.isMine());
    }

    /**
     * Method for testing the default "mine" value of the BackingSquares.
     */
    @Test
    public void test_BackingSquare_MineDefaultValue() {
        Assertions.assertFalse(square.isMine());
    }
}
