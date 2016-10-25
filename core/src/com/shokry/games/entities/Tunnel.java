package com.shokry.games.entities;

import com.badlogic.gdx.physics.box2d.Body;
import com.shokry.games.Game;

public class Tunnel extends B2DSprite{
	
	public Tunnel(Body body) {
		super(body);
		staticTexture = Game.res.getTexture("tunnel");
		
		width = staticTexture.getWidth();
		height = staticTexture.getHeight();
	}
}
