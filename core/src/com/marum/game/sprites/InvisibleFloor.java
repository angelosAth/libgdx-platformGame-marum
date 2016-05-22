package com.marum.game.sprites;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.marum.game.MarumGame;

/**
 * Created by angel on 28/3/2016.
 */
public class InvisibleFloor extends GameSprite{
    private static float WIDTH = 1 / 16f * 32;
    private static float HEIGHT = 1 / 16f * 32;

    private boolean hit = false;

    private MarumGame game;
    private TextureRegion sprite;

    public InvisibleFloor(MarumGame game) {
        super(WIDTH, HEIGHT);
        this.game = game;
        loadSprite();
    }

    private void loadSprite() {
        sprite = game.assets.getFloor();
    }

    public TextureRegion getSprite(){
        return sprite;
    }

    public boolean isHit(){
        return hit;
    }

    public void setIsHit(boolean hit){
        this.hit = hit;
    }
}
