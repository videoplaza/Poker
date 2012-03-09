package com.videoplaza.poker.server.bot;

import com.videoplaza.poker.game.model.Game;

public class AllInBot implements Bot {

   @Override
   public BotResponse askForMove(Game game) {
      return new BotResponse("All in bot", Integer.MAX_VALUE);
   }

}
