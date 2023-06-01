package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.FlappyDemo;
import com.mygdx.game.sprites.Bird;
import com.mygdx.game.sprites.Tube;
import com.badlogic.gdx.math.Intersector;

import java.awt.Shape;


public class PlayState extends State {
    private static final int TUBE_GAP = 150;
    private static final int TUBE_COUNT = 4;
    protected Bird bird;
    protected Texture background;

    protected TextureRegion currentFrame;

    private float stateTime;

    private ShapeRenderer sr;






    protected Array<Tube> tubes;
    public PlayState(GameStateManager gsm) {
        super(gsm);
        sr = new ShapeRenderer();
        bird = new Bird(25, 300);
        cam.setToOrtho(false, FlappyDemo.WIDTH / 2, FlappyDemo.HEIGHT / 2);
        background = new Texture(Gdx.files.internal("background_sample.png"));
        tubes = new Array<Tube>();
        for (int i = 1; i <= 4; i++) {
            tubes.add(new Tube(TUBE_GAP * i));
        }

    }

    @Override
    protected void handleInput() {
        if (Gdx.input.justTouched()) {
            bird.jump();
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        bird.update(dt);
//        Bird.getRect().x;
        cam.position.x = bird.getPosition().x + 80;
        stateTime += dt;


        System.out.println("Bird poly Y: " + bird.getBirdPoly().getY() );
        System.out.println("Bird Pos Y: " + bird.getPosition().y);
        for (Tube tube: tubes) {
            if (cam.position.x - (cam.viewportWidth / 2)  > tube.getPosTopTube().x + tube.getTopTube().getWidth()) {
                tube.reposition(tube.getPosTopTube().x + ( TUBE_GAP * TUBE_COUNT));
            }

            if (Intersector.overlapConvexPolygons(bird.getBirdPoly(), tube.getPolyTopTube()) || Intersector.overlapConvexPolygons(bird.getBirdPoly(), tube.getPolyBotTube()) ) {
                cam.setToOrtho(false);
                cam.update();;
                gsm.set(new MenuState(gsm));
            }
        }
        cam.update();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sr.setProjectionMatrix(cam.combined);
        currentFrame = bird.getAnimation().getKeyFrame(stateTime, true);

        sb.begin();
        sb.draw(background, cam.position.x - (cam.viewportWidth / 2), 0, cam.viewportWidth , cam.viewportHeight);
        sb.draw(currentFrame, bird.getPosition().x, bird.getPosition().y, bird.getBirdPoly().getOriginX(), bird.getBirdPoly().getOriginY(), 30, 30, 1, 1, bird.getBirdPoly().getRotation());
        for (Tube tube: tubes) {
            sb.draw(tube.getTopTube(), tube.getPosTopTube().x, tube.getPosTopTube().y);
            sb.draw(tube.getBotTube(), tube.getPosBotTube().x, tube.getPosBotTube().y);

        }
        sb.end();

//  Vertices

//         Isa isahin mo  bawat vertex sabay lagay mo sa array tapos ayon ung render mo
        float x1 = bird.getBirdPoly().getX();
        float y1 = bird.getBirdPoly().getY();
        float x2 = bird.getBirdPoly().getX() + 23;
        float y2 = bird.getBirdPoly().getY();





//        For collision Vison
        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(1,0,0,1);
        sr.polygon(bird.getBirdPoly().getTransformedVertices());


        for (Tube tube: tubes) {
            sr.polygon(tube.getPolyTopTube().getVertices());
            sr.polygon(tube.getPolyBotTube().getVertices());
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
