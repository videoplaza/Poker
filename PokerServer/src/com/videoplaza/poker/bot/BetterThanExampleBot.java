package com.videoplaza.poker.bot;

import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import com.videoplaza.poker.game.model.Card;
import com.videoplaza.poker.game.model.Game;
import com.videoplaza.poker.game.model.Player;

public class BetterThanExampleBot extends AbstractTournamentBot {

   private static final Random rnd = new Random();

   @Override
   public String getAvatarImageUrl() {
      return "http://disturbingauctions.com/thumbnails/froggolf.jpg";
   }

   @Override
   public String getCreator() {
      return "Example";
   }

   @Override
   public String getName(HttpServletRequest req) {
      return "Example";
   }

   @Override
   public Bet play(Game game, Player me, HttpServletRequest req) {
      try {
         if (game.getCards().isEmpty()) {
            return preFlop(game, me);
         }
         
         int maxPotSize = maxPotSize(game, me);
         
         if (game.getPotSize() < maxPotSize / 2) {
        	 int targetPot = maxPotSize / 2;
        	 int toRaise = targetPot - game.getPotSize();
        	 toRaise = Math.max(game.getMinimumRaise(), toRaise);
        	 return raise(game, me, toRaise, "");
         }
         
         if (game.getPotSize() < maxPotSize)
        	 return call(game, me, "");
         
         return checkOrFold("");
         
      } catch (Exception e) {
         e.printStackTrace();
         return new Bet(0, "Exception");
      }
   }

   private Bet preFlop(Game game, Player me) throws Exception {
      
	   int maxPotSize = maxPotSize(game, me);
	   
	   if (game.getPotSize() > maxPotSize)
		   return checkOrFold("I got a hang over.");
	   
	   Card[] cards = getMyCards(me);

	   int rMax = Math.max(cards[0].getRank(), cards[1].getRank());
	   int rMin = Math.min(cards[0].getRank(), cards[1].getRank());
		
	   
	   if (cards[0].getRank() == cards[1].getRank()) {
           return call(game, me, "Hmm.");
       }
	   
	   boolean sameColor = cards[0].getSuit() == cards[1].getSuit();
	   
	   if (rMax == 14 && sameColor)
		   return call(game, me, "");
	   
	   if (10 <= rMax && rMax <= 13 && 10 <= rMin && rMin <= 13 && sameColor)
		   return call(game, me, "");
	   
	   if (7 <= rMax && rMax <= 11 && 7 <= rMin && rMin <= 11 && sameColor)
		   return call(game, me, "");
	   
	   if (6 <= rMax && rMax <= 10 && 6 <= rMin && rMin <= 10 && sameColor)
		   return call(game, me, "");
	   
	   if (12 <= rMax && rMax <= 14 && 12 <= rMin && rMin <= 14)
		   return call(game, me, "");
	   
	   if (11 <= rMax && rMax <= 13 && 11 <= rMin && rMin <= 13)
		   return call(game, me, "");
	   
	   return checkOrFold("");
          
   }
   
   
   private int maxPotSize(Game game, Player me) {

	   int ourBalance = me.getStackSize();
	   
	   double handRank = 0;
	   
	   if (game.getCards().isEmpty()) {
		   handRank = 0.25;
	   } else
		   handRank = getHandRank(game, me);
	   
	   return (int) (handRank * ourBalance / 2);
   }
   
   
}
