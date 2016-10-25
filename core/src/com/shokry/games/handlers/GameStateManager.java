package com.shokry.games.handlers;

import java.util.Stack;

import com.shokry.games.Game;
import com.shokry.games.states.GameState;
import com.shokry.games.states.LevelSelect;
import com.shokry.games.states.Play;

public class GameStateManager {
	
	private Game game;
	
	private Stack<GameState> gameStates;
	
	public static final int PLAY = 388031654;
	public static final int LEVEL_SELECT = -9238732;
	
	public GameStateManager(Game game) {
		this.game = game;
		gameStates = new Stack<GameState>();
		pushState(LEVEL_SELECT);
	}
	
	public void update(float dt) {
		gameStates.peek().update(dt);
	}
	
	public void render() {
		gameStates.peek().render();
	}
	
	public Game game() { return game; }
	
	private GameState getState(int state) {
		if (state == PLAY) {
			Play.crystalNum = 0;
			Play.bulletNum = 0;
			Play.time = 0;
			Play.health = 100;
			return new Play(this);
		}
		if(state == LEVEL_SELECT) return new LevelSelect(this);
		return null;
	}
	
	public void setState(int state) {
		popState();
		pushState(state);
	}
	
	public void pushState(int state) {
		gameStates.push(getState(state));
	}
	
	public void popState() {
		GameState g = gameStates.pop();
		g.dispose();
	}
	
}
