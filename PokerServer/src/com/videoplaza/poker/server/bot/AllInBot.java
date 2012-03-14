package com.videoplaza.poker.server.bot;

import com.videoplaza.poker.game.model.Game;
import com.videoplaza.poker.game.model.Player;

public class AllInBot implements Bot {

   @Override
   public BotResponse askForMove(Game game, Player player) {
      return new BotResponse("All in bot", Integer.MAX_VALUE);
   }

}
