package com.dt042g.project.testinghelpers;

import com.dt042g.project.mvc.controllers.Controller;

import java.awt.Point;
import java.util.List;

/**
 * A concrete class for the *Controller* abstract class.
 *
 * This class is used to test the default implementations
 * provided in the methods in the *Controller* class. It is
 * needed since the *Controller* class is abstract and therefore
 * cannot be instantiated. And since the purpose is to test
 * the default methods, any method implemented directly by
 * this class are irrelevant; and their outputs don-t matter
 * since they are not used, and only exist to make the concrete
 * class valid.
 */
public class TestingConcreteController extends Controller {
    /**
     * Method used to handle "select square" events.
     *
     * @param location The square location.
     */
    @Override
    public void handleSelectSquareEvent(Point location) {

    }

    /**
     * Method used to handle "flag square" events.
     *
     * @param location The square location.
     */
    @Override
    public void handleFlagSquareEvent(Point location) {

    }

    /**
     * Method used to handle "reset" events.
     */
    @Override
    public void handleResetGameEvent() {

    }

    /**
     * Method used to handle "reveal square" events.
     *
     * @param locations The square locations.
     */
    @Override
    public void handleRevealSquareEvent(List<Point> locations) {

    }

    /**
     * Method used to handle "mine hit" (game over) events.
     *
     * @param location The mine location.
     */
    @Override
    public void handleMineHitEvent(Point location) {

    }

    /**
     * Method used to handle "win" events.
     */
    @Override
    public void handleWinEvent() {

    }
}
