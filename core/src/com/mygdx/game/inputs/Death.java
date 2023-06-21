package com.mygdx.game.inputs;

import com.badlogic.gdx.audio.Sound;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mygdx.game.states.GameStateManager;

public class Death {
    protected Table table;
    protected TextureRegion retryTexture;
    protected TextureRegionDrawable retryTextureDrawable;
    protected ImageButton retryButton;
    protected Sound clickSound;


    public Death () {


        table = new Table();




    }


}


