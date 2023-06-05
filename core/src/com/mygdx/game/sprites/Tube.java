package com.mygdx.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public class Tube {
    private static final int FLUCTUATION = 130;
    private static final int TUBE_GAP = 100;
    private static final int LOWEST_OPENING = 120;
    private Texture topTube, botTube;
    private Vector2 posTopTube, posBotTube;
    private Random rand;

    public Polygon getPolyTopTube() {
        return polyTopTube;
    }

    public Polygon getPolyBotTube() {
        return polyBotTube;
    }

    private Polygon polyTopTube, polyBotTube;





    public Tube(float x) {
        topTube = new Texture("toptube.png");
        botTube = new Texture("bottomtube.png");
        rand = new Random();

        posTopTube = new Vector2(x, rand.nextInt(FLUCTUATION) + TUBE_GAP + LOWEST_OPENING);
        posBotTube = new Vector2(x, posTopTube.y - TUBE_GAP - botTube.getHeight());

//        Polygon

        polyTopTube = new Polygon();
        polyBotTube = new Polygon();

//        polyTopTube.setVertices(new float[] {posTopTube.x, posTopTube.y, posTopTube.x + topTube.getWidth(), posTopTube.y, posTopTube.x + topTube.getWidth(), posTopTube.y + topTube.getHeight(), posTopTube.x, posTopTube.y + topTube.getHeight()});
//        polyBotTube.setVertices(new float[] {posBotTube.x, posBotTube.y, posBotTube.x + botTube.getWidth(), posBotTube.y, posBotTube.x + botTube.getWidth(), posBotTube.y + botTube.getHeight(), posBotTube.x, posBotTube.y + botTube.getHeight()});

        polyTopTube.setVertices(new float[] {0, 0, 0 + topTube.getWidth(), 0, 0 + topTube.getWidth(), 0 + topTube.getHeight(), 0, 0 + topTube.getHeight()});
        polyBotTube.setVertices(new float[] {0, 0, 0 + botTube.getWidth(), 0, 0 + botTube.getWidth(), 0 + botTube.getHeight(), 0, 0+ botTube.getHeight()});

        polyTopTube.setPosition(posTopTube.x, posTopTube.y);
        polyBotTube.setPosition(posBotTube.x, posBotTube.y);

    }

    public Texture getTopTube() {
        return topTube;
    }

    public Texture getBotTube() {
        return botTube;
    }

    public Vector2 getPosTopTube() {
        return posTopTube;
    }

    public Vector2 getPosBotTube() {
        return posBotTube;
    }




    public void reposition(float x) {
        posTopTube = new Vector2(x, rand.nextInt(FLUCTUATION) + TUBE_GAP + LOWEST_OPENING);
        posBotTube = new Vector2(x, posTopTube.y - TUBE_GAP - botTube.getHeight());



//        polyTopTube.setVertices(new float[] {posTopTube.x, posTopTube.y, posTopTube.x + topTube.getWidth(), posTopTube.y, posTopTube.x + topTube.getWidth(), posTopTube.y + topTube.getHeight(), posTopTube.x, posTopTube.y + topTube.getHeight()});
//        polyBotTube.setVertices(new float[] {posBotTube.x, posBotTube.y, posBotTube.x + botTube.getWidth(), posBotTube.y, posBotTube.x + botTube.getWidth(), posBotTube.y + botTube.getHeight(), posBotTube.x, posBotTube.y + botTube.getHeight()});
        polyTopTube.setPosition(posTopTube.x, posTopTube.y);
        polyBotTube.setPosition(posBotTube.x, posBotTube.y);


    }




}
