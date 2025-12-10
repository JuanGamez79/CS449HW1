package Application;

import java.util.Random;

public class ComputerPlayer {
    private String playerName;
    private char playerSymbol;
    private Random random = new Random();
    
    public ComputerPlayer(String playerName, char playerSymbol) {
        this.playerName = playerName;
        this.playerSymbol = playerSymbol;
    }
    

    public char chooseSymbol() {
        // 50% chance to choose S, 50% chance to choose O
        return random.nextBoolean() ? 'S' : 'O';
    }
    
    public String getPlayerName() {
        return playerName;
    }
    
    public char getPlayerSymbol() {
        return playerSymbol;
    }
}