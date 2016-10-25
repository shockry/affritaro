package com.shokry.games.entities;

import com.badlogic.gdx.physics.box2d.Body;
import com.shokry.games.Game;

public class Bridge extends B2DSprite{
	
	public Bridge(Body body) {
		super(body);
		
		staticTexture = Game.res.getTexture("bridge");
		
		width = staticTexture.getWidth();
		height = staticTexture.getHeight();
	}
}
