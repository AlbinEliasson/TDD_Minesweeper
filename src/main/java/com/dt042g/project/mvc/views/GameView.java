package com.dt042g.project.mvc.views;

import com.dt042g.project.mvc.views.gui.Square;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * The game view component containing a concrete implementation of a View.
 *
 * @author Martin K. Herkules (makr1906) & Albin Eliasson (alel2104)
 */
public class GameView extends View {
    private final int _GAME_WIDTH = 600;
    private final int _GAME_HEIGHT = 600;
    private final double _MENU_HEIGHT_PERCENTAGE = 0.1;
    private final int _boardSize;
    private boolean _boardLocked = false;
    private JFrame _frame;
    private JPanel _board;
    private JLabel _menuText;
    private JPanel _menu;
    private JButton _restartButton;
    private JButton _quitButton;
    private JPanel _gamePanel;

    /**
     * Constructor to initialize and show the GUI with the provided board size.
     * @param boardSize the board size
     */
    public GameView(final int boardSize) {
        _boardSize = boardSize;
        createAndShowGUI();
    }

    /**
     * Method to initialize and show the GUI components.
     */
    private void createAndShowGUI() {
        initializeBoard();
        initializeMenuText();
        initializeRestartButton();
        initializeQuitButton();
        initializeMenu();
        initializeGamePanel();
        initializeFrame();
    }

    /**
     * Method to initialize the frame window of the game.
     */
    private void initializeFrame() {
        _frame = new JFrame();
        _frame.add(_gamePanel);
        _frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        _frame.setResizable(false);
        _frame.setTitle("Minesweeper");
        _frame.setVisible(true);
        _frame.pack();
        _frame.setLocationRelativeTo(null);
    }

