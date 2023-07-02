package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.FlappyDemo;
import com.mygdx.game.background.BackgroundLayer;
import com.mygdx.game.utility.HighScoreManager;
import com.mygdx.game.utility.TextureLoop;
import com.mygdx.game.collision.Collision;
import com.mygdx.game.inputs.Death;
import com.mygdx.game.inputs.Pause;
import com.mygdx.game.inputs.Play;
import com.mygdx.game.sprites.Bird;
import com.mygdx.game.sprites.Coin;
import com.mygdx.game.sprites.Score;
import com.mygdx.game.sprites.Tube;


public class PlayState extends State {
//    Constants
    private static final float TUBE_GAP_X = 200;
    private static final int GROUND_Y =  -50;
    private static final int TUBE_COUNT = 4;
    private static final int CAM_POS = 80;
//    Sprites

    // Bird Class
    protected Bird bird;
    protected TextureRegion birdFrame;

    // Background Class
    protected Texture baseBackground;
    protected Texture sunBackground;
    protected Array<BackgroundLayer> backgrounds;
    protected TextureLoop treeBackground;
    // Tube Class
    protected Array<Tube> tubes;
    //Coin Class
    protected Array<Coin> coins;
    protected TextureRegion coinFrame;
    // Ground Class
    protected TextureLoop ground;
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
    private Play inputHandler;
    // InputMultiplexers for handling and ordering of Inputs
    private InputMultiplexer inputMultiplexer;
    // Shape renderer used for debugging and dimming screen
    private ShapeRenderer sr;
   // OverlayColor sets the color when screen is paused
    private Color overlayColor;
    // Collision Handlers
    protected Collision tubeCollision,coinCollision, groundCollision;

    // for Flash effect
    private  float flashIntensity, flashDuration;

    // Death State UI
    private Death deathUi;

   private Viewport viewport;

   private HighScoreManager highScoreManager;



