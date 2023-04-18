package com.dt042g.project.mvc.observer;

import java.awt.Point;
import java.util.List;

/**
 * Interface for use as the "listener" in the observer pattern.
 *
 * @author Martin K. Herkules (makr1906) & Albin Eliasson (alel2104)
 */
public interface Observer {
    /**
     * Method which is triggered by observed objects when an event occurs.
     *
     * @param eventName The name of the event.
     * @param locations An optional list of square locations relevant to the event.
     */
    void handleEvent(String eventName, List<Point> locations);
}
