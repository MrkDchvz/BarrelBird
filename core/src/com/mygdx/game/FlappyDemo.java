package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.states.GameStateManager;
import com.mygdx.game.states.MenuState;
import com.mygdx.game.states.PlayState;

public class FlappyDemo extends ApplicationAdapter {
	public static final int WIDTH = 480;

	public static final int HEIGHT = 700;


	public static final String TITLE = "Barrel Bird";
	private SpriteBatch batch;
	private GameStateManager gsm;







	@Override
	public void create () {
		batch = new SpriteBatch();
		gsm = new GameStateManager();
		gsm.push(new PlayState(gsm));
	}


	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 0);
		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.render(batch);

	}

	@Override
	public void resize(int width, int height) {
		gsm.resize(width, height);
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
