package com.marum.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.marum.game.screens.MainMenuScreen;
import com.marum.game.support.Assets;


public class MarumGame extends Game {
	// used by all screens
	public SpriteBatch batch;
	//boolean enabled or disabled sound
	public boolean soundEnabled;
    //the texture, sound assets of the game
	public Assets assets;

	@Override
	public void create () {
		batch = new SpriteBatch();
		assets = new Assets();

        // we start with the sound enabled
		soundEnabled = true;
		setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {
		super.dispose();
		batch.dispose();
		assets.dispose();
	}
}