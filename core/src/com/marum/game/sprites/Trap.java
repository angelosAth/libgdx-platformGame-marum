package com.marum.game.sprites;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.marum.game.MarumGame;

/**
 * Created by angel on 3/4/2016.
 */
public class Trap {
    private MarumGame game;
    private float WIDTH;
    private float HEIGHT;
    private boolean hit = false;
    private final Vector2 position;
    private float stateTime;
    private final Rectangle bounds;
    private TextureRegion sprite;
    private TextureRegion spriteAfter;

    public Trap(MarumGame game) {
        this.game = game;
        stateTime = 0;
        WIDTH = 1 / 16f * 32;
        HEIGHT = 1 / 16f * 32;
        position = new Vector2();
        bounds = new Rectangle(position.x, position.y, WIDTH, HEIGHT);

    }

    public void update(float delta) {
        if (delta == 0) return;

        if (delta > 0.1f)
            delta = 0.1f;

        stateTime += delta;
        bounds.x = position.x;
        bounds.y = position.y;

        updateFrames();
    }

    private void updateFrames() {
        sprite = game.assets.getBox();
        spriteAfter = game.assets.getHiddenEnemy().getKeyFrame(stateTime, true);
    }
    
    public TextureRegion getSprite(){
        return sprite;
    }

    public TextureRegion getSpriteAfter(){
        return spriteAfter;
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

