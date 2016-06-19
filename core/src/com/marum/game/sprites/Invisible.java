package com.marum.game.sprites;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.marum.game.MarumGame;

/**
 * Created by angel on 27/3/2016.
 */
public class Invisible extends GameSprite{

    private MarumGame game;

    private static final float WIDTH = 1 / 16f * 64;
    private static final float HEIGHT = 1 / 16f * 64;

    private boolean hit = false;

    private TextureRegion sprite;

    public Invisible(MarumGame game) {
        super(WIDTH, HEIGHT);
        this.game = game;
        loadSprite();
    }

    @Override
    public void update(float delta){

    }

    private void loadSprite() {
        sprite = game.assets.getSurprise();
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