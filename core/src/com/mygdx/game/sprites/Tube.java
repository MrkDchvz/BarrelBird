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

    private Rectangle recTopTube, recBotTube;

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

        recTopTube = new Rectangle();
        recBotTube = new Rectangle();

        recTopTube.setSize(topTube.getWidth(), topTube.getHeight());
        recBotTube.setSize(botTube.getWidth(), botTube.getHeight());
        recTopTube.setPosition(posTopTube.x, posTopTube.y);
        recBotTube.setPosition(posBotTube.x, posBotTube.y);

//        Polygon

        polyTopTube = new Polygon();
        polyBotTube = new Polygon();

        polyTopTube.setVertices(new float[] {posTopTube.x, posTopTube.y, posTopTube.x + topTube.getWidth(), posTopTube.y, posTopTube.x + topTube.getWidth(), posTopTube.y + topTube.getHeight(), posTopTube.x, posTopTube.y + topTube.getHeight()});
        polyBotTube.setVertices(new float[] {posBotTube.x, posBotTube.y, posBotTube.x + botTube.getWidth(), posBotTube.y, posBotTube.x + botTube.getWidth(), posBotTube.y + botTube.getHeight(), posBotTube.x, posBotTube.y + botTube.getHeight()});


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

    public Rectangle getRecTopTube() {
        return recTopTube;
    }

    public Rectangle getRecBotTube() {
        return recBotTube;
    }


    public void reposition(float x) {
        posTopTube = new Vector2(x, rand.nextInt(FLUCTUATION) + TUBE_GAP + LOWEST_OPENING);
        posBotTube = new Vector2(x, posTopTube.y - TUBE_GAP - botTube.getHeight());

//        recTopTube.setPosition(posTopTube.x, posTopTube.y);
//        recBotTube.setPosition(posBotTube.x, posBotTube.y);

        polyTopTube.setVertices(new float[] {posTopTube.x, posTopTube.y, posTopTube.x + topTube.getWidth(), posTopTube.y, posTopTube.x + topTube.getWidth(), posTopTube.y + topTube.getHeight(), posTopTube.x, posTopTube.y + topTube.getHeight()});
        polyBotTube.setVertices(new float[] {posBotTube.x, posBotTube.y, posBotTube.x + botTube.getWidth(), posBotTube.y, posBotTube.x + botTube.getWidth(), posBotTube.y + botTube.getHeight(), posBotTube.x, posBotTube.y + botTube.getHeight()});


    }




}
