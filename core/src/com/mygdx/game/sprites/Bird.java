package com.mygdx.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.Rectangle;



public class Bird {
    private static final int GRAVITY = -15;
    private static final int MOVEMENT = 100;
    private static final int FRAME_COLS = 6;
    private static final int FRAME_ROWS = 1;

    private static final int BIRD_HEIGHT = 23;
    private static final int BIRD_WIDTH = 23;
    private Vector3 position;
    private Vector3 velocity;
    private Texture bird;

    private Polygon polyBird;
    private Sound jumpSound;

    private float rotation;



    private Animation<TextureRegion> animation;



    public Bird(int x, int y) {

        position = new Vector3(x,y,0);
        velocity = new Vector3(0,0,0);
        bird = new Texture("flyanimation.png");
        rotation = 0;

        jumpSound = Gdx.audio.newSound(Gdx.files.internal("jump.wav"));


//        Polygon
        polyBird = new Polygon(new float[]{0, 0, 0 + BIRD_WIDTH, 0, 0 + BIRD_WIDTH, 0 + BIRD_HEIGHT, 0, 0 + BIRD_HEIGHT});
        polyBird.setOrigin(BIRD_WIDTH / 2,
                BIRD_HEIGHT / 2);
        polyBird.setPosition(position.x, position.y);

//      Animation
        TextureRegion[][] tmp  = TextureRegion.split(bird, bird.getWidth() / FRAME_COLS, bird.getHeight() / FRAME_ROWS);
        TextureRegion[] flyingFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int j = 0; j < FRAME_COLS; j++) {
            flyingFrames[index++] = tmp[0][j];
            }


        animation = new Animation<TextureRegion>(0.25f, flyingFrames);
    }


    public Texture getBird() {
        return bird;
    }

    public Vector3 getPosition() {
        return position;
    }
    public Animation<TextureRegion> getAnimation() {
        return animation;
    }

    public Polygon getPolyBird() {
        return polyBird;
    }




    public void update(float dt) {

        velocity.add(0, GRAVITY, 0);
        velocity.scl(dt);
        position.add(MOVEMENT * dt, velocity.y, 0);
        velocity.scl(1/dt);
        if (position.y <= 0) {
            position.y = 0;
        }
        rotation = MathUtils.lerp(rotation, velocity.y * 0.3f, 0.1f);


        if (rotation >= 50) {
            rotation = 50;
        }
        if (rotation <= -90) {
            rotation = -90;
        }

        polyBird.setRotation(rotation);


        polyBird.setPosition(position.x, position.y);

    }

    public void jump() {
        jumpSound.play();
        rotation = MathUtils.lerp(rotation, 40, 0.3f);
        velocity.y = 250;

    }


}
