package com.dt042g.project.testinghelpers;

import com.dt042g.project.mvc.observer.Observer;

import java.awt.Point;
import java.util.List;

/**
 * A concrete class for the *Observer* interface.
 *
 * This class is used to test the default implementations
 * provided in the methods in the *Observed* class. It is
 * needed since the *Observer* class needs corresponding
 * *Observer* instances to function. And since the purpose
 * here is to test only the methods of *Observer* this
 * simple and clean concrete implementation is used insulate
 * the tests from any problems that could potentially exist
 * in another non-specific concrete implementation of
 * *Observer*; which could skew the results for these tests.
 *
 * @author Martin K. Herkules (makr1906) & Albin Eliasson (alel2104)
 */
public class TestingConcreteObserver implements Observer {
    /**
     * Method which is triggered by observed objects when an event occurs.
     *
     * @param eventName The name of the event.
     * @param locations An optional list of square locations relevant to the event.
     */
    @Override
    public void handleEvent(String eventName, List<Point> locations) {
        // Do nothing. This method is only implemented since it has to be due
        // to the interface defining it. This method should do nothing; since it
        // is only used to test whether it is actually triggered when an event
        // is pushed by an observed.
    }
}
