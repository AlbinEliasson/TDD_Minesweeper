package com.dt042g.project.mvc.views.gui;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.lang.reflect.Field;

/**
 * Tests the square JPanel component which represents a single square of the minesweeper board.
 *
 * @author Martin K. Herkules (makr1906) & Albin Eliasson (alel2104)
 */
public class SquareTests {
    private Square square;
    private static Field squareContentField;

    /**
     * Method for accessing the private square content JLabel.
     */
    @BeforeAll
    public static void setupAll() throws NoSuchFieldException {
        squareContentField = Square.class.getDeclaredField("_squareContent");
        squareContentField.setAccessible(true);
    }

    /**
     * Method which initializes a new square before every test.
     */
    @BeforeEach
    public void setupEach() {
        square = new Square();
    }

    /**
     * Test the squares initial state.
     */
    @Test
    public void test_Square_InitialState() {
        Square.State hiddenState = Square.State.HIDDEN;

        Square.State resultState = square.getState();

        Assertions.assertEquals(hiddenState, resultState);
    }

    /**
     * Test the squares state after being set to hidden.
     */
    @Test
    public void test_SetHidden_ValidateState() {
        Square.State hiddenState = Square.State.HIDDEN;

        square.setHidden();

        Square.State resultState = square.getState();
        Assertions.assertEquals(hiddenState, resultState);
    }

    /**
     * Test the squares state after being set to a flag.
     */
    @Test
    public void test_SetFlagged_ValidateState() {
        Square.State flaggedState = Square.State.FLAGGED;
        square.setFlagged();

        Square.State resultState = square.getState();
        Assertions.assertEquals(flaggedState, resultState);
    }

    /**
     * Test that the square sets an icon after being set to a flag.
     */
    @Test
    public void test_SetFlagged_ValidateIcon() throws IllegalAccessException {
        JLabel squareLabel = (JLabel) squareContentField.get(square);

        square.setFlagged();

        Icon resultIcon = squareLabel.getIcon();

        Assertions.assertNotNull(resultIcon);
    }

    /**
     * Test the squares state after being set to a value.
     */
    @Test
    public void test_SetSquareValue_ValidateState() {
        Square.State valueState = Square.State.VALUE;
        square.setValue(2);

        Square.State resultState = square.getState();
        Assertions.assertEquals(valueState, resultState);
    }

    /**
     * Test that the square sets the assigned value.
     */
    @Test
    public void test_SetSquareValue_ValidateValue() throws IllegalAccessException {
        JLabel squareLabel;
        String squareValue = "2";
        squareLabel = (JLabel) squareContentField.get(square);

        square.setValue(2);

        String labelTextResult = squareLabel.getText();

        Assertions.assertEquals(squareValue, labelTextResult);
    }

    /**
     * Test that the square does not set the assigned value if the value is zero.
     */
    @Test
    public void test_SetSquareValue_ValidateValueZero() throws IllegalAccessException {
        JLabel squareLabel;
        String expectedValue = "";
        squareLabel = (JLabel) squareContentField.get(square);

        square.setValue(0);

        String labelTextResult = squareLabel.getText();

        Assertions.assertEquals(expectedValue, labelTextResult);
    }

    /**
     * Test the squares state after being set to a mine.
     */
    @Test
    public void test_SetMine_ValidateState() {
        Square.State valueState = Square.State.VALUE;

        square.setMine();

        Square.State resultState = square.getState();

        Assertions.assertEquals(valueState, resultState);
    }

    /**
     * Test that the square sets an icon after being set to a mine.
     */
    @Test
    public void test_SetMine_ValidateIcon() throws IllegalAccessException {
        JLabel squareLabel;
        squareLabel = (JLabel) squareContentField.get(square);

        square.setMine();

        Icon resultIcon = squareLabel.getIcon();

        Assertions.assertNotNull(resultIcon);
    }

    /**
     * Test that the setHidden() method removes a set value.
     */
    @Test
    public void test_SetHidden_RemovesValue() throws IllegalAccessException {
        JLabel squareLabel;
        int value = 4;
        String expectedValue = "";
        squareLabel = (JLabel) squareContentField.get(square);

        square.setValue(value);
        square.setHidden();

        Assertions.assertEquals(expectedValue, squareLabel.getText());
    }

    /**
     * Test that the setHidden() method removes a set image icon.
     */
    @Test
    public void test_SetHidden_RemovesImage() throws IllegalAccessException {
        JLabel squareLabel;
        squareLabel = (JLabel) squareContentField.get(square);

        square.setMine();
        square.setHidden();

        Icon resultIcon = squareLabel.getIcon();

        Assertions.assertNull(resultIcon);
    }

    /**
     * Test squares state change from hide to hide.
     */
    @Test
    public void test_SetHiddenToSetHidden_ValidateState() {
        Square.State expectedState = Square.State.HIDDEN;

        square.setHidden();
        square.setHidden();

        Square.State resultState = square.getState();

        Assertions.assertEquals(expectedState, resultState);
    }

