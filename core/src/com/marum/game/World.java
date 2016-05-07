package com.marum.game;

import com.badlogic.gdx.math.Vector2;
import com.marum.game.sprites.Box;
import com.marum.game.sprites.Coin;
import com.marum.game.sprites.Enemy;
import com.marum.game.sprites.FallingPlatform;
import com.marum.game.sprites.Invisible;
import com.marum.game.sprites.InvisibleFloor;
import com.marum.game.sprites.Marum;
import com.marum.game.sprites.Platform;
import com.marum.game.sprites.Trap;
import com.marum.game.support.Hud;

import java.util.ArrayList;
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

    private final List<InvisibleFloor> invisibleFloors;
    private final List<Platform> platforms;
    private final List<FallingPlatform> fallingPlatforms;
    private final List<Enemy> enemies;
    private final List<Box> boxes;
    private final List<Trap> traps;
    private final List<Invisible> invisibles;
    private final List<Coin> coins;
    //hero doesnt hit wall while on platform
    private boolean isWallFree = true;
    //jumping off screen when the hero dies
    private Vector2 hitVelocity;

    private int [] platformsArray = {36, 3, 126, 15};   //platform positions
    private int[] platformMotion = {1, 1};  //1: moving left, -1: moving right, 0 not moving
    private int [] invisibleFloorsArray = {198, 0, 200, 0, 202, 0, 204, 0, 206, 0, 208, 0, 210, 0, 212, 0, 214, 0, 216, 0};
    private int [] fallingPlatformsArray = { 20, 3 , 70, 3, 75, 3, 80, 3, 85, 3, 152, 10 };
    private int [] enemiesArray = {57, 3};
    private int [] boxesArray = {};
    private int [] trapsArray = {1111,111};//{224, 1};
    private int [] invisiblesArray = {222222,22222};
    private int [] coinArray = {1, 2, 3, 2, 5, 2, 7, 2, 37, 9, 58, 3, 184, 2, 186, 2, 188, 2, 190, 2, 192, 2, 194, 2};


    public World(Hud hud, MarumGame game){
        this.hud = hud;
        this.game = game;
        marum = new Marum(game);  // the game hero
        hitVelocity = new Vector2(0, 30);

        this.invisibleFloors = new ArrayList<InvisibleFloor>();
        this.platforms = new ArrayList<Platform>();
        this.fallingPlatforms = new ArrayList<FallingPlatform>();
        this.enemies = new ArrayList<Enemy>();
        this.boxes = new ArrayList<Box>();
        this.traps = new ArrayList<Trap>();
        this.invisibles = new ArrayList<Invisible>();
        this.coins = new ArrayList<Coin>();

        worldCreation();
    }

    public void update(float delta) {
        // update the hero (process input, collision detection with tiles, position update)
        marum.update(delta);

        for (Platform platform : platforms){
            platform.update(delta);
        }

        for (FallingPlatform fallingPlatform : fallingPlatforms){
            fallingPlatform.update();
        }

        for (Enemy enemy : enemies){
            enemy.update(delta);
        }

        for (Box box : boxes){
            box.update(delta);
        }

        for (Trap trap : traps){
            trap.update();
        }

        for (Invisible invisible : invisibles){
            invisible.update(); //invisible obstruction
        }

        for (InvisibleFloor invisibleFloor : invisibleFloors){
            invisibleFloor.update();
        }

        for (Coin coin : coins){
            coin.update(delta);
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

                if (marum.getPosition().y > box.getPosition().y && marum.getVelocity().y < 0) {  //so dont jump double distance

                    marum.getPosition().y = box.getPosition().y + box.getHEIGHT();
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

                if (marum.getPosition().y > platform.getPosition().y && marum.getVelocity().y < 0) {  //so dont jump double distance

                    marum.getPosition().y = platform.getPosition().y + platform.getHEIGHT();
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
                marum.getPosition().y = fallingPlatform.getPosition().y + fallingPlatform.getHEIGHT();
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
                marum.getPosition().y = invisibleFloor.getPosition().y + invisibleFloor.getHEIGHT();
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


    // creation of the world's object's
    private void worldCreation(){
        int j = 0;  // for the direction of platform
        for (int i = 0; i < platformsArray.length - 1; i += 2) {
            platform = new Platform(game);
            platform.setPosition(platformsArray[i], platformsArray[i+1]);
            platform.setMotion (platformMotion[j]);  //set the direction
            platforms.add(platform);
            j++;
        }

        for (int i = 0; i < invisibleFloorsArray.length - 1; i += 2) {
            invisibleFloor = new InvisibleFloor(game);
            invisibleFloor.setPosition(invisibleFloorsArray[i], invisibleFloorsArray[i+1]);
            invisibleFloors.add(invisibleFloor);
        }

        for (int i = 0; i < fallingPlatformsArray.length - 1; i += 2) {
            fallingPlatform = new FallingPlatform(game);
            fallingPlatform.setPosition(fallingPlatformsArray[i], fallingPlatformsArray[i+1]);
            fallingPlatforms.add(fallingPlatform);
        }

        for (int i = 0; i < enemiesArray.length - 1; i += 2) {
            enemy = new Enemy(game);
            enemy.setPosition(enemiesArray[i], enemiesArray[i+1]);
            enemies.add(enemy);
        }

        for (int i = 0; i < boxesArray.length - 1; i += 2) {
            box = new Box(game);
            box.setPosition(boxesArray[i], boxesArray[i+1]);
            boxes.add(box);
        }

        for (int i = 0; i < trapsArray.length - 1; i += 2) {
            trap = new Trap(game);
            trap.setPosition(trapsArray[i], trapsArray[i + 1]);
            traps.add(trap);
        }

        for (int i = 0; i < invisiblesArray.length - 1; i += 2) {
            invisible = new Invisible(game);
            invisible.setPosition(invisiblesArray[i], invisiblesArray[i+1]);
            invisibles.add(invisible);
        }

        for (int i = 0; i < coinArray.length - 1; i +=2 ) {
            coin = new Coin(game);
            coin.setPosition(coinArray[i], coinArray[i+1]);
            coins.add(coin);
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