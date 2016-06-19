package com.marum.game.sprites;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.marum.game.MarumGame;

/**
 * Created by angel on 3/4/2016.
 */
public class Trap extends GameSprite{
    private MarumGame game;
    private static float WIDTH = 1 / 16f * 32;
    private static float HEIGHT = 1 / 16f * 32;
    private boolean hit = false;
    private TextureRegion sprite;
    private TextureRegion spriteAfter;

    public Trap(MarumGame game) {
        super(WIDTH, HEIGHT);
        this.game = game;
    }

    @Override
    public void update(float delta) {
        updateParent(delta);
        updateFrames();
    }

    private void updateFrames() {
        sprite = game.assets.getBox();
        spriteAfter = game.assets.getHiddenEnemy().getKeyFrame(stateTime, true);
    }
    
    public TextureRegion getSprite(){
        return sprite;
    }

    public TextureRegion getSpriteAfter(){
        return spriteAfter;
    }

    public boolean isHit(){
        return hit;
    }

    public void setIsHit(boolean hit){
        this.hit = hit;
    }
}