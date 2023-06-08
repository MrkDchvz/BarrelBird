package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.FlappyDemo;
import com.mygdx.game.sprites.Bird;
import com.mygdx.game.sprites.Ground;
import com.mygdx.game.sprites.Score;
import com.mygdx.game.sprites.Tube;
import com.badlogic.gdx.math.Intersector;

import java.awt.Shape;


public class PlayState extends State {
    private static final int TUBE_GAP = 150;
    private static final int TUBE_COUNT = 4;

    private static final int CAM_POS = 80;
    protected Bird bird;
    protected Texture background;
    protected Texture ground;

    protected TextureRegion birdFrame;
    protected Array<Tube> tubes;
    protected Ground ground1, ground2;
    protected Score score;

    private float stateTime;

    private ShapeRenderer sr;





    public PlayState(GameStateManager gsm) {
        super(gsm);
        sr = new ShapeRenderer();
        bird = new Bird(0, 300);
        cam.setToOrtho(false, FlappyDemo.WIDTH / 2, FlappyDemo.HEIGHT / 2);
        background = new Texture("background_sample.png");
        score = new Score();

        tubes = new Array<Tube>();
        for (int i = 1; i <= 4; i++) {
            tubes.add(new Tube(TUBE_GAP * i));
        }

        ground1 = new Ground(0 - 40);
        ground2 = new Ground(ground1.getGround().getWidth() - 40);


    }

    @Override
    protected void handleInput() {
        if (Gdx.input.justTouched()) {
            bird.jump();
        }
    }

    @Override
    public void update(float dt) {
        score.increment();
        handleInput();
        bird.update(dt);
        cam.position.x = bird.getPosition().x + CAM_POS;
        stateTime += dt;


        for (Tube tube: tubes) {
            // Reposition Tubes
            if (cam.position.x - (cam.viewportWidth / 2)  > tube.getPosTopTube().x + tube.getTopTube().getWidth()) {
                tube.reposition(tube.getPosTopTube().x + ( TUBE_GAP * TUBE_COUNT));
            }

            //  Bird to pipe collision
            if (Intersector.overlapConvexPolygons(bird.getPolyBird(), tube.getPolyTopTube()) || Intersector.overlapConvexPolygons(bird.getPolyBird(), tube.getPolyBotTube()) ) {
                cam.setToOrtho(false);
                cam.update();;
                gsm.set(new MenuState(gsm));
            }
        }

        //      Reposition Ground
        if (cam.position.x - (cam.viewportWidth / 2) > ground1.getPosGround().x + ground1.getGround().getWidth()) {
            ground1.reposition(ground2.getPosGround().x + ground2.getGround().getWidth());
        }
        if (cam.position.x - (cam.viewportWidth / 2) > ground2.getPosGround().x + ground2.getGround().getWidth()) {
            ground2.reposition(ground1.getPosGround().x + ground1.getGround().getWidth());
        }

        System.out.println("Ground1 X: " + ground1.getPolyGround().getX());


        if (Intersector.overlapConvexPolygons(bird.getPolyBird(), ground1.getPolyGround()) || Intersector.overlapConvexPolygons(bird.getPolyBird(), ground2.getPolyGround())) {
            cam.setToOrtho(false);
            cam.update();;
            gsm.set(new MenuState(gsm));
        }


        cam.update();
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
        sb.draw(birdFrame, bird.getPolyBird().getX(), bird.getPolyBird().getY(), bird.getPolyBird().getOriginX(), bird.getPolyBird().getOriginY(), 30, 30, 1, 1, bird.getPolyBird().getRotation());

        // Tube
        for (Tube tube: tubes) {
            sb.draw(tube.getTopTube(), tube.getPolyTopTube().getX(), tube.getPolyTopTube().getY());
            sb.draw(tube.getBotTube(), tube.getPolyBotTube().getX(), tube.getPolyBotTube().getY());

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




        for (Tube tube: tubes) {
            sr.polygon(tube.getPolyTopTube().getTransformedVertices());
            sr.polygon(tube.getPolyBotTube().getTransformedVertices());
        }
        sr.end();
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
