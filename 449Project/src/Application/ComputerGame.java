package Application;

import javafx.scene.control.Alert;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.application.Platform;
import java.util.Random;

public class ComputerGame extends Game {
    private Random random = new Random();
    
    public ComputerGame(int boardSize, boolean isSimpleGame, Label statusLabel, GridPane gridPane,
                       Button[][] buttons, String bluePlayerChoice, String redPlayerChoice) {
        super(boardSize, isSimpleGame, statusLabel, gridPane, buttons, bluePlayerChoice, redPlayerChoice);
    }
    private char getComputerSymbol(String playerColor) {
        Random random = new Random();
        return random.nextBoolean() ? 'S' : 'O';
    }
    @Override
    public void handleCellClick(int row, int col) {
        if (gameOver) return;
      
        boolean cellEmpty = buttons == null || buttons[row][col].getText().trim().isEmpty();
        if (!cellEmpty && !testMode) {
            new Alert(Alert.AlertType.WARNING, "That spot is already taken!").showAndWait();
            return;
        }
        
        // Place move in board array
        if (currentPlayer.equals("Blue")) {
            if (buttons != null) buttons[row][col].setText(bluePlayerChoice);
            board[row][col] = bluePlayerChoice.charAt(0);
        } else {
            if (buttons != null) buttons[row][col].setText(redPlayerChoice);
            board[row][col] = redPlayerChoice.charAt(0);
        }
        
        //turnCount++;
        
        // Check SOS
        boolean sosFound = checkSOS(row, col);
        

        if (gameOver) {
            return;
        }
        

        if (sosFound) {
        		if(isSimpleGame) {
        			endGame();
            if (isBoardFull()) {
                endGame();
            }
        } else {
   
            switchPlayer();
            
            // If computer's turn, make computer move
            if (currentPlayer.equals("Red") && !redPlayer.isHuman()) {
                Platform.runLater(() -> {
                    makeComputerMove();
                });
            } else if (currentPlayer.equals("Blue") && !bluePlayer.isHuman()) {
                Platform.runLater(() -> {
                    makeComputerMove();
                });
            }
        }
        

        if (isBoardFull()) {
            endGame();
        }
        }
    }
    
    private void makeComputerMove() {
        if (gameOver) return;

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j] == ' ') {

                    char symbol = getComputerSymbol(currentPlayer);
                    buttons[i][j].setText(String.valueOf(symbol));
                    board[i][j] = symbol;

                    boolean sosFound = checkSOS(i, j);


                    if (!isSimpleGame || !sosFound) {
                        switchPlayer();
                        triggerComputerIfNeeded();
						/*
						 * if(sosFound) { endGame(); return; }
						 */
                    }


                    if (isSimpleGame) {
                    		switchPlayer();
                        triggerComputerIfNeeded();
                        if(sosFound) { endGame(); return; }
                    }

                    if (isBoardFull()) endGame();
                    return;
                }
            }
        }
    }






    public void startGameWithComputerMove() {
        if (gameOver) return;

        if ((currentPlayer.equals("Blue") && !bluePlayer.isHuman()) || 
            (currentPlayer.equals("Red") && !redPlayer.isHuman())) {
            
            Platform.runLater(() -> {
                makeComputerMove();
            });
        }
    }
    private void triggerComputerIfNeeded() {
        if (gameOver) return;

        if ((currentPlayer.equals("Blue") && !bluePlayer.isHuman()) ||
            (currentPlayer.equals("Red") && !redPlayer.isHuman())) {

            Platform.runLater(() -> {
                try { Thread.sleep(100); } catch (Exception e) {}
                makeComputerMove();
            });
        }
    }
}