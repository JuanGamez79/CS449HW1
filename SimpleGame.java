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
        boolean sosFound = super.checkSOS(row, col);

        // Immediately end game if SOS found
        if (sosFound && isSimpleGame()) {
            gameOver = true;
        }

        return sosFound;
    }

    @Override
    public void handleCellClick(int row, int col) {
        if (gameOver) return;

        boolean cellEmpty = buttons == null || buttons[row][col].getText().trim().isEmpty();
        if (!cellEmpty && !testMode) {
            new Alert(Alert.AlertType.WARNING, "That spot is already taken!").showAndWait();
            return;
        }

        char symbol = currentPlayer.equals("Blue") ? bluePlayerChoice.charAt(0) : redPlayerChoice.charAt(0);
        board[row][col] = symbol;
        if (buttons != null) buttons[row][col].setText(String.valueOf(symbol));

        logMove(row, col, currentPlayer, symbol);
        turnCount++;

        boolean sosFound = checkSOS(row, col);

        // Stop everything after first SOS in simple game
        if (sosFound && isSimpleGame()) {
            if (!testMode) endGame();
            return;
        }

        // Switch player only if game still active
        if (!gameOver) switchPlayer();
    }
}
