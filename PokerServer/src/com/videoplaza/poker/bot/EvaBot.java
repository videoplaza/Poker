package com.videoplaza.poker.bot;

import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import com.videoplaza.poker.game.model.Card;
import com.videoplaza.poker.game.model.Game;
import com.videoplaza.poker.game.model.Player;

public class YourBot extends AbstractTournamentBot {

	private static String[] Messages = { "WALL-EEEE", "EV-AAAAA", "1001011011" };
	
   private static final Random rnd = new Random(System.currentTimeMillis());

   @Override
   public String getAvatarImageUrl() {
      return "http://www.dan-dare.org/FreeFun/Games/CartoonsMoviesTV/WALL-E7/images/fullscale/eve_2.png";
   }

   @Override
   public String getCreator() {
      return "Mr.Purple";
   }

   @Override
   public String getName(HttpServletRequest req) {
      return "E-VA";
   }

   @Override
   public Bet play(Game game, Player me, HttpServletRequest req) {
      try {
    	  
         if (game.getCards().size() == 0) {
            return preFlop(game, me);
         }
         
         float minimumCallRequired = game.getHighestBet() - me.getCurrentBet();
         System.out.println("Highest bet " + game.getHighestBet());
         System.out.println("Current bet " + me.getCurrentBet());
         
         float handrank = getHandRank(game, me);
         System.out.println("Handrank " + handrank);
         if(numPlaying(game) >= 5) {
        	 if(handrank >= 0.95) {
        		 return allIn(me, "Youre going down");
        	 }
        	 if(handrank >= 0.8) {
        		 return raise(game, me, game.getHighestBet() * 3, "Come at me");
        	 }
        	 if(handrank >= 0.7) {
        		 return raise(game, me, game.getHighestBet() * 2, "Come at me");
        	 }
        	 if(handrank >= 0.6) {
        		 return raise(game, me, game.getHighestBet(), "Come at me");
        	 }
	         if(handrank <= 0.3 || minimumCallRequired/handrank >= game.getSmallBlind() * 7) {
	        	 return checkOrFold("WALL-EEEE");
	        	 
	         } else {
	        	 return call(game, me, "Well, hello there.");
	        	 
	         }
         } else {
	        	 if(handrank >= 0.95) {
	        		 return allIn(me, "Youre going down");
	        	 }
            	 if(handrank >= 0.7) {
            		 return raise(game, me, game.getHighestBet() * 3, "Come at me");
            	 }
            	 if(handrank >= 0.65) {
            		 return raise(game, me, game.getHighestBet() * 2, "Come at me");
            	 }
            	 if(handrank >= 0.6) {
            		 return raise(game, me, game.getHighestBet(), "Come at me");
            	 }
    	         if(handrank <= 0.2 || minimumCallRequired/handrank >= game.getSmallBlind() * 7) {
    	        	 System.out.println("Fold");
    	        	 return checkOrFold("WALL-EEEE");
    	        	 
    	         } else {
    	        	 System.out.println("Call");
    	        	 return call(game, me, "Well, hello there.");
    	        	 
    	         }
         }
         
         /*if (handrank > 0.5f ) {
            return raise(game, me, game.getHighestBet() * 2, "Good postflop handrank");
         }
         return checkOrFold("Bad postflop handrank");*/
      } catch (Exception e) {
         e.printStackTrace();
         return new Bet(0, "Exception");
      }
   }

   private Bet preFlop(Game game, Player me) throws Exception {
      Card[] cards = getMyCards(me);
      
      int handRank = preFlopRank(cards[0], cards[1]);
      
      System.out.println("Preflop handrank " + handRank);
      System.out.println("Num playrs; " + game.getPlayers().size());
      //return call(game, me, "");
      if(numPlaying(game) >= 5) {
    	  if(handRank <= 2) { return raise(game, me, game.getHighestBet() * 3, "Im feeling good");}
    	  if(handRank <= 7) { return call(game, me, "Im in");}
    	  
      } else {
    	  if(handRank <= 3) { return raise(game, me, game.getHighestBet() * 3, "Im feeling good");}
    	  if(handRank <= 7) { return call(game, me, "Im in"); }
      }
      
      return checkOrFold("Next time.");
      
      /*if (cards[0].getRank() == cards[1].getRank()) {
         return allIn(me, "Pocket pairs");
      }
      float handrank = (cards[0].getRank() + cards[1].getRank()) / 28f;
      System.out.println("Preflop handrank " + handrank);
      if (handrank > 0.2f) {
         return call(game, me, "Good preflop hand");
      }
      return checkOrFold("Bad preflop hand");*/
   }
   private boolean eq(Card card1, Card card2) {
	 return (card1.getRank() == card2.getRank());
   }
   private boolean su(Card card1, Card card2 )
   {
	   return (card1.getSuit()==card2.getSuit());
   }
   
   private int numPlaying(Game game) {
	   int result = 0;
	   for(Player player : game.getPlayers()) {
		   if(player.isInPot()) {
			   result++;
		   }
	   }
	   return result;
   }
   
   private int preFlopRank(Card card1, Card card2) {
	   if(eq(card1, card2)) {
		   int rank = card1.getRank();
		   
		   if(rank >= 11) return 1;
		   if(rank == 10) return 2;
		   if(rank == 9) return 3;
		   if(rank == 8) return 4;
		   if(rank >= 5) return 5;
		   return 7;
	   }
	   
	   if (su(card1, card2)) {
		  int rank1 = card1.getRank();
		  int rank2 = card2.getRank();
		   if(rank1 >= 13 && rank2 >= 13) return 1;
		   if(rank1 + rank2 >= 25) return 2;
		   if(rank1 + rank2 >= 21) return 3;
		   if(rank1 + rank2 >= 19) return 4;
		   if(rank1 + rank2 >= 17) return 5;
	   }
	   
	   return 99;
   }
}
