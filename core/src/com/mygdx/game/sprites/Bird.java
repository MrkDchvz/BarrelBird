package com.mygdx.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;


public class Bird {
    private static final int GRAVITY = -15;

    private static final float SPEED_INCREMENT = 20;
    private static final float MAX_SPEED = 250;

    private static final int FRAME_COLS = 6;
    private static final int FRAME_ROWS = 1;

    private static final int BIRD_HEIGHT = 23;
    private static final int BIRD_WIDTH = 23;
    private Vector3 position;
    private Vector3 velocity;

    private  float movement;


    private Texture birdAlive;
    private Texture birdDead;

    private Texture birdIdle;

    private Polygon polyBird;
    private Sound jumpSound;

    private  Sound collisionSound;

//    Ground level is the lowest that the bird can go
    private float groundLevel;
    private float ceilingLevel;

    private float rotation;

    private boolean isIdle;

    private float timeAccumulator;

    private Boolean isDead;



    private Animation<TextureRegion> aliveAnimation;
    private Animation<TextureRegion> deadAnimation;




    public Bird(int x, int y) {

        position = new Vector3(x,y,0);
        velocity = new Vector3(0,0,0);
        birdAlive = new Texture("sprites/bird/alive_animation.png");
        birdDead = new Texture("sprites/bird/dead_animation.png");
        birdIdle = new Texture("sprites/bird/idle_animation.png");
        rotation = 0;
        movement = 100;
        groundLevel = 0;
        isIdle = true;
        isDead = false;
        timeAccumulator = 0;


        jumpSound = Gdx.audio.newSound(Gdx.files.internal("sounds/jump.wav"));
        collisionSound = Gdx.audio.newSound(Gdx.files.internal("sounds/collision.wav"));
        //        Polygon
        polyBird = new Polygon(new float[]{0, 0, 0 + BIRD_WIDTH, 0, 0 + BIRD_WIDTH, 0 + BIRD_HEIGHT, 0, 0 + BIRD_HEIGHT});
        polyBird.setOrigin(BIRD_WIDTH / 2,
                BIRD_HEIGHT / 2);
        polyBird.setPosition(position.x, position.y);

//      Animation

        // Alive Animation
        Integer aliveFrameCols = 4;
        Integer aliveFrameRows = 1;
        TextureRegion[][] aliveTmp  = TextureRegion.split(birdAlive, birdAlive.getWidth() / aliveFrameCols, birdAlive.getHeight() / aliveFrameRows);
        TextureRegion[] aliveFrames = new TextureRegion[aliveFrameCols * aliveFrameRows];
        for (int i = 0, index = 0; i < aliveFrameRows; i++) {
            for (int j = 0; j < aliveFrameCols;  j++) {
                aliveFrames[index++] = aliveTmp[i][j];
            }
            }
        aliveAnimation = new Animation<TextureRegion>(0.25f, aliveFrames);

        // Dead animation
        Integer deadFrameCols = 4;
        Integer deadFrameRows = 1;
        TextureRegion[][] deadTmp = TextureRegion.split(birdDead, birdDead.getWidth() /  deadFrameCols, birdDead.getHeight() / deadFrameRows);
        TextureRegion[] deadFrames = new TextureRegion[deadFrameCols * deadFrameRows];
        for (int i = 0, index = 0; i < deadFrameRows; i++) {
            for (int j = 0; j < deadFrameCols; j++) {
                deadFrames[index++] = deadTmp[i][j];
            }
        }
        deadAnimation = new Animation<TextureRegion>(0.25f, deadFrames);
    }


    public Texture getBirdAlive() {
        return birdAlive;
    }
    public Texture getBirdDead() {
        return birdDead;
    }

    public Vector3 getPosition() {
        return position;
    }
    public Animation<TextureRegion> getAliveAnimation() {
        return aliveAnimation;
    }
    public Animation<TextureRegion> getDeadAnimation() {
        return deadAnimation;
    }



    public Texture getBirdIdle() {
        return birdIdle;
    }

    public Polygon getPolyBird() {
        return polyBird;
    }

    public Boolean isIdle() {return isIdle; }

    public Boolean isDead() {return isDead;}

    public void killBird() {
        this.isDead = true;
    }


    public void idle(float dt) {
        float idleAmplitude = 1f; // Adjust this to control the intensity of the idle motion
        float idleFrequency = 1f; // Adjust this to control the frequency of the idle motion
        float maxidleTime = 2f;
        timeAccumulator += dt;


        // Calculate the vertical displacement based on time using a sine wave
        float idleOffset = idleAmplitude * MathUtils.sin(MathUtils.PI2 * idleFrequency * timeAccumulator);
        position.y += idleOffset;
        if (timeAccumulator >= maxidleTime) {
            timeAccumulator = 0f;
        }
        polyBird.setPosition(position.x, position.y);


    }


    public void update(float dt) {
        updateBirdPosition(dt, movement);
        updateRotation();
        updatePolyPosition();

    }

    public void fall(float dt) {
        updateBirdPosition(dt, 0);
        updateRotation();
        updatePolyPosition();
    }



    public void collision() {
        collisionSound.play();
    }

    public void jump() {
        jumpSound.play();
        rotation = MathUtils.lerp(rotation, 40, 0.3f);
        velocity.y = 250;

    }


    public  void incrementSpeed() {
        if (movement <= MAX_SPEED) {
            movement += SPEED_INCREMENT;
            System.out.println("Speed UP!");
        }

    }

    public void setGroundLevel(float groundLevel) {
        this.groundLevel = groundLevel;
    }

    public void setCeilingLevel(float ceilingLevel) {
        this.ceilingLevel = ceilingLevel;
    }

    public void setIdle(boolean isIdle) {this.isIdle = isIdle; }


    public void updateBirdPosition(float dt, float movement) {
        velocity.add(0, GRAVITY, 0);
        velocity.scl(dt);
        position.add(movement * dt, velocity.y, 0);
        velocity.scl(1/dt);
        if (position.y <= groundLevel) {
            position.y = groundLevel;
        }
        if (position.y >= ceilingLevel) {
            position.y = ceilingLevel;
        }
    }

    public void updateRotation() {
        rotation = MathUtils.lerp(rotation, velocity.y * 0.3f, 0.1f);

        if (rotation >= 50) {
            rotation = 50;
        }
        if (rotation <= -90) {
            rotation = -90;
        }
        polyBird.setRotation(rotation);
    }

    public void updatePolyPosition() {
        polyBird.setPosition(position.x, position.y);
    }

    public void dispose() {
        birdAlive.dispose();
        birdDead.dispose();
        birdIdle.dispose();
        jumpSound.dispose();
        collisionSound.dispose();
    }


}
