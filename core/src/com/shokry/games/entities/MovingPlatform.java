package com.shokry.games.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.shokry.games.Game;

public class MovingPlatform extends B2DSprite {

	Vector2 coordinates = new Vector2();

	public MovingPlatform(Body body) {
		super(body);

		staticTexture = Game.res.getTexture("platform");

		width = staticTexture.getWidth();
		height = staticTexture.getHeight();
	}

	public void setCoordinates(float x, float y) {
		coordinates.add(x, y);
	}

	public Vector2 getCoordinates() {
		return coordinates;
	}
}
