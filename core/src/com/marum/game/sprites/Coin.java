package com.marum.game.sprites;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.marum.game.MarumGame;

/**
 * Created by angel on 13/4/2016.
 */
public class Coin extends GameSprite{
    private MarumGame game;
    private static float WIDTH = 1 / 32f * 46;
    private static float HEIGHT = 1 / 32f * 46;
    private TextureRegion frames;
    private boolean notPicked = true;

    public Coin(MarumGame game) {
        super(WIDTH, HEIGHT);
        this.game = game;
    }

    public void update(float delta) {
        updateParent(delta);
        updateFrames();
    }


    private void updateFrames() {
        frames = game.assets.getCoin().getKeyFrame(stateTime, true);
    }

    public TextureRegion getFrames(){
        return frames;
    }

    public boolean isNotPicked(){
        return notPicked;
    }

    public void setNotPicked (boolean notPicked){
        this.notPicked = notPicked;
    }

}