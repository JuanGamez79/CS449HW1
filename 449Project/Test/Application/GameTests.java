package Application;

import static org.junit.jupiter.api.Assertions.*;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameTest {

    private static final int BOARD_SIZE = 3;
    private Label statusLabel;
    private GridPane gridPane;
    private Button[][] buttons;

    @BeforeAll
    static void initJFX() {
        new JFXPanel();
    }

    @BeforeEach
    void setup() {
        statusLabel = new Label();
        gridPane = new GridPane();
        buttons = new Button[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                buttons[i][j] = new Button();
            }
        }
    }

    @Test
    void testSimpleGameSOSEndsGame() {
        SimpleGame game = new SimpleGame(BOARD_SIZE, true, statusLabel, gridPane, buttons, "S", "O");
        game.setTestMode(true); // Enable test mode

        // Blue places S at (0,0)
        game.handleCellClick(0, 0);
        // Red places O at (0,1)
        game.handleCellClick(0, 1);
        // Blue places S at (0,2) => SOS completed
        game.handleCellClick(0, 2);

        // Game should end automatically in SimpleGame
        assertTrue(game.gameOver, "SimpleGame should end after SOS");
        assertEquals(1, game.getBlueScore(), "Blue should have scored 1");
    }

    @Test
    void testGeneralGameSOSDoesNotEndGameImmediately() {
        GeneralGame game = new GeneralGame(BOARD_SIZE, false, statusLabel, gridPane, buttons, "S", "O");
        game.setTestMode(true); // Enable test mode

        // Blue places S at (0,0)
        game.handleCellClick(0, 0);
        // Red places O at (0,1)
        game.handleCellClick(0, 1);
        // Blue places S at (0,2) => SOS completed
        game.handleCellClick(0, 2);

        // Game should NOT end in GeneralGame
        assertFalse(game.gameOver, "GeneralGame should NOT end after a single SOS");
        assertEquals(1, game.getBlueScore(), "Blue should have scored 1");
        // Blue should play again after scoring SOS
        assertEquals("Blue", game.currentPlayer, "Blue should play again after SOS");
    }

    @Test
    void testSwitchPlayer() {
        SimpleGame game = new SimpleGame(BOARD_SIZE, true, statusLabel, gridPane, buttons, "S", "O");
        game.setTestMode(true);

        assertEquals("Blue", game.currentPlayer);
        game.switchPlayer();
        assertEquals("Red", game.currentPlayer);
        game.switchPlayer();
        assertEquals("Blue", game.currentPlayer);
    }

    @Test
    void testBoardFull() {
        SimpleGame game = new SimpleGame(BOARD_SIZE, true, statusLabel, gridPane, buttons, "S", "O");
        game.setTestMode(true);

        // Fill board
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                game.board[i][j] = 'S';
            }
        }

        assertTrue(game.isBoardFull(), "Board should be full");
    }

    @Test
    void testHandleCellClickOccupied() {
        SimpleGame game = new SimpleGame(BOARD_SIZE, true, statusLabel, gridPane, buttons, "S", "O") {
            @Override
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
                    boolean sosFound = checkSOS(row, col);

                    if (!sosFound && !gameOver) {
                        switchPlayer();
                    }

                    updateStatusLabel();
                } else {
                    return;
                }
            }
        };
        game.setTestMode(true);


        game.handleCellClick(0, 0);
        int initialTurnCount = game.turnCount;


        game.handleCellClick(0, 0);


        assertEquals(initialTurnCount, game.turnCount, "Clicking occupied cell should not increment turn");
    }
}
