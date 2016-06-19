package com.marum.game.sprites;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.marum.game.MarumGame;


/**
 * Created by angel on 17/3/2016.
 */
public class Platform extends DynamicGameSprite{
    private static float WIDTH = 1 / 16f * 64;
    private static float HEIGHT = 1 / 16f * 12;
    private float MAX_VELOCITY;
    private MarumGame game;
    private float initialXcoord;
    private int initial;

    private int motion;
    private TextureRegion sprite;


    public Platform(MarumGame game) {
        super(WIDTH, HEIGHT, game);
        MAX_VELOCITY = 0.2f;
        this.game = game;
        initial = 0;
        velocity.x = MAX_VELOCITY / 4;
        loadSprite();
    }

    @Override
    public void update(float delta) {
        updateParent(delta);

        if (initial == 0){
            initialXcoord = position.x;
            velocity.x *= motion;   // for left, right or still
            initial = 1;
        }
        position.add(velocity);
        motionController();
    }

    private void loadSprite() {
        sprite = game.assets.getPlatform();
    }

    private void motionController() {
        if (Math.abs(initialXcoord - position.x) > 4)
            reverseVelocity();
    }

    private void reverseVelocity(){
        velocity.x *= -1;
    }

    public void setMotion(int motion){
        this.motion = motion;
    }

    public TextureRegion getSprite(){
        return sprite;
    }
}