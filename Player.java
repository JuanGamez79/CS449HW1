package Application;

public class Player {
    private String name;
    private boolean isHuman;
    private int score;
    private String symbol;
    
    public Player(String name, boolean isHuman, int score) {
        this.name = name;
        this.isHuman = isHuman;
        this.score = score;
    }
    
    public String getName() {
        return name;
    }
    
    public boolean isHuman() {
        return isHuman;
    }
    
    public int getScore() {
        return score;
    }
    
    public void addScore(int points) {
        this.score += points;
    }
    
    public String getSymbol() {
        return symbol;
    }
    
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
    
    public void setHuman(boolean isHuman) {
        this.isHuman = isHuman;
    }
}