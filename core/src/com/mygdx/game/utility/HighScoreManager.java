package com.mygdx.game.utility;

public class HighScoreManager {
    // singleton pattern
    private static HighScoreManager instance;
    private int highScore;

    // private constructor to ensure there is only one instance of this class
    private HighScoreManager() {
        highScore = 0;
    }

    public static HighScoreManager getInstance() {
        if (instance == null) {
            instance = new HighScoreManager();
        }
        return instance;
    }

    public void  updateHighScore(int score) {
        if (score > highScore) {
            highScore = score;
        }
    }

    public Integer getHighScore() {
        return this.highScore;
    }

}
