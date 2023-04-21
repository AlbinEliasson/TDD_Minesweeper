package com.dt042g.project.mvc.models.gamemodel;

import com.dt042g.project.mvc.models.BackingSquare;
import com.dt042g.project.mvc.models.GameModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import java.awt.Point;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

/**
 * Class containing dynamic unit-tests for the *GameModel* class.
 *
 * @author Martin K. Herkules (makr1906) & Albin Eliasson (alel2104)
 */
public class GameModelDynamicTests {
    private static final Integer[] sizes = new Integer[] { 5, 10, 20 };

    private static Field fieldBoard;
    private static Field fieldMineChance;
    private static Method methodGenerateSquares;

    /**
     * Argument generator method for running tests with different sized boards.
     *
     * @return Argument with a model and board size.
     */
    private static Stream<Arguments> Model() {
        return Arrays.stream(sizes).map(size -> Arguments.of(new GameModel(size), size));
    }

    /**
     * Argument generator method for running tests with different sized boards,
     * and with all possible locations.
     *
     * @return Argument with a model and board size.
     */
    private static Stream<Arguments> ModelAndLocation() {
        return Arrays.stream(sizes).mapMulti((size, consumer) -> {
            GameModel model = new GameModel(size);

            for(int x = 0; x < size; x++)
                for(int y = 0; y < size; y++)
                    consumer.accept(Arguments.of(model, size, new Point(x, y)));
        });
    }

