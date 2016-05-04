package com.marum.game.sprites;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.marum.game.MarumGame;
import com.marum.game.Tiles;

/**
 * Created by angel on 25/3/2016.
 */
public class Box {

    private final float WIDTH;
    private final float HEIGHT;
    private final float MAX_VELOCITY;
    private final float DAMPING;

    private Tiles tile;
    private MarumGame game;

    private final float GRAVITY;

    private final Vector2 position;
    private final Vector2 velocity;
    private boolean boxStuck;

    private final Rectangle bounds;
    private Array<Rectangle> tiles;
    private int startX, startY, endX, endY;
    private TextureRegion sprite;

    public Box(MarumGame game) {
        this.game = game;
        tiles = new Array<Rectangle>();
        WIDTH = 1 / 16f * 32;
        HEIGHT = 1 / 16f * 32;
        MAX_VELOCITY = 10f;
        DAMPING = 0.90f;
        GRAVITY = -2.1f;
        boxStuck = false;
        position = new Vector2();
        velocity = new Vector2();

        this.bounds = new Rectangle(position.x, position.y, WIDTH, HEIGHT);

        tile = new Tiles(game);
        loadSprite();
    }

    public void update(float delta) {
        if (delta == 0) return;

        if (delta > 0.1f)
            delta = 0.1f;

        bounds.x = position.x;
        bounds.y = position.y;

        // apply gravity if its falling
        velocity.add(0, GRAVITY);

        // clamp the velocity to the maximum, x-axis only
        velocity.x = MathUtils.clamp(velocity.x,
                -MAX_VELOCITY, MAX_VELOCITY);

        // multiply by delta time so we know how far it goes
        // in this frame
        velocity.scl(delta);

        collisionDetectionXaxis();
        collisionDetectionYaxis();

        position.add(velocity);
        velocity.scl(1 / delta);

        // Apply damping to the velocity on the x-axis so the box wont
        // move infinitely once its pushed
        velocity.x *= DAMPING;
    }

    private void loadSprite() {
        sprite = game.assets.getBox();
    }

    private void collisionDetectionXaxis() {
        // check the tiles to the right of it's right bounding box edge, otherwise
        // check the ones to the left
        if (velocity.x > 0) {
            startX = endX = (int) (position.x + WIDTH + velocity.x);
        }
        else {
            startX = endX = (int) (position.x + velocity.x);
        }
        startY = (int) (position.y);
        endY = (int) (position.y + HEIGHT);

        tile.getTiles(startX, startY, endX, endY, tiles);

        bounds.x += velocity.x;

        for (Rectangle tile : tiles) {
            if (bounds.overlaps(tile)) {
                velocity.x = 0;
                boxStuck = true;
                break;
            }
        }
        bounds.x = position.x;
    }

    public void collisionDetectionYaxis() {

        //check tiles to the bottom
        startY = endY = (int) (position.y + velocity.y);
        startX = (int) (position.x);
        endX = (int) (position.x + WIDTH);

        tile.getTiles(startX, startY, endX, endY, tiles);

        bounds.y += velocity.y;
        for (Rectangle tile : tiles) {
            if (bounds.overlaps(tile)) {
                position.y = tile.y + tile.height;
                velocity.y = 0;
                break;
            }
        }
    }

    public TextureRegion getSprite(){
        return sprite;
    }

    public Vector2 getPosition(){
        return position;
    }

    public float getWIDTH(){boxStuck = false;
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

    public boolean isBoxStuck(){
        return boxStuck;
    }

    public Vector2 getVelocity(){
        return velocity;
    }

    public void setIsBoxStuck(boolean boxStuck){
        this.boxStuck = boxStuck;
    }
}