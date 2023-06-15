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
import com.mygdx.game.collision.Collision;
import com.mygdx.game.inputs.Pause;
import com.mygdx.game.inputs.PlayStateInputHandler;
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
    private static final float TUBE_GAP_X = 200;
    private static final int TUBE_COUNT = 4;
    private static final int CAM_POS = 80;
//    Sprites

    // Bird Class
    protected Bird bird;
    protected TextureRegion birdFrame;
    // Background Class
    protected Texture background;
    // Tube Class
    protected Array<Tube> tubes;
    //Coin Class
    protected Array<Coin> coins;
    protected TextureRegion coinFrame;
    // Ground Class
    protected Ground ground1, ground2;
    // Score Class
    protected Score score;
    // Stage for UI Elements
    protected Stage stage;
// Misc
    // StateTime for animation sprites
    private float stateTime;
    // Pause Button
    private Pause pauseButton;
    // Input Handler
    private PlayStateInputHandler inputHandler;
    // InputMultiplexers for handling and ordering of Inputs
    private InputMultiplexer inputMultiplexer;
    // Shape renderer used for debugging and dimming screen
    private ShapeRenderer sr;
   // OverlayColor sets the color when screen is paused
    private Color overlayColor;
    // Collision Handlers
    protected Collision pipeCollision,coinCollision, groundCollision ;



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
       // Pause Button
        pauseButton = new Pause(false);
       // Background Texture
        background = new Texture("background_sample.png");
       // Stage for UI Elements
        stage = new Stage();
        stage.addActor(pauseButton.getImageButton());
        // InputHandlers
        inputHandler = new PlayStateInputHandler(bird, pauseButton);
        // Input Multiplexers for handling Input
        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(pauseButton.getImageButton().getStage());
        inputMultiplexer.addProcessor(inputHandler);
        // Overlay Color used when pausing
        overlayColor = new Color();
        cam.setToOrtho(false, FlappyDemo.WIDTH / 2, FlappyDemo.HEIGHT / 2);
        // Shape Renderer
        sr = new ShapeRenderer();
        // Collision Handlers
        pipeCollision = new Collision(bird, tubes, this);
        coinCollision = new Collision(bird, coins, score);
        groundCollision = new Collision(bird, ground1, ground2, this);




    }
    @Override
    protected void handleInput() {
//        Checks if the pauseButton is in pause, if it is, disable the bird jumping mechanism.
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void update(float dt) {
        if (groundCollision.isDead() || pipeCollision.isDead()) {
            updateDeathState(dt);
        }
        else if (pauseButton.isPaused()) {
            updatePausedState();
        } else {
            updateRunningState(dt);
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sr.setProjectionMatrix(cam.combined);
        renderOnPlay(sb);
        debugMode();
    }

    @Override
    public void dispose() {
        bird.getBird().dispose();
        for (Tube tube : tubes) {
            tube.getTopTube().dispose();
            tube.getBotTube().dispose();
        }

    }

// Update Methods
    public void updatePausedState() {
        pauseButton.getImageButton().setPosition((FlappyDemo.WIDTH / 2) - (pauseButton.getImageButton().getWidth() / 2), cam.viewportHeight);
    }

    public void updateDeathState(float dt) {
        // Stops the bird from moving horizontally
        stateTime += dt;
        bird.fall(dt);
        // Removes pause button
        pauseButton.getImageButton().remove();
        // Removes inputs
        Gdx.input.setInputProcessor(null);



    }
    public void updateRunningState(float dt) {
        handleInput();
        stage.act(dt);
        stateTime += dt;
        bird.update(dt);
        cam.position.x = bird.getPosition().x + CAM_POS;
        updateGameElements();
        cam.update();
        System.out.println("Bird Position: " + bird.getPosition().y + " "  + "Bird Poly Position:" + bird.getPolyBird().getY() );
    }
    public void updateGameElements() {
        // speed up the game
        pauseButton.setPauseBtnPosition();
        // Reposition Tubes & coins
        repositionTubesAndCoins();
        repositionGround();
        // Handle object Collision

        pipeCollision.updateCollision();
        coinCollision.updateCollision();
        groundCollision.updateCollision();
        cam.update();
    }



//    Reposition Methods
    public void repositionTubesAndCoins() {
        for (Tube tube: tubes) {
            // Wasn't able to separate tube and coin due to its tight relationship (coin relies on the position of the tube)
            if (cam.position.x - (cam.viewportWidth / 2)  > tube.getPosTopTube().x + tube.getTopTube().getWidth()) {
                tube.reposition(tube.getPosTopTube().x + ( TUBE_GAP_X * TUBE_COUNT));
                System.out.println(coins.size);
                if (coins.size >= 4) {
                    coins.removeIndex(0);
                }
                coins.add(new Coin(tube.getPolyTopTube().getX() + (tube.getTopTube().getWidth() / 2) ,tube.getPolyTopTube().getY() - (100 / 2)));
            }
        }
    }
    public void repositionGround() {
        if (cam.position.x - (cam.viewportWidth / 2) > ground1.getPosGround().x + ground1.getGround().getWidth()) {
            ground1.reposition(ground2.getPosGround().x + ground2.getGround().getWidth());
        }
        if (cam.position.x - (cam.viewportWidth / 2) > ground2.getPosGround().x + ground2.getGround().getWidth()) {
            ground2.reposition(ground1.getPosGround().x + ground1.getGround().getWidth());
        }
    }
//    Render Methods

    public  void renderOnPlay(SpriteBatch sb) {
        sb.begin();
        renderBackground(sb);
        renderTubes(sb);
        renderBird(sb);
        renderGround(sb);
        renderCoins(sb);
        renderScore(sb);
        sb.end();

        renderPauseOverlay();
        renderUiElements();


    }



    public void renderBackground(SpriteBatch sb) {
        sb.draw(background, cam.position.x - (cam.viewportWidth / 2), 0, cam.viewportWidth , cam.viewportHeight);

    }
    public void renderBird(SpriteBatch sb) {
        birdFrame = bird.getAnimation().getKeyFrame(stateTime, true);
        sb.draw(birdFrame, bird.getPosition().x, bird.getPosition().y, bird.getPolyBird().getOriginX(), bird.getPolyBird().getOriginY(), 30, 30, 1, 1, bird.getPolyBird().getRotation());


    }
    public void renderTubes(SpriteBatch sb) {
        for (Tube tube: tubes) {
            sb.draw(tube.getTopTube(), tube.getPolyTopTube().getX(), tube.getPolyTopTube().getY());
            sb.draw(tube.getBotTube(), tube.getPolyBotTube().getX(), tube.getPolyBotTube().getY());
        }

    }
    public void renderCoins(SpriteBatch sb) {
        for (Coin coin : coins) {
            coinFrame = coin.getAnimation().getKeyFrame(stateTime, true);
            //  Set the width & height same as the width & height of the polygon in Coin class
            sb.draw(coinFrame, coin.getPolyCoin().getX() , coin.getPolyCoin().getY(), 25, 25 );
        }

    }

    public void renderScore(SpriteBatch sb) {
        score.getFont().draw(sb, score.getText(), cam.position.x  - (score.getText().width / 2), cam.viewportHeight - 40);
        score.getFont().setUseIntegerPositions(false);

    }

    public void renderGround(SpriteBatch sb) {
        sb.draw(ground1.getGround(), ground1.getPolyGround().getTransformedVertices()[0], ground1.getPolyGround().getTransformedVertices()[1]);
        sb.draw(ground2.getGround(), ground2.getPolyGround().getTransformedVertices()[0], ground2.getPolyGround().getTransformedVertices()[1]);

    }

    public void renderUiElements() {
        stage.draw();
    }

    public void renderPauseOverlay() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        sr.begin(ShapeRenderer.ShapeType.Filled);
        if (pauseButton.isPaused()) {
            overlayColor.set(0,0,0, 0.5f);
            sr.setColor(overlayColor);
            sr.rect(0, 0,  cam.position.x + (FlappyDemo.WIDTH / 2), FlappyDemo.HEIGHT);
        }
        sr.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }



    public void debugMode() {
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

    }

}
