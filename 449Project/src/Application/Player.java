package Application;

public class Player {
    private String name;
    private char color;
    private int score;
    private boolean isHuman;

    public Player(String name, boolean isHuman, int score) {
        this.name = name;
        this.isHuman = isHuman;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public char getSymbol() {
        return color;
    }

    public int getScore() {
        return score;
    }

    public void addScore(int points) {
        this.score += points;
    }

    public boolean isHuman() {
        return isHuman;
    }

    public void setSymbol(char symbol) {
        this.color = symbol;
    }
}
