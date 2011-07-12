package com.videoplaza.poker.server.game;

import java.util.Random;

import com.videoplaza.poker.game.model.Game;
import com.videoplaza.poker.game.model.Player;

public class MockBotUtil {

	private static Random rnd = new Random(System.currentTimeMillis());
	
	public static int getMockMove(Game game, Player player) {
		
		if(player.getHoleCards() != null){
			int sum = player.getHoleCards().get(0).getRank() + player.getHoleCards().get(1).getRank();
			if(rnd.nextInt(100) < 5){
				player.setMessage("Crazy!");
				return player.getStackSize();
			}
			if(player.getHoleCards().get(0).getRank() == player.getHoleCards().get(1).getRank()){
				player.setMessage("Like a boss!");
				return player.getStackSize();
			}
			if(sum >= 20 && game.getHighestBet() < (player.getStackSize() / 2)){
				player.setMessage("Yes.");
				return game.getMinimumRaise();
			} else if(sum > 15  && game.getHighestBet() < (player.getStackSize() / 2)){
				player.setMessage("Ok.");
				return game.getHighestBet() - player.getLastBet();
			} else {
				player.setMessage("ouch.");
				return 0;
			}
		}
		player.setMessage("What?");
		return 0;
	}
	
}
