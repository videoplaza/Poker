package com.videoplaza.poker.server.bot;

import com.videoplaza.poker.game.model.Game;

public interface Bot {
   BotResponse askForMove(Game game);

}
