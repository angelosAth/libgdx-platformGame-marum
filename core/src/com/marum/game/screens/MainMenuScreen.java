package com.marum.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.marum.game.MarumGame;

/**
 * Created by angel on 17/3/2016.
 */
public class MainMenuScreen implements Screen{
    //reference to our game
    private MarumGame game;
    private OrthographicCamera cam;
    //main menu bounds
    private Rectangle playBounds;
    private Rectangle soundBounds;
    private Vector3 touchPoint;

    public MainMenuScreen(MarumGame game){
        this.game = game;
        cam = new OrthographicCamera();
        cam.setToOrtho(false, 800, 480);
        playBounds = new Rectangle(340, 140, 120, 60);
        soundBounds = new Rectangle(0, 0, 64, 64);
        touchPoint = new Vector3();

        if (game.soundEnabled)
            game.assets.getMusic().play();
            game.assets.getGameMusic().stop();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        cam.update();
        game.batch.setProjectionMatrix(cam.combined);
        game.batch.disableBlending();
        game.batch.begin();
        //draw main's menu background
        game.batch.draw(game.assets.getBackGroundRegion(), 0, 0, 860, 484); //800 480
        game.batch.end();
        game.batch.enableBlending();
        game.batch.begin();
        game.batch.draw(game.assets.getLogo(), 280, 330, 256, 128);
        game.batch.draw(game.assets.getPlay(), 340, 140, 120, 60);
        // if sound is enabled show the sound icon otherwise the no sound icon
        game.batch.draw(game.soundEnabled ? game.assets.getSound() : game.assets.getNoSound(), 0, 0, 64, 64);
        game.batch.end();
        //user input
        updateInput();
        dispose();
    }

    private void updateInput(){
        if (Gdx.input.justTouched()) {
            cam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
            if (playBounds.contains(touchPoint.x, touchPoint.y)) {
                //begins the game
                game.assets.getClickSound().play();
                game.setScreen(new GameScreen(game));
            }
            //sound ON/OFF
            if (soundBounds.contains(touchPoint.x, touchPoint.y)) {
                game.assets.getClickSound().play();
                game.soundEnabled = !game.soundEnabled;
                if (game.soundEnabled)
                    game.assets.getMusic().play();
                else
                    game.assets.getMusic().pause();
            }
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}