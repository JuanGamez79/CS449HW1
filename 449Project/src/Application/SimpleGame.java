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

        if (sosFound && isSimpleGame()) {
            super.gameOver = true;
            if (!testMode) super.endGame(); // do not reset score in test mode
        }

        return sosFound;
    }



    @Override
    public void handleCellClick(int row, int col) {
        if (gameOver) return;

        // Only check button text if not null
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

        turnCount++;

        // Check SOS
        boolean sosFound = checkSOS(row, col);

        // SimpleGame ends immediately after SOS
        if (sosFound && isSimpleGame()) {
            gameOver = true;
            if (!testMode) endGame();
            return;
        }

        // GeneralGame continues after SOS, only check board full
        if (!sosFound && isBoardFull()) {
            gameOver = true;
            if (!testMode) endGame();
            return;
        }

        // Switch player if needed
        if (!sosFound && !gameOver) switchPlayer();
    }


}
