package com.dt042g.project.mvc.views;

import com.dt042g.project.mvc.views.gui.Square;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Point;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Class containing unit-tests for the GameView class.
 */
public class GameViewTests {
    private static GameView gameView;
    private static final int boardSize = 20;
    private static ArrayList<Point> boardPositions;
    private static Field boardField;
    private static Field boardLockedField;
    private static Field squareContentField;
    private static Method calculateSquarePosition;
    private static Method selectSquare;
    private static Method flagSquare;
    private static Method getSquareFromPosition;

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
        squareContentField = Square.class.getDeclaredField("_squareContent");
        squareContentField.setAccessible(true);

        // Private methods
        calculateSquarePosition = GameView.class.getDeclaredMethod(
                "calculateSquareBoardPosition", int.class, int.class);
        calculateSquarePosition.setAccessible(true);
        selectSquare = GameView.class.getDeclaredMethod("selectSquare", Square.class, Point.class);
        selectSquare.setAccessible(true);
        flagSquare = GameView.class.getDeclaredMethod("flagSquare", Square.class, Point.class);
        flagSquare.setAccessible(true);
        getSquareFromPosition = GameView.class.getDeclaredMethod("getSquareFromPosition", Point.class);
        getSquareFromPosition.setAccessible(true);

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

        Mockito.verify(gameView, Mockito.times(1)).pushSelectEvent(Mockito.any());
        Mockito.verify(gameView, Mockito.times(1)).pushSelectEvent(position);
        Mockito.verify(gameView, Mockito.times(0)).pushFlagEvent(Mockito.any());
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

        Mockito.verify(gameView, Mockito.times(0)).pushSelectEvent(Mockito.any());
        Mockito.verify(gameView, Mockito.times(0)).pushFlagEvent(Mockito.any());
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

        Mockito.verify(gameView, Mockito.times(0)).pushSelectEvent(Mockito.any());
        Mockito.verify(gameView, Mockito.times(0)).pushFlagEvent(Mockito.any());
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

        Mockito.verify(gameView, Mockito.times(0)).pushSelectEvent(Mockito.any());
        Mockito.verify(gameView, Mockito.times(0)).pushFlagEvent(Mockito.any());
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

        Mockito.verify(gameView, Mockito.times(1)).pushFlagEvent(Mockito.any());
        Mockito.verify(gameView, Mockito.times(1)).pushFlagEvent(position);
        Mockito.verify(gameView, Mockito.times(0)).pushSelectEvent(Mockito.any());
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

        Mockito.verify(gameView, Mockito.times(0)).pushFlagEvent(Mockito.any());
        Mockito.verify(gameView, Mockito.times(0)).pushSelectEvent(Mockito.any());
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

        Mockito.verify(gameView, Mockito.times(0)).pushFlagEvent(Mockito.any());
        Mockito.verify(gameView, Mockito.times(0)).pushSelectEvent(Mockito.any());
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

