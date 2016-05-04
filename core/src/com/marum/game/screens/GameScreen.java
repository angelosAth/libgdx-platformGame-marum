package com.marum.game.screens;


import com.badlogic.gdx.Screen;
import com.marum.game.GameRenderer;
import com.marum.game.MarumGame;
import com.marum.game.World;
import com.marum.game.support.Hud;

/**
 * Created by angel on 18/3/2016.
 */
public class GameScreen implements Screen {
    private MarumGame game;
    private World world;
    private GameRenderer renderer;
    private Hud hud;
    private final float DEATHDELAY;

    public GameScreen (MarumGame game) {
        this.game = game;
        DEATHDELAY = 1.3f;
        //create our game HUD for scores/timers/level info
        hud = new Hud(game);
        //create the world, objects of the game
        world = new World(hud, game);
        //the rendering of the world of the game
        renderer = new GameRenderer(world, game);

        if (game.soundEnabled) {
            game.assets.getMusic().stop();
            game.assets.getGameMusic().play();
        }
    }

    @Override
    public void show() {


    }

    @Override
    public void render(float delta) {
        world.update(delta);
        renderer.render(hud);
        hud.update(delta);
        // when hero dies go to the main menu screen
        if (world.getMarum().isDead() && world.getMarum().getDelayTime() > DEATHDELAY)
            game.setScreen(new MainMenuScreen(game));
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
        hud.dispose();
    }
}
