package com.shokry.games.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.shokry.games.Game;

public class MovingPlatform extends B2DSprite{
	
	Vector2 coordinates = new Vector2();
	
	public MovingPlatform(Body body) {
		super(body);
		Texture tex = Game.res.getTexture("platform");
		TextureRegion[] sprites = TextureRegion.split(tex, 70, 70)[0];
		animation.setFrames(sprites, 0f);
		
		width = sprites[0].getRegionWidth();
		height = sprites[0].getRegionHeight();
		
//		coordinates.x = 0f;
//		coordinates.y = 0f;

	}
	
	public void setCoordinates(float x, float y){
		//coordinates.x = x;
		coordinates.add(x, y);
		//coordinates.y = y;
	}
	
	public Vector2 getCoordinates(){
		return coordinates;
	}
	

}
