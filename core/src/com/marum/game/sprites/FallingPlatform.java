package com.marum.game.sprites;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.marum.game.MarumGame;

/**
 * Created by angel on 1/4/2016.
 */
public class FallingPlatform extends DynamicGameSprite{

    private MarumGame game;
    private static float WIDTH = 1 / 16f * 64;
    private static float HEIGHT = 1 / 16f * 12;
    private TextureRegion sprite;

    public FallingPlatform(MarumGame game) {
        super(WIDTH, HEIGHT, game);
        this.game = game;

        loadSprite();
    }

    @Override
    public void update(float delta) {
        position.add(velocity);
    }

    private void loadSprite() {
        sprite = game.assets.getPlatform();
    }

    public void fallingDown(){
        velocity.add(0, -0.06f);
    }

    public TextureRegion getSprite(){
        return sprite;
    }
}