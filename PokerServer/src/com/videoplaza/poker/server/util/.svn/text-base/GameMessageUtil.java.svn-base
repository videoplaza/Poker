package com.videoplaza.poker.server.util;

import java.util.ArrayList;

import com.videoplaza.poker.game.model.Card;
import com.videoplaza.poker.game.model.Game;
import com.videoplaza.poker.game.model.Player;

public class GameMessageUtil {
   public static Game applyPlayerPerpective(Game game, Player player) {
      Game g = new Game(game);
      for (Player p : g.getPlayers()) {
         if (player.getPosition() != p.getPosition()) {
            p.setHoleCards(new ArrayList<Card>());
            p.setBotUrl("");
         }
      }
      g.setNextPlayer(player.getPosition());
      return g;
   }
}
