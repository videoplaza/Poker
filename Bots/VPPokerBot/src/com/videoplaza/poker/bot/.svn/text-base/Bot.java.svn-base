package com.videoplaza.poker.bot;

import com.videoplaza.poker.game.model.Game;

public class Bot {
	
	private static final Bot INSTANCE = new Bot();  
	
	public Bet play(Game game) {
		if (game.isTimeToBet()) {
			return new Bet(game.getHighestBet(), "Yihaa motafakkaaaaaa!");
		}
		return null;
	}
	
	public static Bot getInstance() {
		return INSTANCE;
	}

}
