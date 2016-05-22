package com.marum.game.sprites;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.marum.game.MarumGame;

/**
 * Created by angel on 27/3/2016.
 */
public class Enemy extends DynamicGameSprite{

    private static final float WIDTH = 1 / 32f * 64;
    private static final float HEIGHT = 1 / 32f * 64;
    private final float MAX_VELOCITY;
    private boolean enemyRight;

    private MarumGame game;

    private boolean hitWall = false;
    private TextureRegion frames;
    private int kindOfEnemy;

    public Enemy(MarumGame game) {
        super(WIDTH, HEIGHT, game);
        this.game = game;
        MAX_VELOCITY = 10f;
        enemyRight = true;
        kindOfEnemy = 0;
    }

    public void update(float delta) {
        updateParent(delta);
        //which enemy sprite to use, depends on its position
        if (bounds.x % 2 == 0)
            kindOfEnemy = 1;

        // apply gravity if he is falling
        velocity.add(0, GRAVITY);

        // multiply by delta time so we know how far he goes
        // in this frame
        velocity.scl(delta);

        collisionDetectionXaxis();
        collisionDetectionYaxis();

        if (velocity.x == 0){
            // enemy hits wall and changes direction
            hitWall = !hitWall;
            enemyRight = !enemyRight;
        }

        position.add(velocity);
        velocity.scl(1 / delta);

        updateFrames();
        motionController();
    }

    private void updateFrames() {
        if (kindOfEnemy == 0)
            frames = game.assets.getEnemy().getKeyFrame(stateTime, true);
        else
            frames = game.assets.getPinkEnemy().getKeyFrame(stateTime, true);
    }

    private void motionController() {
        if (!hitWall)
            // enemy hits wall change direction
            velocity.x = -MAX_VELOCITY;
        else
            velocity.x = MAX_VELOCITY;
    }

    public TextureRegion getFrames(){
        return frames;
    }

    public boolean isEnemyRight(){
        return enemyRight;
    }
}