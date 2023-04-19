package com.dt042g.project.mvc.models;

import com.dt042g.project.testinghelpers.TestingConcreteModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.awt.Point;
import java.util.Arrays;
import java.util.List;

/**
 * Class containing unit-tests for the *Model* class.
 *
 * @author Martin K. Herkules (makr1906) & Albin Eliasson (alel2104)
 */
public class ModelTests {
    private static final List<Point> singleLocations = Arrays.asList(
            null, new Point(0, 0), new Point(Integer.MAX_VALUE, Integer.MAX_VALUE)
    );
    private static final List<List<Point>> multiLocations = Arrays.asList(
            Arrays.asList(new Point(0, 0)),
            Arrays.asList(new Point(Integer.MAX_VALUE, Integer.MAX_VALUE)),
            Arrays.asList(new Point(0, 0), null),
            Arrays.asList(new Point(Integer.MAX_VALUE, Integer.MAX_VALUE), null),
            Arrays.asList(new Point(0, 0), new Point(Integer.MAX_VALUE, Integer.MAX_VALUE)),
            Arrays.asList(new Point(0, 0), new Point(Integer.MAX_VALUE, Integer.MAX_VALUE), null)
    );

    private Model model;

    /**
     * Method which is triggered before each individual test method is executed.
     */
    @BeforeEach
    public void setupEach() {
        model = Mockito.spy(new TestingConcreteModel());
    }

    /**
     * Method for testing the pushRevealSquareEvent method; by testing that the
     * pushEvent method is triggered correctly when is executed.
     */
    @Test
    public void test_Model_PushRevealSquareEvent() {
        for(int i = 0; i < multiLocations.size(); i++) {
            model.pushRevealSquareEvent(multiLocations.get(i));
            Mockito.verify(model, Mockito.times(1)).pushEvent(Model.MODEL_REVEAL_SQUARE_EVENT, multiLocations.get(i));
        }
    }

    /**
     * Method for testing the pushMineHitEvent method; by testing that the
     * pushEvent method is triggered correctly when is executed.
     */
    @Test
    public void test_Model_pushMineHitEvent() {
        for(int i = 0; i < singleLocations.size(); i++) {
            model.pushMineHitEvent(singleLocations.get(i));
            Mockito.verify(model, Mockito.times(1)).pushEvent(Model.MODEL_MINE_HIT_EVENT, Arrays.asList(singleLocations.get(i)));
        }
    }

    /**
     * Method for testing the pushWinEvent method; by testing that the
     * pushEvent method is triggered correctly when is executed.
     */
    @Test
    public void test_Model_pushWinEvent() {
        model.pushWinEvent();
        Mockito.verify(model, Mockito.times(1)).pushEvent(Mockito.eq(Model.MODEL_WIN_EVENT), Mockito.any());
    }
}
