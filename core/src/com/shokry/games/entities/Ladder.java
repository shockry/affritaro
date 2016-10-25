package com.shokry.games.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.shokry.games.Game;

public class Ladder extends B2DSprite{
public Ladder(Body body) {
		
		super(body);
		
		Texture tex = Game.res.getTexture("ladder");
		TextureRegion[] sprites = TextureRegion.split(tex, 70, 70)[0];
		animation.setFrames(sprites, 0f);
		
		width = sprites[0].getRegionWidth();
		height = sprites[0].getRegionHeight();
		
//		width = tex.getWidth();
//		height = tex.getHeight();
		//System.out.println("ladder size: " + width + ", " + height);
	}
}
