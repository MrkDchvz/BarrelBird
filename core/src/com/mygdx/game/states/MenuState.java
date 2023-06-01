package com.mygdx.game.states;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.FlappyDemo;

import java.util.Stack;


public class MenuState extends State {
    private Texture background;
    private Texture playBtn;

    public MenuState(GameStateManager gsm) {

        super(gsm);
        cam.setToOrtho(false ,FlappyDemo.WIDTH, FlappyDemo.HEIGHT);
        background = new Texture(Gdx.files.internal("bg.png"));
        playBtn = new Texture(Gdx.files.internal("playbtn.png"));

       float playBtnWidth = playBtn.getWidth();
       float playBtnHeight = playBtn.getHeight();



    }

    @Override
    public void handleInput() {
        if (Gdx.input.isTouched()) {
            gsm.set(new PlayState(gsm));
            dispose();
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        cam.update();

    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        ScreenUtils.clear(1, 0, 0, 1);
        sb.begin();
        sb.draw(background, 0, 0, FlappyDemo.WIDTH, FlappyDemo.HEIGHT);
        sb.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        playBtn.dispose();

    }


}
