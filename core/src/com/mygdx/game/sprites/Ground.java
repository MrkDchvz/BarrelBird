package com.mygdx.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

public class Ground {
    private static final int Y =  -20;

    private Texture ground;



    private Polygon polyGround;
    private Vector2 posGround;


    public Ground (float x) {
        ground = new Texture("ground.png");
        polyGround = new Polygon();
        posGround = new Vector2(x, Y);
        polyGround.setVertices(new float[] {0,0, 0 + ground.getWidth(), 0, 0 + ground.getWidth(), 0 + ground.getHeight(), 0, 0 + ground.getHeight()});
        polyGround.setPosition(posGround.x, posGround.y);

    }

    public Vector2 getPosGround() {
        return posGround;
    }

    public Texture getGround() {
        return ground;
    }

    public Polygon getPolyGround() {
        return polyGround;
    }

    public float[] getVertices() {
        return polyGround.getVertices();
    }

    public void reposition(float x) {
        posGround = new Vector2(x, Y);
        polyGround.setPosition(posGround.x, posGround.y);

    }




}
