package com.marum.game.sprites;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.marum.game.MarumGame;

/**
 * Created by angel on 1/4/2016.
 */
public class FallingPlatform {

    private MarumGame game;
    private float WIDTH;
    private float HEIGHT;
    private Vector2 position;
    private Vector2 velocity;
    private Rectangle bounds;
    private TextureRegion sprite;



    public FallingPlatform(MarumGame game) {
        this.game = game;
        WIDTH = 1 / 16f * 64;
        HEIGHT = 1 / 16f * 12;
        position = new Vector2();
        velocity = new Vector2();
        bounds = new Rectangle(position.x, position.y, WIDTH, HEIGHT);
        velocity.x = 0;

        loadSprite();
    }

    public void update() {
        bounds.x = position.x;
        bounds.y = position.y;

        position.add(velocity);
    }


    private void loadSprite() {
        sprite = game.assets.getPlatform();
    }

    public void fallingDown(){
        velocity.add(0, -0.06f);

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
}