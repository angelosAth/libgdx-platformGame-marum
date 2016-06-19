package com.marum.game;

import com.badlogic.gdx.math.Vector2;
import com.marum.game.sprites.Box;
import com.marum.game.sprites.Coin;
import com.marum.game.sprites.Enemy;
import com.marum.game.sprites.FallingPlatform;
import com.marum.game.sprites.GameSprite;
import com.marum.game.sprites.Invisible;
import com.marum.game.sprites.InvisibleFloor;
import com.marum.game.sprites.Marum;
import com.marum.game.sprites.Platform;
import com.marum.game.sprites.Trap;
import com.marum.game.support.Hud;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by angel on 23/3/2016.
 */
public class World {
    private Marum marum;
    private MarumGame game;
    private Hud hud;
    //tha game objects
    private Box box;
    private Enemy enemy;
    private Invisible invisible;
    private Trap trap;
    private FallingPlatform fallingPlatform;
    private Platform platform;
    private InvisibleFloor invisibleFloor;
    private Coin coin;

    private List<InvisibleFloor> invisibleFloors;
    private List<Platform> platforms;
    private List<FallingPlatform> fallingPlatforms;
    private List<Enemy> enemies;
    private List<Box> boxes;
    private List<Trap> traps;
    private List<Invisible> invisibles;
    private List<Coin> coins;
    private List<GameSprite> gameObjects;

    //hero doesn't hit wall while on platform
    private boolean isWallFree = true;
    //jumping off screen when the hero dies
    private Vector2 hitVelocity;

    private int[][] platformsArray = {
            {36, 3}, {126, 15}, {291, 4}, {322, 10},
            {327, 14}, {335, 18}, {348, 21}, {363, 17}
    };   //platform hard-coded positions
    private int[] platformMotion = {
            1, 1, -1, 1, -1, 1, -1, 0
    };  //1: starting moving from left, -1: moving right, 0: not moving
    private int[][] invisibleFloorsArray = {
            {198, 0}, {200, 0}, {202, 0}, {204, 0},
            {206, 0}, {208, 0}, {210, 0}, {212, 0},
            {214, 0}, {216, 0}
    };
    private int[][] fallingPlatformsArray = {
            {20, 3}, {70, 3}, {75, 3}, {80, 3},
            {85, 3}, {152, 10}
    };
    private int[][] enemiesArray = {
            {57, 2}, {262, 3}, {309, 10}
    };//, 269, 3};
    private int[][] boxesArray = {
            {171, 3}, {241, 2}
    };
    private int[][] trapsArray = {
            {178, 2}
    };
    private int[][] invisiblesArray = {
            {304, 13}
    };
    private int[][] coinArray = {
            {1, 2}, {3, 2}, {5, 2},
            {7, 2}, {37, 9}, {58, 3},
            {184, 2}, {186, 2}, {188, 2},
            {190, 2}, {192, 2}, {194, 2}
    };


    public World(Hud hud, MarumGame game){
        this.hud = hud;
        this.game = game;
        marum = new Marum(game);  // the game hero
        hitVelocity = new Vector2(0, 30);

        invisibleFloors = new ArrayList<InvisibleFloor>();
        platforms = new ArrayList<Platform>();
        fallingPlatforms = new ArrayList<FallingPlatform>();
        enemies = new ArrayList<Enemy>();
        boxes = new ArrayList<Box>();
        traps = new ArrayList<Trap>();
        invisibles = new ArrayList<Invisible>();
        coins = new ArrayList<Coin>();
        gameObjects = new LinkedList<GameSprite>();

        worldCreation();
    }

    public void update(float delta) {
        // update the hero (process input, collision detection with tiles, position update)
        marum.update(delta);

        for (GameSprite gameObject : gameObjects){
            gameObject.update(delta);
        }

        marum.setBounds(delta);

        heroBoxInteraction();
        heroPlatformsInteraction();
        heroFallingPlatformsInteraction();
        heroEnemyInteraction();
        heroInvisibleInteraction();
        heroTrapInteraction();
        heroCoinInteraction();
        checkFalling();
        isTimeUp();
    }

    private void heroBoxInteraction() {
        for (Box box : boxes){
            if (marum.getBounds().overlaps(box.getBounds())) {
                if ((!(marum.getPosition().y > box.getPosition().y) && !box.isBoxStuck()) && (marum.getPosition().x > box.getPosition().x || marum.getPosition().x < box.getPosition().x)) {
                    box.getVelocity().x = marum.getVelocity().x;
                } else if (box.isBoxStuck()) {
                    marum.getVelocity().x = 0;  //hit box at corner
                    if (!marum.isHeroRight()) {
                        marum.getPosition().x += 0.35f;
                        marum.stuckLeft(true);
                    }
                    if (marum.isHeroRight()) {
                        marum.getPosition().x -= 0.35f;
                        marum.stuckRight(true);
                    }
                }

                //in order to not jump double distance
                if (marum.getPosition().y > box.getPosition().y && marum.getVelocity().y < 0) {
                    marum.getPosition().y = box.getPosition().y + box.getHeight();
                    marum.setGrounded(true);
                    marum.getVelocity().y = 0;

                    marum.stuckRight(false);
                    marum.stuckLeft(false);
                }
            }
            box.setIsBoxStuck(false);
        }
    }

    private void heroEnemyInteraction(){
        for (Enemy enemy : enemies){
            if (marum.getBounds().overlaps(enemy.getBounds())) {
                heroDie();
            }
        }
    }

