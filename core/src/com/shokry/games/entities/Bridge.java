package com.shokry.games.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.shokry.games.Game;

public class Bridge extends B2DSprite{
	
	//private float time=0;

	public Bridge(Body body) {
		super(body);
		
		Texture tex = Game.res.getTexture("bridge");
		TextureRegion[] sprites = TextureRegion.split(tex, 70, 20)[0];
		
		animation.setFrames(sprites, 0f);
		
		width = sprites[0].getRegionWidth();
		height = sprites[0].getRegionHeight();
	}
	
//	public float getTime(){
//		return time;
//	}
//	
//	public void setTime(float timeIncrement){
//		time+=timeIncrement;
//	}

}
