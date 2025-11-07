package Application;

import javafx.geometry.Point2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;

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
    

  
    protected java.util.List<Line> lines = new java.util.ArrayList<>();

    public void clearLine() {
        Pane parentPane = (Pane) gridPane.getParent();
        parentPane.getChildren().removeAll(lines);
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
         
            if (currentPlayer.equals("Blue")) {
                buttons[row][col].setText(bluePlayerChoice);
                board[row][col] = bluePlayerChoice.charAt(0);
            } else {
                buttons[row][col].setText(redPlayerChoice);
                board[row][col] = redPlayerChoice.charAt(0);
            }

           
            turnCount++;
            checkSOS(row, col);
            if (gameOver)
                return;

          
            switchPlayer();

        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "That spot is already taken!");
            alert.showAndWait();
        }

        updateStatusLabel();
    }

    public boolean checkSOS(int row, int col) {
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

            // Check if positions are within bounds
            if (newRow1 >= 0 && newRow1 < boardSize
                    && newCol1 >= 0 && newCol1 < boardSize
                    && newRow2 >= 0 && newRow2 < boardSize
                    && newCol2 >= 0 && newCol2 < boardSize) {

                // Check if SOS pattern exists
                if (board[row][col] == 'S'
                        && board[newRow1][newCol1] == 'O'
                        && board[newRow2][newCol2] == 'S') {

                    sosFound = true;

                   
                    highlightSOS(row, col, newRow1, newCol1, newRow2, newCol2);

                  
                    if (currentPlayer.equals("Blue")) {
                        blueScore += 1;
                    } else {
                        redScore += 1;
                    }
                }
            }
        }

        return sosFound;
    }

    protected void highlightSOS(int row1, int col1, int row2, int col2, int row3, int col3) {
        Pane parentPane = (Pane) gridPane.getParent();

        if (parentPane != null) {
            Point2D firstButtonPos = buttons[row1][col1].localToScene(0, 0);
            Point2D lastButtonPos = buttons[row3][col3].localToScene(0, 0);

            double firstX = firstButtonPos.getX() + 20;
            double firstY = firstButtonPos.getY() + 20;
            double lastX = lastButtonPos.getX() + 20;
            double lastY = lastButtonPos.getY() + 20;

            Line line = new Line(firstX, firstY, lastX, lastY);
            if (currentPlayer == "Blue")
                line.setStroke(Color.BLUE);
            else
                line.setStroke(Color.RED);

            line.setStrokeWidth(3);

         
            parentPane.getChildren().add(line);
            lines.add(line); 
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

        if (!testMode) {
            String result;
            if (blueScore > redScore) {
                result = "Blue wins! Score: Blue " + blueScore + " - Red " + redScore;
            } else if (redScore > blueScore) {
                result = "Red wins! Score: Red " + redScore + " - Blue " + blueScore;
            } else {
                result = "Draw! Both players scored " + blueScore;
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Game Over!\n" + result);
            alert.showAndWait();

 
            for (int i = 0; i < boardSize; i++) {
                for (int j = 0; j < boardSize; j++) {
                    buttons[i][j].setText("");
                    board[i][j] = ' ';
                    buttons[i][j].setStyle(null);
                }
            }

            Pane parentPane = (Pane) gridPane.getParent();
            parentPane.getChildren().removeAll(lines);
        }

        lines.clear();

  
        if (!testMode) {
            blueScore = 0;
            redScore = 0;
            turnCount = 0;
            currentPlayer = "Blue";
            updateStatusLabel();
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
