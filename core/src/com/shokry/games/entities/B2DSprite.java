package com.shokry.games.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.shokry.games.handlers.Animation;
import com.shokry.games.handlers.B2DVars;

/**
 * Attaches animated sprites to box2d bodies
 */
public class B2DSprite {
	
	protected Body body;
	protected Animation animation;
	protected float width;
	protected float height;
	protected TextureRegion texture;
	
	public B2DSprite(Body body) {
		this.body = body;
		animation = new Animation();
	}
	
	public void setAnimation(TextureRegion reg, float delay) {
		setAnimation(new TextureRegion[] { reg }, delay);
	}
	
	public void setAnimation(TextureRegion[] reg, float delay) {
		animation.setFrames(reg, delay);
		width = reg[0].getRegionWidth();
		height = reg[0].getRegionHeight();
	}
	
	public void update(float dt) {
		animation.update(dt);
	}
	
	public void render(SpriteBatch sb) {
		sb.begin();
//		if(body.getFixtureList().first().getUserData()!= null && body.getFixtureList().first().getUserData().equals("ladder")) 
//			System.out.println("About to draw ladder " );
		
		sb.draw(animation.getFrame(), (body.getPosition().x * B2DVars.PPM - width / 2), (int) (body.getPosition().y * B2DVars.PPM - height / 2));
		sb.end();
	}
	
	public void renderImg(SpriteBatch sb){
		sb.begin();
		sb.draw(texture, (body.getPosition().x * B2DVars.PPM - width / 2),
				(int) (body.getPosition().y * B2DVars.PPM - height / 2));
		sb.end();
	}
	
	public Body getBody() { return body; }
	public Vector2 getPosition() { return body.getPosition(); }
	public float getWidth() { return width; }
	public float getHeight() { return height; }
	
}
