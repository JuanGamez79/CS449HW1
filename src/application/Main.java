package application;
import java.awt.Color;

import javax.swing.JOptionPane;
import javafx.geometry.Rectangle2D;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.Scene; 
import javafx.scene.Group;
import javafx.scene.text.Text;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;






public class Main extends Application {
	 int size;
	 GridPane grid = new GridPane();
	 int turnCount=0;
	
    public void start(Stage stage) {
    	
    	
    	TextField boardSize = new TextField();
    	Alert one = new Alert(AlertType.INFORMATION);one.setTitle("error");one.setContentText("Number Must be from 3 to 10");
    	
    	
    	RadioButton bHuman = new RadioButton("Human");
    	RadioButton bS = new RadioButton("S");
    	bS.setDisable(true);
    	RadioButton bO = new RadioButton("O");
    	bO.setDisable(true);
    	RadioButton bComputer = new RadioButton("Computer");
    	
    	RadioButton rHuman = new RadioButton("Human");
    	RadioButton rS = new RadioButton("S");
    	rS.setDisable(true);
    	RadioButton rO = new RadioButton("O");
    	rO.setDisable(true);
    	RadioButton rComputer = new RadioButton("Computer");
    	
    	RadioButton simple = new RadioButton("Simple game");
    	RadioButton general = new RadioButton("general game");
        Button start = new Button("start");
        
        ToggleGroup gameMode = new ToggleGroup();
        simple.setToggleGroup(gameMode);
        general.setToggleGroup(gameMode);
        
        ToggleGroup bSettings = new ToggleGroup();
        bHuman.setToggleGroup(bSettings);
        bComputer.setToggleGroup(bSettings);
        
        ToggleGroup bSO = new ToggleGroup();
        bS.setToggleGroup(bSO);
        bO.setToggleGroup(bSO);
        
        bHuman.setOnAction(e ->{
        	bS.setDisable(false);
        	bO.setDisable(false);
        }
        );
        bComputer.setOnAction(e ->{
        	bS.setDisable(true);
        	bO.setDisable(true);
        });
        ToggleGroup rSettings = new ToggleGroup();
        rHuman.setToggleGroup(rSettings);
        rComputer.setToggleGroup(rSettings);
        
        ToggleGroup rSO = new ToggleGroup();
        rS.setToggleGroup(rSO);
        rO.setToggleGroup(rSO);
        
        rHuman.setOnAction(e ->{
        	rS.setDisable(false);
        	rO.setDisable(false);
        }
        );
        rComputer.setOnAction(e ->{
        	rS.setDisable(true);
        	rO.setDisable(true);
        });
        
       start.setOnAction(event -> {
    	   turnCount = 0;
    	   grid.getChildren().clear();
    	   String userInput = boardSize.getText().trim();
    	   
    	   if (userInput.isEmpty()) {
    		  size = 5;
    	   } 
    		   try {
    			   int parsed = Integer.valueOf(userInput);
    		   } catch(NumberFormatException e) {
    			   one.showAndWait();
    			  return;
    		   }
    		   if(Integer.valueOf(userInput)>10 || Integer.valueOf(userInput)<3) {
    			   one.showAndWait();
    		   }
    		   else
    			    size = Integer.valueOf(userInput);
    		   		

    		   		grid.setMinSize(400, 400);
    		   		grid.setAlignment(Pos.CENTER);
    	       
    		   		for(int x = 0; x < size; x++)
    		   			for(int y = 0; y < size; y++) {
    		   				Label label = new Label();
    		   				label.setPrefSize(40, 40);
    		   				label.setStyle("-fx-font-size: 24; -fx-font-weight:bold;-fx-border-color: black; -fx-border-width: 1;");
    		   				grid.add(label,x,y);
    		   				label.setOnMouseClicked(e ->{
    		   					
    		   							turnCount++;
    		   							if(turnCount % 2 == 0) {
    		   								label.setText("  O");
    		   								label.setDisable(true);}
    		   							else {
    		   								label.setText("  S");
    		   								label.setDisable(true);}
    		   								
    		   						}
    		   						);
    		   						
    	    	   }
    		   });

       
       
       
       
    	
    	// Top Of program
    	Text sos = new Text("SOS");
    	sos.setStyle("-fx-font-size: 24; -fx-font-weight:bold;");
    	Text bInput = new Text("Board Size:");
    	bInput.setStyle("-fx-font-size: 18; -fx-font-weight:bold;");
    	
    	HBox game = new HBox(50,sos,simple,general,bInput,boardSize,start);
    	game.setAlignment(Pos.TOP_CENTER);
    	
    	// blue player side
    	Text blueP = new Text(300,30,"Blue Player");
    	blueP.setStyle("-fx-font-size: 24; -fx-font-weight:bold;");
    	
    	VBox blue = new VBox(10,blueP,bHuman,bS,bO,bComputer);
    	blue.setAlignment(Pos.CENTER);
    	
    	// red Player side
    	Text redP = new Text("Red Player");
    	redP.setStyle("-fx-font-size: 24; -fx-font-weight:bold;");
    	
    	VBox red = new VBox(10,redP,rHuman,rS,rO,rComputer);
    	red.setAlignment(Pos.CENTER);
    	
    
    	

    	
    	BorderPane root = new BorderPane();
    	///root.setLeft(blueP);
    	///BorderPane.setAlignment(blueP, Pos.CENTER);
    	root.setTop(game);
    	root.setCenter(grid);
    	root.setLeft(blue);
    	root.setRight(red);
    	
    	Scene scene = new Scene(root,1000,1000);
 
    	
        stage.setTitle("SOS Game BOard");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) { launch(args); }
}
