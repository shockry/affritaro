package com.shokry.games.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.shokry.games.Game;

public class EnemySnail extends B2DSprite{

	public EnemySnail(Body body) {
		super(body);
		
		Texture tex = Game.res.getTexture("snail");
		TextureRegion[] sprites = TextureRegion.split(tex, 60, 34)[0];
		animation.setFrames(sprites, 1 / 6f);
		
		width = sprites[0].getRegionWidth();
		height = sprites[0].getRegionHeight();
	}

}
