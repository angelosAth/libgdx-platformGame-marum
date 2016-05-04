package com.marum.game.sprites;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.marum.game.MarumGame;
import com.marum.game.Tiles;


/**
 * Created by angel on 17/3/2016.
 */
public class Marum {

    private float WIDTH;
    private float HEIGHT;
    private float MAX_VELOCITY;
    private float JUMP_VELOCITY;
    private float DAMPING;
    private boolean stuckRight;
    private boolean stuckLeft;
    private final float GRAVITY;
    //private boolean boxJump = false;    //check
    private boolean die;

    private enum State {
        STANDING, RUNNING, JUMPING
    }

    private final Vector2 position;
    private final Vector2 velocity;
    private State state;
    private float stateTime;
    private float delayTime;
    private boolean heroRight;
    private boolean grounded;
    private Rectangle bounds;

    private Tiles tile;
    private MarumGame game;
    private Array<Rectangle> tiles;
    private TextureRegion frames;



    public Marum (MarumGame game){

        position = new Vector2();
        position.set(20, 18);
        WIDTH = 1 / 16f * 32;
        HEIGHT = 1 / 16f * 42;
        MAX_VELOCITY = 10f;
        JUMP_VELOCITY = 32.9f;
        stuckRight = false;
        stuckLeft = false;
        die = false;
        GRAVITY = -2.1f;
        velocity = new Vector2();
        state = State.RUNNING;
        stateTime = 0;
        delayTime = 0;
        heroRight = true;
        grounded = false;
        this.game = game;
        tile = new Tiles(game);
        tiles = new Array<Rectangle>();
        this.bounds = new Rectangle(position.x, position.y, WIDTH , HEIGHT);
    }

    public void update (float delta){
        if (delta == 0) return;

        if (delta > 0.1f)
            delta = 0.1f;

        stateTime += delta;
        bounds.setPosition(position);
        velocity.add(0, GRAVITY);

        // clamp the velocity to the maximum, x-axis only
        velocity.x = MathUtils.clamp(velocity.x,
                -MAX_VELOCITY, MAX_VELOCITY);

        // If the velocity is < 1, set it to 0 and set state to Standing
        if (Math.abs(velocity.x) < 1) {
            velocity.x = 0;
            if (grounded)
                state = State.STANDING;
        }
        // multiply by delta time so we know how far we go
        // in this frame
        velocity.scl(delta);

        // perform collision detection & response, on each axis, separately
        // if the hero is moving right, check the tiles to the right of it's
        // right bounding box edge, otherwise check the ones to the left
        int startX, startY, endX, endY;
        if (velocity.x > 0) {
            startX = endX = (int)(position.x + WIDTH + velocity.x);
        } else {
            startX = endX = (int)(position.x + velocity.x);
        }
        startY = (int)(position.y);
        endY = (int)(position.y + HEIGHT);

        tile.getTiles(startX, startY, endX, endY, tiles);

        bounds.x += velocity.x;
        for (Rectangle tile : tiles) {
            if (bounds.overlaps(tile)) {
                velocity.x = 0;
                break;
            }
        }
        bounds.x = position.x;

        // if the hero is moving upwards, check the tiles to the top of its
        // top bounding box edge, otherwise check the ones to the bottom
        if (velocity.y > 0) {
            startY = endY = (int)(position.y + HEIGHT + velocity.y);
        } else {
            startY = endY = (int)(position.y + velocity.y);
        }
        startX = (int)(position.x);
        endX = (int)(position.x + WIDTH);

        tile.getTiles(startX, startY, endX, endY, tiles);

        bounds.y += velocity.y;
        for (Rectangle tile : tiles) {
            if (bounds.overlaps(tile) && !die) {
                // we actually reset heros y-position here
                // so it is just below/above the tile we collided with
                // this removes bouncing :)
                if (velocity.y > 0) {
                    position.y = tile.y - HEIGHT;
                    // we hit a block jumping upwards, let's destroy it!
                    TiledMapTileLayer layer = (TiledMapTileLayer)game.assets.getMap().getLayers().get("walls");
                    layer.setCell((int)tile.x, (int)tile.y, null);
                } else {
                    position.y = tile.y + tile.height;
                    // if we hit the ground, mark us as grounded so we can jump
                    grounded = true;
                }
                velocity.y = 0;
                break;
            }
        }

        position.add(velocity);
        velocity.scl(1 / delta);

        // Apply damping to the velocity on the x-axis so we don't
        // walk infinitely once a key was pressed
        velocity.x *= DAMPING;

        //we use it in order to gain some seconds before going to main screen
        if (die)
            delayTime += delta;

        updateFrames();
        inputController();
    }

    private void updateFrames(){

        if (state == State.RUNNING) {
            frames = game.assets.getHeroRun().getKeyFrame(stateTime, true);
        }
        if (state == State.STANDING) {
            frames = game.assets.getHeroStand().getKeyFrame(stateTime, true);
        }

        if (state == State.JUMPING) {
            frames = game.assets.getHeroJump().getKeyFrame(stateTime, true);
        }
    }

    private void inputController(){

        if ((Gdx.input.isKeyPressed(Input.Keys.UP) && grounded)) {
            game.assets.getJumpSound().play();
            velocity.y += JUMP_VELOCITY;
            state = State.JUMPING;
            grounded = false;
            //boxJump = true;
            stuckLeft = false;
            stuckRight = false;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && !stuckLeft){
            velocity.x = -MAX_VELOCITY;
            if (grounded)
                state = State.RUNNING;
            heroRight = false;
            stuckRight = false;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && !stuckRight){
            if(Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT))
                velocity.x = MAX_VELOCITY / 5;
            else
                velocity.x = MAX_VELOCITY;
            if (grounded)
                state = State.RUNNING;
            heroRight = true;
            stuckLeft = false;
        }
    }

    public void setBounds(float delta){
        bounds.y += velocity.y * delta;
        bounds.x += velocity.x * delta;
    }

    public Rectangle getBounds(){
        return bounds;
    }

    public void stuckLeft(boolean stuckLeft){
        this.stuckLeft = stuckLeft;
    }

    public void stuckRight(boolean stuckRight){
        this.stuckRight = stuckRight;
    }

    public boolean isDead(){
        return die;
    }

    public void dead(boolean die){
        this.die = die;
    }

    public TextureRegion getFrames(){
        return frames;
    }

    public boolean isHeroRight(){
        return heroRight;
    }

    public float getWIDTH(){
        return WIDTH;
    }

    public float getHEIGHT(){
        return HEIGHT;
    }

    public Vector2 getPosition(){
        return position;
    }

    public Vector2 getVelocity(){
        return velocity;
    }

    public void setVelocity(Vector2 velocity){
        this.velocity.set(velocity);
    }

    public float getDelayTime(){
        return delayTime;
    }

    public void setGrounded(boolean grounded){
        this.grounded = grounded;
    }
}