package com.shokry.games.handlers;


public class GameInput {
	public static int x;
	public static int y;
	public static boolean down;
	public static boolean pdown;
	
	public static boolean[] keys;
	public static boolean[] pkeys;
	private static final int NUM_KEYS = 5;
	public static final int BTNJMP = 0;
	public static final int BTNFIRE = 1;
	public static final int BTNRIGHT = 2;
	public static final int BTNLEFT = 3;
	public static final int BTNUP = 4;
	
	static {
		keys = new boolean[NUM_KEYS];
		pkeys = new boolean[NUM_KEYS];
	}
	
	public static void update() {
		pdown = down;
		for(int i = 0; i < NUM_KEYS; i++) {
			pkeys[i] = keys[i];
		}
	}
	
	public static boolean isDown() { return down; }
	public static boolean isPressed() { return down && !pdown; }
	public static boolean isReleased() { return !down && pdown; }
	public static boolean isMvReleased(int i) { return !keys[i]; }
	public static boolean isClimbPressed(int i) { return !keys[i] && pkeys[i]; }
	
	public static void setKey(int i, boolean b) { keys[i] = b; }
	public static boolean isDown(int i) { return keys[i]; }
	public static boolean isPressed(int i) { return keys[i] && !pkeys[i]; }
}
