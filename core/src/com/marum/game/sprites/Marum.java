package com.marum.game.sprites;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.marum.game.MarumGame;


/**
 * Created by angel on 17/3/2016.
 */
public class Marum extends DynamicGameSprite{

    private static final float WIDTH = 1 / 32f * 64;
    private static final float HEIGHT = 1 / 32f * 84;
    private float MAX_VELOCITY;
    private float JUMP_VELOCITY;
    private boolean stuckRight;
    private boolean stuckLeft;
    private boolean die;
    private State state;
    private float delayTime;
    private boolean heroRight;
    private MarumGame game;
    private TextureRegion frames;


    public Marum (MarumGame game){
        super(WIDTH, HEIGHT, game);
        position.set(15, 13); //240, 13
        MAX_VELOCITY = 10f;
        JUMP_VELOCITY = 32.9f;
        stuckRight = false;
        stuckLeft = false;
        die = false;
        state = State.RUNNING;
        delayTime = 0;
        heroRight = true;
        this.game = game;
        this.bounds = new Rectangle(position.x, position.y, WIDTH , HEIGHT);
    }

    private enum State {
        STANDING, RUNNING, JUMPING, HIT
    }

    @Override
    public void update (float delta){
        updateParent(delta);
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

        collisionDetectionXaxis();

        if (!die)
            collisionDetectionYaxis();

        position.add(velocity);
        velocity.scl(1 / delta);

        // Apply damping to the velocity on the x-axis so we don't
        // walk infinitely once a key was pressed
        velocity.x *= DAMPING;

        dieDelay(delta);
        updateFrames();
        inputController();
    }

    private void updateFrames(){
        switch(state){
            case RUNNING:
                frames = game.assets.getHeroRun().getKeyFrame(stateTime, true);
                break;
            case JUMPING:
                frames = game.assets.getHeroJump().getKeyFrame(stateTime, true);
                break;
            case HIT:
                frames = game.assets.getHeroHit();
                break;
            default:
                frames = game.assets.getHeroStand().getKeyFrame(stateTime, true);
        }
    }

    private void inputController(){

        if ((Gdx.input.isKeyPressed(Input.Keys.UP) || isTouched(0.5f, 1)) && grounded) {

            game.assets.getJumpSound().play();
            velocity.y += JUMP_VELOCITY;
            state = State.JUMPING;
            grounded = false;
            //boxJump = true;
            stuckLeft = false;
            stuckRight = false;
        }

        if ((Gdx.input.isKeyPressed(Input.Keys.LEFT) || isTouched(0, 0.25f)) && !stuckLeft){

            velocity.x = -MAX_VELOCITY;
            if (grounded)
                state = State.RUNNING;
            heroRight = false;
            stuckRight = false;
        }

        if ((Gdx.input.isKeyPressed(Input.Keys.RIGHT)|| isTouched(0.25f, 0.5f)) && !stuckRight){
            //slower walking
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

    private boolean isTouched (float startX, float endX) {
        // Check for touch inputs between startX and endX
        // startX/endX are given between 0 (left edge of the screen) and 1 (right edge of the screen)
        for (int i = 0; i < 2; i++) {
            float x = Gdx.input.getX(i) / (float)Gdx.graphics.getWidth();
            if (Gdx.input.isTouched(i) && (x >= startX && x <= endX)) {
                return true;
            }
        }
        return false;
    }

    public void setBounds(float delta){
        bounds.y += velocity.y * delta;
        bounds.x += velocity.x * delta;
    }

    private void dieDelay(float delta){

        if (die) {
            //we use it in order to gain some seconds before going to main screen
            delayTime += delta;
            state = State.HIT;
        }
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