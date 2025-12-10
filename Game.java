package Application;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Game {
    protected Player redPlayer;
    protected Player bluePlayer;
    protected char[][] board;
    protected int boardSize;
    protected boolean isSimpleGame;
    protected String currentPlayer;
    protected Label statusLabel;
    protected GridPane gridPane;
    protected Button[][] buttons;
    protected int blueScore = 0;
    protected int redScore = 0;
    protected int turnCount = 0;
    protected boolean gameOver = false;
    public String bluePlayerChoice = "S";
    public String redPlayerChoice = "S";
    protected boolean testMode = false;
    protected PrintWriter logWriter;
    protected String logFileName;
    protected java.util.List<Line> lines = new java.util.ArrayList<>();
    
    public void clearLine() {
        Pane parentPane = (Pane) gridPane.getParent();
        parentPane.getChildren().removeAll(lines);
    }
    
    public void setLogFileName(String fileName) {
        this.logFileName = fileName;
        try {
            logWriter = new PrintWriter(new FileWriter(fileName, true));
            System.out.println("Log file initialized: " + fileName); // Debug output
        } catch (IOException e) {
            System.out.println("Error creating log file: " + e.getMessage()); // Debug output
            e.printStackTrace();
        }
    }
    
 
    public void logMove(int row, int col, String player, char symbol) {
        if (logWriter != null) {
            logWriter.println(row + "," + col + "," + player + "," + symbol);
            logWriter.flush();
            System.out.println("Logged move: " + row + "," + col + "," + player + "," + symbol); // Debug output
        } else {
            System.out.println("Warning: logWriter is null - cannot log move"); // Debug output
        }
    }
    
    public void setTestMode(boolean testMode) {
        this.testMode = testMode;
    }
    
    public Game(int boardSize, boolean isSimpleGame, Label statusLabel, GridPane gridPane, Button[][] buttons,
                String bluePlayerChoice, String redPlayerChoice) {
        redPlayer = new Player("Red", true, 0);
        bluePlayer = new Player("Blue", true, 0);
        this.buttons = buttons;
        this.boardSize = boardSize;
        this.isSimpleGame = isSimpleGame;
        this.statusLabel = statusLabel;
        this.gridPane = gridPane;
        this.bluePlayerChoice = bluePlayerChoice;
        this.redPlayerChoice = redPlayerChoice;
        board = new char[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                board[i][j] = ' ';
            }
        }
        currentPlayer = "Blue";
        updateStatusLabel();
    }
    
    public void handleCellClick(int row, int col) {
        if (buttons[row][col].getText() == null || buttons[row][col].getText().trim().isEmpty()) {
            char symbol = currentPlayer.equals("Blue") ? bluePlayerChoice.charAt(0) : redPlayerChoice.charAt(0);
            if (currentPlayer.equals("Blue")) {
                buttons[row][col].setText(bluePlayerChoice);
                board[row][col] = bluePlayerChoice.charAt(0);
            } else {
                buttons[row][col].setText(redPlayerChoice);
                board[row][col] = redPlayerChoice.charAt(0);
            }
            // Log the move
            logMove(row, col, currentPlayer, symbol);
            turnCount++;
            switchPlayer();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "That spot is already taken!");
            alert.showAndWait();
        }
        checkSOS(row, col);
        if (isBoardFull()) {
            endGame();
            return;
        }
        updateStatusLabel();
    }
    
    public boolean checkSOS(int row, int col) {
        if (gameOver) return false; // Stop scoring after game over

        boolean sosFound = false;
        int[][] directions = {
            { -1, -1 }, { -1, 0 }, { -1, 1 }, // Up-left, Up, Up-right
            {  0, -1 },           {  0, 1 }, // Left, Right
            {  1, -1 }, {  1, 0 }, {  1, 1 }  // Down-left, Down, Down-right
        };

        for (int[] dir : directions) {
            int newRow1 = row + dir[0];
            int newCol1 = col + dir[1];
            int newRow2 = row + 2 * dir[0];
            int newCol2 = col + 2 * dir[1];

            if (newRow1 >= 0 && newRow1 < boardSize
                    && newCol1 >= 0 && newCol1 < boardSize
                    && newRow2 >= 0 && newRow2 < boardSize
                    && newCol2 >= 0 && newCol2 < boardSize) {

                if (board[row][col] == 'S' 
                        && board[newRow1][newCol1] == 'O' 
                        && board[newRow2][newCol2] == 'S') {

                    sosFound = true;

                    // Only increment score if game not over
                    if (!gameOver) {
                        if (currentPlayer.equals("Blue")) blueScore++;
                        else redScore++;
                        highlightSOS(row, col, newRow1, newCol1, newRow2, newCol2);
                    }
                }
            }
        }

        return sosFound;
    }

    
    protected void highlightSOS(int row1, int col1, int row2, int col2, int row3, int col3) {
        Pane parentPane = (Pane) gridPane.getParent();
        if (parentPane != null) {
            Platform.runLater(() -> {
                Point2D firstButtonPos = buttons[row1][col1].localToScene(0, 0);
                Point2D lastButtonPos = buttons[row3][col3].localToScene(0, 0);
                double firstX = firstButtonPos.getX() + buttons[row1][col1].getWidth() / 2;
                double firstY = firstButtonPos.getY() + buttons[row1][col1].getHeight() / 2;
                double lastX = lastButtonPos.getX() + buttons[row3][col3].getWidth() / 2;
                double lastY = lastButtonPos.getY() + buttons[row3][col3].getHeight() / 2;
                Line line = new Line(firstX, firstY, lastX, lastY);
                if (currentPlayer.equals("Blue"))
                    line.setStroke(Color.BLUE);
                else
                    line.setStroke(Color.RED);
                line.setStrokeWidth(3);
                parentPane.getChildren().add(line);
                lines.add(line);
            });
        }
    }
    
    protected void switchPlayer() {
        if (currentPlayer.equals("Blue")) {
            currentPlayer = "Red";
        } else {
            currentPlayer = "Blue";
        }
        updateStatusLabel();
    }
    
    protected void updateStatusLabel() {
        statusLabel.setText("Current Player: " + currentPlayer);
    }
    
    public void endGame() {
        gameOver = true;

        if (logWriter != null) {
            logWriter.close();
        }

        // Only show dialog and reset UI if not in test mode
        if (!testMode) {
            String result;
            if (blueScore > redScore) {
                result = "Blue wins! Score: Blue " + blueScore + " - Red " + redScore;
            } else if (redScore > blueScore) {
                result = "Red wins! Score: Red " + redScore + " - Blue " + blueScore;
            } else {
                result = "Draw! Both players scored " + blueScore;
            }

            // Run alert safely on FX thread
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Game Over!\n" + result);
                alert.showAndWait();

                // Reset buttons and board
                if (buttons != null) {
                    for (int i = 0; i < boardSize; i++) {
                        for (int j = 0; j < boardSize; j++) {
                            buttons[i][j].setText("");
                            board[i][j] = ' ';
                            buttons[i][j].setStyle(null);
                        }
                    }
                }

                // Remove SOS highlight lines
                if (gridPane != null && gridPane.getParent() != null) {
                    ((Pane) gridPane.getParent()).getChildren().removeAll(lines);
                }
                lines.clear();

                // Reset game state
                blueScore = 0;
                redScore = 0;
                turnCount = 0;
                currentPlayer = "Blue";
                updateStatusLabel();
            });
        } else {
            // In test mode, just reset scores/state without showing alert
            lines.clear();
            blueScore = 0;
            redScore = 0;
            turnCount = 0;
            currentPlayer = "Blue";
        }
    }

    
    public void replayGame(String logFileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(logFileName))) {
            // Reset board, buttons, and state
            for (int i = 0; i < boardSize; i++) {
                for (int j = 0; j < boardSize; j++) {
                    board[i][j] = ' ';
                    if (buttons != null && i < buttons.length && j < buttons[0].length) {
                        buttons[i][j].setText("");
                    }
                }
            }
            blueScore = 0;
            redScore = 0;
            turnCount = 0;
            currentPlayer = "Blue";
            gameOver = false;
            updateStatusLabel();
            clearLine();

            // Read all moves from log
            java.util.List<String[]> moveList = new java.util.ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) moveList.add(parts);
            }

            // Timeline to replay moves
            javafx.animation.Timeline timeline = new javafx.animation.Timeline();
            for (int i = 0; i < moveList.size(); i++) {
                final String[] parts = moveList.get(i);

                javafx.animation.KeyFrame keyFrame = new javafx.animation.KeyFrame(
                    javafx.util.Duration.seconds(i * 0.5),
                    event -> {
                        if (gameOver) return;

                        int row = Integer.parseInt(parts[0]);
                        int col = Integer.parseInt(parts[1]);
                        String player = parts[2];
                        char symbol = parts[3].charAt(0);

                        board[row][col] = symbol;
                        if (buttons != null) buttons[row][col].setText(String.valueOf(symbol));

                        currentPlayer = player;

                        boolean sosFound = checkSOS(row, col);

                        // If SimpleGame and SOS found, stop immediately
                        if (sosFound && isSimpleGame()) {
                            gameOver = true;
                            timeline.stop();
                            if (!testMode) Platform.runLater(this::endGame);
                            return;
                        }

                        // If last move in GeneralGame, end game
                        if (row == Integer.parseInt(moveList.get(moveList.size() - 1)[0])
                            && col == Integer.parseInt(moveList.get(moveList.size() - 1)[1])) {
                            gameOver = true;
                            if (!testMode) Platform.runLater(this::endGame);
                        }
                    }
                );
                timeline.getKeyFrames().add(keyFrame);
            }

            timeline.play();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    
    public boolean isSimpleGame() {
        return isSimpleGame;
    }
    
    public void setBluePlayerChoice(String choice) {
        this.bluePlayerChoice = choice;
    }
    
    public void setRedPlayerChoice(String choice) {
        this.redPlayerChoice = choice;
    }
    
    public int getBlueScore() {
        return blueScore;
    }
    
    public int getRedScore() {
        return redScore;
    }
    
    public boolean isBoardFull() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j] == ' ')
                    return false;
            }
        }
        return true;
    }
}