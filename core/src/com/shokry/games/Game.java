package com.shokry.games;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.shokry.games.handlers.GameInput;
import com.shokry.games.handlers.Content;
import com.shokry.games.handlers.GameInputProcessor;
import com.shokry.games.handlers.BoundedCamera;
import com.shokry.games.handlers.GameStateManager;

public class Game extends ApplicationAdapter {
	public static final String TITLE = "Affritaro Returns";
	public static final int V_WIDTH = 320;
	public static final int V_HEIGHT = 240;
	public static final int SCALE = 2;
	public static final float STEP = 1 / 60f;
	
	private SpriteBatch sb;
	private BoundedCamera cam;
	private OrthographicCamera hudCam;
	
	private GameStateManager gsm;
	
	public static Content res;
	
	public void create() {
		Gdx.input.setInputProcessor(new GameInputProcessor());
		
		res = new Content();
		res.loadTexture("res/images/menu.png");
		res.loadTexture("res/images/bgs.png");
		res.loadTexture("res/images/hud.png");
		res.loadTexture("res/images/cat.png");
		res.loadTexture("res/images/crystal.png");
		res.loadTexture("res/images/spikes.png");
		res.loadTexture("res/images/ladder.png");
		res.loadTexture("res/images/p3_walk.png");
		res.loadTexture("res/images/platform.png");
		res.loadTexture("res/images/bullet.png");
		res.loadTexture("res/images/raygun.png");
		res.loadTexture("res/images/tunnel.png");
		res.loadTexture("res/images/bridge.png");
		res.loadTexture("res/images/healthbar.png");
		res.loadTexture("res/images/snail.png");
		res.loadTexture("res/images/springboardDown.png");
		
		res.loadSound("res/sfx/jump.wav");
		res.loadSound("res/sfx/crystal.wav");
		res.loadSound("res/sfx/levelselect.wav");
		res.loadSound("res/sfx/hit.wav");
		res.loadSound("res/sfx/changeblock.wav");
		res.loadSound("res/sfx/WeaponBlow.wav");
		res.loadSound("res/sfx/enemyHit.wav");
		
		cam = new BoundedCamera();
		cam.setToOrtho(false, V_WIDTH, V_HEIGHT);
		hudCam = new OrthographicCamera();
		hudCam.setToOrtho(false, V_WIDTH, V_HEIGHT);
		
		sb = new SpriteBatch();
		
		gsm = new GameStateManager(this);
		
	}
	
	public void render() {
		Gdx.graphics.setTitle(TITLE + " -- FPS: " + Gdx.graphics.getFramesPerSecond());
		
		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.render();
		GameInput.update();
	}
	
	public void dispose() {
		res.removeAll();
	}
	
	public void resize(int w, int h) {}
	
	public void pause() {}
	
	public void resume() {}
	
	public SpriteBatch getSpriteBatch() { return sb; }
	public BoundedCamera getCamera() { return cam; }
	public OrthographicCamera getHUDCamera() { return hudCam; }
}
