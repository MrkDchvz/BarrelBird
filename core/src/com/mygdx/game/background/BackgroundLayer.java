package com.mygdx.game.background;

import com.badlogic.gdx.graphics.Texture;

public class BackgroundLayer {
    private float speed;
    private float x;
    private Texture texture;

    public BackgroundLayer(Texture texture, float speed, float x) {
        this.speed = speed;
        this.texture = texture;
        this.x = x;
    }

    public void reposition(float x) {
        this.x = x;
    }

    public float getSpeed() {
        return speed;
    }

    public float getX() {
        return x;
    }

    public Texture getTexture() {
        return texture;
    }

    public void dispose() {
        texture.dispose();
    }
}


