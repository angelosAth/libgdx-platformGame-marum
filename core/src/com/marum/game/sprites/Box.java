package com.marum.game.sprites;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.marum.game.MarumGame;

/**
 * Created by angel on 25/3/2016.
 */
public class Box extends DynamicGameSprite{

    private static final float WIDTH = 1 / 16f * 32;
    private static final float HEIGHT = 1 / 16f * 32;
    private MarumGame game;
    private TextureRegion sprite;

    public Box(MarumGame game) {
        super(WIDTH, HEIGHT, game);
        this.game = game;
        loadSprite();
    }

    @Override
    public void update(float delta) {
        updateParent(delta);
        // apply gravity if it's falling
        velocity.add(0, GRAVITY);

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

    public TextureRegion getSprite(){
        return sprite;
    }

    public boolean isBoxStuck(){
        return boxStuck;
    }

    public void setIsBoxStuck(boolean boxStuck){
        this.boxStuck = boxStuck;
    }
}