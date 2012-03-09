package com.videoplaza.poker.bot;

import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import com.videoplaza.poker.game.model.Game;
import com.videoplaza.poker.game.model.Player;

public class ExampleBotImpl implements Bot {

   private static final Random rnd = new Random(System.currentTimeMillis());

   @Override
   public String getAvatarImageUrl() {
      return "http://disturbingauctions.com/thumbnails/froggolf.jpg";
   }

   @Override
   public String getCreator() {
      return "ExampleBot";
   }

   @Override
   public String getName(HttpServletRequest req) {
      return "Example " + rnd.nextInt(100);
   }

   @Override
   public Bet play(Game game, Player me, HttpServletRequest req) {
      if (rnd.nextInt(100) < 25) {
         return new Bet(0, "");
      }
      if (rnd.nextInt(100) > 98) {
         return new Bet(me.getStackSize(), "All in");
      }
      if (rnd.nextInt(100) > 50) {
         return new Bet(me.getStackSize(), "All in");
      }
      return new Bet(rnd.nextInt(toCall(game, me) + game.getMinimumRaise()), "Like a boss");
   }

   private int toCall(Game game, Player myself) {
      return game.getHighestBet() - myself.getCurrentBet();
   }

}
