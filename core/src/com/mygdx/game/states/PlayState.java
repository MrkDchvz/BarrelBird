package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.FlappyDemo;
import com.mygdx.game.sprites.Bird;
import com.mygdx.game.sprites.Coin;
import com.mygdx.game.sprites.Ground;
import com.mygdx.game.sprites.Score;
import com.mygdx.game.sprites.Tube;
import com.badlogic.gdx.math.Intersector;

import java.awt.Image;
import java.awt.Shape;


public class PlayState extends State {
//    Constants
    private static final float TUBE_GAP_X = 150;
    private static final int TUBE_COUNT = 4;
    private static final int CAM_POS = 80;
//    Sprites
    protected Bird bird;
    protected TextureRegion birdFrame;
    protected Texture background;
    protected Array<Tube> tubes;
    protected Array<Coin> coins;
    protected TextureRegionDrawable pause;
//    protected TextureRegionDrawable pausePressed;
    protected TextureRegionDrawable resume;
    protected ImageButton pauseButton;
    protected Sound pauseSound;

    protected Sound coinSound;
    protected  Sound coinSoundPlus;
    protected TextureRegion coinFrame;

    protected Ground ground1, ground2;
    protected Score score;
    protected Stage stage;
// Misc
    private float stateTime;
    private boolean isPaused;
    private InputProcessor touchDownInput;
    private InputMultiplexer inputMultiplexer;

    private ShapeRenderer sr;

    private Color overlayColor;
    private float overlayAlpha;




