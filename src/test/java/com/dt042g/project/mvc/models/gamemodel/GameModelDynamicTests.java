package com.dt042g.project.mvc.models.gamemodel;

import com.dt042g.project.mvc.models.BackingSquare;
import com.dt042g.project.mvc.models.GameModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.awt.Point;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
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

    /**
     * Method for checking the getSquareValue method to ensure that correct
     * values are returned when a valid location is provided.
     *
     * @param model The model to use.
     * @param boardSize The expected size of the board.
     * @param location The "first location" argument.
     */
    @ParameterizedTest
    @MethodSource("ModelAndLocationGenerated")
    public void test_GetSquareValue_LocationValid(GameModel model, int boardSize, Point location) throws InvocationTargetException, IllegalAccessException {
        methodGenerateSquares.invoke(model, location);
        List<List<BackingSquare>> board = (List<List<BackingSquare>>) fieldBoard.get(model);

        int value = 0;

        if(location.x > 0)
            value += board.get(location.x - 1).get(location.y).isMine() ? 1 : 0; // West
        if(location.x < boardSize - 1)
            value += board.get(location.x + 1).get(location.y).isMine() ? 1 : 0; // East
        if(location.y > 0)
            value += board.get(location.x).get(location.y - 1).isMine() ? 1 : 0; // North
        if(location.y < boardSize - 1)
            value += board.get(location.x).get(location.y + 1).isMine() ? 1 : 0; // South
        if(location.x > 0 && location.y > 0)
            value += board.get(location.x - 1).get(location.y - 1).isMine() ? 1 : 0; // North-west
        if(location.x < boardSize - 1 && location.y < boardSize - 1)
            value += board.get(location.x + 1).get(location.y + 1).isMine() ? 1 : 0; // South-east
        if(location.x > 0 && location.y < boardSize - 1)
            value += board.get(location.x - 1).get(location.y + 1).isMine() ? 1 : 0; // South-west
        if(location.x < boardSize - 1 && location.y > 0)
            value += board.get(location.x + 1).get(location.y - 1).isMine() ? 1 : 0; // North-east

        Assertions.assertEquals(value, model.getSquareValue(location));
    }

    /**
     * Method for checking the behaviour of the getSquareValue method when a
     * null location is provided.
     *
     * @param model The model to use.
     * @param boardSize The expected size of the board.
     */
    @ParameterizedTest
    @MethodSource("Model")
    public void test_GetSquareValue_LocationNull(GameModel model, int boardSize) throws InvocationTargetException, IllegalAccessException {
        methodGenerateSquares.invoke(model, new Point(0, 0));

        Assertions.assertThrows(NullPointerException.class, () -> model.getSquareValue(null));
    }

    /**
     * Method for checking the behaviour of the getSquareValue method when a
     * negative location is provided.
     *
     * @param model The model to use.
     * @param boardSize The expected size of the board.
     */
    @ParameterizedTest
    @MethodSource("Model")
    public void test_GetSquareValue_LocationNegative(GameModel model, int boardSize) throws InvocationTargetException, IllegalAccessException {
        methodGenerateSquares.invoke(model, new Point(0, 0));

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> model.getSquareValue(new Point(Integer.MAX_VALUE * -1, Integer.MAX_VALUE * -1)));
    }

    /**
     * Method for checking the behaviour of the getSquareValue method when a OOB
     * location is provided.
     *
     * @param model The model to use.
     * @param boardSize The expected size of the board.
     */
    @ParameterizedTest
    @MethodSource("Model")
    public void test_GetSquareValue_LocationOOB(GameModel model, int boardSize) throws InvocationTargetException, IllegalAccessException {
        methodGenerateSquares.invoke(model, new Point(0, 0));

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> model.getSquareValue(new Point(Integer.MAX_VALUE, Integer.MAX_VALUE)));
    }

    /**
     * Method for testing the selectSquare method to ensure that the board is
     * automatically generated if it does not exist upon method call.
     *
     * @param model The model to use.
     * @param boardSize The expected size of the board.
     */
    @ParameterizedTest
    @MethodSource("Model")
    public void test_SelectSquare_LocationValid_EnsureGeneration(GameModel model, int boardSize) throws IllegalAccessException {
        List<List<BackingSquare>> board = (List<List<BackingSquare>>) fieldBoard.get(model);

        // Check that the board is null to start with; since if it isn't this
        // test would be useless; since it wouldn't have to trigger generation.
        // This could change in the future; but as it stands now, a first
        // location is needed to generate the squares. And since the model is
        // freshly generated for each iteration of this test; the board already
        // existing at this stage would mean a bug.
        Assertions.assertNull(board, "Board already exists; cannot test selectSquare automatic generation!");

        Assertions.assertDoesNotThrow(() -> model.selectSquare(new Point(0, 0)), "Board generation test failed due to selectSquare method throwing an error!");

        board = (List<List<BackingSquare>>) fieldBoard.get(model);

        Assertions.assertNotNull(board, "Board not generated!");
    }

    /**
     * Method for checking the selectSquare method to ensure that correct
     * values events are pushed when a valid location is provided.
     *
     * @param model The model to use.
     * @param boardSize The expected size of the board.
     * @param location The "first location" argument.
     */
    @ParameterizedTest
    @MethodSource("ModelAndLocationGenerated")
    public void test_SelectSquare_LocationValid_EnsureEvents(GameModel model, int boardSize, Point location) {
        Mockito.clearInvocations(model);

        boolean originalIsRevealed = model.isRevealed(location);

        model.selectSquare(location);

        if(originalIsRevealed || model.isFlagged(location)) {
            Mockito.verify(model, Mockito.times(0).description("Invalid number of MineHit events when calling on revealed/flagged square; expected 0!")).pushMineHitEvent(Mockito.any());
            Mockito.verify(model, Mockito.times(0).description("Invalid number of RevealSquare events when calling on revealed/flagged square; expected 0!")).pushRevealSquareEvent(Mockito.anyList());
        } else if(model.isMine(location)) {
            Mockito.verify(model, Mockito.times(1).description("Invalid number of MineHit events when calling on mine square; expected 1!")).pushMineHitEvent(Mockito.any());
            Mockito.verify(model, Mockito.times(0).description("Invalid number of RevealSquare events when calling on mine square; expected 0!")).pushRevealSquareEvent(Mockito.anyList());
        } else {
            Mockito.verify(model, Mockito.times(0).description("Invalid number of MineHit events when calling on value square; expected 0!")).pushMineHitEvent(Mockito.any());
            Mockito.verify(model, Mockito.times(1).description("Invalid number of RevealSquare events when calling on value square; expected 1!")).pushRevealSquareEvent(Mockito.anyList());
        }
    }

    /**
     * Method for checking the selectSquare method to ensure that correct
     * squares are revealed when a valid location is provided.
     *
     * It tests to ensure that all revealed squares are in some way connected to
     * the original location. Either by being the original square, or by being
     * connected to it via a zero-value square.
     *
     * @param model The model to use.
     * @param boardSize The expected size of the board.
     * @param location The "first location" argument.
     */
    @ParameterizedTest
    @MethodSource("ModelAndLocationGenerated")
    public void test_SelectSquare_LocationValid_ValidateRevealConnections(GameModel model, int boardSize, Point location) {
        if(model.isRevealed(location) || model.isFlagged(location) || model.isMine(location))
            return;

        Mockito.clearInvocations(model);

        ArgumentCaptor<List<Point>> reveals = ArgumentCaptor.forClass(List.class);

        model.selectSquare(location);
        Mockito.verify(model).pushRevealSquareEvent(reveals.capture());

        List<Point> connectedSquares = new ArrayList<>();
        connectedSquares.add(location);

        for(Point revealed : reveals.getValue()) {
            Point foundConnected = helper_CheckPointConnection(revealed, connectedSquares);
            if(
                    foundConnected != null &&
                            (
                                    location == foundConnected ||
                                            model.getSquareValue(location) == 0 ||
                                            model.getSquareValue(foundConnected) == 0
                            )
            )
                connectedSquares.add(revealed);
            else
                Assertions.fail(String.format(
                        "Square at (%d, %d) is not properly connected to location (%d, %d) or to a neighbor that is connected to location.",
                        revealed.x, revealed.y, location.x, location.y));
        }
    }

    /**
     * Method for checking if a location is connected to any location in a set.
     *
     * @param subject The location to check
     * @param locations The set of locations to check connection to.
     *
     * @return Whether the locations are connected.
     */
    private Point helper_CheckPointConnection(Point subject, List<Point> locations) {
        for(Point location : locations)
            if(subject == location || helper_PointIsNeighbor(subject, location))
                return location;

        return null;
    }

    /**
     * Method for checking of two locations are next to each other.
     *
     * @param locationA The first location.
     * @param locationB The second location.
     *
     * @return Whether the two locations are neighbors.
     */
    private boolean helper_PointIsNeighbor(Point locationA, Point locationB) {
        int differenceX = Math.abs(locationA.x - locationB.x);
        int differenceY = Math.abs(locationA.y - locationB.y);
        return differenceX <= 1 && differenceY <= 1 && (differenceX + differenceY) != 0;
    }

    /**
     * Method for checking the behaviour of the selectSquare method when a
     * null location is provided.
     *
     * @param model The model to use.
     * @param boardSize The expected size of the board.
     */
    @ParameterizedTest
    @MethodSource("Model")
    public void test_SelectSquare_LocationNull(GameModel model, int boardSize) throws InvocationTargetException, IllegalAccessException {
        methodGenerateSquares.invoke(model, new Point(0, 0));

        Assertions.assertThrows(NullPointerException.class, () -> model.selectSquare(null));
    }

    /**
     * Method for checking the behaviour of the selectSquare method when a
     * negative location is provided.
     *
     * @param model The model to use.
     * @param boardSize The expected size of the board.
     */
    @ParameterizedTest
    @MethodSource("Model")
    public void test_SelectSquare_LocationNegative(GameModel model, int boardSize) throws InvocationTargetException, IllegalAccessException {
        methodGenerateSquares.invoke(model, new Point(0, 0));

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> model.selectSquare(new Point(Integer.MAX_VALUE * -1, Integer.MAX_VALUE * -1)));
    }

    /**
     * Method for checking the behaviour of the selectSquare method when a OOB
     * location is provided.
     *
     * @param model The model to use.
     * @param boardSize The expected size of the board.
     */
    @ParameterizedTest
    @MethodSource("Model")
    public void test_SelectSquare_LocationOOB(GameModel model, int boardSize) throws InvocationTargetException, IllegalAccessException {
        methodGenerateSquares.invoke(model, new Point(0, 0));

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> model.selectSquare(new Point(Integer.MAX_VALUE, Integer.MAX_VALUE)));
    }

    /**
     * Method for checking the behaviour of the reset method when a board
     * exists.
     *
     * @param model The model to use.
     * @param boardSize The expected size of the board.
     */
    @ParameterizedTest
    @MethodSource("Model")
    public void test_Reset_BoardExists(GameModel model, int boardSize) throws InvocationTargetException, IllegalAccessException {
        methodGenerateSquares.invoke(model, new Point(0, 0));

        Assertions.assertNotNull(fieldBoard.get(model), "Unable to test reset method; board was null before reset!");

        model.reset();

        Assertions.assertNull(fieldBoard.get(model), "Failed to reset board; board still exists after call!");
    }

    /**
     * Method for checking the behaviour of the reset method when a board
     * does not exist.
     *
     * @param model The model to use.
     * @param boardSize The expected size of the board.
     */
    @ParameterizedTest
    @MethodSource("Model")
    public void test_Reset_NoBoard(GameModel model, int boardSize) throws InvocationTargetException, IllegalAccessException {
        // Call method twice first to actually delete the board if it exists,
        // and the second time to check behaviour when resetting with null
        // board.
        Assertions.assertDoesNotThrow(model::reset);
        Assertions.assertDoesNotThrow(model::reset);

        Assertions.assertNull(fieldBoard.get(model), "Failed to reset board; board still exists after call!");
    }
}
