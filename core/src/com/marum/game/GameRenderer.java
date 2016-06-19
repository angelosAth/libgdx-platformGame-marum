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
import java.util.List;


/**
 * Created by angel on 23/3/2016.
 */
public class GameRenderer {
    private World world;
    private OrthographicCamera cam;
    private MarumGame game;
    private ShapeRenderer debugRenderer;
    private List <Box> boxes;
    private List <Platform> platforms;
    private List <Enemy> enemies;
    private List <Invisible> invisibles;
    private List <InvisibleFloor> invisibleFloors;
    private List <Trap> traps;
    private List <Coin> coins;
    private List <FallingPlatform> fallingPlatforms;

    public GameRenderer(World world, MarumGame game) {
        this.world = world;
        this.game = game;
        boxes = new ArrayList<Box>(world.getBox());
        platforms = new ArrayList<Platform>(world.getPlatform());
        enemies = new ArrayList<Enemy>(world.getEnemy());
        invisibles = new ArrayList<Invisible>(world.getInvisible());
        invisibleFloors = new ArrayList<InvisibleFloor>(world.getInvisibleFloor());
        traps = new ArrayList<Trap>(world.getTrap());
        coins = new ArrayList<Coin>(world.getCoin());
        fallingPlatforms = new ArrayList<FallingPlatform>(world.getFallingPlatform());

        cam = new OrthographicCamera();
        cam.setToOrtho(false, 30, 20);
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
        if (world.getMarum().getPosition(). x > 318 && world.getMarum().getPosition().y > 17) {  //camera start to follow hero y and x axis
            cam.position.y = world.getMarum().getPosition().y - 6;  //correction for camera jumping
            cam.position.x = world.getMarum().getPosition().x;
        }
        else if (!world.getMarum().isDead()) {
            cam.position.x = world.getMarum().getPosition().x;
           // cam.position.y = 10;
        }
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
                    world.getMarum().getPosition().y, world.getMarum().getWidth(), world.getMarum().getHeight());
        else
            game.batch.draw(world.getMarum().getFrames(), world.getMarum().getPosition().x + world.getMarum().getWidth(),
                    world.getMarum().getPosition().y, -world.getMarum().getWidth(), world.getMarum().getHeight());

        for (Box box : boxes){
            game.batch.draw(box.getSprite(), box.getPosition().x,
                    box.getPosition().y, box.getWidth(), box.getHeight());
        }

        for (Platform platform : platforms){
            game.batch.draw(platform.getSprite(), platform.getPosition().x,
                    platform.getPosition().y, platform.getWidth(), platform.getHeight());
        }

        for (Enemy enemy : enemies){
            if (enemy.isEnemyRight())
                game.batch.draw(enemy.getFrames(), enemy.getPosition().x,
                        enemy.getPosition().y, enemy.getWidth(), enemy.getHeight());
            else
                game.batch.draw(enemy.getFrames(), enemy.getPosition().x + enemy.getWidth(),
                        enemy.getPosition().y, -enemy.getWidth(), enemy.getHeight());
        }

        for (Invisible invisible : invisibles){
            if (invisible.isHit()) {
                game.batch.draw(invisible.getSprite(), invisible.getPosition().x, invisible.getPosition().y,
                        invisible.getWidth(), invisible.getHeight());
            }
        }

        for (InvisibleFloor invisibleFloor : invisibleFloors){
            if (invisibleFloor.isHit()) {
                game.batch.draw(invisibleFloor.getSprite(), invisibleFloor.getPosition().x,
                        invisibleFloor.getPosition().y, invisibleFloor.getWidth(), invisibleFloor.getHeight());
            }
        }

        for (Coin coin : coins){
            if (coin.isNotPicked()) {
                game.batch.draw(coin.getFrames(), coin.getPosition().x,
                        coin.getPosition().y, coin.getWidth(), coin.getHeight());
            }
        }

        for (Trap trap : traps){
            if (trap.isHit()) {
                game.batch.draw(trap.getSpriteAfter(), trap.getPosition().x,
                        trap.getPosition().y, trap.getWidth(), trap.getHeight());
            }
            else{
                game.batch.draw(trap.getSprite(), trap.getPosition().x,
                        trap.getPosition().y, trap.getWidth(), trap.getHeight());
            }
        }

        for (FallingPlatform fallingPlatform : fallingPlatforms){
            game.batch.draw(fallingPlatform.getSprite(), fallingPlatform.getPosition().x,
                    fallingPlatform.getPosition().y, fallingPlatform.getWidth(), fallingPlatform.getHeight());
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