    public PlayState(GameStateManager gsm) {
        super(gsm);
        // ====Class Variables====
        // Bird Class
        bird = new Bird(50, 300);
        // Score class
        score = new Score();
        // Ground Class
        ground1 = new Ground(0 - 40);
        ground2 = new Ground(ground1.getGround().getWidth() - 40);
        // Tube Class
        tubes = new Array<Tube>();
        // Add 4 tubes
        for (int i = 1; i <= 4; i++) {
            tubes.add(new Tube(TUBE_GAP_X * i));
        }
        // Coin Class
        coins = new Array<Coin>();
        // Add 4 coins on the middle of the tube gaps
        for (Tube tube : tubes) {
        // Subtracted by 100 because that's the Tube gap in Y Axis
            coins.add(new Coin(tube.getPolyTopTube().getX() + (tube.getTopTube().getWidth() / 2) ,tube.getPolyTopTube().getY() - (100 / 2)));

        }

       // ====Texture Variables====
        // Background Texture
        background = new Texture("background_sample.png");
        // Pause Button Texture
        pause = new TextureRegionDrawable(new TextureRegion(new Texture("pause.png")));
        pause.setMinSize(50, 50);
        // Resume Button Texture
        resume = new TextureRegionDrawable(new TextureRegion(new Texture("resume.png")));

        // ====Sound Variables====
        // coin sound when it gets collected
        coinSound = Gdx.audio.newSound(Gdx.files.internal("coin.wav"));
        // coin sound every 10 coins collected
        coinSoundPlus = Gdx.audio.newSound(Gdx.files.internal("coinPlus.wav"));
        // Pause Sound
        pauseSound = Gdx.audio.newSound(Gdx.files.internal("pause_sound.wav"));

        // ====Button Variables====
        // Pause Bottom
        pauseButton = new ImageButton(pause);

        // ====Action Variables====
        // Sets the image of pauseButton to pause Texture
        Action pauseAction = new Action() {
            @Override
            public boolean act(float delta) {
                pauseButton.getStyle().imageUp = pause;
                return true;
            }
        };
        // Sets the image of resumeButton to resume Texture
        Action resumeAction = new Action() {
            @Override
            public boolean act(float delta) {
                pauseButton.getStyle().imageUp = resume;
                return true;
            }
        };

        //Input Elements
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                if (!isPaused) {
                    isPaused = true;
                    System.out.println("pause");
                    pauseButton.getStyle().imageUp = resume;
                } else {
                    isPaused = false;
                    System.out.println("unpause");
                    pauseButton.getStyle().imageUp = pause;
                }

                pauseSound.play();
            }
        });

        touchDownInput = new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                if (!isPaused) {
                    System.out.println("Click");
                    bird.jump();
                }
                return true;
            };
        };

        stage = new Stage();
        stage.addActor(pauseButton);



        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(pauseButton.getStage());
        inputMultiplexer.addProcessor(touchDownInput);

        overlayAlpha = 0f;
        overlayColor = new Color(0,0,0,0.5f);
        isPaused = false;
        cam.setToOrtho(false, FlappyDemo.WIDTH / 2, FlappyDemo.HEIGHT / 2);
        sr = new ShapeRenderer();

    }

    @Override
    protected void handleInput() {
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void update(float dt) {
        handleInput();
        stage.act(dt);
        if (isPaused) {
            overlayAlpha = 0.5f;
            pauseButton.setPosition((FlappyDemo.WIDTH / 2) - (pauseButton.getWidth() / 2), cam.viewportHeight + pauseButton.getHeight());
        }
        else {
            overlayAlpha = 0f;

            // Subtracted to 50 as an offset to make sure that the pause button isn't in the top-right most part
            pauseButton.setPosition((FlappyDemo.WIDTH - pauseButton.getWidth()) - 20, (FlappyDemo.HEIGHT - pauseButton.getHeight()) - 20);

            stateTime += dt;
            bird.update(dt);
            cam.position.x = bird.getPosition().x + CAM_POS;

            for (Tube tube: tubes) {
                // Reposition Tubes
                if (cam.position.x - (cam.viewportWidth / 2)  > tube.getPosTopTube().x + tube.getTopTube().getWidth()) {
                    tube.reposition(tube.getPosTopTube().x + ( TUBE_GAP_X * TUBE_COUNT));
                    coins.add(new Coin(tube.getPolyTopTube().getX() + (tube.getTopTube().getWidth() / 2) ,tube.getPolyTopTube().getY() - (100 / 2)));

                }

                //  Bird to pipe collision
                if (Intersector.overlapConvexPolygons(bird.getPolyBird(), tube.getPolyTopTube()) || Intersector.overlapConvexPolygons(bird.getPolyBird(), tube.getPolyBotTube()) ) {
                    cam.setToOrtho(false);
                    cam.update();
                    gsm.set(new MenuState(gsm));
                }
            }
            //  Coin Collision
            for (Coin coin : coins) {
                Tube recentTube = tubes.get(0);
                if (Intersector.overlapConvexPolygons(coin.getPolyCoin(), bird.getPolyBird())) {
                    score.increment();
                    if (score.getScore() % 10 == 0) {
                        coinSoundPlus.play();
                    } else {
                        coinSound.play();
                    }
                    // removes the nearest coin
                    coins.removeIndex(0);
                }
            }

            //  Reposition Ground
            if (cam.position.x - (cam.viewportWidth / 2) > ground1.getPosGround().x + ground1.getGround().getWidth()) {
                ground1.reposition(ground2.getPosGround().x + ground2.getGround().getWidth());
            }
            if (cam.position.x - (cam.viewportWidth / 2) > ground2.getPosGround().x + ground2.getGround().getWidth()) {
                ground2.reposition(ground1.getPosGround().x + ground1.getGround().getWidth());
            }

            // Bird to ground collision
            if (Intersector.overlapConvexPolygons(bird.getPolyBird(), ground1.getPolyGround()) || Intersector.overlapConvexPolygons(bird.getPolyBird(), ground2.getPolyGround())) {
                cam.setToOrtho(false);
                cam.update();;
                gsm.set(new MenuState(gsm));
            }
            cam.update();

        }
        stage.act(dt);

        // This Section is for debugging purposes don't mix actual code in this part

    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sr.setProjectionMatrix(cam.combined);
        // Bird animation
        birdFrame = bird.getAnimation().getKeyFrame(stateTime, true);

        sb.begin();
        // Background
        sb.draw(background, cam.position.x - (cam.viewportWidth / 2), 0, cam.viewportWidth , cam.viewportHeight);
        // Bird
        sb.draw(birdFrame, bird.getPosition().x, bird.getPosition().y, bird.getPolyBird().getOriginX(), bird.getPolyBird().getOriginY(), 30, 30, 1, 1, bird.getPolyBird().getRotation());
        // Tube
        for (Tube tube: tubes) {
            sb.draw(tube.getTopTube(), tube.getPolyTopTube().getX(), tube.getPolyTopTube().getY());
            sb.draw(tube.getBotTube(), tube.getPolyBotTube().getX(), tube.getPolyBotTube().getY());
        }
        // Coin
        for (Coin coin : coins) {
            coinFrame = coin.getAnimation().getKeyFrame(stateTime, true);
           //  Set the width & height same as the width & height of the polygon in Coin class
            sb.draw(coinFrame, coin.getPolyCoin().getX() , coin.getPolyCoin().getY(), 25, 25 );
        }
        // Ground
        sb.draw(ground1.getGround(),ground1.getPolyGround().getTransformedVertices()[0], ground1.getPolyGround().getTransformedVertices()[1]);
        sb.draw(ground2.getGround(),ground2.getPolyGround().getTransformedVertices()[0], ground2.getPolyGround().getTransformedVertices()[1]);
        // Score
        score.getFont().draw(sb, score.getText(), cam.position.x  - (score.getText().width / 2), cam.viewportHeight - 40);
        score.getFont().setUseIntegerPositions(false);
        sb.end();

//        For collision Vison
        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(1,0,0,1);
        sr.polygon(bird.getPolyBird().getTransformedVertices());

        sr.polygon(ground1.getPolyGround().getTransformedVertices());
        sr.polygon(ground2.getPolyGround().getTransformedVertices());


        for (Coin coin: coins) {
            sr.polygon(coin.getPolyCoin().getTransformedVertices());
        }

        for (Tube tube: tubes) {
            sr.polygon(tube.getPolyTopTube().getTransformedVertices());
            sr.polygon(tube.getPolyBotTube().getTransformedVertices());
        }


        sr.end();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        sr.begin(ShapeRenderer.ShapeType.Filled);
            if (isPaused) {
                sr.setColor(overlayColor);
                sr.rect(0, 0,  cam.position.x + (FlappyDemo.WIDTH / 2), FlappyDemo.HEIGHT);
            }
        sr.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);


    // UI Elements
        stage.draw();
    }

    @Override
    public void dispose() {
        bird.getBird().dispose();
        for (Tube tube : tubes) {
            tube.getTopTube().dispose();
            tube.getBotTube().dispose();
        }

    }
}
