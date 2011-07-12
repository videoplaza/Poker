package com.videoplaza.poker.bot;

import com.videoplaza.poker.game.model.Game;
import com.videoplaza.poker.game.model.Player;

public interface Bot {

   public String getAvatarImageUrl();

   public String getCreator();

   public String getName();

   public Bet play(Game game, Player me);

}
