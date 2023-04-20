package com.dt042g.project.mvc.controllers;

import com.dt042g.project.mvc.models.Model;
import com.dt042g.project.mvc.views.View;
import com.dt042g.project.testinghelpers.TestingConcreteController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.awt.Point;
import java.util.Arrays;
import java.util.List;

/**
 * Class containing unit-tests for the *Controller* class.
 *
 * @author Martin K. Herkules (makr1906) & Albin Eliasson (alel2104)
 */
public class ControllerTests {
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

    private Controller controller;

    /**
     * Method which is triggered before each individual test method is executed.
     */
    @BeforeEach
    public void setupEach() {
        controller = Mockito.spy(new TestingConcreteController());
    }

    /**
     * Method for testing the handleEvent method; by testing that the helper
     * method handleSelectSquareEvent is triggered correctly when an appropriate
     * event appears.
     */
    @Test
    public void test_Controller_HandleSelectSquareEvent() {
        for(int i = 0; i < singleLocations.size(); i++) {
            controller.handleEvent(View.VIEW_SELECT_SQUARE_EVENT, Arrays.asList(singleLocations.get(i)));
            Mockito.verify(controller, Mockito.times(1)).handleSelectSquareEvent(singleLocations.get(i));
        }
    }

    /**
     * Method for testing the handleEvent method; by testing that the helper
     * method handleFlagSquareEvent is triggered correctly when an appropriate
     * event appears.
     */
    @Test
    public void test_Controller_HandleFlagSquareEvent() {
        for(int i = 0; i < singleLocations.size(); i++) {
            controller.handleEvent(View.VIEW_FLAG_SQUARE_EVENT, Arrays.asList(singleLocations.get(i)));
            Mockito.verify(controller, Mockito.times(1)).handleFlagSquareEvent(singleLocations.get(i));
        }
    }

    /**
     * Method for testing the handleEvent method; by testing that the helper
     * method handleResetGameEvent is triggered correctly when an appropriate
     * event appears.
     */
    @Test
    public void test_Controller_HandleResetGameEvent() {
        for(int i = 0; i < singleLocations.size(); i++) {
            controller.handleEvent(View.VIEW_RESET_GAME_EVENT, Arrays.asList(singleLocations.get(i)));
            Mockito.verify(controller, Mockito.times(i + 1)).handleResetGameEvent();
        }
    }

    /**
     * Method for testing the handleEvent method; by testing that the helper
     * method handleRevealSquareEvent is triggered correctly when an appropriate
     * event appears.
     */
    @Test
    public void test_Controller_HandleRevealSquareEvent() {
        for(int i = 0; i < multiLocations.size(); i++) {
            controller.handleEvent(Model.MODEL_REVEAL_SQUARE_EVENT, multiLocations.get(i));
            Mockito.verify(controller, Mockito.times(1)).handleRevealSquareEvent(multiLocations.get(i));
        }
    }

    /**
     * Method for testing the handleEvent method; by testing that the helper
     * method handleMineHitEvent is triggered correctly when an appropriate
     * event appears.
     */
    @Test
    public void test_Controller_HandleMineHitEvent() {
        for(int i = 0; i < singleLocations.size(); i++) {
            controller.handleEvent(View.VIEW_SELECT_SQUARE_EVENT, Arrays.asList(singleLocations.get(i)));
            Mockito.verify(controller, Mockito.times(1)).handleSelectSquareEvent(singleLocations.get(i));
        }
    }

    /**
     * Method for testing the handleEvent method; by testing that the helper
     * method handleWinEvent is triggered correctly when an appropriate
     * event appears.
     */
    @Test
    public void test_Controller_HandleWinEvent() {
        for(int i = 0; i < singleLocations.size(); i++) {
            controller.handleEvent(Model.MODEL_WIN_EVENT, Arrays.asList(singleLocations.get(i)));
            Mockito.verify(controller, Mockito.times(i + 1)).handleWinEvent();
        }
    }
}
