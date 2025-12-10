package Application;
import javafx.scene.control.Alert;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.shape.Line;
import javafx.scene.paint.Color;
import java.util.Random;

public class ComputerGame extends Game {
    private Random random = new Random();
    
    public ComputerGame(int boardSize, boolean isSimpleGame, Label statusLabel, GridPane gridPane,
                        Button[][] buttons, String bluePlayerChoice, String redPlayerChoice) {
        super(boardSize, isSimpleGame, statusLabel, gridPane, buttons, bluePlayerChoice, redPlayerChoice);
    }
    
    // Randomly choose S or O for the computer
    private char getComputerSymbol() {
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
        // Determine symbol
        char symbol;
        if ((currentPlayer.equals("Blue") && !bluePlayer.isHuman()) ||
            (currentPlayer.equals("Red") && !redPlayer.isHuman())) {
            // AI turn
            symbol = getComputerSymbol();
        } else {
            // Human turn uses chosen symbol
            symbol = currentPlayer.equals("Blue") ? bluePlayerChoice.charAt(0)
                                                  : redPlayerChoice.charAt(0);
        }
        // Place move
        if (buttons != null) buttons[row][col].setText(String.valueOf(symbol));
        board[row][col] = symbol;
        
        // Log the move - THIS IS THE FIX
        logMove(row, col, currentPlayer, symbol);
        
        // Capture current player for highlighting
        String playerForMove = currentPlayer;
        // Check SOS using captured player
        boolean sosFound = checkSOSForPlayer(row, col, playerForMove);
        // Simple game ends immediately after SOS
        if (sosFound && isSimpleGame) {
            endGame();
            return;
        }
        // Switch player if no SOS or General game rules
        if (!sosFound || !isSimpleGame) {
            switchPlayer();
        }
        if (isBoardFull()) {
            endGame();
            return;
        }
        // Trigger next move if next player is AI
        if ((currentPlayer.equals("Blue") && !bluePlayer.isHuman()) ||
            (currentPlayer.equals("Red") && !redPlayer.isHuman())) {
            Platform.runLater(this::makeComputerMove);
        }
    }
    
    private void makeComputerMove() {
        if (gameOver) return;
        outer:
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j] == ' ') {
                    char symbol = getComputerSymbol();
                    buttons[i][j].setText(String.valueOf(symbol));
                    board[i][j] = symbol;
                    
                    // Log the computer move - THIS IS THE FIX
                    logMove(i, j, currentPlayer, symbol);
                    
                    String playerForMove = currentPlayer;
                    boolean sosFound = checkSOSForPlayer(i, j, playerForMove);
                    if (!isSimpleGame || !sosFound) {
                        switchPlayer();
                    }
                    if (isBoardFull() || (isSimpleGame && sosFound)) {
                        endGame();
                        return;
                    }
                    if ((currentPlayer.equals("Blue") && !bluePlayer.isHuman()) ||
                        (currentPlayer.equals("Red") && !redPlayer.isHuman())) {
                        Platform.runLater(this::makeComputerMove);
                    }
                    break outer;
                }
            }
        }
    }
    
    // Start game if first player is AI
    public void startGameWithComputerMove() {
        if (gameOver) return;
        if ((currentPlayer.equals("Blue") && !bluePlayer.isHuman()) ||
            (currentPlayer.equals("Red") && !redPlayer.isHuman())) {
            Platform.runLater(this::makeComputerMove);
        }
    }
    
    // Check SOS and highlight using specified player
    private boolean checkSOSForPlayer(int row, int col, String playerForMove) {
        boolean sosFound = false;
        int[][] directions = {
            { -1, -1 }, { -1, 0 }, { -1, 1 },
            {  0, -1 },           {  0, 1 },
            {  1, -1 }, {  1, 0 }, {  1, 1 }
        };
        for (int[] dir : directions) {
            int newRow1 = row + dir[0];
            int newCol1 = col + dir[1];
            int newRow2 = row + 2 * dir[0];
            int newCol2 = col + 2 * dir[1];
            if (newRow1 >= 0 && newRow1 < boardSize &&
                newCol1 >= 0 && newCol1 < boardSize &&
                newRow2 >= 0 && newRow2 < boardSize &&
                newCol2 >= 0 && newCol2 < boardSize) {
                if (board[row][col] == 'S' &&
                    board[newRow1][newCol1] == 'O' &&
                    board[newRow2][newCol2] == 'S') {
                    sosFound = true;
                    if (playerForMove.equals("Blue")) blueScore++;
                    else redScore++;
                    highlightSOSForPlayer(row, col, newRow2, newCol2, playerForMove);
                }
            }
        }
        return sosFound;
    }
    
    private void highlightSOSForPlayer(int row1, int col1, int row2, int col2, String playerForMove) {
        Pane parentPane = (Pane) gridPane.getParent();
        if (parentPane == null) return;
        Platform.runLater(() -> {
            // Force GridPane to layout its children
            gridPane.applyCss();
            gridPane.layout();
            // Get center positions of buttons relative to parent
            Point2D start = buttons[row1][col1].localToScene(
                    buttons[row1][col1].getWidth() / 2,
                    buttons[row1][col1].getHeight() / 2);
            Point2D end = buttons[row2][col2].localToScene(
                    buttons[row2][col2].getWidth() / 2,
                    buttons[row2][col2].getHeight() / 2);
            // Convert to parent coordinates
            double startX = start.getX() - parentPane.localToScene(0, 0).getX();
            double startY = start.getY() - parentPane.localToScene(0, 0).getY();
            double endX = end.getX() - parentPane.localToScene(0, 0).getX();
            double endY = end.getY() - parentPane.localToScene(0, 0).getY();
            Line line = new Line(startX, startY, endX, endY);
            line.setStroke(playerForMove.equals("Blue") ? Color.BLUE : Color.RED);
            line.setStrokeWidth(3);
            parentPane.getChildren().add(line);
            lines.add(line);
        });
    }
}