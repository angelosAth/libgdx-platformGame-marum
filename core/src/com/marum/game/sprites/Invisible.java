package com.marum.game.sprites;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.marum.game.MarumGame;

/**
 * Created by angel on 27/3/2016.
 */
public class Invisible {

    private MarumGame game;

    private float WIDTH;
    private float HEIGHT;

    private boolean hit = false;

    private final Vector2 position;

    private final Rectangle bounds;
    private TextureRegion sprite;

    public Invisible(MarumGame game) {
        this.game = game;
        position = new Vector2();
        WIDTH = 1 / 16f * 64;
        HEIGHT = 1 / 16f * 64;
        bounds = new Rectangle(position.x, position.y, WIDTH, HEIGHT);

        loadSprite();
    }

    public void update() {
        bounds.x = position.x;
        bounds.y = position.y;
    }

    private void loadSprite() {
        sprite = game.assets.getSurprise();
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

    public boolean isHit(){
        return hit;
    }

    public void setIsHit(boolean hit){
        this.hit = hit;
    }

    public Rectangle getBounds(){
        return bounds;
    }

}