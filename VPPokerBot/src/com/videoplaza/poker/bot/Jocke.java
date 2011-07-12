package com.videoplaza.poker.bot;

import java.util.Random;

import com.videoplaza.poker.game.model.Game;
import com.videoplaza.poker.game.model.Player;

public class Jocke implements Bot {

   private static int counter = 0;

   private static final Random rnd = new Random(System.currentTimeMillis());

   @Override
   public String getAvatarImageUrl() {
      // TODO Auto-generated method stub
      return "http://www.familjemannen.se/wp-content/uploads/2011/02/Boss_Hogg.jpg";
   }

   @Override
   public String getCreator() {
      // TODO Auto-generated method stub
      return "Jocke";
   }

   @Override
   public String getName() {
      // TODO Auto-generated method stub
      return "Jocke " + rnd.nextInt(100);
   }

   @Override
   public Bet play(Game game, Player me) {
      if (rnd.nextInt(100) < 20) {
         return new Bet(0, "");
      }
      if (rnd.nextInt(100) > 90) {
         return new Bet(me.getStackSize(), "All in");
      }
      return new Bet(rnd.nextInt(me.getStackSize()), "Like a boss");
   }

}
