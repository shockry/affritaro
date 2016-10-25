package com.shokry.games.handlers;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;
import com.shokry.games.states.Play;

public class GameContactListener implements ContactListener {

	private int numFootContacts;
	private Array<Body> bodiesToRemove;
	private boolean playerDead;
	private boolean playerClimb;
	private boolean playerOnPlatform;
	private boolean playerInTunnel;
	private boolean playerOnBridge;
	private boolean playerOnSpring;
	private Body currentBridge;
	
	
	public GameContactListener() {
		super();
		bodiesToRemove = new Array<Body>();
	}

	public void beginContact(Contact contact) {

		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();

		if (fa == null || fb == null)
			return;

		if (fa.getUserData() != null && fa.getUserData().equals("foot")) {
			numFootContacts++;
		}
		if (fb.getUserData() != null && fb.getUserData().equals("foot")) {
			numFootContacts++;
		}

		if (fa.getUserData() != null && fa.getUserData().equals("crystal")) {
			bodiesToRemove.add(fa.getBody());
		}
		if (fb.getUserData() != null && fb.getUserData().equals("crystal")) {
			bodiesToRemove.add(fb.getBody());
		}

		if (fa.getUserData() != null && fa.getUserData().equals("spike")) {
			playerDead = true;
		}
		if (fb.getUserData() != null && fb.getUserData().equals("spike")) {
			playerDead = true;
		}

		if (fb.getUserData() != null && fb.getUserData().equals("ladder")) {
			if (fa.getUserData() != null && fa.getUserData().equals("player")) {
				playerClimb = true;
				Play.player.climb(true);
			}
		}
		
		//Snail enemy head collision
		if (fa.getUserData() != null && fa.getUserData().equals("snailHead")) {
			if (fb.getUserData() != null && fb.getUserData().equals("player")) {
				if (Play.health>=20) Play.health -= 20;
				else playerDead = true;
			}

		}
		if (fb.getUserData() != null && fb.getUserData().equals("snailHead")) {
			if (fa.getUserData() != null && fa.getUserData().equals("player")) {
				if (Play.health>20) Play.health -= 20;
				else playerDead = true;
			}
		}
		
		if (fa.getUserData() != null
				&& fa.getUserData().equals("movingPlatform")) {
			playerOnPlatform = true;
			numFootContacts++;
		}
		if (fb.getUserData() != null
				&& fb.getUserData().equals("movingPlatform")) {
			playerOnPlatform = true;
			numFootContacts++;
		}
		

		if (fa.getUserData() != null
				&& fa.getUserData().equals("springTop")) {
			playerOnSpring = true;
		}
		
		if ( fb.getUserData() != null
				&& fb.getUserData().equals("springTop")) {
			playerOnSpring = true;
		} 

		if (fa.getUserData() != null && fa.getUserData().equals("bullet")
				&& fb.getUserData().equals("snail")) {
			bodiesToRemove.add(fb.getBody());
		}
		if (fb.getUserData() != null && fb.getUserData().equals("bullet")
				&& fa.getUserData().equals("snail")) {
			bodiesToRemove.add(fa.getBody());
		}
		
		if (fa.getUserData() != null && fa.getUserData().equals("bullet")) {
			bodiesToRemove.add(fa.getBody());
		}
		if (fb.getUserData() != null && fb.getUserData().equals("bullet")) {
			bodiesToRemove.add(fb.getBody());
		}
		
		if (fa.getUserData() != null && fa.getUserData().equals("gun")) {
			bodiesToRemove.add(fa.getBody());
			Play.bulletNum = 3;
		}
		if (fb.getUserData() != null && fb.getUserData().equals("gun")) {
			bodiesToRemove.add(fb.getBody());
			Play.bulletNum = 3;
		}
		
		if (fa.getUserData() != null && fa.getUserData().equals("tunnel")) {
			playerInTunnel = true;
			System.out.println(Play.player.getBody().getPosition().x);
		}
		if (fb.getUserData() != null && fb.getUserData().equals("tunnel")) {
			playerInTunnel = true;
			System.out.println(Play.player.getBody().getPosition().x);
		}
		
		
		if (fa.getUserData() != null && fa.getUserData().equals("bridge")) {
			playerOnBridge = true;
			numFootContacts++;
			currentBridge = fa.getBody();
		}
		if (fb.getUserData() != null && fb.getUserData().equals("bridge")) {
			playerOnBridge = true;
			numFootContacts++;
			currentBridge = fb.getBody();
		}

	}

	public void endContact(Contact contact) {

		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();

		if (fa == null || fb == null)
			return;

		if (fa.getUserData() != null && fa.getUserData().equals("foot")) {
			numFootContacts--;
		}
		if (fb.getUserData() != null && fb.getUserData().equals("foot")) {
			numFootContacts--;
		}

		if (fa.getUserData() != null && fa.getUserData().equals("ladder")) {
			playerClimb = false;
			Play.player.climb(false);
		}
		if (fb.getUserData() != null && fb.getUserData().equals("ladder")) {
			playerClimb = false;
			Play.player.climb(false);
		}

		if (fa.getUserData() != null
				&& fa.getUserData().equals("movingPlatform")) {
			playerOnPlatform = false;
			numFootContacts--;
		}
		if (fb.getUserData() != null
				&& fb.getUserData().equals("movingPlatform")) {
			playerOnPlatform = false;
			numFootContacts--;
		}
		
		if ( fb.getUserData() != null
				&& fb.getUserData().equals("springTop")) {
			playerOnSpring = false;
		}  
		
		if (fa.getUserData() != null
				&& fa.getUserData().equals("springTop")) {
			playerOnSpring = false;
		}
		
		if (fa.getUserData() != null && fa.getUserData().equals("tunnel")) {
			playerInTunnel = false;
			
		}
		if (fb.getUserData() != null && fb.getUserData().equals("tunnel")) {
			playerInTunnel = false;
		}
		
		if (fa.getUserData() != null && fa.getUserData().equals("bridge")) {
			playerOnBridge = false;
			numFootContacts--;
		}
		if (fb.getUserData() != null && fb.getUserData().equals("bridge")) {
			playerOnBridge = false;
			numFootContacts--;
		}

	}

	public int playerCanJump() {
		return numFootContacts;
	}

	public Array<Body> getBodies() {
		return bodiesToRemove;
	}

	public boolean isPlayerDead() {
		return playerDead;
	}

	public boolean isPlayerClimb() {
		return playerClimb;
	}

	public boolean isPlayerOnPlatform() {
		return playerOnPlatform;
	}
	public boolean isPlayerOnSpring() {
		return playerOnSpring;
	}
	
	public boolean isPlayerInTunnel(){
		return playerInTunnel;
	}
	
	public boolean isPlayerOnBridge(){
		return playerOnBridge;
	}
	
	public Body getCurrentBridge(){
		return currentBridge;
	}

	public void preSolve(Contact c, Manifold m) {
	}

	public void postSolve(Contact c, ContactImpulse ci) {
	}

}