    /**
     * Method to initialize the board containing the square GUI components with mouse-listeners to call
     * the Views pushSelectEvent and pushFlagEvent methods.
     */
    private void initializeBoard() {
        _board = new JPanel();
        _board.setPreferredSize(new Dimension(_GAME_WIDTH, _GAME_HEIGHT));
        _board.setLayout(new GridLayout(_boardSize, _boardSize, 0, 0));

        for (int i = 0; i < (_boardSize * _boardSize); i++) {
            final int boardIndex = i;
            Square square = new Square();
            square.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(final MouseEvent mouseEvent) {
                    Point location = calculateSquareBoardPosition(boardIndex, _boardSize);

                    if (mouseEvent.getButton() == MouseEvent.BUTTON1) {
                        selectSquare(square, location);

                    } else if (mouseEvent.getButton() == MouseEvent.BUTTON3) {
                        flagSquare(square, location);
                    }
                }
            });
            _board.add(square);
        }
    }

    /**
     * Method for initializing the menu text of the game.
     */
    private void initializeMenuText() {
        _menuText = new JLabel();
        _menuText.setFont(new Font("Monaco", Font.BOLD, (int) (_GAME_HEIGHT * 0.03)));
        _menuText.setForeground(Color.BLACK);
        _menuText.setText("Minesweeper");
        _menuText.setHorizontalAlignment(JLabel.CENTER);
    }

    /**
     * Method for initializing the menu of the game containing the menu text, restart and quit buttons.
     */
    private void initializeMenu() {
        _menu = new JPanel();
        _menu.setBackground(new Color(164, 164, 164));
        _menu.setPreferredSize(new Dimension(_GAME_WIDTH, (int) ((double) _GAME_HEIGHT * _MENU_HEIGHT_PERCENTAGE)));
        _menu.setLayout(new GridLayout(1, 3, 60, 0));
        _menu.setBorder(BorderFactory.createBevelBorder(1, Color.GRAY, Color.WHITE));

        _menu.add(_restartButton);
        _menu.add(_menuText);
        _menu.add(_quitButton);
    }

    /**
     * Method for initializing the menu restart button.
     */
    private void initializeRestartButton() {
        _restartButton = new JButton();
        _restartButton.setText("Restart");

        addRestartButtonListener(_restartButton);
        initializeQuitResetButtons(_restartButton);
    }

    /**
     * Method for adding the restart button-listener to push a reset game event.
     * @param restartButton the restart button
     */
    private void addRestartButtonListener(final JButton restartButton) {
        restartButton.addActionListener(e -> pushResetGameEvent());
    }

    /**
     * Method for initializing the menu quit button.
     */
    private void initializeQuitButton() {
        _quitButton = new JButton();
        _quitButton.setText("Quit");

        addQuitButtonListener(_quitButton);
        initializeQuitResetButtons(_quitButton);
    }

    /**
     * Method for adding the quit button-listener to exit the game.
     * @param quitButton the quit button
     */
    private void addQuitButtonListener(final JButton quitButton) {
        quitButton.addActionListener(e -> System.exit(0));
    }

    /**
     * Common method for initializing the restart and quit button.
     * @param button quit or restart button
     */
    private void initializeQuitResetButtons(final JButton button) {
        button.setPreferredSize(new Dimension(100, 50));
        button.setEnabled(true);
        button.setFont(new Font("Monaco", Font.BOLD, (int) (_GAME_WIDTH * 0.03)));
        button.setVisible(false);
        button.setBackground(Color.LIGHT_GRAY);
        button.setBorder(BorderFactory.createBevelBorder(0));
    }

    /**
     * Method for initializing the game panel which holds both the board and the menu of the game.
     */
    private void initializeGamePanel() {
        _gamePanel = new JPanel();
        _gamePanel.setLayout(new BorderLayout());
        _gamePanel.setPreferredSize(new Dimension(
                _GAME_WIDTH, (int) ((double) _GAME_HEIGHT * (1 + _MENU_HEIGHT_PERCENTAGE))));

        _gamePanel.add(_menu, BorderLayout.NORTH);
        _gamePanel.add(_board, BorderLayout.CENTER);
    }

    /**
     * Method for calculating the point position (X and Y), of a chosen square in the board.
     * @param boardIndex the board index of the chosen square (0 is the first square, 1 is the second, etc...)
     * @param boardSize the size of the board
     * @return the point position of the square
     */
    private Point calculateSquareBoardPosition(final int boardIndex, final int boardSize) {
        return new Point(boardIndex % boardSize, boardIndex / boardSize);
    }

    /**
     * Method for checking if the board is not in locked mode (game is over or won) and a square has not yet
     * been selected (its state is hidden), to call the View push select event.
     * @param square the square selected for select event pushing
     * @param location the point location of the square in the board
     */
    private void selectSquare(final Square square, final Point location) {
        if (square != null && !_boardLocked && square.getState() == Square.State.HIDDEN) {
            pushSelectEvent(location);
        }
    }

    /**
     * Method for checking if the board is not in locked mode (game is over or won) and a square has not yet
     * been selected (its state is not value), to call the View push flag event.
     * @param square the square selected for flag event pushing
     * @param location the point location of the square in the board
     */
    private void flagSquare(final Square square, final Point location) {
        if (square != null && !_boardLocked && square.getState() != Square.State.VALUE) {
            pushFlagEvent(location);
        }
    }

    /**
     * Method for accessing a square in the board from a point location.
     * @param location the point location
     * @return the square
     */
    private Square getSquareFromPosition(final Point location) {
        if (location == null || location.x < 0 || location.x >= _boardSize || location.y < 0 || location.y >= _boardSize) {
            return null;
        }
        return (Square) _board.getComponent((_boardSize * location.y) + location.x);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setHidden(final Point location) {
        Square square = getSquareFromPosition(location);

        if (square != null) {
            square.setHidden();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFlagged(final Point location) {
        Square square = getSquareFromPosition(location);

        if (square != null) {
            square.setFlagged();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue(final Point location, final int value) {
        Square square = getSquareFromPosition(location);

        if (square != null) {
            square.setValue(value);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMine(final Point location) {
        Square square = getSquareFromPosition(location);

        if (square != null) {
            square.setMine();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void gameOver(final Point location) {
        throw new UnsupportedOperationException("Not implemented!");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void win() {
        throw new UnsupportedOperationException("Not implemented!");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reset() {
        throw new UnsupportedOperationException("Not implemented!");
    }
}
