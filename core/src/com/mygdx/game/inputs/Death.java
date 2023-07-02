package com.mygdx.game.inputs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
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

public class Death implements InputProcessor {
    private TextButton textButton;
    private  BitmapFont font;

    private HighScoreManager highScoreManager;

    private InputProcessor inputProcessor;

    private PlayState playState;




    public Death (PlayState playState) {
        this.playState = playState;
        highScoreManager = new HighScoreManager();
        font = new BitmapFont(Gdx.files.internal("fonts/font.fnt"));

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        textButton = new TextButton("Tap to retry\nBest: " + highScoreManager.getHighscore(),  textButtonStyle);
    }


    public TextButton getTextButton() {
        return textButton;
    }

    public void updateHighScoreText(int highScore) {
        textButton.setText("Tap to retry\nBest: " + highScore);
    }

    public void dispose() {
        font.dispose();
    }


    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        playState.endGame();
        System.out.println("Dead");
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}


