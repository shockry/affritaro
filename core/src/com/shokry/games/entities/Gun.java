package com.shokry.games.entities;

import com.badlogic.gdx.physics.box2d.Body;
import com.shokry.games.Game;

public class Gun extends B2DSprite{

	public Gun(Body body) {
		super(body);
		
		staticTexture = Game.res.getTexture("raygun");
		
		width = staticTexture.getWidth();
		height = staticTexture.getHeight();
	}

}
