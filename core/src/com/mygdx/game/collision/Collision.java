package com.mygdx.game.collision;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.sprites.Bird;
import com.mygdx.game.sprites.Coin;
import com.mygdx.game.sprites.Ground;
import com.mygdx.game.sprites.Score;
import com.mygdx.game.sprites.Tube;
import com.mygdx.game.states.GameStateManager;
import com.mygdx.game.states.MenuState;
import com.mygdx.game.states.PlayState;

public class Collision {

    private Bird bird;
    private Array<Coin> coins;
    private Array<Tube> tubes;
    private Score score;
    private Ground ground1, ground2;

    private PlayState playState;

    private Boolean isDead;

//   Coin Collision
    public Collision(Bird bird, Array<Coin> coins, Score score) {
        this.bird = bird;
        this.coins = coins;
        this.score = score;
        isDead = false;


    }
//  Tube Collision
    public Collision(Bird bird, Array<Tube> tubes, PlayState playState) {
        this.bird = bird;
        this.tubes = tubes;

        isDead = false;

    }
//  Ground Collision
    public Collision(Bird bird, Ground ground1, Ground ground2, PlayState playState) {
        this.bird = bird;
        this.playState = playState;
        this.ground1 = ground1;
        this.ground2 = ground2;

        isDead = false;


    }

    public boolean isColliding() {
        if (coins != null) {
            for (Coin coin : coins) {
                if (Intersector.overlapConvexPolygons(coin.getPolyCoin(), bird.getPolyBird())) {
                    return true;
                }
            }
        }
        if (tubes != null) {
            for (Tube tube : tubes) {
                if (Intersector.overlapConvexPolygons(bird.getPolyBird(), tube.getPolyTopTube()) || Intersector.overlapConvexPolygons(bird.getPolyBird(), tube.getPolyBotTube())) {
                    return true;
                }
            }
            if (ground1 != null && ground2 != null) {
                if (Intersector.overlapConvexPolygons(bird.getPolyBird(), ground1.getPolyGround()) || Intersector.overlapConvexPolygons(bird.getPolyBird(), ground2.getPolyGround())) {
                    return true;
                }
            }
        }
        return false;
    }
    public void updateCollision() {
        if (coins != null) {
            for (Coin coin : coins) {
                if (Intersector.overlapConvexPolygons(coin.getPolyCoin(), bird.getPolyBird())) {
                    score.increment();
                    if (score.getScore() % 10 == 0 && score.getScore() != 0) {
                        bird.incrementSpeed();
                        coin.getCoinSoundOnTens().play();
                    } else {
                        coin.getCoinSound().play();
                    }
                    // removes the nearest coin
                    coins.removeIndex(0);
                }
            }
        }
        if (tubes != null) {
            //  Bird to pipe collision
            for (Tube tube: tubes) {
                if (Intersector.overlapConvexPolygons(bird.getPolyBird(), tube.getPolyTopTube()) || Intersector.overlapConvexPolygons(bird.getPolyBird(), tube.getPolyBotTube()) ) {
                    onCollision();
                }
            }
        }
        if (ground1 != null || ground2 != null) {
            if (bird.getPosition().y <= 66.0) {
                onCollision();
            }
        }
    }

    public boolean isDead() {
       return isDead;
    }

    public void killBird() {
        isDead = true;
    }

    public  void onCollision() {
        bird.collision();
        killBird();
    }
}