        Mockito.verify(gameView, Mockito.times(0)).pushFlagEvent(Mockito.any());
        Mockito.verify(gameView, Mockito.times(0)).pushSelectEvent(Mockito.any());
        Mockito.verify(gameView, Mockito.times(0)).pushResetGameEvent();
    }

    /**
     * Method for testing the getSquareFromPosition method; by testing that a null location returns
     * a null value.
     */
    @Test
    public void test_getSquareFromPosition_NullPointPosition() throws IllegalAccessException, InvocationTargetException {
        Point position = null;

        Square resultSquare = (Square) getSquareFromPosition.invoke(gameView, position);
        Assertions.assertNull(resultSquare);
    }

    /**
     * Method for testing the getSquareFromPosition method; by testing invalid X and Y-point positions returns
     * a null value.
     */
    @Test
    public void test_getSquareFromPosition_InvalidPointPositions() throws IllegalAccessException, InvocationTargetException {
        List<Point> invalidPositions = List.of(
                new Point(boardSize, 0),  // X-position to big (outside the board)
                new Point(-1, 0),      // X-position to small (outside the board)
                new Point(0, boardSize),  // Y-position to big (outside the board)
                new Point(0, -1));     // Y-position to small (outside the board)

        for (Point position : invalidPositions) {
            Square resultSquare = (Square) getSquareFromPosition.invoke(gameView, position);

            Assertions.assertNull(resultSquare);
        }
    }

    /**
     * Method for testing the getSquareFromPosition method; by testing that valid point positions return the
     * correct square from the board.
     */
    @Test
    public void test_getSquareFromPosition_validateSquare() throws IllegalAccessException, InvocationTargetException {
        JPanel boardPanel = (JPanel) boardField.get(gameView);

        for (int i = 0; i < boardSize * boardSize; i++) {
            Point position = boardPositions.get(i);
            Square square = (Square) boardPanel.getComponent(i);

            Square resultSquare = (Square) getSquareFromPosition.invoke(gameView, position);

            Assertions.assertEquals(square, resultSquare);
        }
    }

    /**
     * Method for testing the GameView setHidden method; by calling the setFlagged Square method on the
     * squares, execute the setHidden View method and checking that the State, icon and text of the squares
     * is of expected hidden values.
     */
    @Test
    public void test_SetHidden_SquareSetHidden() throws IllegalAccessException {
        JPanel boardPanel = (JPanel) boardField.get(gameView);
        Square.State expectedState = Square.State.HIDDEN;
        String expectedText = "";

        for (int i = 0; i < boardSize * boardSize; i++) {
            Point position = boardPositions.get(i);
            Square square = (Square) boardPanel.getComponent(i);
            JLabel squareLabel = (JLabel) squareContentField.get(square);

            square.setFlagged();

            gameView.setHidden(position);

            Assertions.assertEquals(expectedState, square.getState());
            Assertions.assertNull(squareLabel.getIcon());
            Assertions.assertEquals(expectedText, squareLabel.getText());
        }
    }

    /**
     * Method for testing the GameView setHidden method; by calling the method with an invalid
     * point location and checking that the State, icon and text of the squares has not changed.
     */
    @Test
    public void test_SetHidden_SquareIsNull() throws IllegalAccessException {
        JPanel boardPanel = (JPanel) boardField.get(gameView);
        Square.State expectedState = Square.State.FLAGGED;
        String expectedText = "";
        Point invalidPosition = new Point(boardSize, boardSize);

        for (int i = 0; i < boardSize * boardSize; i++) {
            Square square = (Square) boardPanel.getComponent(i);
            JLabel squareLabel = (JLabel) squareContentField.get(square);

            square.setFlagged();

            gameView.setHidden(invalidPosition);

            Assertions.assertEquals(expectedState, square.getState());
            Assertions.assertNotNull(squareLabel.getIcon());
            Assertions.assertEquals(expectedText, squareLabel.getText());
        }
    }

    /**
     * Method for testing the GameView setFlagged method; by calling the setHidden Square method on the
     * squares, execute the setFlagged View method and checking that the State, icon and text of the squares
     * is of expected flagged values.
     */
    @Test
    public void test_SetFlagged_SquareSetFlagged() throws IllegalAccessException {
        JPanel boardPanel = (JPanel) boardField.get(gameView);
        Square.State expectedState = Square.State.FLAGGED;
        String expectedText = "";

        for (int i = 0; i < boardSize * boardSize; i++) {
            Point position = boardPositions.get(i);
            Square square = (Square) boardPanel.getComponent(i);
            JLabel squareLabel = (JLabel) squareContentField.get(square);

            square.setHidden();

            gameView.setFlagged(position);

            Assertions.assertEquals(expectedState, square.getState());
            Assertions.assertNotNull(squareLabel.getIcon());
            Assertions.assertEquals(expectedText, squareLabel.getText());
        }
    }

    /**
     * Method for testing the GameView setFlagged method; by calling the method with an invalid
     * point location and checking that the State, icon and text of the squares has not changed.
     */
    @Test
    public void test_SetFlagged_SquareIsNull() throws IllegalAccessException {
        JPanel boardPanel = (JPanel) boardField.get(gameView);
        Square.State expectedState = Square.State.HIDDEN;
        Point invalidPosition = new Point(boardSize, boardSize);
        String expectedText = "";

        for (int i = 0; i < boardSize * boardSize; i++) {
            Square square = (Square) boardPanel.getComponent(i);
            JLabel squareLabel = (JLabel) squareContentField.get(square);

            square.setHidden();

            gameView.setFlagged(invalidPosition);

            Assertions.assertEquals(expectedState, square.getState());
            Assertions.assertNull(squareLabel.getIcon());
            Assertions.assertEquals(expectedText, squareLabel.getText());
        }
    }

    /**
     * Method for testing the GameView SetValue method; by calling the setHidden Square method on the
     * squares, execute the SetValue View method and checking that the State, icon and text of the squares
     * is of expected SetValue values.
     */
    @Test
    public void test_SetValue_SquareSetValue() throws IllegalAccessException {
        JPanel boardPanel = (JPanel) boardField.get(gameView);
        Square.State expectedState = Square.State.VALUE;
        int value = 4;

        for (int i = 0; i < boardSize * boardSize; i++) {
            Point position = boardPositions.get(i);
            Square square = (Square) boardPanel.getComponent(i);
            JLabel squareLabel = (JLabel) squareContentField.get(square);

            square.setHidden();

            gameView.setValue(position, value);

            Assertions.assertEquals(expectedState, square.getState());
            Assertions.assertNull(squareLabel.getIcon());
            Assertions.assertEquals(String.valueOf(value), squareLabel.getText());
        }
    }

    /**
     * Method for testing the SetValue setFlagged method; by calling the method with an invalid
     * point location and checking that the State, icon and text of the squares has not changed.
     */
    @Test
    public void test_SetValue_SquareIsNull() throws IllegalAccessException {
        JPanel boardPanel = (JPanel) boardField.get(gameView);
        Square.State expectedState = Square.State.HIDDEN;
        String expectedText = "";
        Point invalidPosition = new Point(boardSize, boardSize);
        int value = 4;

        for (int i = 0; i < boardSize * boardSize; i++) {
            Square square = (Square) boardPanel.getComponent(i);
            JLabel squareLabel = (JLabel) squareContentField.get(square);

            square.setHidden();

            gameView.setValue(invalidPosition, value);

            Assertions.assertEquals(expectedState, square.getState());
            Assertions.assertNull(squareLabel.getIcon());
            Assertions.assertEquals(expectedText, squareLabel.getText());
        }
    }

    /**
     * Method for testing the GameView SetMine method; by calling the setHidden Square method on the
     * squares, execute the SetMine View method and checking that the State, icon and text of the squares
     * is of expected SetMine values.
     */
    @Test
    public void test_SetMine_SquareSetMine() throws IllegalAccessException {
        JPanel boardPanel = (JPanel) boardField.get(gameView);
        Square.State expectedState = Square.State.VALUE;
        String expectedText = "";

        for (int i = 0; i < boardSize * boardSize; i++) {
            Point position = boardPositions.get(i);
            Square square = (Square) boardPanel.getComponent(i);
            JLabel squareLabel = (JLabel) squareContentField.get(square);

            square.setHidden();

            gameView.setMine(position);

            Assertions.assertEquals(expectedState, square.getState());
            Assertions.assertNotNull(squareLabel.getIcon());
            Assertions.assertEquals(expectedText, squareLabel.getText());
        }
    }

    /**
     * Method for testing the SetMine setFlagged method; by calling the method with an invalid
     * point location and checking that the State, icon and text of the squares has not changed.
     */
    @Test
    public void test_SetMine_SquareIsNull() throws IllegalAccessException {
        JPanel boardPanel = (JPanel) boardField.get(gameView);
        Square.State expectedState = Square.State.HIDDEN;
        Point invalidPosition = new Point(boardSize, boardSize);
        String expectedText = "";

        for (int i = 0; i < boardSize * boardSize; i++) {
            Square square = (Square) boardPanel.getComponent(i);
            JLabel squareLabel = (JLabel) squareContentField.get(square);

            square.setHidden();

            gameView.setMine(invalidPosition);

            Assertions.assertEquals(expectedState, square.getState());
            Assertions.assertNull(squareLabel.getIcon());
            Assertions.assertEquals(expectedText, squareLabel.getText());
        }
    }
}
