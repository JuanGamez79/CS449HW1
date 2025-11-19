package Application;
import static org.junit.jupiter.api.Assertions.*;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ComputerGameTest {
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
    void testComputerPlayerChooseSymbol() {
        // Test ComputerPlayer symbol selection
        ComputerPlayer computerPlayer = new ComputerPlayer("Computer", 'S');
        int sCount = 0;
        int oCount = 0;
        
        // Run multiple trials to check randomness
        for (int i = 0; i < 1000; i++) {
            char symbol = computerPlayer.chooseSymbol();
            if (symbol == 'S') sCount++;
            else if (symbol == 'O') oCount++;
        }
        
        assertTrue(sCount > 0, "Computer should choose S at least once");
        assertTrue(oCount > 0, "Computer should choose O at least once");
    }
    
    @Test
    void testComputerGameHandlesSOS() {
        // Test that computer game properly handles SOS detection
        ComputerGame game = new ComputerGame(BOARD_SIZE, false, statusLabel, gridPane, buttons, "S", "O");
        game.setTestMode(true);
        
        // Simulate human player placing S at (0,0)
        game.handleCellClick(0, 0);
        
        // Simulate computer placing O at (0,1) 
        game.handleCellClick(0, 1);
        
        // Simulate human player placing S at (0,2) - creates SOS
        game.handleCellClick(0, 2);
        
        // Check that SOS was detected
        assertTrue(game.checkSOS(0, 0), "SOS should be detected");
        assertEquals(1, game.getBlueScore(), "Blue should score 1 point for SOS");
    }
    
    @Test
    void testComputerGameEndsWhenBoardFull() {
        // Test that computer game ends when board is full
        ComputerGame game = new ComputerGame(BOARD_SIZE, false, statusLabel, gridPane, buttons, "S", "O");
        game.setTestMode(true);
        
        // Fill the board with moves
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                game.handleCellClick(i, j);
            }
        }
        
        assertTrue(game.isBoardFull(), "Board should be full");
        assertTrue(game.gameOver, "Game should end when board is full");
    }
    
    @Test
    void testComputerGameSwitchesPlayers() {
        // Test that computer game switches players correctly
        ComputerGame game = new ComputerGame(BOARD_SIZE, false, statusLabel, gridPane, buttons, "S", "O");
        game.setTestMode(true);
        
        assertEquals("Blue", game.currentPlayer, "Game should start with Blue player");
        
        // Simulate human move
        game.handleCellClick(0, 0);
        
        // Check that player switched to Red (computer)
        assertEquals("Red", game.currentPlayer, "Player should switch to Red after human move");
    }
    
    @Test
    void testComputerGameHandlesInvalidMove() {
        // Test that computer game handles invalid moves correctly
        ComputerGame game = new ComputerGame(BOARD_SIZE, false, statusLabel, gridPane, buttons, "S", "O");
        game.setTestMode(true);
        
        // Place a move at (0,0)
        game.handleCellClick(0, 0);
        
        // Try to place another move at the same position
        int initialTurnCount = game.turnCount;
        game.handleCellClick(0, 0);
        
        // Turn count should not increase
        assertEquals(initialTurnCount, game.turnCount, "Turn count should not increase for invalid move");
    }
    
    @Test
    void testComputerGameSimpleModeEndsAfterSOS() {
        // Test that simple mode ends after SOS is detected
        ComputerGame game = new ComputerGame(BOARD_SIZE, true, statusLabel, gridPane, buttons, "S", "O");
        game.setTestMode(true);
        
        // Place moves to form SOS pattern
        game.handleCellClick(0, 0); // Blue S
        game.handleCellClick(0, 1); // Red O  
        game.handleCellClick(0, 2); // Blue S
        
        // Check that game ended after SOS
        assertTrue(game.gameOver, "Simple game should end after SOS");
    }
    
    @Test
    void testComputerGamePlayerChoice() {
        // Test that computer game respects player choices
        ComputerGame game = new ComputerGame(BOARD_SIZE, false, statusLabel, gridPane, buttons, "S", "O");
        game.setTestMode(true);
        
        // Check initial choices
        assertEquals("S", game.bluePlayerChoice, "Blue player choice should be S");
        assertEquals("O", game.redPlayerChoice, "Red player choice should be O");
        
        // Change choices
        game.setBluePlayerChoice("O");
        game.setRedPlayerChoice("S");
        
        assertEquals("O", game.bluePlayerChoice, "Blue player choice should be updated to O");
        assertEquals("S", game.redPlayerChoice, "Red player choice should be updated to S");
    }
}