package com.mygdx.game.collision;


import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.sprites.Bird;
import com.mygdx.game.sprites.Coin;
import com.mygdx.game.sprites.Score;
import com.mygdx.game.sprites.Tube;

import com.mygdx.game.states.PlayState;
import com.mygdx.game.utility.TextureLoop;

public class Collision {

    private Bird bird;
    private Array<Coin> coins;
    private Array<Tube> tubes;
    private Score score;
    private TextureLoop ground;

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
    public Collision(Bird bird, TextureLoop ground, PlayState playState) {
        this.bird = bird;
        this.playState = playState;
        this.ground = ground;
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
                    return isDead;
                }
            }
            if (ground != null) {

                return isDead;
            }
        }
        return  isDead;
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
        if (ground != null) {
            if (bird.getPosition().y <= ground.getTexture1().getHeight() + ground.getPosTexture1().y) {
                onCollision();
            }

        }
    }

    public void killBird() {
        isDead = true;
    }

    public  void onCollision() {
//       play the crash sound when the bird collides.
        bird.collision();
//      set the isDead to true
        killBird();
    }
}
