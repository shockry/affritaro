package com.shokry.games.entities;

import com.badlogic.gdx.physics.box2d.Body;
import com.shokry.games.Game;

public class Ladder extends B2DSprite{

	public Ladder(Body body) {
		super(body);
		
		staticTexture = Game.res.getTexture("ladder");
		
		width = staticTexture.getWidth();
		height = staticTexture.getHeight();
		
	}
}
