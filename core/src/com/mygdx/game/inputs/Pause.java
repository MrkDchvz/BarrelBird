package com.mygdx.game.inputs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mygdx.game.FlappyDemo;

public class Pause {
    protected TextureRegion pauseTexture;
    protected  TextureRegion resumeTexture;
    protected TextureRegionDrawable pause;
    protected TextureRegionDrawable resume;
    protected ImageButton imageButton;
    protected Sound pauseSound;


    protected Boolean isPaused;

    public Pause(boolean isPaused) {
        // Pause Button Texture
        pauseTexture = new TextureRegion(new Texture("ui/pause_btn.png"));
        pause = new TextureRegionDrawable(pauseTexture);
        pause.setMinSize(pauseTexture.getRegionWidth(), pauseTexture.getRegionHeight());

        // Resume Button Texture
        resumeTexture = new TextureRegion(new Texture("ui/resume_btn.png"));
        resume = new TextureRegionDrawable(resumeTexture);
        resume.setMinSize(resumeTexture.getRegionWidth() , resumeTexture.getRegionHeight());
        this.isPaused = isPaused;

        pauseSound = Gdx.audio.newSound(Gdx.files.internal("sounds/pause_sound.wav"));
        imageButton = new ImageButton(pause);
        imageButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                togglePause();
            }
        });
    }

    public TextureRegion getPauseTexture() {
        return pauseTexture;
    }

    public TextureRegion getResumeTexture() {
        return resumeTexture;
    }



    public ImageButton getImageButton() {
        return imageButton;
    }

    public Boolean isPaused() {
        return isPaused;
    }


    public void togglePause() {
            if (isPaused == false) {
            isPaused = true;
            System.out.println("pause");
            imageButton.getStyle().imageUp = resume;
            imageButton.setSize(resumeTexture.getRegionWidth() * 0.5f , resumeTexture.getRegionHeight() * 0.5f );
        } else {
            isPaused = false;
            System.out.println("unpause");
            imageButton.getStyle().imageUp = pause;
            imageButton.setSize(pauseTexture.getRegionWidth() , pauseTexture.getRegionHeight() );

        }

        pauseSound.play();
    }

    public void dispose() {
        pauseSound.dispose();
        resumeTexture.getTexture().dispose();
        pauseTexture.getTexture().dispose();
    }
}
