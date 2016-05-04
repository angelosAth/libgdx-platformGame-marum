package com.marum.game.sprites;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.marum.game.MarumGame;

/**
 * Created by angel on 28/3/2016.
 */
public class InvisibleFloor {
    private float WIDTH;
    private float HEIGHT;

    private boolean hit = false;

    private MarumGame game;

    private Vector2 position;

    private Rectangle bounds;
    private TextureRegion sprite;

    public InvisibleFloor(MarumGame game) {
        this.game = game;
        position = new Vector2();
        WIDTH = 1 / 16f * 32;
        HEIGHT = 1 / 16f * 32;
        bounds = new Rectangle(position.x, position.y, WIDTH, HEIGHT);

        loadSprite();
    }

    public void update() {
        bounds.x = position.x;
        bounds.y = position.y;
    }


    private void loadSprite() {
        sprite = game.assets.getFloor();
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

    public boolean isHit(){
        return hit;
    }

    public void setIsHit(boolean hit){
        this.hit = hit;
    }
}
