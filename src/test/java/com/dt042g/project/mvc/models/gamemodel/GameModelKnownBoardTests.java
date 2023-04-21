package com.dt042g.project.mvc.models.gamemodel;

import com.dt042g.project.mvc.models.BackingSquare;
import com.dt042g.project.mvc.models.GameModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.awt.Point;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Class containing "known board" unit-tests for the *GameModel* class.
 *
 * @author Martin K. Herkules (makr1906) & Albin Eliasson (alel2104)
 */
public class GameModelKnownBoardTests {
    /*
     * A template for a known board.
     *
     * Each cell corresponds to a square in the model. The first character
     * decides revealed(R)/flagged(F)/hidden(H). And the second its value, M is
     * for mines; and a number is for a "value square", with the number being
     * the exact number of neighboring squares.
     */
    private static final String[][] boardTemplate = new String[][] {
            { "H0", "H1", "HM", "R2", "FM" },   // (Hidden Zero     ), (Hidden One      ), (Hidden Mine     ), (Revealed Two    ), (Flagged Mine    )
            { "H2", "H3", "R2", "R2", "R1" },   // (Hidden Two      ), (Hidden Three    ), (Revealed Two    ), (Revealed Two    ), (Revealed One    )
            { "HM", "HM", "H1", "H0", "H0" },   // (Hidden Mine     ), (Hidden Mine     ), (Hidden One      ), (Hidden Zero     ), (Hidden Zero     )
            { "H2", "H2", "F2", "H2", "H2" },   // (Hidden Two      ), (Hidden Two      ), (Flagged Two     ), (Hidden Two      ), (Hidden Two      )
            { "H0", "H0", "H1", "HM", "FM" }    // (Hidden Zero     ), (Hidden Zero     ), (Hidden One      ), (Hidden Mine     ), (Flagged Mine    )
    };

    private static Field fieldBoard;

    /**
     * Argument generator method for running tests with all possible locations.
     *
     * @return Argument with a model and board size.
     */
    private static Stream<Arguments> Location() {
        return IntStream.range(0, boardTemplate.length).boxed()
                .flatMap(x -> IntStream.range(0, boardTemplate.length)
                        .mapToObj(y -> Arguments.of(new Point(x, y))));
    }

    private GameModel model;

    /**
     * Method which is triggered before any test method is executed.
     */
    @BeforeAll
    public static void setupAll() throws NoSuchFieldException {
        fieldBoard = GameModel.class.getDeclaredField("_board");
        fieldBoard.setAccessible(true);
    }

    /**
     * Method which is triggered before each individual test method is executed.
     */
    @BeforeEach
    public void setupEach() throws IllegalAccessException {
        model = new GameModel(boardTemplate.length);

        // Parse board template into backing squares
        List<List<BackingSquare>> board = Arrays.stream(boardTemplate)
                        .map(row -> Arrays.stream(row).map(cell -> {
                            BackingSquare square = new BackingSquare();
                            square.setMine(cell.charAt(1) == 'M');
                            square.setRevealed(cell.charAt(0) == 'R');
                            square.setFlagged(cell.charAt(0) == 'F');
                            return square;
                        }).toList()).toList();

        fieldBoard.set(model, board);
    }

    /**
     * Method for checking the isMine method to ensure that correct values are
     * returned.
     *
     * @param location Location to check.
     */
    @ParameterizedTest
    @MethodSource("Location")
    public void test_IsMine_CorrectReturn(Point location) {
        Assertions.assertEquals(boardTemplate[location.x][location.y].charAt(1) == 'M', model.isMine(location));
    }

    /**
     * Method for checking the isFlagged method to ensure that correct values
     * are returned.
     *
     * @param location Location to check.
     */
    @ParameterizedTest
    @MethodSource("Location")
    public void test_IsFlagged_CorrectReturn(Point location) {
        Assertions.assertEquals(boardTemplate[location.x][location.y].charAt(0) == 'F', model.isFlagged(location),
                String.format(
                        "Square at [%d, %d] (%s) gave incorrect 'isFlagged()' value!",
                        location.x, location.y, boardTemplate[location.x][location.y]));
    }
}
