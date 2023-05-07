package com.dt042g.project;

import com.dt042g.project.mvc.controllers.GameController;
import com.dt042g.project.mvc.models.GameModel;
import com.dt042g.project.mvc.views.GameView;

import javax.swing.SwingUtilities;

/**
 * Class containing main entry point of the Minesweeper application.
 *
 * @author Martin K. Herkules (makr1906) & Albin Eliasson (alel2104)
 */
public class Main {
    /**
     * com.dt042g.project.Main entry point of the Minesweeper application.
     *
     * @param args command-line arguments.
     */
    public static void main(String... args) {
        System.out.println("Minesweeper!");

        final int boardSize = 20;
        SwingUtilities.invokeLater(() -> new GameController(new GameModel(boardSize), new GameView(boardSize)));
    }
}
