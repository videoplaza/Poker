package com.videoplaza.poker.server.bot;

import com.videoplaza.poker.game.model.Game;
import com.videoplaza.poker.game.model.Player;

public interface Bot {
   BotResponse askForMove(Game game, Player player);

}
