package com.shokry.games.entities;

import com.badlogic.gdx.physics.box2d.Body;
import com.shokry.games.Game;

public class Bullet extends B2DSprite{

	public Bullet(Body body) {
		super(body);
		
		staticTexture = Game.res.getTexture("bullet");
		
		width = staticTexture.getWidth();
		height = staticTexture.getHeight();
	}
}
