package com.marum.game.sprites;

/**
 * Created by angel on 20/5/2016.
 */
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class GameSprite {
    public final Vector2 position;
    public Rectangle bounds;
    public float width;
    public float height;
    public float stateTime;


    public GameSprite (float width, float height) {
        this.position = new Vector2();
        this.width = width;
        this.height = height;
        stateTime = 0;
    }

    public void updateParent(float delta) {
        if (delta == 0) return;
        if (delta > 0.1f)
            delta = 0.1f;

        stateTime += delta;

        bounds.x = position.x;
        bounds.y = position.y;
    }

    public void setPosition(float x, float y){
        this.position.x = x;
        this.position.y = y;
        bounds = new Rectangle(position.x, position.y, width, height);
    }

    public Rectangle getBounds(){
        return bounds;
    }

    public Vector2 getPosition(){
        return position;
    }

    public float getWidth(){
        return width;
    }

    public float getHeight(){
        return height;
    }

}