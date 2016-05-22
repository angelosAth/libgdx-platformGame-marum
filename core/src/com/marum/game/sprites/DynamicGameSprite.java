package com.marum.game.sprites;

/**
 * Created by angel on 21/5/2016.
 */
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.marum.game.MarumGame;
import com.marum.game.support.Tiles;

public class DynamicGameSprite extends GameSprite {
    protected final Vector2 velocity;
    protected final float GRAVITY = -2.1f;
    protected float DAMPING = 0.4f;
    protected float width;
    protected float height;
    protected int startX, startY, endX, endY;
    private Tiles tile;
    private Array<Rectangle> tiles;
    public boolean boxStuck = false;
    public boolean grounded = false;


    public DynamicGameSprite (float width, float height, MarumGame game) {
        super(width, height);
        this.width = width;
        this.height = height;
        velocity = new Vector2();
        tile = new Tiles(game);
        tiles = new Array<Rectangle>();
    }

    public Vector2 getVelocity(){
        return velocity;
    }

    public void collisionDetectionXaxis() {

        // perform collision detection & response, on each axis, separately
        // if the object is moving right, check the tiles to the right of it's
        // right bounding box edge, otherwise check the ones to the left
        if (velocity.x > 0) {
            startX = endX = (int) (position.x + width + velocity.x);
        } else {
            startX = endX = (int) (position.x + velocity.x);
        }
        startY = (int) (position.y);
        endY = (int) (position.y + height);

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

        // if it is moving upwards, check the tiles to the top of its
        // top bounding box edge, otherwise check the ones to the bottom
        if (velocity.y > 0) {
            startY = endY = (int) (position.y + height + velocity.y);
        } else {
            startY = endY = (int) (position.y + velocity.y);
        }
        startX = (int) (position.x);
        endX = (int) (position.x + width);

        tile.getTiles(startX, startY, endX, endY, tiles);

        bounds.y += velocity.y;
        for (Rectangle tile : tiles) {
            if (bounds.overlaps(tile)) {
                // we actually reset y-position here
                // so it is just below/above the tile we collided with
                // this removes bouncing
                if (velocity.y > 0) {
                    position.y = tile.y - height;
                } else {
                    position.y = tile.y + tile.height;
                    // if we hit the ground, mark us as grounded so we can jump
                    grounded = true;
                }
                velocity.y = 0;
                break;
            }
        }
    }
}
