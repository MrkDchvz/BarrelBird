package com.mygdx.game.inputs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mygdx.game.states.GameStateManager;
import com.mygdx.game.states.PlayState;
import com.mygdx.game.utility.HighScoreManager;

public class Death {
    private TextButton textButton;
    private  BitmapFont font;

    private HighScoreManager highScoreManager;



    public Death () {
        highScoreManager = HighScoreManager.getInstance();
        font = new BitmapFont(Gdx.files.internal("fonts/font.fnt"));
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        textButton = new TextButton("Tap to retry\nBest: " + highScoreManager.getHighScore(), textButtonStyle);


    }


    public TextButton getTextButton() {
        return textButton;
    }

    public void updateHighScore (Integer score) {
        highScoreManager.updateHighScore(score);
        textButton.setText("Tap to retry\nBest: " + highScoreManager.getHighScore());
    }

    public void dispose() {
        font.dispose();
    }

}


