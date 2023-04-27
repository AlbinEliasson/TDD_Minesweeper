package com.dt042g.project.mvc.controllers;

import com.dt042g.project.mvc.models.Model;
import com.dt042g.project.mvc.views.View;

import java.awt.Point;
import java.util.List;

/**
 * The game controller component containing a concrete implementation of a Controller.
 *
 * @author Martin K. Herkules (makr1906) & Albin Eliasson (alel2104)
 */
public class GameController extends Controller {
    private final Model _model;
    private final View _view;

    /**
     * Constructor to initialize the Model and View superclasses and adding this component as an
     * observer according to the observer pattern.
     * @param model the model superclass
     * @param view the view superclass
     */
    public GameController(final Model model, final View view) {
        _model = model;
        _view = view;

        _model.attachObserver(this);
        _view.attachObserver(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleSelectSquareEvent(final Point location) {
        _model.selectSquare(location);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleFlagSquareEvent(final Point location) {
        _model.setSquareFlag(location, !_model.isFlagged(location));

        if (_model.isFlagged(location)) {
            _view.setFlagged(location);

        } else {
            _view.setHidden(location);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleResetGameEvent() {
        _model.reset();
        _view.reset();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleRevealSquareEvent(final List<Point> locations) {
        for (Point location : locations) {
            _view.setValue(location, _model.getSquareValue(location));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleMineHitEvent(final Point location) {
        _view.gameOver(location);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleWinEvent() {
        _view.win();
    }
}
