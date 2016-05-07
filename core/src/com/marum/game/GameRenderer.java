package com.marum.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.marum.game.sprites.Box;
import com.marum.game.sprites.Coin;
import com.marum.game.sprites.Enemy;
import com.marum.game.sprites.FallingPlatform;
import com.marum.game.sprites.Invisible;
import com.marum.game.sprites.InvisibleFloor;
import com.marum.game.sprites.Platform;
import com.marum.game.sprites.Trap;
import com.marum.game.support.Hud;

import java.util.ArrayList;


/**
 * Created by angel on 23/3/2016.
 */
public class GameRenderer {
    private World world;
    private OrthographicCamera cam;
    private MarumGame game;
    private ShapeRenderer debugRenderer;


    public GameRenderer(World world, MarumGame game) {
        this.world = world;
        this.game = game;
        cam = new OrthographicCamera();
        cam.setToOrtho(false, 30, 20);  //30  20
        debugRenderer = new ShapeRenderer();
    }

    public void render(Hud hud) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderBackground();

        game.batch.enableBlending();
        hud.stage.draw();

        // let the camera follow the hero if he is not dead
        //cam.position.y = world.getMarum().position.y;
        if (world.getMarum().getPosition().x > 284) {
            cam.position.y = world.getMarum().getPosition().y + 5;  //correction for camera jumping
        }
        else if (!world.getMarum().isDead())
            cam.position.x = world.getMarum().getPosition().x;
        cam.update();

        // set the TiledMapRenderer view based on what the
        // camera sees, and render the map
        game.assets.getRenderer().setView(cam);
        game.assets.getRenderer().render();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        game.batch.setProjectionMatrix(cam.combined);
        game.batch.enableBlending();
        game.batch.begin();

        if (world.getMarum().isHeroRight())
            game.batch.draw(world.getMarum().getFrames(), world.getMarum().getPosition().x,
                    world.getMarum().getPosition().y, world.getMarum().getWIDTH(), world.getMarum().getHEIGHT());
        else
            game.batch.draw(world.getMarum().getFrames(), world.getMarum().getPosition().x + world.getMarum().getWIDTH(),
                    world.getMarum().getPosition().y, -world.getMarum().getWIDTH(), world.getMarum().getHEIGHT());

        for (Box box : (new ArrayList<Box>(world.getBox()))){
            game.batch.draw(box.getSprite(), box.getPosition().x,
                    box.getPosition().y, box.getWIDTH(), box.getHEIGHT());
        }

        for (Platform platform : (new ArrayList<Platform>(world.getPlatform()))){
            game.batch.draw(platform.getSprite(), platform.getPosition().x,
                    platform.getPosition().y, platform.getWIDTH(), platform.getHEIGHT());
        }

        for (Enemy enemy : (new ArrayList<Enemy>(world.getEnemy()))){
            if (enemy.isEnemyRight())
                game.batch.draw(enemy.getFrames(), enemy.getPosition().x,
                        enemy.getPosition().y, enemy.getWIDTH(), enemy.getHEIGHT());
            else
                game.batch.draw(enemy.getFrames(), enemy.getPosition().x + enemy.getWIDTH(),
                        enemy.getPosition().y, -enemy.getWIDTH(), enemy.getHEIGHT());
        }

        for (Invisible invisible : (new ArrayList<Invisible>(world.getInvisible()))){
            if (invisible.isHit()) {
                game.batch.draw(invisible.getSprite(), invisible.getPosition().x, invisible.getPosition().y,
                        invisible.getWIDTH(), invisible.getHEIGHT());
            }
        }

        for (InvisibleFloor invisibleFloor : (new ArrayList<InvisibleFloor>(world.getInvisibleFloor()))){
            if (invisibleFloor.isHit()) {
                game.batch.draw(invisibleFloor.getSprite(), invisibleFloor.getPosition().x,
                        invisibleFloor.getPosition().y, invisibleFloor.getWIDTH(), invisibleFloor.getHEIGHT());
            }
        }

        for (Coin coin : (new ArrayList<Coin>(world.getCoin()))){
            if (coin.isNotPicked()) {
                game.batch.draw(coin.getFrames(), coin.getPosition().x,
                        coin.getPosition().y, coin.getWIDTH(), coin.getHEIGHT());
            }
        }

        for (Trap trap : (new ArrayList<Trap>(world.getTrap()))){
            if (trap.isHit()) {
                game.batch.draw(trap.getSpriteAfter(), trap.getPosition().x,
                        trap.getPosition().y, trap.getWIDTH(), trap.getHEIGHT());
            }
            else{
                game.batch.draw(trap.getSprite(), trap.getPosition().x,
                        trap.getPosition().y, trap.getWIDTH(), trap.getHEIGHT());
            }
        }

        for (FallingPlatform fallingPlatform : (new ArrayList<FallingPlatform>(world.getFallingPlatform()))){
            game.batch.draw(fallingPlatform.getSprite(), fallingPlatform.getPosition().x,
                    fallingPlatform.getPosition().y, fallingPlatform.getWIDTH(), fallingPlatform.getHEIGHT());
        }
        game.batch.end();

        // render debug rectangles
        //renderDebug();
    }

    private void renderBackground(){
        game.batch.disableBlending();
        game.batch.begin();
        game.batch.draw(game.assets.getBackGroundRegion(), cam.position.x - 30 / 2, cam.position.y - 20 / 2, 30, 20);
        game.batch.end();
    }

    private void renderDebug() {
        debugRenderer.setProjectionMatrix(cam.combined);
        debugRenderer.begin(ShapeRenderer.ShapeType.Line);

        debugRenderer.setColor(Color.RED);
        //debugRenderer.rect(world.getMarum().bounds.x, world.getMarum().bounds.y, world.getMarum().WIDTH, world.getMarum().HEIGHT);
        //debugRenderer.rect(world.getBox().bounds.x, world.getBox().bounds.y, world.getBox().WIDTH, world.getBox().HEIGHT);
        //debugRenderer.rect(world.getPlatform().bounds.x, world.getPlatform().bounds.y, world.getPlatform().WIDTH, world.getPlatform().HEIGHT);
        //debugRenderer.rect(world.getEnemy().bounds.x, world.getEnemy().bounds.y, world.getEnemy().WIDTH, world.getEnemy().HEIGHT);
        //debugRenderer.rect(world.getInvisible().bounds.x, world.getInvisible().bounds.y, world.getInvisible().WIDTH, world.getInvisible().HEIGHT);
        //debugRenderer.rect(world.getInvisibleFloor().bounds.x, world.getInvisibleFloor().bounds.y, world.getInvisibleFloor().WIDTH, world.getInvisibleFloor().HEIGHT);

        debugRenderer.setColor(Color.YELLOW);
        TiledMapTileLayer layer = (TiledMapTileLayer) game.assets.getMap().getLayers().get("walls");
        for (int y = 0; y <= layer.getHeight(); y++) {
            for (int x = 0; x <= layer.getWidth(); x++) {
                TiledMapTileLayer.Cell cell = layer.getCell(x, y);
                if (cell != null) {
                    if (cam.frustum.boundsInFrustum(x + 0.5f, y + 0.5f, 0, 1, 1, 0))
                        debugRenderer.rect(x, y, 1, 1);
                }
            }
        }
        debugRenderer.end();
    }

}
