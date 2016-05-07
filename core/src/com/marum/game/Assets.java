package com.marum.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Array;

/**
 * Created by angel on 24/3/2016.
 */
public class Assets {
    //for loading images from texture atlas
    private TextureAtlas atlas;
    //the background image
    private Texture background;
    private TextureRegion backGroundRegion;
    //main menu textures
    private TextureRegion logo;
    private TextureRegion play;
    private TextureRegion sound;
    private TextureRegion noSound;
    //game textures
    private TextureRegion box;
    private TextureRegion platform;
    private TextureRegion floor;
    private TextureRegion surprise;
    //game animated textures
    private Animation heroStand;
    private Animation heroRun;
    private Animation heroJump;
    private Animation enemy;
    private Animation coin;
    //music and sound effects
    private Music music;
    private Music gameMusic;
    private Sound jumpSound;
    private Sound hitSound;
    private Sound fallSound;
    private Sound coinSound;
    private Sound clickSound;

    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;


    public Assets () {
        background = loadTexture("back3.png");
        backGroundRegion = new TextureRegion(background, 0, 0, 800, 480);   //850 600
        atlas = new TextureAtlas("hero.pack");
        // load the map, set the unit scale to 1/16 (1 unit == 16 pixels)
        map = new TmxMapLoader().load("level5.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / 32f);  // 1 / 32

        loadAnimationTextures();
        loadSprites();
        loadSound();
    }

    private void loadAnimationTextures(){
        Array<TextureRegion> frames = new Array<TextureRegion>();
        //running animation frames
        for (int i = 1;i < 9; i++){
            frames.add(new TextureRegion(atlas.findRegion("Run" + i)));
        }
        heroRun = new Animation(0.1f, frames);
        frames.clear();
        //jumping frames
        for (int i = 1; i < 7; i++){
            frames.add(new TextureRegion(atlas.findRegion("Jump" + 6)));
        }
        heroJump = new Animation(0.1f, frames);
        frames.clear();
        //idle animation frames
        for (int i = 1; i < 11; i++){
            frames.add(new TextureRegion(atlas.findRegion("Idle" + i)));
        }
        //create texture region for hero standing
        heroStand = new Animation(0.1f, frames);
        frames.clear();
        //enemy animation frames
        for (int i = 3; i < 5; i++){
            frames.add(new TextureRegion(atlas.findRegion("bad" + i)));
        }
        enemy = new Animation(0.1f, frames);
        //coin animation frames
        frames.clear();
        for (int i = 1; i < 4; i++){
            frames.add(new TextureRegion(atlas.findRegion("coin" + i)));
        }
        coin = new Animation(0.1f, frames);
    }
    //load the game sprites/textures
    private void loadSprites(){
        box = new TextureRegion(atlas.findRegion("box"));
        surprise = new TextureRegion(atlas.findRegion("surp"));
        platform = new TextureRegion(atlas.findRegion("platform2"));
        floor = new TextureRegion(atlas.findRegion("box"));
        logo = new TextureRegion(atlas.findRegion("marum"));
        play = new TextureRegion(atlas.findRegion("play"));
        sound = new TextureRegion(atlas.findRegion("sound"));
        noSound = new TextureRegion(atlas.findRegion("noSound"));
    }

    //load game sound effects and music
    private void loadSound(){
        music = Gdx.audio.newMusic(Gdx.files.internal("skies.mp3"));
        music.setLooping(true);
        music.setVolume(0.5f);

        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("gameMusic.mp3"));
        gameMusic.setLooping(true);
        gameMusic.setVolume(0.3f);

        fallSound = Gdx.audio.newSound(Gdx.files.internal("fall.wav"));
        hitSound = Gdx.audio.newSound(Gdx.files.internal("hit.wav"));

        jumpSound = Gdx.audio.newSound(Gdx.files.internal("jump.wav"));
        coinSound = Gdx.audio.newSound(Gdx.files.internal("coin.wav"));
        clickSound = Gdx.audio.newSound(Gdx.files.internal("click.wav"));
    }

    private Texture loadTexture (String file) {
        return new Texture(Gdx.files.internal(file));
    }

    public TextureRegion getBackGroundRegion(){
        return backGroundRegion;
    }

    public TextureRegion getLogo(){
        return logo;
    }

    public TextureRegion getBox(){
        return box;
    }
    public TextureRegion getSurprise(){
        return surprise;
    }

    public TextureRegion getPlatform(){
        return platform;
    }

    public TextureRegion getFloor(){
        return floor;
    }

    public TextureRegion getPlay(){
        return play;
    }

    public TextureRegion getSound(){
        return sound;
    }

    public TextureRegion getNoSound(){
        return noSound;
    }

    public Animation getHeroStand(){
        return heroStand;
    }

    public Animation getHeroRun(){
        return heroRun;
    }

    public Animation getHeroJump(){
        return heroJump;
    }

    public Animation getEnemy(){
        return enemy;
    }

    public Animation getCoin(){
        return coin;
    }

    public Music getMusic(){
        return music;
    }
    public Music getGameMusic(){
        return gameMusic;
    }
    public Sound getHitSound(){
        return hitSound;
    }

    public Sound getFallSound(){
        return fallSound;
    }

    public Sound getClickSound(){
        return clickSound;
    }

    public Sound getCoinSound(){
        return coinSound;
    }

    public Sound getJumpSound(){
        return jumpSound;
    }

    public TiledMap getMap(){
        return map;
    }

    public OrthogonalTiledMapRenderer getRenderer(){
        return renderer;
    }

    public void dispose(){
        atlas.dispose();
        map.dispose();
    }
}
