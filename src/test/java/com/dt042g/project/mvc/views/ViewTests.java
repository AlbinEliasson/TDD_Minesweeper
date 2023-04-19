package com.dt042g.project.mvc.views;

import com.dt042g.project.testinghelpers.TestingConcreteView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.awt.Point;
import java.util.Arrays;
import java.util.List;

/**
 * Class containing unit-tests for the *View* class.
 *
 * @author Martin K. Herkules (makr1906) & Albin Eliasson (alel2104)
 */
public class ViewTests {
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

    private View view;

    /**
     * Method which is triggered before each individual test method is executed.
     */
    @BeforeEach
    public void setupEach() {
        view = Mockito.spy(new TestingConcreteView());
    }

    /**
     * Method for testing the pushSelectEvent method; by testing that the
     * pushEvent method is triggered correctly when is executed.
     */
    @Test
    public void test_View_PushSelectEvent() {
        for(int i = 0; i < singleLocations.size(); i++) {
            view.pushSelectEvent(singleLocations.get(i));
            Mockito.verify(view, Mockito.times(1)).pushEvent(View.VIEW_SELECT_SQUARE_EVENT, Arrays.asList(singleLocations.get(i)));
        }
    }

    /**
     * Method for testing the pushFlagEvent method; by testing that the
     * pushEvent method is triggered correctly when is executed.
     */
    @Test
    public void test_View_PushFlagEvent() {
        for(int i = 0; i < singleLocations.size(); i++) {
            view.pushFlagEvent(singleLocations.get(i));
            Mockito.verify(view, Mockito.times(1)).pushEvent(View.VIEW_FLAG_SQUARE_EVENT, Arrays.asList(singleLocations.get(i)));
        }
    }

    /**
     * Method for testing the pushResetGameEvent method; by testing that the
     * pushEvent method is triggered correctly when is executed.
     */
    @Test
    public void test_View_PushResetGameEvent() {
        view.pushResetGameEvent();
        Mockito.verify(view, Mockito.times(1)).pushEvent(Mockito.eq(View.VIEW_RESET_GAME_EVENT), Mockito.any());
    }
}