    private void heroCoinInteraction(){
        for (Coin coin : coins){
            if (marum.getBounds().overlaps(coin.getBounds()) && coin.isNotPicked()){
                game.assets.getCoinSound().play();
                hud.addScore(1);
                coin.setNotPicked(false);
            }
        }
    }

    private void heroPlatformsInteraction(){
        for (Platform platform : platforms){
            if (marum.getBounds().overlaps(platform.getBounds())) {
                // in order to not double jump
                if (marum.getPosition().y > platform.getPosition().y && marum.getVelocity().y < 0) {
                    marum.getPosition().y = platform.getPosition().y + platform.getHeight();
                    marum.setGrounded(true);
                    marum.getVelocity().y = 0;
                    // doesn't hit wall while on platform
                    if (isWallFree)
                        marum.getPosition().x += platform.getVelocity().x;
                }
            }
        }
    }

    private void heroTrapInteraction(){
        for (Trap trap : traps){
            if (marum.getBounds().overlaps(trap.getBounds())) {
                heroDie();
                trap.setIsHit(true);
            }
        }
    }

    private void heroFallingPlatformsInteraction(){
        for (FallingPlatform fallingPlatform : fallingPlatforms){
            if (marum.getBounds().overlaps(fallingPlatform.getBounds())) {
               fallingPlatform.fallingDown();
                marum.getPosition().y = fallingPlatform.getPosition().y + fallingPlatform.getHeight();
                marum.setGrounded(true);
                marum.getVelocity().y = 0;
            }
        }
    }

    private void heroInvisibleInteraction(){
        for (Invisible  invisible : invisibles){
            if (marum.getBounds().overlaps(invisible.getBounds())) {
                invisible.setIsHit(true);
                if (marum.getVelocity().y > 0)
                    marum.getVelocity().y *= -1;

                marum.getVelocity().x = 0;
                isWallFree = false;
            }
            else
                isWallFree = true;
        }

        for (InvisibleFloor invisibleFloor : invisibleFloors){
            if (marum.getBounds().overlaps(invisibleFloor.getBounds())) {
                invisibleFloor.setIsHit(true);
                marum.getPosition().y = invisibleFloor.getPosition().y + invisibleFloor.getHeight();
                marum.setGrounded(true);
                marum.getVelocity().y = 0;
            }
        }
    }

    private void checkFalling(){
        if (marum.getPosition().y < 0){
            heroFalltoDeath();
        }
    }

    private void isTimeUp(){
        if (hud.isTimeUp())
            heroFalltoDeath();
    }


    // creation of the world's objects
    private void worldCreation(){
        int j = 0;  // for the direction of platform

        for (int[] pArray : platformsArray) {
            platform = new Platform(game);
            platform.setPosition(pArray[0], pArray[1]);
            platform.setMotion(platformMotion[j]);  //set the direction
            platforms.add(platform);
            gameObjects.add(platform);
            j++;
        }

        for (int[] iArray : invisibleFloorsArray) {
            invisibleFloor = new InvisibleFloor(game);
            invisibleFloor.setPosition(iArray[0], iArray[1]);
            invisibleFloors.add(invisibleFloor);
            gameObjects.add(invisibleFloor);
        }

        for (int[] fArray : fallingPlatformsArray) {
            fallingPlatform = new FallingPlatform(game);
            fallingPlatform.setPosition(fArray[0], fArray[1]);
            fallingPlatforms.add(fallingPlatform);
            gameObjects.add(fallingPlatform);
        }

        for (int[] eArray : enemiesArray) {
            enemy = new Enemy(game);
            enemy.setPosition(eArray[0], eArray[1]);
            enemies.add(enemy);
            gameObjects.add(enemy);
        }

        for (int[] bArray : boxesArray) {
            box = new Box(game);
            box.setPosition(bArray[0], bArray[1]);
            boxes.add(box);
            gameObjects.add(box);
        }

        for (int[] tArray : trapsArray) {
            trap = new Trap(game);
            trap.setPosition(tArray[0], tArray[1]);
            traps.add(trap);
            gameObjects.add(trap);
        }

        for (int[] iArray : invisiblesArray) {
            invisible = new Invisible(game);
            invisible.setPosition(iArray[0], iArray[1]);
            invisibles.add(invisible);
            gameObjects.add(invisible);
        }

        for (int[] cArray : coinArray) {
            coin = new Coin(game);
            coin.setPosition(cArray[0], cArray[1]);
            coins.add(coin);
            gameObjects.add(coin);
        }
    }

    private void heroDie(){
        if (!marum.isDead()) {
            game.assets.getGameMusic().stop();
            //hero screams before he dies
            game.assets.getHitSound().play();
            //hero dies and gets off screen
            marum.setVelocity(hitVelocity);
            dieDefaults();
        }
    }

    private void heroFalltoDeath(){
        if (!marum.isDead()) {
            game.assets.getGameMusic().stop();
            //hero screams falling to his death
            game.assets.getFallSound().play();
            dieDefaults();
        }
    }

    private void dieDefaults(){
        //hero dies
        marum.dead(true);
        invisible.setIsHit(false);
        trap.setIsHit(false);
    }

    public Marum getMarum() {
        return marum;
    }

    public List getBox() {
        return boxes;
    }

    public List getInvisibleFloor(){
        return invisibleFloors;
    }

    public List getEnemy() {
        return enemies;
    }

    public List getFallingPlatform() {
        return fallingPlatforms;
    }

    public List getTrap() {
        return traps;
    }

    public List getPlatform(){
        return platforms;
    }

    public List getInvisible() {
        return invisibles;
    }

    public List getCoin() {
        return coins;
    }

}