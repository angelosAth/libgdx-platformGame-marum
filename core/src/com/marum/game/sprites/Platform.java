package com.marum.game.sprites;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.marum.game.MarumGame;


/**
 * Created by angel on 17/3/2016.
 */
public class Platform {
    private float WIDTH;
    private float HEIGHT;
    private float MAX_VELOCITY;
    private MarumGame game;
    private float initialXcoord;
    private Vector2 position;
    private Vector2 velocity;
    private int initial;
    private float platformTimer;
    private final Rectangle bounds;

    private int motion;
    private TextureRegion sprite;


    public Platform(MarumGame game) {
        WIDTH = 1 / 16f * 64;
        HEIGHT = 1 / 16f * 12;
        MAX_VELOCITY = 0.2f;
        position = new Vector2();
        velocity = new Vector2();
        this.game = game;
        bounds = new Rectangle(position.x, position.y, WIDTH, HEIGHT);
        platformTimer = 0;
        initial = 0;
        velocity.x = MAX_VELOCITY / 4;
        loadSprite();
    }

    public void update(float delta) {
        platformTimer += delta;

        if (initial == 0){
            initialXcoord = position.x;
            velocity.x *= motion;   // for left, right or still
            initial = 1;
        }

        bounds.x = position.x;
        bounds.y = position.y;
        position.add(velocity);

        motionController();
    }

    private void loadSprite() {
        sprite = game.assets.getPlatform();
    }

    private void motionController() {
        if (Math.abs(initialXcoord - position.x) > 4)
            reverseVelocity();

        //if (platformTimer > 3.9f){       //other way
          //  reverseVelocity();
            //platformTimer = 0;
        //}

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

    public Vector2 getPosition(){
        return position;
    }

    public float getWIDTH(){
        return WIDTH;
    }

    public float getHEIGHT(){
        return HEIGHT;
    }

    public void setPosition(float x, float y){
        this.position.x = x;
        this.position.y = y;
    }

    public Rectangle getBounds(){
        return bounds;
    }

    public Vector2 getVelocity(){
        return velocity;
    }
}