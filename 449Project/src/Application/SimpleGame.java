package Application;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class SimpleGame extends Game {
    public SimpleGame(int boardSize, boolean isSimpleGame, Label statusLabel, GridPane gridPane,
                      Button[][] buttons, String bluePlayerChoice, String redPlayerChoice) {
        super(boardSize, isSimpleGame, statusLabel, gridPane, buttons, bluePlayerChoice, redPlayerChoice);
    }

    @Override
    public boolean checkSOS(int row, int col) {
        boolean sosFound = super.checkSOS(row, col); // super increments score

        if (sosFound && isSimpleGame()) {
            super.gameOver = true; // mark game over
            super.endGame();
        }

        return sosFound;
    }

    @Override
    public void handleCellClick(int row, int col) {
        if (gameOver) return;
        if (buttons[row][col].getText() != null && !buttons[row][col].getText().trim().isEmpty()) return;

        if (currentPlayer.equals("Blue")) {
            buttons[row][col].setText(bluePlayerChoice);
            board[row][col] = bluePlayerChoice.charAt(0);
        } else {
            buttons[row][col].setText(redPlayerChoice);
            board[row][col] = redPlayerChoice.charAt(0);
        }
        turnCount++;
        boolean sosFound = checkSOS(row, col);

        // Only switch if game not over and no SOS
        if (!sosFound && !gameOver) switchPlayer();
    }
}
