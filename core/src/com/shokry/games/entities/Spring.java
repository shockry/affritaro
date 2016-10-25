package com.shokry.games.entities;

import com.badlogic.gdx.physics.box2d.Body;
import com.shokry.games.Game;

public class Spring extends B2DSprite{
	
	public Spring(Body body) {
		super(body);
		
		staticTexture = Game.res.getTexture("springboardDown");
		
		width = staticTexture.getWidth();
		height = staticTexture.getHeight();
	}
}
