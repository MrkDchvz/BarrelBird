package com.mygdx.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Ground {
    private static final int Y =  -50;

    private Texture ground1;
    private Texture ground2;
    private Vector2 posGround1;
    private Vector2 posGround2;

    private float height;
    private float width;





    public Ground (float x) {
        ground1 = new Texture("sprites/ground/ground.png");
        ground2 = ground1;
        posGround1 = new Vector2(x, Y);
        posGround2 = new Vector2(x + ground1.getWidth(), posGround1.y);
//        height and width properties to have a general width and height
        width = ground1.getWidth();
        height = ground1.getHeight();


    }

    public float getHeight() {
        return height;
    }

    public float getWidth() {
        return width;
    }

    public Texture getGround1() {
        return ground1;
    }

    public Texture getGround2() {
        return ground2;
    }

    public Vector2 getPosGround1() {
        return posGround1;
    }

    public Vector2 getPosGround2() {
        return posGround2;
    }

    public void repositionGround1(float x) {
        posGround1 = new Vector2(x, Y);
    }

    public void repositionGround2(float x) {
        posGround2 = new Vector2(x, posGround1.y);
    }




}
