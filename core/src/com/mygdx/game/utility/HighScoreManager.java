package com.mygdx.game.utility;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class HighScoreManager {
    private static final String HIGHSCORE_KEY = "highscore";
    Preferences pref;

    // private constructor to ensure there is only one instance of this class
    public HighScoreManager() {
        pref = Gdx.app.getPreferences("com.mygdx.game.utility.HighScoreManager");

    }

    public Preferences getPreference() {
        return this.pref;
    }

    public int getHighscore() {
        return pref.getInteger(HIGHSCORE_KEY, 0);
    }

    public void setHighscore(int score) {
        pref.putInteger(HIGHSCORE_KEY, score);
        pref.flush();
    }

    public Boolean isHighscore(int score) {
        if (score >= this.getHighscore()) {
            return true;
        }
        return false;
    }
}
