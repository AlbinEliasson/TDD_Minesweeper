package com.dt042g.project.mvc.controllers;

import com.dt042g.project.mvc.models.Model;
import com.dt042g.project.mvc.observer.Observer;
import com.dt042g.project.mvc.views.View;

import java.awt.Point;
import java.util.List;

/**
 * Class serving as the superclass of any controller.
 *
 * @author Martin K. Herkules (makr1906) & Albin Eliasson (alel2104)
 */
public abstract class Controller implements Observer {
    /**
     * Method which is triggered by observed objects when an event occurs.
     *
     * @param eventName The name of the event.
     * @param locations An optional list of square locations relevant to the event.
     */
    @Override
    public void handleEvent(String eventName, List<Point> locations) {
        switch(eventName) {
            case View.VIEW_SELECT_SQUARE_EVENT -> handleSelectSquareEvent(locations.get(0));
            case View.VIEW_FLAG_SQUARE_EVENT -> handleFlagSquareEvent(locations.get(0));
            case View.VIEW_RESET_GAME_EVENT -> handleResetGameEvent();
            case Model.MODEL_REVEAL_SQUARE_EVENT -> handleRevealSquareEvent(locations);
            case Model.MODEL_MINE_HIT_EVENT -> handleMineHitEvent(locations.get(0));
            case Model.MODEL_WIN_EVENT -> handleWinEvent();
        }
    }

    /**
     * Method used to handle "select square" events.
     *
     * @param location The square location.
     */
    abstract public void handleSelectSquareEvent(Point location);

    /**
     * Method used to handle "flag square" events.
     *
     * @param location The square location.
     */
    abstract public void handleFlagSquareEvent(Point location);

    /**
     * Method used to handle "reset" events.
     */
    abstract public void handleResetGameEvent();

    /**
     * Method used to handle "reveal square" events.
     *
     * @param locations The square locations.
     */
    abstract public void handleRevealSquareEvent(List<Point> locations);

    /**
     * Method used to handle "mine hit" (game over) events.
     *
     * @param location The mine location.
     */
    abstract public void handleMineHitEvent(Point location);

    /**
     * Method used to handle "win" events.
     */
    abstract public void handleWinEvent();
}
