package Application;

import javafx.scene.control.ButtonBar;
import javafx.scene.control.Button;
import java.util.Optional;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class GUI extends Application {
    private int turnCount = 0;
    private int boardSize;
    private String boardSizeInput;
    private GridPane gridPane;
    private Button[][] buttons;
    private Label statusLabel;
    private Stage primaryStage;
    private Button newGame;
    private RadioButton simpleGameRadio;
    private RadioButton generalGameRadio;
    private RadioButton blueS, blueO, redS, redO;
    private String bluePlayerChoice = "S"; // Default choice
    private String redPlayerChoice = "S";  // Default choice
    private Game currentGame;

    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("SOS Game Board");
        createInterface();
        primaryStage.show();
    }

    private void getBoardSize() {
        // Take User input and whether or not its valid it defaults to 3 or user input
        if (!boardSizeInput.trim().isEmpty()) {
            try {
                System.out.println(boardSizeInput);
                boardSize = Integer.parseInt(boardSizeInput);
                if (boardSize < 3 || boardSize > 10) {
                    Alert alert = new Alert(AlertType.WARNING, "Board Size must be from 3 to 10");
                    alert.showAndWait();
                    boardSize = 0;
                }
            } catch (NumberFormatException e) {
                Alert alert = new Alert(AlertType.WARNING, "Board Size must be from 3 to 10");
                alert.showAndWait();
                boardSize = 0;
            }
        } else {
            Alert alert = new Alert(AlertType.WARNING, "Board Size must be from 3 to 10");
            alert.showAndWait();
            boardSize = 0;
        }
    }

    private void createInterface() {
        // main interface layout
        gridPane = new GridPane();
        BorderPane root = new BorderPane();

        // top Layout
        HBox top = new HBox(10);
        top.setPadding(new Insets(10));
        Text SOS = new Text("SOS");
        SOS.setFont(new Font(20));
        simpleGameRadio = new RadioButton("Simple Game");
        simpleGameRadio.setFont(new Font(20));
        generalGameRadio = new RadioButton("General Game");
        generalGameRadio.setFont(new Font(20));
        Text InputBoardSize = new Text();
        TextField userInput = new TextField();
        InputBoardSize.setFont(new Font(20));

        ToggleGroup gameModeGroup = new ToggleGroup();
        simpleGameRadio.setSelected(true);
        simpleGameRadio.setToggleGroup(gameModeGroup);
        generalGameRadio.setToggleGroup(gameModeGroup);

        top.getChildren().addAll(SOS, simpleGameRadio, generalGameRadio, InputBoardSize, userInput);

        // Bottom layout
        HBox bottom = new HBox(10);
        bottom.setPadding(new Insets(10));
        statusLabel = new Label("Current Player: Blue");
        statusLabel.setFont(new Font(20));
        newGame = new Button("New Game");
        newGame.setFont(new Font(20));

        bottom.getChildren().addAll(statusLabel, newGame);

        // Left Layout
        VBox left = new VBox(10);
        left.setPadding(new Insets(10));
        Text BluePlayer = new Text("Blue Player");
        BluePlayer.setFont(new Font(20));
        blueS = new RadioButton("S");
        blueS.setFont(new Font(20));
        blueO = new RadioButton("O");
        blueO.setFont(new Font(20));

        left.getChildren().addAll(BluePlayer, blueS, blueO);

        // Right Layout
        VBox right = new VBox(10);
        right.setPadding(new Insets(10));
        Text RedPlayer = new Text("Red Player");
        RedPlayer.setFont(new Font(20));
        redS = new RadioButton("S");
        redS.setFont(new Font(20));
        redO = new RadioButton("O");
        redO.setFont(new Font(20));

        right.getChildren().addAll(RedPlayer, redS, redO);

        ToggleGroup blueSO = new ToggleGroup();
        blueS.setSelected(true);
        blueS.setToggleGroup(blueSO);
        blueO.setToggleGroup(blueSO);

        ToggleGroup redSO = new ToggleGroup();
        redS.setSelected(true);
        redS.setToggleGroup(redSO);
        redO.setToggleGroup(redSO);

        // Add event handlers for radio buttons to set player choices
        blueS.setOnAction(event -> {
            bluePlayerChoice = "S";
            if (currentGame != null) {
                currentGame.setBluePlayerChoice(bluePlayerChoice);
            }
        });
        blueO.setOnAction(event -> {
            bluePlayerChoice = "O";
            if (currentGame != null) {
                currentGame.setBluePlayerChoice(bluePlayerChoice);
            }
        });
        redS.setOnAction(event -> {
            redPlayerChoice = "S";
            if (currentGame != null) {
                currentGame.setRedPlayerChoice(redPlayerChoice);
            }
        });
        redO.setOnAction(event -> {
            redPlayerChoice = "O";
            if (currentGame != null) {
                currentGame.setRedPlayerChoice(redPlayerChoice);
            }
        });

        newGame.setOnAction(event -> {
            if (currentGame != null) {
                currentGame.clearLine();
            }

            turnCount = 0;
            boardSizeInput = userInput.getText();
            getBoardSize(); // Handle board size input

            if (boardSize == 0) {
                return; 
            }

  
            gridPane.getChildren().clear();

            // Create new board 
            createBoard();
            root.setCenter(gridPane); 

            boolean isSimpleGame = simpleGameRadio.isSelected();
            boolean isGeneralGame = generalGameRadio.isSelected();

            // ALWAYS create a fresh Game (or SimpleGame) instance so it references
            // the current buttons/gridPane/boardSize
            if (isSimpleGame) {
                currentGame = new SimpleGame(boardSize, true, statusLabel, gridPane, buttons, bluePlayerChoice,
                                             redPlayerChoice);
            } else if (isGeneralGame) {
                currentGame = new GeneralGame(boardSize, false, statusLabel, gridPane, buttons, bluePlayerChoice,
                                              redPlayerChoice);
            }

            root.setCenter(gridPane);
        });

        // Sets position of elements
        right.setAlignment(Pos.CENTER_RIGHT);
        bottom.setAlignment(Pos.BOTTOM_CENTER);
        top.setAlignment(Pos.TOP_CENTER);
        left.setAlignment(Pos.CENTER_LEFT);
        root.setLeft(left);
        root.setRight(right);
        root.setBottom(bottom);
        root.setTop(top);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
    }

    // ask user for board Size
    private void createBoard() {
        gridPane.getChildren().clear();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(5);
        gridPane.setVgap(5);

        buttons = new Button[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                buttons[i][j] = new Button("");
                final int row = i;
                final int col = j;
                buttons[i][j].setPrefSize(50, 50);
                buttons[i][j].setOnAction(event -> {
                    handleClick(row, col);
                });
                gridPane.add(buttons[i][j], j, i);
                gridPane.setAlignment(Pos.CENTER);
            }
        }
    }

    private void handleClick(int row, int col) {
        System.out.println("Clicked button (" + row + "," + col + ")");
        if (currentGame != null) {
            currentGame.handleCellClick(row, col);
        }
    }
}
