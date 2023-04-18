package com.dt042g.project.mvc.observer;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class used as the "subject" in the observer pattern.
 *
 * @author Martin K. Herkules (makr1906) & Albin Eliasson (alel2104)
 */
public abstract class Observed {
    private final ArrayList<Observer> observers = new ArrayList<>();

    /**
     * Method for attaching an observer object to listen of object events.
     *
     * @param observer The observer to attach.
     */
    public void attachObserver(Observer observer) {
        if(!this.observers.contains(observer))
            this.observers.add(observer);
    }

    /**
     * Method for detaching an observer.
     *
     * @param observer The observer to detach.
     */
    public void detachObserver(Observer observer) {
        this.observers.remove(observer);
    }

    /**
     * Method for pushing an event to all attached observers.
     *
     * @param eventName The name of the event.
     * @param locations An optional list of square locations relevant to the event.
     */
    public void pushEvent(String eventName, List<Point> locations) {
        observers.forEach(o -> o.handleEvent(eventName, locations));
    }
}
