package com.mygdx.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class Score {
    private BitmapFont font;
   private int score;

   private GlyphLayout text;



    public BitmapFont getFont() {
        return font;
    }
    public int getScore() {
        return score;
    }

    public GlyphLayout getText() {
        return text;
    }



    public Score() {
        font = new BitmapFont(Gdx.files.internal("fonts/font.fnt"));
        score = 0;
        text = new GlyphLayout();
        text.setText(font, score + "");

    }

    public void increment() {

        score++;
        text.setText(font, score + "");
    }

    public void dispose() {
        font.dispose();
    }
}

