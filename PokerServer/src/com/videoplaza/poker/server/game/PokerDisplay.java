package com.videoplaza.poker.server.game;

import com.videoplaza.poker.game.model.Game;
import com.videoplaza.poker.game.model.Player;

public interface PokerDisplay {

   public abstract void displayEvent(Game state, String event);

   public abstract void displayPlayerMove(Game state, Player previousPlayer, Player nextPlayer);

}