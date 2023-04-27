package com.dt042g.project.mvc.controllers;

import com.dt042g.project.mvc.models.GameModel;
import com.dt042g.project.mvc.views.GameView;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import java.awt.Point;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Class containing unit-tests for the GameController class.
 *
 * @author Martin K. Herkules (makr1906) & Albin Eliasson (alel2104)
 */
public class GameControllerTests {
    private static final int boardSize = 10;
    private static GameController gameController;
    private static GameView gameView;
    private static GameModel gameModel;
    private static Method generateSquares;

    /**
     * Argument generator method for running tests with all possible locations.
     *
     * @return Argument with a point position and board size.
     */
    private static Stream<Arguments> Location() {
        return IntStream.range(0, boardSize).boxed()
                .flatMap(x -> IntStream.range(0, boardSize)
                        .mapToObj(y -> Arguments.of(new Point(x, y))));
    }

    /**
     * Method for accessing the private GameModel method generateSquares() which generates a board.
     */
    @BeforeAll
    public static void setupAll() throws NoSuchMethodException {
        generateSquares = GameModel.class.getDeclaredMethod("generateSquares", Point.class);
        generateSquares.setAccessible(true);
    }

    /**
     * Method which initializes the GameModel, GameView and GameController classes and is triggered
     * before each individual test method is executed.
     */
    @BeforeEach
    public void setupEach() {
        gameModel = Mockito.spy(new GameModel(boardSize));
        gameView = Mockito.spy(new GameView(boardSize));
        gameController = new GameController(gameModel, gameView);
    }

    /**
     * Method that tests the handleSelectSquareEvent method; by testing that the Model selectSquare is called.
     * @param location a location in the board
     */
    @ParameterizedTest
    @MethodSource("Location")
    public void test_HandleSelectSquareEvent_CallModelSelectSquare(Point location) {
        gameController.handleSelectSquareEvent(location);

        Mockito.verify(gameModel, Mockito.times(1)).selectSquare(location);
        Mockito.verify(gameModel, Mockito.times(1)).selectSquare(Mockito.any());
    }

    /**
     * Method that tests the handleFlagSquareEvent method; by testing that the method sets the Models squares
     * flag value if they are not already set.
     * @param location a location in the board
     */
    @ParameterizedTest
    @MethodSource("Location")
    public void test_HandleFlagSquareEvent_ShouldSetFlagTrue(Point location) throws InvocationTargetException, IllegalAccessException {
        Point firstLocation = new Point(0, 0);

        // Make sure the board is generated
        generateSquares.invoke(gameModel, firstLocation);

        gameController.handleFlagSquareEvent(location);

        Assertions.assertTrue(gameModel.isFlagged(location));
    }

    /**
     * Method that tests the handleFlagSquareEvent method; by testing that the method unsets the models squares
     * flag value if they are already set.
     * @param location a location in the board
     */
    @ParameterizedTest
    @MethodSource("Location")
    public void test_HandleFlagSquareEvent_CallModelSetSquareFlagFalse(Point location) throws InvocationTargetException, IllegalAccessException {
        Point firstLocation = new Point(0, 0);

        // Make sure the board is generated
        generateSquares.invoke(gameModel, firstLocation);

        // Sets square flag in model to true
        gameModel.setSquareFlag(location, true);

        gameController.handleFlagSquareEvent(location);

        Assertions.assertFalse(gameModel.isFlagged(location));
    }

    /**
     * Method that tests the handleFlagSquareEvent method; by testing that the View setFlagged method is called
     * if the models squares flag value is set.
     * @param location a location in the board
     */
    @ParameterizedTest
    @MethodSource("Location")
    public void test_HandleFlagSquareEvent_CallViewSetFlagged(Point location) throws InvocationTargetException, IllegalAccessException {
        Point firstLocation = new Point(0, 0);

        // Make sure the board is generated
        generateSquares.invoke(gameModel, firstLocation);

        gameController.handleFlagSquareEvent(location);

        Mockito.verify(gameView, Mockito.times(1)).setFlagged(location);
    }

    /**
     * Method that tests the handleFlagSquareEvent method; by testing that the View setHidden method is called
     * if the model squares flag value is not set.
     * @param location a location in the board
     */
    @ParameterizedTest
    @MethodSource("Location")
    public void test_HandleFlagSquareEvent_CallViewSetHidden(Point location) throws InvocationTargetException, IllegalAccessException {
        Point firstLocation = new Point(0, 0);

        // Make sure the board is generated
        generateSquares.invoke(gameModel, firstLocation);

        // Sets square flag in model to true
        gameModel.setSquareFlag(location, true);

        gameController.handleFlagSquareEvent(location);

        Mockito.verify(gameView, Mockito.times(1)).setHidden(location);
    }

    /**
     * Method for testing the handleResetGameEvent method; by testing that the Model reset method
     * is called.
     */
    @Test
    public void test_HandleResetGameEvent_CallModelReset() {
        gameController.handleResetGameEvent();

        Mockito.verify(gameModel, Mockito.times(1)).reset();
    }

    /**
     * Method for testing the handleResetGameEvent method; by testing that the View reset method
     * is called.
     */
    @Test
    public void test_HandleResetGameEvent_CallViewReset() {
        gameController.handleResetGameEvent();

        Mockito.verify(gameView, Mockito.times(1)).reset();
    }

    /**
     * Method for testing the handleRevealSquareEvent method; by testing that the Model
     * getSquareValue method is called with the provided point location.
     */
    @Test
    public void test_HandleRevealSquareEvent_CallModelGetSquareValue() {
        List<Point> locations = new ArrayList<>(Arrays.asList(
                new Point(0, 0), new Point(1, 0), new Point(2, 0)));

        gameController.handleRevealSquareEvent(locations);

        for (Point location : locations) {
            Mockito.verify(gameModel, Mockito.times(1)).getSquareValue(location);
        }
    }

    /**
     * Method for testing the handleRevealSquareEvent method; by testing that the View setValue method
     * is called with the provided point location.
     */
    @Test
    public void test_HandleRevealSquareEvent_CallViewSetValue() {
        List<Point> locations = new ArrayList<>(Arrays.asList(
                new Point(0, 0), new Point(1, 0), new Point(2, 0)));

        gameController.handleRevealSquareEvent(locations);

        for (Point location : locations) {
            Mockito.verify(gameView, Mockito.times(1)).setValue(
                    location, gameModel.getSquareValue(location));
        }
    }

    /**
     * Method for testing the handleMineHitEvent method; by testing that the View gameOver method is
     * called with the provided point location.
     * @param location a location in the board
     */
    @ParameterizedTest
    @MethodSource("Location")
    public void test_handleMineHitEvent_CallViewGameOver(Point location) {
        gameController.handleMineHitEvent(location);

        Mockito.verify(gameView, Mockito.times(1)).gameOver(location);
    }

    /**
     * Method for testing the handleWinEvent method; by testing that the View win method is called.
     */
    @Test
    public void test_handleWinEvent_CallViewWin() {
        gameController.handleWinEvent();

        Mockito.verify(gameView, Mockito.times(1)).win();
    }
}