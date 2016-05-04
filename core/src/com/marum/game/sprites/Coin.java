package com.marum.game.sprites;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.marum.game.MarumGame;

/**
 * Created by angel on 13/4/2016.
 */
public class Coin {
    private MarumGame game;
    private final float WIDTH;
    private final float HEIGHT;
    private final Vector2 position;
    private float stateTime;
    private final Rectangle bounds;
    private TextureRegion frames;
    private boolean notPicked = true;

    public Coin(MarumGame game) {
        this.game = game;
        WIDTH = 1 / 32f * 46;
        HEIGHT = 1 / 32f * 46;
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
        frames = game.assets.getCoin().getKeyFrame(stateTime, true);
    }

    public TextureRegion getFrames(){
        return frames;
    }

    public Vector2 getPosition(){
        return position;
    }

    public float getWIDTH(){
        return WIDTH;
    }

    public boolean isNotPicked(){
        return notPicked;
    }


    public float getHEIGHT(){
        return HEIGHT;
    }

    public void setPosition(float x, float y){
        this.position.x = x;
        this.position.y = y;
    }

    public void setNotPicked (boolean notPicked){
        this.notPicked = notPicked;
    }

    public Rectangle getBounds(){
        return bounds;
    }

}