    /**
     * Test squares state change from hide to value.
     */
    @Test
    public void test_SetHiddenToSetValue_ValidateState() {
        Square.State expectedState = Square.State.VALUE;

        square.setHidden();
        square.setValue(2);

        Square.State resultState = square.getState();

        Assertions.assertEquals(expectedState, resultState);
    }

    /**
     * Test squares state change from hide to flag.
     */
    @Test
    public void test_SetHiddenToSetFlagged_ValidateState() {
        Square.State expectedState = Square.State.FLAGGED;

        square.setHidden();
        square.setFlagged();

        Square.State resultState = square.getState();

        Assertions.assertEquals(expectedState, resultState);
    }

    /**
     * Test squares state change from hide to mine.
     */
    @Test
    public void test_SetHiddenToSetMine_ValidateState() {
        Square.State expectedState = Square.State.VALUE;

        square.setHidden();
        square.setMine();

        Square.State resultState = square.getState();

        Assertions.assertEquals(expectedState, resultState);
    }

    /**
     * Test squares state change from value to value.
     */
    @Test
    public void test_SetValueToSetValue_ValidateState() {
        Square.State expectedState = Square.State.VALUE;
        int value = 2;

        square.setValue(value);
        square.setValue(value);

        Square.State resultState = square.getState();

        Assertions.assertEquals(expectedState, resultState);
    }

    /**
     * Test squares state change from value to hide.
     */
    @Test
    public void test_SetValueToSetHidden_ValidateState() {
        Square.State expectedState = Square.State.HIDDEN;
        int value = 2;

        square.setValue(value);
        square.setHidden();

        Square.State resultState = square.getState();

        Assertions.assertEquals(expectedState, resultState);
    }

    /**
     * Test squares state change from value to flag.
     */
    @Test
    public void test_SetValueToSetFlagged_ValidateState() {
        Square.State expectedState = Square.State.FLAGGED;
        int value = 2;

        square.setValue(value);
        square.setFlagged();

        Square.State resultState = square.getState();

        Assertions.assertEquals(expectedState, resultState);
    }

    /**
     * Test squares state change from value to mine.
     */
    @Test
    public void test_SetValueToSetMine_ValidateState() {
        Square.State expectedState = Square.State.VALUE;
        int value = 2;

        square.setValue(value);
        square.setMine();

        Square.State resultState = square.getState();

        Assertions.assertEquals(expectedState, resultState);
    }

    /**
     * Test squares state change from flag to flag.
     */
    @Test
    public void test_SetFlaggedToSetFlagged_ValidateState() {
        Square.State expectedState = Square.State.FLAGGED;

        square.setFlagged();
        square.setFlagged();

        Square.State resultState = square.getState();

        Assertions.assertEquals(expectedState, resultState);
    }

    /**
     * Test squares state change from flag to hide.
     */
    @Test
    public void test_SetFlaggedToSetHidden_ValidateState() {
        Square.State expectedState = Square.State.HIDDEN;

        square.setFlagged();
        square.setHidden();

        Square.State resultState = square.getState();

        Assertions.assertEquals(expectedState, resultState);
    }

    /**
     * Test squares state change from flag to value.
     */
    @Test
    public void test_SetFlaggedToSetValue_ValidateState() {
        Square.State expectedState = Square.State.VALUE;
        int value = 3;

        square.setFlagged();
        square.setValue(value);

        Square.State resultState = square.getState();

        Assertions.assertEquals(expectedState, resultState);
    }

    /**
     * Test squares state change from flag to mine.
     */
    @Test
    public void test_SetFlaggedToSetMine_ValidateState() {
        Square.State expectedState = Square.State.VALUE;

        square.setFlagged();
        square.setMine();

        Square.State resultState = square.getState();

        Assertions.assertEquals(expectedState, resultState);
    }

    /**
     * Test squares state change from mine to mine.
     */
    @Test
    public void test_SetMineToSetMine_ValidateState() {
        Square.State expectedState = Square.State.VALUE;

        square.setMine();
        square.setMine();

        Square.State resultState = square.getState();

        Assertions.assertEquals(expectedState, resultState);
    }

    /**
     * Test squares state change from mine to hide.
     */
    @Test
    public void test_SetMineToSetHidden_ValidateState() {
        Square.State expectedState = Square.State.HIDDEN;

        square.setMine();
        square.setHidden();

        Square.State resultState = square.getState();

        Assertions.assertEquals(expectedState, resultState);
    }

    /**
     * Test squares state change from mine to value.
     */
    @Test
    public void test_SetMineToSetValue_ValidateState() {
        Square.State expectedState = Square.State.VALUE;
        int value = 5;

        square.setMine();
        square.setValue(value);

        Square.State resultState = square.getState();

        Assertions.assertEquals(expectedState, resultState);
    }

    /**
     * Test squares state change from mine to flag.
     */
    @Test
    public void test_SetMineToSetFlagged_ValidateState() {
        Square.State expectedState = Square.State.FLAGGED;

        square.setMine();
        square.setFlagged();

        Square.State resultState = square.getState();

        Assertions.assertEquals(expectedState, resultState);
    }

}
