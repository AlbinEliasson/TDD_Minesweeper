package com.dt042g.project.mvc.views;

import com.dt042g.project.mvc.views.gui.Square;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.swing.JPanel;
import java.awt.Point;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Class containing unit-tests for the GameView class.
 */
public class GameViewTests {
    private static GameView gameView;
    private static final int boardSize = 20;
    private static ArrayList<Point> boardPositions;
    private static Field boardField;
    private static Field boardLockedField;
    private static Method calculateSquarePosition;
    private static Method selectSquare;
    private static Method flagSquare;

    /**
     * Method for accessing private fields/methods and initialize a list of point locations
     * from a set board size.
     */
    @BeforeAll
    public static void setupAll() throws NoSuchFieldException, NoSuchMethodException {
        // Private fields
        boardField = GameView.class.getDeclaredField("_board");
        boardField.setAccessible(true);
        boardLockedField = GameView.class.getDeclaredField("_boardLocked");
        boardLockedField.setAccessible(true);

        // Private methods
        calculateSquarePosition = GameView.class.getDeclaredMethod(
                "calculateSquareBoardPosition", int.class, int.class);
        calculateSquarePosition.setAccessible(true);
        selectSquare = GameView.class.getDeclaredMethod("selectSquare", Square.class, Point.class);
        selectSquare.setAccessible(true);
        flagSquare = GameView.class.getDeclaredMethod("flagSquare", Square.class, Point.class);
        flagSquare.setAccessible(true);

        boardPositions = new ArrayList<>();
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                boardPositions.add(new Point(j, i));
            }
        }
    }

    /**
     * Method which is triggered before each individual test method is executed.
     */
    @BeforeEach
    public void setupEach() {
        gameView = Mockito.spy(new GameView(boardSize));
    }

    /**
     * Method for testing the correct amount of components is present in the initialized board.
     */
    @Test
    public void test_InitializeBoard_ValidateComponentAmount() throws IllegalAccessException {
        JPanel boardPanel = (JPanel) boardField.get(gameView);
        int componentAmount = boardSize * boardSize;

        int resultComponentAmount = boardPanel.getComponentCount();

        Assertions.assertEquals(componentAmount, resultComponentAmount);
    }

    /**
     * Method for testing the components in the initialized board is an instance of the
     * Square GUI component.
     */
    @Test
    public void test_InitializeBoard_ContainsSquareComponents() throws IllegalAccessException {
        JPanel boardPanel = (JPanel) boardField.get(gameView);

        for (int i = 0; i < boardSize * boardSize; i++) {
            Assertions.assertInstanceOf(Square.class, boardPanel.getComponent(i));
        }
    }

    /**
     * Method for testing the correct point positions are calculated from a given board size and index.
     */
    @Test
    public void test_CalculateSquareBoardPosition_ValidatePositions() throws InvocationTargetException, IllegalAccessException {
        for (int i = 0; i < boardSize * boardSize; i++) {
            Point resultPosition = (Point) calculateSquarePosition.invoke(gameView, i, boardSize);

            Assertions.assertEquals(boardPositions.get(i), resultPosition);
        }
    }

    /**
     * Method for testing the selectSquare method; by testing that the View pushSelectEvent
     * method is called.
     */
    @Test
    public void test_SelectSquare_PushSelectEvent() throws IllegalAccessException, InvocationTargetException {
        JPanel boardPanel = (JPanel) boardField.get(gameView);
        Square square = (Square) boardPanel.getComponent(0);
        Point position = (Point) calculateSquarePosition.invoke(gameView, 0, boardSize);

        selectSquare.invoke(gameView, square, position);

        Mockito.verify(gameView, Mockito.times(1)).pushSelectEvent(position);
        Mockito.verify(gameView, Mockito.times(0)).pushFlagEvent(position);
        Mockito.verify(gameView, Mockito.times(0)).pushResetGameEvent();
    }

    /**
     * Method for testing the selectSquare method; by testing that the View pushSelectEvent
     * method is not called if square is null.
     */
    @Test
    public void test_SelectSquare_SquareIsNull() throws IllegalAccessException, InvocationTargetException {
        Square square = null;
        Point position = (Point) calculateSquarePosition.invoke(gameView, 0, boardSize);

        selectSquare.invoke(gameView, square, position);

        Mockito.verify(gameView, Mockito.times(0)).pushSelectEvent(position);
        Mockito.verify(gameView, Mockito.times(0)).pushFlagEvent(position);
        Mockito.verify(gameView, Mockito.times(0)).pushResetGameEvent();
    }

    /**
     * Method for testing the selectSquare method; by testing that the View pushSelectEvent
     * method is not called when the selected square has a value (its state is not hidden).
     */
    @Test
    public void test_SelectSquare_AlreadySelected() throws IllegalAccessException, InvocationTargetException {
        JPanel boardPanel = (JPanel) boardField.get(gameView);
        Square square = (Square) boardPanel.getComponent(0);
        square.setValue(2);
        Point position = (Point) calculateSquarePosition.invoke(gameView, 0, boardSize);

        selectSquare.invoke(gameView, square, position);

        Mockito.verify(gameView, Mockito.times(0)).pushSelectEvent(position);
        Mockito.verify(gameView, Mockito.times(0)).pushFlagEvent(position);
        Mockito.verify(gameView, Mockito.times(0)).pushResetGameEvent();
    }

    /**
     * Method for testing the selectSquare method; by testing that the View pushSelectEvent
     * method is not called when the board is locked (game is over or won).
     */
    @Test
    public void test_SelectSquare_BoardLocked() throws IllegalAccessException, InvocationTargetException {
        JPanel boardPanel = (JPanel) boardField.get(gameView);
        Square square = (Square) boardPanel.getComponent(0);
        boardLockedField.setBoolean(gameView, true);
        Point position = (Point) calculateSquarePosition.invoke(gameView, 0, boardSize);

        selectSquare.invoke(gameView, square, position);

        Mockito.verify(gameView, Mockito.times(0)).pushSelectEvent(position);
        Mockito.verify(gameView, Mockito.times(0)).pushFlagEvent(position);
        Mockito.verify(gameView, Mockito.times(0)).pushResetGameEvent();
    }

    /**
     * Method for testing the flagSquare method; by testing that the View pushFlagEvent
     * method is called.
     */
    @Test
    public void test_FlagSquare_PushFlagEvent() throws IllegalAccessException, InvocationTargetException {
        JPanel boardPanel = (JPanel) boardField.get(gameView);
        Square square = (Square) boardPanel.getComponent(0);
        Point position = (Point) calculateSquarePosition.invoke(gameView, 0, boardSize);

        flagSquare.invoke(gameView, square, position);

        Mockito.verify(gameView, Mockito.times(1)).pushFlagEvent(position);
        Mockito.verify(gameView, Mockito.times(0)).pushSelectEvent(position);
        Mockito.verify(gameView, Mockito.times(0)).pushResetGameEvent();
    }

    /**
     * Method for testing the flagSquare method; by testing that the View pushFlagEvent
     * method is not called if square is null.
     */
    @Test
    public void test_FlagSquare_SquareIsNull() throws InvocationTargetException, IllegalAccessException {
        Square square = null;
        Point position = (Point) calculateSquarePosition.invoke(gameView, 0, boardSize);

        flagSquare.invoke(gameView, square, position);

        Mockito.verify(gameView, Mockito.times(0)).pushFlagEvent(position);
        Mockito.verify(gameView, Mockito.times(0)).pushSelectEvent(position);
        Mockito.verify(gameView, Mockito.times(0)).pushResetGameEvent();
    }

    /**
     * Method for testing the flagSquare method; by testing that the View pushFlagEvent
     * method is not called when the selected square has a value (its state is not hidden or flagged).
     */
    @Test
    public void test_FlagSquare_AlreadySelected() throws IllegalAccessException, InvocationTargetException {
        JPanel boardPanel = (JPanel) boardField.get(gameView);
        Square square = (Square) boardPanel.getComponent(0);
        square.setValue(2);
        Point position = (Point) calculateSquarePosition.invoke(gameView, 0, boardSize);

        flagSquare.invoke(gameView, square, position);

        Mockito.verify(gameView, Mockito.times(0)).pushFlagEvent(position);
        Mockito.verify(gameView, Mockito.times(0)).pushSelectEvent(position);
        Mockito.verify(gameView, Mockito.times(0)).pushResetGameEvent();
    }

    /**
     * Method for testing the flagSquare method; by testing that the View pushFlagEvent
     * method is not called when the board is locked (game is over or won).
     */
    @Test
    public void test_FlagSquare_BoardLocked() throws IllegalAccessException, InvocationTargetException {
        JPanel boardPanel = (JPanel) boardField.get(gameView);
        Square square = (Square) boardPanel.getComponent(0);
        boardLockedField.setBoolean(gameView,true);
        Point position = (Point) calculateSquarePosition.invoke(gameView, 0, boardSize);

        flagSquare.invoke(gameView, square, position);

        Mockito.verify(gameView, Mockito.times(0)).pushFlagEvent(position);
        Mockito.verify(gameView, Mockito.times(0)).pushSelectEvent(position);
        Mockito.verify(gameView, Mockito.times(0)).pushResetGameEvent();
    }
}
