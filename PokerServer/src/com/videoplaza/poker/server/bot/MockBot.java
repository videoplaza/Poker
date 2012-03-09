package com.videoplaza.poker.server.bot;

import com.videoplaza.poker.game.model.Game;
import com.videoplaza.poker.game.model.Player;

public class MockBot implements Bot {

   final Player player;

   public MockBot(Player player) {
      this.player = player;
   }

   @Override
   public BotResponse askForMove(Game game) {
      return new BotResponse("", getMockMove(game, player));
   }

   public int getMockMove(Game game, Player player) {
      if (player.getHoleCards() != null) {
         int sum = player.getHoleCards().get(0).getRank() + player.getHoleCards().get(1).getRank();
         if (player.getHoleCards().get(0).getRank() == player.getHoleCards().get(1).getRank()) {
            player.setMessage("Like a boss!");
            return player.getStackSize();
         }
         if (sum >= 20 && game.getHighestBet() < (player.getStackSize() / 2)) {
            player.setMessage("Yes.");
            return toCall(game, player) + game.getMinimumRaise();
         } else if (sum > 15 && game.getHighestBet() < (player.getStackSize() / 2)) {
            player.setMessage("Ok.");
            return toCall(game, player);
         } else {
            player.setMessage("ouch.");
            return 0;
         }
      }
      player.setMessage("What?");
      return 0;
   }

   private int toCall(Game game, Player player) {
      return game.getHighestBet() - player.getLastBet();
   }
}
