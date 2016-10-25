package com.shokry.games.handlers;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input.Keys;

public class GameInputProcessor extends InputAdapter {
	
	public boolean mouseMoved(int x, int y) {
		GameInput.x = x;
		GameInput.y = y;
		return true;
	}
	
	public boolean touchDragged(int x, int y, int pointer) {
		GameInput.x = x;
		GameInput.y = y;
		GameInput.down = true;
		return true;
	}
	
	public boolean touchDown(int x, int y, int pointer, int button) {
		GameInput.x = x;
		GameInput.y = y;
		GameInput.down = true;
		return true;
	}
	
	public boolean touchUp(int x, int y, int pointer, int button) {
		GameInput.x = x;
		GameInput.y = y;
		GameInput.down = false;
		return true;
	}
	
	public boolean keyDown(int k) {
		if(k == Keys.CONTROL_LEFT) GameInput.setKey(GameInput.BTNJMP, true);
		if(k == Keys.RIGHT) GameInput.setKey(GameInput.BTNRIGHT, true);
		if(k == Keys.LEFT) GameInput.setKey(GameInput.BTNLEFT, true);
		if(k == Keys.UP) GameInput.setKey(GameInput.BTNUP, true);
		if(k == Keys.SPACE) GameInput.setKey(GameInput.BTNFIRE, true);
		return true;
	}
	
	public boolean keyUp(int k) {
		if(k == Keys.CONTROL_LEFT) GameInput.setKey(GameInput.BTNJMP, false);
		if(k == Keys.RIGHT) GameInput.setKey(GameInput.BTNRIGHT, false);
		if(k == Keys.LEFT) GameInput.setKey(GameInput.BTNLEFT, false);
		if(k == Keys.UP) GameInput.setKey(GameInput.BTNUP, false);
		if(k == Keys.SPACE) GameInput.setKey(GameInput.BTNFIRE, false);
		return true;
	}
}
