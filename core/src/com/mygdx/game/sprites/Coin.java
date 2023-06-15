package com.mygdx.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

public class Coin {

    private static final float COIN_HEIGHT = 25;
    private static final float COIN_WIDTH = 25;
    private TextureRegion[] frames;
    private Polygon polyCoin;
    private Animation<TextureRegion> animation;

    private TextureRegion coinTexture;

    protected Sound coinSound;
    protected Sound coinSoundOnTens;

    private Vector2 posCoin;


    public Coin(float x, float y) {

        Texture coin1 = new Texture("coin_1.png");
        Texture coin2 = new Texture("coin_2.png");
        Texture coin3 = new Texture("coin_3.png");
        Texture coin4 = new Texture("coin_4.png");

        posCoin = new Vector2(x, y);
        coinSound = Gdx.audio.newSound(Gdx.files.internal("coin.wav"));
        coinSoundOnTens = Gdx.audio.newSound(Gdx.files.internal("coinPlus.wav"));
        polyCoin = new Polygon();
        polyCoin.setVertices(new float[] {0, 0, COIN_HEIGHT, 0, COIN_WIDTH, COIN_HEIGHT, 0, COIN_HEIGHT});
        polyCoin.setPosition(posCoin.x - (COIN_WIDTH / 2), posCoin.y - (COIN_HEIGHT / 2));

        frames = new TextureRegion[4];
        frames[0] = new TextureRegion(coin1);
        frames[1] = new TextureRegion(coin2);
        frames[2] = new TextureRegion(coin3);
        frames[3] = new TextureRegion(coin4);

        animation = new Animation<>(0.25f, frames);
    }

    // Getters

    public TextureRegion[] getFrames() {
        return frames;
    }

    public Polygon getPolyCoin() {
        return polyCoin;
    }

    public Vector2 getPosCoin() {
        return posCoin;
    }

    public Animation<TextureRegion> getAnimation() {
        return animation;
    }

    public void setCoinTexture(TextureRegion coinTexture) {
        this.coinTexture = coinTexture;
    }

    public TextureRegion getCoinTexture() {
        return coinTexture;
    }

    public Sound getCoinSound() {
        return coinSound;
    }

    public Sound getCoinSoundOnTens() {
        return coinSoundOnTens;
    }

    public void reposition(float x, float y) {
        posCoin = new Vector2(x, y);
        polyCoin.setPosition(posCoin.x, posCoin.y);
    }



}


