package com.videoplaza.poker.bot;

import java.util.Random;

import com.videoplaza.poker.game.model.Game;
import com.videoplaza.poker.game.model.Player;
import com.videoplaza.poker.game.util.PokerUtil;

public class BotImpl implements Bot {

   private String name = names[new Random(System.currentTimeMillis()).nextInt(names.length)];

   private static final String[] names = {
         "Rouzbeh", "Jocke", "Jakob", "Dante", "Alfred", "Jesper", "Erik", "Per-Anders", "Bergman", "Nic", "Pablo", "Emma", "Elin", "Alex"
   };

   @Override
   public String getAvatarImageUrl() {
      return "http://www.modernpooch.com/archives/samtheugliestdog.jpg";
   }

   @Override
   public String getCreator() {
      return "Jocke";
   }

   @Override
   public String getName() {
      return name;
   }

   @Override
   public Bet play(Game game, Player me) {
      try {
         if (me.getStackSize() == 0) {
            System.err.println("I have no money");
            return new Bet(0, "I have no money");
         }
         if (me.getStackSize() < 0) {
            System.err.println("I have less than 0 money");
            return new Bet(0, "I have less than 0 money");
         }
         if (me.getHoleCards() == null || me.getHoleCards().size() != 2) {
            System.err.println("I have no or invalid hole cards " + me.getHoleCards() == null ? null : me.getHoleCards().size());
            return new Bet(0, "I have no or invalid hole cards " + (me.getHoleCards() == null ? null : me.getHoleCards().size()));
         }
         if (game.getCards() != null && game.getCards().size() >= 3) {
            float handRank = PokerUtil.getHandRank(me.getHoleCards(), game.getCards());
            int bet = 0;
            if (handRank > 0.8) {
               bet = (int) (handRank * Math.min(me.getStackSize(), 500));
            }
            return new Bet(bet, "I have hand rank " + (int) (handRank * 100));
         }

         float handRank = me.getHoleCards().get(0).getRank() + me.getHoleCards().get(1).getRank();
         int bet;
         if (handRank > 24) {
            bet = (int) ((handRank / 28.0f) * Math.min(me.getStackSize(), 400));
         } else if (handRank > 12) {
            bet = Math.min(game.getBigBlind(), game.getHighestBet() - me.getCurrentBet());
         } else {
            bet = 0;
         }
         return new Bet(bet, "I have hole cards rank " + (int) handRank);
      } catch (Exception e) {
         e.printStackTrace();
         return new Bet(0, "Error");
      }
   }
}
