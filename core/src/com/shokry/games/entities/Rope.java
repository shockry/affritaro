package com.shokry.games.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.shokry.games.Game;

public class Rope extends B2DSprite{

	public Rope(Body body) {
		super(body);
		
		Texture ropeTex = Game.res.getTexture("rope");
		TextureRegion gun = new TextureRegion(ropeTex, 0, 0, 16, 32);
		
		texture = gun;
		width = ropeTex.getWidth();
		height = ropeTex.getHeight();
	}

}
