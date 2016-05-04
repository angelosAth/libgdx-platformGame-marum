package com.marum.game.sprites;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.marum.game.MarumGame;
import com.marum.game.Tiles;

/**
 * Created by angel on 27/3/2016.
 */
public class Enemy {

    private final float WIDTH;
    private final float HEIGHT;
    private final float MAX_VELOCITY;
    private boolean enemyRight;

    private Tiles tile;
    private MarumGame game;

    private final float GRAVITY;

    private Vector2 position;
    private Vector2 velocity;

    private float stateTime;
    private boolean hitWall = false;

    private final Rectangle bounds;
    private Array<Rectangle> tiles;

    private int startX, startY, endX, endY;

    private TextureRegion frames;

    public Enemy(MarumGame game) {
        this.game = game;
        WIDTH = 1 / 16f * 32;
        HEIGHT = 1 / 16f * 32;
        GRAVITY = -2.1f;
        MAX_VELOCITY = 10f;
        tiles = new Array<Rectangle>();
        position = new Vector2();
        velocity = new Vector2();
        bounds = new Rectangle(position.x, position.y, WIDTH, HEIGHT);
        tile = new Tiles(game);
        enemyRight = true;
    }

    public void update(float delta) {
        if (delta == 0) return;
        if (delta > 0.1f)
            delta = 0.1f;

        stateTime += delta;

        bounds.x = position.x;
        bounds.y = position.y;

        // apply gravity if he is falling
        velocity.add(0, GRAVITY);

        // multiply by delta time so we know how far he goes
        // in this frame
        velocity.scl(delta);

        collisionDetectionXaxis();
        collisionDetectionYaxis();

        position.add(velocity);
        velocity.scl(1 / delta);

        updateFrames();
        motionController();
    }

    private void updateFrames() {
        frames = game.assets.getEnemy().getKeyFrame(stateTime, true);
    }

    private void motionController() {
        if (!hitWall)
            // enemy hits wall change direction
            velocity.x = -MAX_VELOCITY;
        else
            velocity.x = MAX_VELOCITY;
    }

    private void collisionDetectionXaxis() {

        // perform collision detection & response
        // if it is moving right, check the tiles to the right of it's
        // right bounding box edge, otherwise check the ones to the left
        if (velocity.x > 0) {
            startX = endX = (int) (position.x + WIDTH + velocity.x);
        } else {
            startX = endX = (int) (position.x + velocity.x);
        }
        startY = (int) (position.y);
        endY = (int) (position.y + HEIGHT);

        tile.getTiles(startX, startY, endX, endY, tiles);

        bounds.x += velocity.x;

        for (Rectangle tile : tiles) {
            if (bounds.overlaps(tile)) {
                // enemy hits wall changes facing
                hitWall = !hitWall;
                enemyRight = !enemyRight;
                break;
            }
        }
        bounds.x = position.x;
    }

    private void collisionDetectionYaxis() {
        //enemy doesnt move upwards so we check only the tiles down
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

    public Vector2 getPosition(){
        return position;
    }

    public TextureRegion getFrames(){
        return frames;
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

    public boolean isEnemyRight(){
        return enemyRight;
    }


}
