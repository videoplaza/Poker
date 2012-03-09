package com.videoplaza.poker.server.game;

import com.videoplaza.poker.game.model.Game;
import com.videoplaza.poker.game.model.Player;

public class MockDisplay implements PokerDisplay {

   @Override
   public void displayEvent(Game state, String event) {
      System.out.print("Dealer: " + event + " ");
      if (event.contains("Dealing"))
         System.out.print(state.getCards());
      System.out.println();
   }

   @Override
   public void displayPlayerMove(Game state, Player previousPlayer, Player nextPlayer) {
      System.out.println("Player " + previousPlayer.getName() + ": " + previousPlayer.getLastMove());
   }

}
