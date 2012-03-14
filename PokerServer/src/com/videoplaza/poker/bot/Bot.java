package com.videoplaza.poker.bot;

import javax.servlet.http.HttpServletRequest;

import com.videoplaza.poker.game.model.Game;
import com.videoplaza.poker.game.model.Player;

public interface Bot {

   public String getAvatarImageUrl();

   public String getCreator();

   public String getName(HttpServletRequest req);

   public Bet play(Game game, Player me, HttpServletRequest req);

}
