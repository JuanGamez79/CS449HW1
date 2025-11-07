package Application;

import javafx.scene.control.Alert;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class GeneralGame extends Game {

    public GeneralGame(int boardSize, boolean isSimpleGame, Label statusLabel, GridPane gridPane,
                       Button[][] buttons, String bluePlayerChoice, String redPlayerChoice) {
        super(boardSize, isSimpleGame, statusLabel, gridPane, buttons, bluePlayerChoice, redPlayerChoice);
    }

    @Override
    public void handleCellClick(int row, int col) {
        if (gameOver)
            return;

        if (buttons[row][col].getText() != null && !buttons[row][col].getText().trim().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "That spot is already taken!").showAndWait();
            return;
        }

 
        if (currentPlayer.equals("Blue")) {
            buttons[row][col].setText(bluePlayerChoice);
            board[row][col] = bluePlayerChoice.charAt(0);
        } else {
            buttons[row][col].setText(redPlayerChoice);
            board[row][col] = redPlayerChoice.charAt(0);
        }

        // Check if SOS
        boolean sosFound = super.checkSOS(row, col);


        if (gameOver)
            return;

   
        if (!sosFound) {
            switchPlayer(); 
        }

        if (isBoardFull()) {
            endGame();
        }
    }
}