    /**
     * Argument generator method for running tests with different sized boards,
     * and with all possible locations; including pre-generating the board and
     * performing random "fake interactions" with the board to add revealed and
     * flagged squares.
     *
     * @return Argument with a model and board size.
     */
    private static Stream<Arguments> ModelAndLocationGenerated() {
        Random random = new Random();
        return Arrays.stream(sizes).mapMulti((size, consumer) -> {
            GameModel model = Mockito.spy(new GameModel(size));

            try {
                methodGenerateSquares.invoke(model, new Point(0, 0));
                for(List<BackingSquare> row : (List<List<BackingSquare>>) fieldBoard.get(model)) {
                    for(BackingSquare square : row) {
                        double r = random.nextDouble();
                        if(square.isMine()) {
                            if (r < .5)
                                square.setFlagged(true);
                        } else if(r < .34) {
                            square.setRevealed(true);
                        } else if(r > .8) {
                            square.setFlagged(true);
                        }
                    }
                }
            } catch (Exception e) {
                Assertions.fail("Failed to initialize model!", e);
            }

            for(int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    consumer.accept(Arguments.of(model, size, new Point(x, y)));
                }
            }
        });
    }

    /**
     * Method which is triggered before any test method is executed.
     */
    @BeforeAll
    public static void setupAll() throws NoSuchFieldException, NoSuchMethodException {
        fieldBoard = GameModel.class.getDeclaredField("_board");
        fieldBoard.setAccessible(true);
        fieldMineChance = GameModel.class.getDeclaredField("MINE_CHANCE");
        fieldMineChance.setAccessible(true);
        methodGenerateSquares = GameModel.class.getDeclaredMethod("generateSquares", Point.class);
        methodGenerateSquares.setAccessible(true);
    }

    /**
     * Method for testing the generateSquares method and check that the board
     * was in fact initialized to the correct size.
     *
     * @param model The model to use.
     * @param boardSize The expected size of the board.
     * @param location The "first location" argument.
     */
    @ParameterizedTest
    @MethodSource("ModelAndLocation")
    public void test_GenerateSquares_Size(GameModel model, int boardSize, Point location) throws IllegalAccessException, InvocationTargetException {
        methodGenerateSquares.invoke(model, location);
        Assertions.assertNotNull(fieldBoard.get(model));

        List<List<BackingSquare>> board = (List<List<BackingSquare>>) fieldBoard.get(model);

        Assertions.assertEquals(boardSize, board.size());
        for(List<BackingSquare> row : board)
            Assertions.assertEquals(boardSize, row.size());
    }

    /**
     * Method for testing the generateSquares method and check that all
     * BackingSquares for the board were initialized correctly.
     *
     * @param model The model to use.
     * @param boardSize The expected size of the board.
     * @param location The "first location" argument.
     */
    @ParameterizedTest
    @MethodSource("ModelAndLocation")
    public void test_GenerateSquares_InitializeBackingSquares(GameModel model, int boardSize, Point location) throws InvocationTargetException, IllegalAccessException {
        methodGenerateSquares.invoke(model, location);
        Assertions.assertNotNull(fieldBoard.get(model));

        List<List<BackingSquare>> board = (List<List<BackingSquare>>) fieldBoard.get(model);

        for(int x = 0; x < boardSize; x++) {
            for (int y = 0; y < boardSize; y++)
                Assertions.assertInstanceOf(BackingSquare.class, board.get(x).get(y));
        }
    }

    /**
     * Method for testing the generateSquares method and check that the "first
     * location" square and its neighbors are not mines.
     *
     * @param model The model to use.
     * @param boardSize The expected size of the board.
     * @param location The "first location" argument.
     */
    @ParameterizedTest
    @MethodSource("ModelAndLocation")
    public void test_GenerateSquares_FirstIsNotMine(GameModel model, int boardSize, Point location) throws InvocationTargetException, IllegalAccessException {
        methodGenerateSquares.invoke(model, location);
        List<List<BackingSquare>> board = (List<List<BackingSquare>>) fieldBoard.get(model);

        Assertions.assertFalse(board.get(location.x).get(location.y).isMine());

        if(location.x > 0)
            Assertions.assertFalse(board.get(location.x - 1).get(location.y).isMine()); // West
        if(location.x < boardSize - 1)
            Assertions.assertFalse(board.get(location.x + 1).get(location.y).isMine()); // East
        if(location.y > 0)
            Assertions.assertFalse(board.get(location.x).get(location.y - 1).isMine()); // North
        if(location.y < boardSize - 1)
            Assertions.assertFalse(board.get(location.x).get(location.y + 1).isMine()); // South
        if(location.x > 0 && location.y > 0)
            Assertions.assertFalse(board.get(location.x - 1).get(location.y - 1).isMine()); // North-west
        if(location.x < boardSize - 1 && location.y < boardSize - 1)
            Assertions.assertFalse(board.get(location.x + 1).get(location.y + 1).isMine()); // South-east
        if(location.x > 0 && location.y < boardSize - 1)
            Assertions.assertFalse(board.get(location.x - 1).get(location.y + 1).isMine()); // South-west
        if(location.x < boardSize - 1 && location.y > 0)
            Assertions.assertFalse(board.get(location.x + 1).get(location.y - 1).isMine()); // North-east
    }

    /**
     * Method for testing the generateSquares method and check that the number
     * of generated mines roughly matches the configures mineChance.
     *
     * @param model The model to use.
     * @param boardSize The expected size of the board.
     * @param location The "first location" argument.
     */
    @ParameterizedTest
    @MethodSource("ModelAndLocation")
    public void test_GenerateSquares_MineChance(GameModel model, int boardSize, Point location) throws InvocationTargetException, IllegalAccessException {
        int iterations = 100;
        double[] chances = new double[iterations];

        for(int i = 0; i < iterations; i++) {
            methodGenerateSquares.invoke(model, location);
            List<List<BackingSquare>> board = (List<List<BackingSquare>>) fieldBoard.get(model);

            double mineCount = 0;
            for(List<BackingSquare> row : board)
                for(BackingSquare square: row)
                    mineCount += square.isMine() ? 1 : 0;

            chances[i] = mineCount / (boardSize * boardSize);
        }

        double average = Arrays.stream(chances).average().getAsDouble();

        double mineChance = (double) fieldMineChance.get(model);
        Assertions.assertTrue(mineChance - 1 < average && mineChance + 1 > average);
    }

    /**
     * Method for testing behaviour of generateSquares method when a null first
     * location is passed.
     *
     * @param model The model to use.
     * @param boardSize The expected size of the board.
     */
    @ParameterizedTest
    @MethodSource("Model")
    public void test_GenerateSquares_FirstLocationNull(GameModel model, int boardSize) {
        Assertions.assertDoesNotThrow(() -> methodGenerateSquares.invoke(model, new Object[] { null }));
    }

    /**
     * Method for testing behaviour of generateSquares method when a negative
     * first location is passed.
     *
     * @param model The model to use.
     * @param boardSize The expected size of the board.
     */
    @ParameterizedTest
    @MethodSource("Model")
    public void test_GenerateSquares_FirstLocationNegative(GameModel model, int boardSize) throws IllegalAccessException {
        try {
            methodGenerateSquares.invoke(model, new Point(Integer.MAX_VALUE * -1, Integer.MAX_VALUE * -1));
        } catch(InvocationTargetException e) {
            return;
        }

        Assertions.fail("Expected exception to be caused!");
    }

    /**
     * Method for testing behaviour of generateSquares method when a OOB first
     * location is passed.
     *
     * @param model The model to use.
     * @param boardSize The expected size of the board.
     */
    @ParameterizedTest
    @MethodSource("Model")
    public void test_GenerateSquares_FirstLocationOOB(GameModel model, int boardSize) throws IllegalAccessException {
        try {
            methodGenerateSquares.invoke(model, new Point(Integer.MAX_VALUE, Integer.MAX_VALUE));
        } catch(InvocationTargetException e) {
            return;
        }

        Assertions.fail("Expected exception to be caused!");
    }

    /**
     * Method for checking the isMine method to ensure that correct values are
     * returned when a valid location is provided.
     *
     * @param model The model to use.
     * @param boardSize The expected size of the board.
     * @param location The "first location" argument.
     */
    @ParameterizedTest
    @MethodSource("ModelAndLocationGenerated")
    public void test_IsMine_LocationValid(GameModel model, int boardSize, Point location) throws InvocationTargetException, IllegalAccessException {
        methodGenerateSquares.invoke(model, location);
        List<List<BackingSquare>> board = (List<List<BackingSquare>>) fieldBoard.get(model);

        boolean originalRealValue = board.get(location.x).get(location.y).isMine();
        Assertions.assertEquals(originalRealValue, model.isMine(location));

        board.get(location.x).get(location.y).setMine(!originalRealValue);
        Assertions.assertEquals(!originalRealValue, model.isMine(location));
    }

    /**
     * Method for checking the behaviour of the isMine method when a null
     * location is provided.
     *
     * @param model The model to use.
     * @param boardSize The expected size of the board.
     */
    @ParameterizedTest
    @MethodSource("Model")
    public void test_IsMine_LocationNull(GameModel model, int boardSize) throws InvocationTargetException, IllegalAccessException {
        methodGenerateSquares.invoke(model, new Point(0, 0));

        Assertions.assertThrows(NullPointerException.class, () -> model.isMine(null));
    }

    /**
     * Method for checking the behaviour of the isMine method when a negative
     * location is provided.
     *
     * @param model The model to use.
     * @param boardSize The expected size of the board.
     */
    @ParameterizedTest
    @MethodSource("Model")
    public void test_IsMine_LocationNegative(GameModel model, int boardSize) throws InvocationTargetException, IllegalAccessException {
        methodGenerateSquares.invoke(model, new Point(0, 0));

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> model.isMine(new Point(Integer.MAX_VALUE * -1, Integer.MAX_VALUE * -1)));
    }

    /**
     * Method for checking the behaviour of the isMine method when a OOB
     * location is provided.
     *
     * @param model The model to use.
     * @param boardSize The expected size of the board.
     */
    @ParameterizedTest
    @MethodSource("Model")
    public void test_IsMine_LocationOOB(GameModel model, int boardSize) throws InvocationTargetException, IllegalAccessException {
        methodGenerateSquares.invoke(model, new Point(0, 0));

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> model.isMine(new Point(Integer.MAX_VALUE, Integer.MAX_VALUE)));
    }

    /**
     * Method for checking the setSquareFlag method to ensure that correct
     * values are returned when a valid location is provided.
     *
     * @param model The model to use.
     * @param boardSize The expected size of the board.
     * @param location The "first location" argument.
     */
    @ParameterizedTest
    @MethodSource("ModelAndLocationGenerated")
    public void test_SetSquareFlag_LocationValid(GameModel model, int boardSize, Point location) throws InvocationTargetException, IllegalAccessException {
        methodGenerateSquares.invoke(model, location);
        List<List<BackingSquare>> board = (List<List<BackingSquare>>) fieldBoard.get(model);

        boolean originalValue = board.get(location.x).get(location.y).isFlagged();

        model.setSquareFlag(location, originalValue);
        Assertions.assertEquals(originalValue, board.get(location.x).get(location.y).isFlagged());

        model.setSquareFlag(location, !originalValue);
        Assertions.assertEquals(!originalValue, board.get(location.x).get(location.y).isFlagged());

        model.setSquareFlag(location, !originalValue);
        Assertions.assertEquals(!originalValue, board.get(location.x).get(location.y).isFlagged());

        model.setSquareFlag(location, originalValue);
        Assertions.assertEquals(originalValue, board.get(location.x).get(location.y).isFlagged());
    }

    /**
     * Method for checking the behaviour of the setSquareFlag method when a null
     * location is provided.
     *
     * @param model The model to use.
     * @param boardSize The expected size of the board.
     */
    @ParameterizedTest
    @MethodSource("Model")
    public void test_SetSquareFlag_LocationNull(GameModel model, int boardSize) throws InvocationTargetException, IllegalAccessException {
        methodGenerateSquares.invoke(model, new Point(0, 0));

        Assertions.assertThrows(NullPointerException.class, () -> model.setSquareFlag(null, true));
        Assertions.assertThrows(NullPointerException.class, () -> model.setSquareFlag(null, false));
    }

    /**
     * Method for checking the behaviour of the setSquareFlag method when a
     * negative location is provided.
     *
     * @param model The model to use.
     * @param boardSize The expected size of the board.
     */
    @ParameterizedTest
    @MethodSource("Model")
    public void test_SetSquareFlag_LocationNegative(GameModel model, int boardSize) throws InvocationTargetException, IllegalAccessException {
        methodGenerateSquares.invoke(model, new Point(0, 0));

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> model.setSquareFlag(new Point(Integer.MAX_VALUE * -1, Integer.MAX_VALUE * -1), true));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> model.setSquareFlag(new Point(Integer.MAX_VALUE * -1, Integer.MAX_VALUE * -1), false));
    }

    /**
     * Method for checking the behaviour of the setSquareFlag method when a OOB
     * location is provided.
     *
     * @param model The model to use.
     * @param boardSize The expected size of the board.
     */
    @ParameterizedTest
    @MethodSource("Model")
    public void test_SetSquareFlag_LocationOOB(GameModel model, int boardSize) throws InvocationTargetException, IllegalAccessException {
        methodGenerateSquares.invoke(model, new Point(0, 0));

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> model.setSquareFlag(new Point(Integer.MAX_VALUE, Integer.MAX_VALUE), true));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> model.setSquareFlag(new Point(Integer.MAX_VALUE, Integer.MAX_VALUE), false));
    }

    /**
     * Method for checking the isFlagged method to ensure that correct values are
     * returned when a valid location is provided.
     *
     * @param model The model to use.
     * @param boardSize The expected size of the board.
     * @param location The "first location" argument.
     */
    @ParameterizedTest
    @MethodSource("ModelAndLocationGenerated")
    public void test_IsFlagged_LocationValid(GameModel model, int boardSize, Point location) throws InvocationTargetException, IllegalAccessException {
        methodGenerateSquares.invoke(model, location);
        List<List<BackingSquare>> board = (List<List<BackingSquare>>) fieldBoard.get(model);

        boolean originalRealValue = board.get(location.x).get(location.y).isFlagged();
        Assertions.assertEquals(originalRealValue, model.isFlagged(location));

        board.get(location.x).get(location.y).setFlagged(!originalRealValue);
        Assertions.assertEquals(!originalRealValue, model.isFlagged(location));
    }

    /**
     * Method for checking the behaviour of the isFlagged method when a null
     * location is provided.
     *
     * @param model The model to use.
     * @param boardSize The expected size of the board.
     */
    @ParameterizedTest
    @MethodSource("Model")
    public void test_IsFlagged_LocationNull(GameModel model, int boardSize) throws InvocationTargetException, IllegalAccessException {
        methodGenerateSquares.invoke(model, new Point(0, 0));

        Assertions.assertThrows(NullPointerException.class, () -> model.isFlagged(null));
    }

    /**
     * Method for checking the behaviour of the isFlagged method when a negative
     * location is provided.
     *
     * @param model The model to use.
     * @param boardSize The expected size of the board.
     */
    @ParameterizedTest
    @MethodSource("Model")
    public void test_IsFlagged_LocationNegative(GameModel model, int boardSize) throws InvocationTargetException, IllegalAccessException {
        methodGenerateSquares.invoke(model, new Point(0, 0));

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> model.isFlagged(new Point(Integer.MAX_VALUE * -1, Integer.MAX_VALUE * -1)));
    }

    /**
     * Method for checking the behaviour of the isFlagged method when a OOB
     * location is provided.
     *
     * @param model The model to use.
     * @param boardSize The expected size of the board.
     */
    @ParameterizedTest
    @MethodSource("Model")
    public void test_IsFlagged_LocationOOB(GameModel model, int boardSize) throws InvocationTargetException, IllegalAccessException {
        methodGenerateSquares.invoke(model, new Point(0, 0));

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> model.isFlagged(new Point(Integer.MAX_VALUE, Integer.MAX_VALUE)));
    }

    /**
     * Method for checking the isRevealed method to ensure that correct values are
     * returned when a valid location is provided.
     *
     * @param model The model to use.
     * @param boardSize The expected size of the board.
     * @param location The "first location" argument.
     */
    @ParameterizedTest
    @MethodSource("ModelAndLocationGenerated")
    public void test_IsRevealed_LocationValid(GameModel model, int boardSize, Point location) throws InvocationTargetException, IllegalAccessException {
        methodGenerateSquares.invoke(model, location);
        List<List<BackingSquare>> board = (List<List<BackingSquare>>) fieldBoard.get(model);

        boolean originalRealValue = board.get(location.x).get(location.y).isRevealed();
        Assertions.assertEquals(originalRealValue, model.isRevealed(location));

        board.get(location.x).get(location.y).setRevealed(!originalRealValue);
        Assertions.assertEquals(!originalRealValue, model.isRevealed(location));
    }

    /**
     * Method for checking the behaviour of the isRevealed method when a null
     * location is provided.
     *
     * @param model The model to use.
     * @param boardSize The expected size of the board.
     */
    @ParameterizedTest
    @MethodSource("Model")
    public void test_IsRevealed_LocationNull(GameModel model, int boardSize) throws InvocationTargetException, IllegalAccessException {
        methodGenerateSquares.invoke(model, new Point(0, 0));

        Assertions.assertThrows(NullPointerException.class, () -> model.isRevealed(null));
    }

    /**
     * Method for checking the behaviour of the isRevealed method when a
     * negative location is provided.
     *
     * @param model The model to use.
     * @param boardSize The expected size of the board.
     */
    @ParameterizedTest
    @MethodSource("Model")
    public void test_IsRevealed_LocationNegative(GameModel model, int boardSize) throws InvocationTargetException, IllegalAccessException {
        methodGenerateSquares.invoke(model, new Point(0, 0));

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> model.isRevealed(new Point(Integer.MAX_VALUE * -1, Integer.MAX_VALUE * -1)));
    }

    /**
     * Method for checking the behaviour of the isRevealed method when a OOB
     * location is provided.
     *
     * @param model The model to use.
     * @param boardSize The expected size of the board.
     */
    @ParameterizedTest
    @MethodSource("Model")
    public void test_IsRevealed_LocationOOB(GameModel model, int boardSize) throws InvocationTargetException, IllegalAccessException {
        methodGenerateSquares.invoke(model, new Point(0, 0));

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> model.isRevealed(new Point(Integer.MAX_VALUE, Integer.MAX_VALUE)));
    }
}