    public PlayState(GameStateManager gsm) {
        super(gsm);
        // ====Class Variables====
        ground = new TextureLoop(new Texture("sprites/ground/ground.png"), 0, GROUND_Y); // Ground Class
        score = new Score(); // Score class
        tubes = new Array<Tube>(); // Tube Class
            // Add 4 tubes
            for (int i = 0; i < 4; i++) {
                if (i == 0) {
                    // Place first tube at 500 of X axis
                    tubes.add(new Tube(500));
                } else {
                    // add Previous tube x position and add a gap
                    tubes.add(new Tube(tubes.get(i - 1).getPosBotTube().x + TUBE_GAP_X));
                }

            }
        bird = new Bird(50, 200); // Bird Class
        bird.setGroundLevel(ground.getTexture1().getHeight() + ground.getPosTexture1().y); // Set ground Level
        bird.setCeilingLevel(FlappyDemo.HEIGHT - 200); // set ceiling level offset it with 200px
        coins = new Array<Coin>(); // Coin Class
            // Add 4 coins on the middle of the tube gaps
            for (Tube tube : tubes) {
            coins.add(new Coin(tube.getPolyTopTube().getX() + (tube.getTopTube().getWidth() / 2) ,tube.getPolyTopTube().getY() - (tube.getTubeGapY() / 2)));
            }
       // Pause Button
        pauseButton = new Pause(false);
       // Background Texture
        baseBackground = new Texture("backgrounds/background.png");
        sunBackground = new Texture("backgrounds/sun.png");
        backgrounds = new Array<BackgroundLayer>();
        backgrounds.add(new BackgroundLayer(new Texture("backgrounds/wind_1.png"),20f, 0));
        backgrounds.add(new BackgroundLayer(new Texture("backgrounds/wind_2.png"),15f, backgrounds.get(0).getX() + 50));
        backgrounds.add(new BackgroundLayer(new Texture("backgrounds/wind_3.png"),10f, backgrounds.get(1).getX() + 50));

        treeBackground = new TextureLoop(new Texture("backgrounds/background_3.png"), 0, ground.getTexture1().getHeight() + ground.getPosTexture1().y);



        viewport = new ExtendViewport(FlappyDemo.WIDTH / 2.0f  , FlappyDemo.HEIGHT / 2.0f , cam);
        viewport.apply();
        cam.setToOrtho(false, FlappyDemo.WIDTH / 2.0f, FlappyDemo.HEIGHT / 2.0f);
        // Stage for UI Elements
        stage = new Stage(viewport);
        stage.addActor(pauseButton.getImageButton());
        // InputHandlers
        inputHandler = new Play(bird, pauseButton);
        // Input Multiplexers for handling Input
        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(pauseButton.getImageButton().getStage());
        inputMultiplexer.addProcessor(inputHandler);
        // Overlay Color used when pausing
        overlayColor = new Color();

        // Shape Renderer
        sr = new ShapeRenderer();
        // Collision Handlers
        tubeCollision = new Collision(bird, tubes, this);
        coinCollision = new Collision(bird, coins, score);
        groundCollision = new Collision(bird, ground, this);
        // For flash effect
        flashDuration = 0.06f;
        flashIntensity = 1f;
        // Death UI
        deathUi = new Death(this);
        deathUi.getTextButton().addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                endGame();
            }
        });
        // HighScore Manager
        highScoreManager = new HighScoreManager();



    }
    @Override
    protected void handleInput() {
//        Checks if the pauseButton is in pause, if it is, disable the bird jumping mechanism.
        Gdx.input.setInputProcessor(inputMultiplexer);
    }


    @Override
    public void update(float dt) {
        if (bird.isIdle()) {
            updateIdleState(dt);
        }
        else if (bird.isDead()) {
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
        cam.update();
        sb.setProjectionMatrix(cam.combined);
        sr.setProjectionMatrix(cam.combined);
        renderOnPlay(sb);
//        debugMode();

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        viewport.apply();
    }

    @Override
    public void dispose() {
        bird.dispose();
        score.dispose();
        stage.dispose();
        deathUi.dispose();
        pauseButton.dispose();
        baseBackground.dispose();
        sunBackground.dispose();
        treeBackground.dispose();
        ground.dispose();
        for (Coin coin : coins) {
            coin.dispose();
        }
        for (Tube tube: tubes) {
            tube.dispose();
        }
        for (BackgroundLayer bg: backgrounds) {
            bg.dispose();
        }
    }

// Update Methods
    public void  updateIdleState(float dt) {
        pauseButton.getImageButton().remove();
        handleInput();
        stage.act(dt);
        stateTime += dt;
        bird.idle(dt);
        cam.position.x = cam.viewportWidth - cam.position.x;
        updateGameElements();
        repositionBackGround(dt);
        cam.update();
    }
    public void updatePausedState() {
        pauseButton.getImageButton().setPosition(cam.position.x - (pauseButton.getImageButton().getWidth() / 2)  , (cam.viewportHeight / 2) - pauseButton.getImageButton().getHeight());
    }

    public void updateDeathState(float dt) {
//        Update clickListener
       inputMultiplexer.addProcessor(deathUi);
        handleInput();
        // Stops the bird from moving horizontally
        stateTime += dt;
        // Allows the bird to fall down
        bird.fall(dt);
        // Flash Effect
        updateFlashEffect(dt);
//         Removes pause button
        pauseButton.getImageButton().remove();
        // Check for new HighScore
        if (highScoreManager.isHighscore(score.getScore())) {
            highScoreManager.setHighscore(score.getScore());
            // Assign textButton text to highscore.
            deathUi.updateHighScoreText(highScoreManager.getHighscore());
        }
        // add death ui to stage
        float x = (cam.position.x - (deathUi.getTextButton().getWidth() / 2.0f));
        float duration = 0.8f; // Duration of the animation in seconds
        float targetY = (cam.viewportHeight / 2.0f) - (deathUi.getTextButton().getHeight() / 2); // Target Y position for the button
        float initialY = 10; // Initial Y position below the screen
        deathUi.getTextButton().setPosition(x, initialY);
        deathUi.getTextButton().addAction(Actions.moveTo(x, targetY, duration));
        stage.addActor(deathUi.getTextButton());
        stage.act(dt);


        inputMultiplexer.addProcessor(deathUi.getTextButton().getStage());

//         Removes inputs
        inputMultiplexer.removeProcessor(pauseButton.getImageButton().getStage());
        inputMultiplexer.removeProcessor(inputHandler);

//         Reposition Background
        repositionBackGround(dt);
        // add death ui

    }
    public void updateRunningState(float dt) {
        stage.addActor(pauseButton.getImageButton());
        handleInput();
        stage.act(dt);
        stateTime += dt;
        bird.update(dt);
        cam.position.x = bird.getPosition().x + CAM_POS;
        updateGameElements();
        repositionBackGround(dt);
        cam.update();
    }
    public void updateGameElements() {
        //
        pauseButton.getImageButton().setPosition((cam.position.x + (cam.viewportWidth / 2)) - (pauseButton.getImageButton().getWidth() + 10)   , cam.viewportHeight - pauseButton.getImageButton().getHeight() - 10);
        // Reposition Tubes & coins
        repositionTubesAndCoins();
        repositionGround();
        repositionTreeBackground();

        // Handle object Collision

        tubeCollision.updateCollision();
        coinCollision.updateCollision();
        groundCollision.updateCollision();
        cam.update();
    }


    public void updateFlashEffect(float dt) {
        if (flashDuration > 0) {
            flashDuration -= dt;
            flashIntensity -= dt * 2;
            if (flashIntensity <= 0) {
                flashIntensity = 0;
            }
        }
    }

    public void updatePlayState() {

    }


//    Reposition Methods
    public void repositionTubesAndCoins() {
        for (Tube tube: tubes) {
            // Wasn't able to separate tube and coin due to its tight relationship (coin relies on the position of the tube)
            if (cam.position.x - (cam.viewportWidth / 2)  > tube.getPosTopTube().x + tube.getTopTube().getWidth()) {
                // Reposition tube at the end of the array of tubes
                tube.reposition(tube.getPosTopTube().x + ( TUBE_GAP_X * TUBE_COUNT));
                System.out.println(coins.size);
                if (coins.size >= 4) {
                    coins.removeIndex(0);
                }
                coins.add(new Coin(tube.getPolyTopTube().getX() + (tube.getTopTube().getWidth() / 2.0f) ,tube.getPolyTopTube().getY() - (100 / 2)));
            }
        }
    }
    public void repositionGround() {
        // Reposition the ground when ground reaches the leftmost part of the screen
        if (cam.position.x - (cam.viewportWidth / 2) > ground.getPosTexture1().x + ground.getTexture1().getWidth()) {
            // set the new X position of the ground after the next ground.
            ground.repositionTexture1(ground.getPosTexture2().x + ground.getTexture2().getWidth(), ground.getPosTexture1().y);
        }
        if (cam.position.x - (cam.viewportWidth / 2) > ground.getPosTexture2().x + ground.getTexture2().getWidth()) {
            ground.repositionTexture2(ground.getPosTexture1().x + ground.getTexture2().getWidth(), ground.getPosTexture2().y);
        }
    }

    public void repositionBackGround(float dt) {
        for (int i = 0; i < backgrounds.size; i++) {
            backgrounds.get(i).reposition(backgrounds.get(i).getX() - (backgrounds.get(i).getSpeed() * dt));
        }

        for (BackgroundLayer layer : backgrounds) {
            if (cam.position.x - (cam.viewportWidth / 2) > layer.getX() + layer.getTexture().getWidth()) {
                layer.reposition( layer.getX() + layer.getTexture().getWidth() + cam.viewportWidth);

            }
        }
    }

    public void repositionTreeBackground() {
        if (cam.position.x - (cam.viewportWidth / 2) > treeBackground.getPosTexture1().x + treeBackground.getTexture1().getWidth()) {
            treeBackground.repositionTexture1( treeBackground.getPosTexture2().x + treeBackground.getTexture2().getWidth(), treeBackground.getPosTexture1().y);
            System.out.println("BACKGROUND REPOSITION! T1");
        }
        if (cam.position.x - (cam.viewportWidth / 2) > treeBackground.getPosTexture2().x + treeBackground.getTexture2().getWidth()) {
            treeBackground.repositionTexture2(treeBackground.getPosTexture1().x + treeBackground.getTexture1().getWidth(), treeBackground.getPosTexture2().y);
            System.out.println("BACKGROUND REPOSITION! T2");
        }
    }
//    Render Methods

    public  void renderOnPlay(SpriteBatch sb) {
        sb.begin();
        // Render Game elements
        renderBackground(sb);
        renderTubes(sb);
        renderBird(sb);
        renderGround(sb);
        renderCoins(sb);
        renderScore(sb);

        sb.end();
        // Render Flash effect on bird collision
        renderFlashEffect();
        // Render the rectangle that has alpha blending to make a shade
        renderPauseOverlay();
        // Render Pause Button
        renderUiElements();


    }



    public void renderBackground(SpriteBatch sb) {
        sb.draw(baseBackground, cam.position.x - (cam.viewportWidth / 2), 0 , cam.viewportWidth, cam.viewportHeight);
        sb.draw(sunBackground, cam.position.x - (cam.viewportWidth / 2), 0);
        for(int i = 0; i < backgrounds.size; i++) {
            sb.draw(backgrounds.get(i).getTexture(), backgrounds.get(i).getX(), 0);
        }

        sb.draw(treeBackground.getTexture1(), treeBackground.getPosTexture1().x, treeBackground.getPosTexture1().y);
        sb.draw(treeBackground.getTexture2(), treeBackground.getPosTexture2().x, treeBackground.getPosTexture2().y);






    }
    public void renderBird(SpriteBatch sb) {
        if (bird.isIdle()) {
            sb.draw(bird.getBirdIdle(), bird.getPosition().x, bird.getPosition().y, 30, 30);
        }
        else {
            if (groundCollision.isColliding() || tubeCollision.isColliding()) {
                birdFrame = bird.getDeadAnimation().getKeyFrame(stateTime, true);
            } else {
                birdFrame = bird.getAliveAnimation().getKeyFrame(stateTime, true);
            }
            sb.draw(birdFrame, bird.getPosition().x, bird.getPosition().y, bird.getPolyBird().getOriginX(), bird.getPolyBird().getOriginY(), 30, 30, 1, 1, bird.getPolyBird().getRotation());
        }


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
        sb.draw(ground.getTexture1(), ground.getPosTexture1().x, ground.getPosTexture1().y);
        sb.draw(ground.getTexture2(), ground.getPosTexture2().x, ground.getPosTexture2().y);
    }

    public void renderUiElements() {
        stage.draw();
    }

    public void renderFlashEffect() {
        if (groundCollision.isColliding() || tubeCollision.isColliding()) {
            if (flashDuration > 0) {
                Gdx.gl.glEnable(GL20.GL_BLEND);
                Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
                sr.begin(ShapeRenderer.ShapeType.Filled);
                sr.setColor(flashIntensity, flashIntensity, flashIntensity, 0.6f);
                sr.rect(0, 0, cam.position.x + FlappyDemo.WIDTH, FlappyDemo.HEIGHT);
                sr.end();
                Gdx.gl.glDisable(GL20.GL_BLEND);
            }
        }

    }

    public void renderPauseOverlay() {

        if (pauseButton.isPaused()) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            sr.begin(ShapeRenderer.ShapeType.Filled);
            overlayColor.set(0,0,0, 0.5f);
            sr.setColor(overlayColor);
            sr.rect(0, 0,  cam.position.x + (FlappyDemo.WIDTH / 2), FlappyDemo.HEIGHT);
            sr.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }

    }



    public void debugMode() {
        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(1,0,0,1);
        sr.polygon(bird.getPolyBird().getTransformedVertices());


        for (Coin coin: coins) {
            sr.polygon(coin.getPolyCoin().getTransformedVertices());
        }

        for (Tube tube: tubes) {
            sr.polygon(tube.getPolyTopTube().getTransformedVertices());
            sr.polygon(tube.getPolyBotTube().getTransformedVertices());
        }

        sr.end();

    }

    public void endGame() {
        gsm.set(new PlayState(gsm));
    }

}
