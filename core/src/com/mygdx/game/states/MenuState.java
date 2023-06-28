package com.mygdx.game.states;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.FlappyDemo;

import java.util.Stack;


public class MenuState extends State {
    private Texture background;
    private BitmapFont credits;

    private  BitmapFont team;
    private GlyphLayout creditsText;
    private GlyphLayout teamText;



    public MenuState(GameStateManager gsm) {

        super(gsm);
        cam.setToOrtho(false ,FlappyDemo.WIDTH, FlappyDemo.HEIGHT);
        credits = new BitmapFont(Gdx.files.internal("fonts/font.fnt"));
        team = new BitmapFont(Gdx.files.internal("fonts/font.fnt"));
        team.getData().setScale(0.5f);
        background = new Texture(Gdx.files.internal("backgrounds/background.png"));
        cam.setToOrtho(false, FlappyDemo.WIDTH / 2.0f, FlappyDemo.HEIGHT / 2.0f);
        creditsText = new GlyphLayout(credits, "Credits");
        String text = "Programmer\n Mark Dechavez\nArt Design\n Mark Dechavez\nConcept Design\n Jon Ken Vergara\nQuality Assurance\n Sebastian Candava";
        teamText = new GlyphLayout(team, text);



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
        credits.draw(sb, creditsText, cam.viewportWidth / 2 - (creditsText.width / 2), cam.viewportHeight - creditsText.height);
        team.draw(sb, teamText, cam.viewportWidth / 2 - (teamText.width / 2), cam.viewportHeight  - (cam.viewportHeight / 2));

        sb.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void dispose() {

        credits.dispose();
        team.dispose();
        background.dispose();
    }


}
