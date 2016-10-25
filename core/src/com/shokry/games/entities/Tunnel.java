package com.shokry.games.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.shokry.games.Game;

public class Tunnel extends B2DSprite{
	
	Vector2 coordinates = new Vector2();
	
	public Tunnel(Body body) {
		super(body);
		Texture tex = Game.res.getTexture("tunnel");
		TextureRegion[] sprites = TextureRegion.split(tex, 70, 70)[0];
		animation.setFrames(sprites, 0f);
		
		width = sprites[0].getRegionWidth();
		height = sprites[0].getRegionHeight();
		
//		coordinates.x = 0f;
//		coordinates.y = 0f;

	}
	
	

}
