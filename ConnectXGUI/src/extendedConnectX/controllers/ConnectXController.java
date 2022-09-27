package cpsc2150.extendedConnectX.controllers;

import cpsc2150.extendedConnectX.models.*;
import cpsc2150.extendedConnectX.views.*;

/**
 * The controller class will handle communication between our View and our Model ({@link IGameBoard})
 * <p>
 * This is where you will write code
 * <p>
 * You will need to include your {@link IGameBoard} interface
 * and both of the {@link IGameBoard} implementations from Project 4
 * If your code was correct you will not need to make any changes to your {@link IGameBoard} implementation class
 *
 * @version 2.0
 */
public class ConnectXController {

    /**
     * <p>
     * The current game that is being played
     * </p>
     */
    private IGameBoard curGame;

    /**
     * <p>
     * The screen that provides our view
     * </p>
     */
    private ConnectXView screen;

    /**
     * <p>
     * Constant for the maximum number of players.
     * </p>
     */
    public static final int MAX_PLAYERS = 10;
    
    /**
     * <p>
     * The number of players for this game. Note that our player tokens are hard coded.
     * </p>
     */
    private int numPlayers;

    /**
     * <p>
     * The hard-coded player tokens. These will be used in order according to player count.
     * </p>
     */
    private char[] playerChar = {'X', 'O', 'Y', 'Z', 'W', 'A', 'B', 'C', 'D', 'E'};

    /**
     * <p>
     * The current player playing the game.
     * </p>
     */
    private int currentPlayer;

    /**
     * <p>
     * This creates a controller for running the Extended ConnectX game
     * </p>
     * 
     * @param model
     *      The board implementation
     * @param view
     *      The screen that is shown
     * 
     * @post [ the controller will respond to actions on the view using the model. ]
     */
    public ConnectXController(IGameBoard model, ConnectXView view, int np) {
        this.curGame = model;
        this.screen = view;
        numPlayers = np;
    }

    /**
     * <p>
     * This processes a button click from the view.
     * </p>
     * 
     * @param col 
     *      The column of the activated button
     * 
     * @post [ will allow the player to place a token in the column if it is not full, otherwise it will display an error
     * and allow them to pick again. Will check for a win as well. If a player wins it will allow for them to play another
     * game hitting any button ]
     */
    public void processButtonClick(int col) {
        for(int i = 0; i < curGame.getNumColumns(); i++) {
            if(curGame.checkForWin(i)) {
                newGame();
                return;
            }
        }

        if(curGame.checkTie()) {
            newGame();
            return;
        }

        screen.setMessage("It is player " + playerChar[(currentPlayer+1) % numPlayers] + "'s move. " +
                "Select a column to place your token in.");

        if(!curGame.checkIfFree(col)) {
            screen.setMessage("That column is full! Player " + playerChar[currentPlayer % numPlayers] +
                    ", please choose again.");
        }

        else if(curGame.checkIfFree(col) && !curGame.checkForWin(col) && !curGame.checkTie()) {
            //initializing here and making sure conditional in loop ensures it gets placed in the lowest row
            int row = -1;

            for(int i = 0; i < curGame.getNumRows() && row == -1; i++) {
                BoardPosition pos = new BoardPosition(i, col);
                if(curGame.whatsAtPos(pos) == ' ') {
                    row = i;
                }
            }

            curGame.placeToken(playerChar[currentPlayer % numPlayers], col);
            screen.setMarker(row, col, playerChar[currentPlayer % numPlayers]);
            currentPlayer++;

            if(curGame.checkForWin(col)) {
                currentPlayer--;
                screen.setMessage("Player " + playerChar[currentPlayer % numPlayers] + " won! To start a new game, " +
                        "click any button.");
            }

            if(curGame.checkTie()) {
                screen.setMessage("The game has tied! To start a new game, click any button.");
            }

        }

    }

    /**
     * <p>
     * This method will start a new game by returning to the setup screen and controller
     * </p>
     * 
     * @post [ a new game gets started ]
     */
    private void newGame() {
        //close the current screen
        screen.dispose();
        
        //start back at the set up menu
        SetupView screen = new SetupView();
        SetupController controller = new SetupController(screen);
        screen.registerObserver(controller);
    }
}