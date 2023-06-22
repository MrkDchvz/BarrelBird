package com.mygdx.game.utility;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class TextureLoop {


    private Texture texture1;
    private Texture texture2;
    private Vector2 posTexture1;
    private Vector2 posTexture2;

    private float height;
    private float width;




// Single Texture Constructor
    public TextureLoop (Texture texture1, float x, float y) {
        this.texture1 = texture1;
        texture2 = this.texture1;
        posTexture1 = new Vector2(x, y);
        posTexture2 = new Vector2(x + texture1.getWidth(), posTexture1.y);
    }

    public TextureLoop (Texture texture1, Texture texture2, float x, float y) {
        this.texture1 = texture1;
        this.texture2 = texture2;
        posTexture1 = new Vector2(x, y);
        posTexture2 = new Vector2(x + texture1.getWidth(), posTexture1.y);
    }


    public Texture getTexture1() {
        return texture1;
    }

    public Texture getTexture2() {
        return texture2;
    }

    public Vector2 getPosTexture1() {
        return posTexture1;
    }

    public Vector2 getPosTexture2() {
        return posTexture2;
    }

    public void repositionTexture1( float x, float y) {
        posTexture1 = new Vector2(x, y);
    }

    public void repositionTexture2( float x, float y) {
        posTexture2 = new Vector2(x, y);
    }

    public void dispose() {
        texture1.dispose();
        texture2.dispose();
    }


}
