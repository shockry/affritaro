package com.shokry.games.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.shokry.games.states.Play;
import com.shokry.games.Game;

public class HUD {
	
	private Player player;
	
	private TextureRegion[] blocks;
	private TextureRegion crystal;
	private TextureRegion[] font;
	private TextureRegion gun;
	private TextureRegion[] healthbar;
	
	public HUD(Player player) {
		
		this.player = player;
		
		Texture gunTex = Game.res.getTexture("raygun");
		gun = new TextureRegion(gunTex, 0, 0, 70, 70);
		
		Texture tex = Game.res.getTexture("hud");
		Texture healthTex = Game.res.getTexture("healthbar");
		
		blocks = new TextureRegion[3];
		for(int i = 0; i < blocks.length; i++) {
			blocks[i] = new TextureRegion(tex, 32 + i * 16, 0, 16, 16);
		}
		
		healthbar = new TextureRegion[5];
		for(int i = 0; i < healthbar.length; i++) {
			healthbar[i] = new TextureRegion(healthTex, i * 204, 0, 204, 30);
		}
		
		crystal = new TextureRegion(tex, 80, 0, 16, 16);
		
		font = new TextureRegion[11];
		for(int i = 0; i < 6; i++) {
			font[i] = new TextureRegion(tex, 32 + i * 9, 16, 9, 9);
		}
		for(int i = 0; i < 5; i++) {
			font[i + 6] = new TextureRegion(tex, 32 + i * 9, 25, 9, 9);
		}
		
	}
	
	public void render(SpriteBatch sb) {
		
		sb.begin();
				
		//Draw gun
		if (Play.bulletNum>0){
			sb.draw(gun, 0, 190);
			drawString(sb, Play.bulletNum + " / " + "3", 55, 221);
		}
		
		// draw crystal
		sb.draw(crystal, 25, 190);
		// draw crystal amount
		drawString(sb, Play.crystalNum + " / " + player.getTotalCrystals(), 52, 195);
		
		//counter for bridge fall
		if(Play.time>0)
		drawString(sb, (int)Play.time + " / " + 3, 264, player.getPosition().y);
		
		
		//Draw health bar
		switch(Play.health){
			case 100:
				sb.draw(healthbar[0], 120, 208);
				break;
			case 80:
				sb.draw(healthbar[1], 120, 208);
				break;
			case 60:
				sb.draw(healthbar[2], 120, 208);
				break;
			case 40:
				sb.draw(healthbar[3], 120, 208);
				break;
			case 20:
				sb.draw(healthbar[4], 120, 208);
				break;
		}
		
		sb.end();
	}
	
	private void drawString(SpriteBatch sb, String s, float x, float y) {
		for(int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if(c == '/') c = 10;
			else if(c >= '0' && c <= '9') c -= '0';
			else continue;
			sb.draw(font[c], x + i * 9, y);
		}
	}
}
