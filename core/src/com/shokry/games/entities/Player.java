package com.shokry.games.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.shokry.games.Game;
import com.shokry.games.states.Play;

public class Player extends B2DSprite {
	
	private int totalCrystals;
	TextureRegion[] sprites;
	
	public Player(Body body) {
		
		super(body);
		
		
		Texture tex = Game.res.getTexture("cat");
		sprites = new TextureRegion[4];
		if(Play.facingRight){
			for(int i = 0; i < sprites.length; i++) {
				sprites[i] = new TextureRegion(tex, i * 32, 0, 32, 32);
			}
		} else {
			for(int i = 0; i < sprites.length; i++) {
				sprites[i] = new TextureRegion(tex, i * 32, 0, -32, 32);
			}
		}
		//Cuz first frame reversed is empty
		sprites[0] = sprites[sprites.length-1];
		
		animatePlayer(true);
		
		width = sprites[0].getRegionWidth();
		height = sprites[0].getRegionHeight();
		
	}
	
	public void  climb(boolean climb){
		if(climb){
			Texture tex = Game.res.getTexture("p3_walk");
			sprites = new TextureRegion[5];
			for(int i = 0; i < sprites.length; i++) {
				sprites[i] = new TextureRegion(tex, i * 18, 0, 18, 26);
			}
		
		} else {
			Play.player = new Player(this.body);
		}
		
		width = sprites[0].getRegionWidth();
		height = sprites[0].getRegionHeight();
	}
	
	public void animatePlayer(boolean animate){
		if (animate) animation.setFrames(sprites, 1 / 12f);
	}
	
	public void setTotalCrystals(int i) { totalCrystals = i; }
	public int getTotalCrystals() { return totalCrystals; }
	
}
