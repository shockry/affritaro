package com.shokry.games.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.shokry.games.Game;

public class Bullet extends B2DSprite{

	public Bullet(Body body) {
		super(body);
		
		Texture tex = Game.res.getTexture("bullet");
		TextureRegion[] sprites = TextureRegion.split(tex, 47, 47)[0];
		animation.setFrames(sprites, 0f);
		
		width = sprites[0].getRegionWidth();
		height = sprites[0].getRegionHeight();
	}

}
