package com.shokry.games.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.shokry.games.Game;

public class Spring extends B2DSprite{
	
	//private float time=0;

	public Spring(Body body) {
		super(body);
		
		Texture tex = Game.res.getTexture("springboardDown");
		TextureRegion[] sprites = TextureRegion.split(tex, 70, 38)[0];
		
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
