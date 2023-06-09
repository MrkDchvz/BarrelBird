package com.mygdx.game.inputs;

import com.badlogic.gdx.InputProcessor;
import com.mygdx.game.sprites.Bird;

public class Play implements InputProcessor {
    private Bird bird;
    private Pause pause;

    public Play(Bird bird, Pause pause) {
        super();
        this.bird = bird;
        this.pause = pause;

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
        if (bird.isIdle()) {
            bird.setIdle(false);
        }  else if (pause.isPaused() == false) {
            bird.jump();
        }
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

