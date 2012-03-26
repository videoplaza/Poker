package com.videoplaza.poker.bot;

import java.util.*;
import java.math.*;

import javax.servlet.http.HttpServletRequest;

import com.videoplaza.poker.game.model.Card;
import com.videoplaza.poker.game.model.Game;
import com.videoplaza.poker.game.model.Player;
import com.videoplaza.poker.game.model.Player.Move;

/*
 * More calculating bot
 * @author Erik Odenman
 */
public class EriKKoBot2 extends AbstractTournamentBot {
	
	private static final double AGGRESSION_FACTOR = 0.66d;

   private static Move[] aggressive = new Move[] {Move.ALL_IN, Move.RAISE_ALL_IN};
   private static Move[] pseudo_aggressive = new Move[] {Move.ALL_IN, Move.RAISE_ALL_IN, Move.RAISE, Move.BET};

   @Override
   public String getAvatarImageUrl() {
      return "http://ubuntuone.com/5AuuRI1JxZDJsYQZMSTZQp";
   }

   @Override
   public String getCreator() {
      return "Erik";
   }

   @Override
   public String getName(HttpServletRequest req) {
      return "ERIKKOOOO2";
   }

   @Override
   public Bet play(Game game, Player me, HttpServletRequest req) {
      try {
         if (game.getCards().size() == 0) {
            return preFlop(game, me);
         }
         double winChance = chanceToWin(game, me);
         System.out.println("Winchance " + winChance);
         return getBet(game, me, winChance);
      } catch (Exception e) {
         e.printStackTrace();
         return new Bet(0, "Exception");
      }
   }

   private Bet preFlop(Game game, Player me) throws Exception {
      Card[] cards = getMyCards(me);
      int r1 = cards[0].getRank();
      int r2 = cards[1].getRank();
      /*
      if (r1 == r2 && r1 >= 10) {
    	  return raise(game, me, game.getMinimumRaise(), "BLUFFING?"); 
      }
      */
      double rank = (r1 + r2) / 28d;
      System.out.println("Preflop handrank " + rank);
      double winChance = rank;
      if (r1 != r2) {
    	  winChance *= rank;
      } 
      winChance *= Math.pow(AGGRESSION_FACTOR, numMoves(game, me, aggressive));
      return getBet(game, me, winChance);
   }
   
   private Bet getBet(Game game, Player me, double winChance) throws Exception {
	   double expectedFold = me.getStackSize();
	   double expectedCall = expectedValueFromCall(game, me, winChance);
	   int smallRaiseValue = game.getMinimumRaise();
	   double expectedSmallRaise = expectedValueFromRaise(game, me, winChance, smallRaiseValue);
	   int bigRaiseValue = game.getHighestBet()*2;
	   double expectedBigRaise = expectedValueFromRaise(game, me, winChance, bigRaiseValue);
	   System.out.println("chance: "+winChance+", fold: "+expectedFold+", call: "+expectedCall+", small: "+expectedSmallRaise+", big: "+expectedBigRaise);
	   if (expectedBigRaise >= expectedSmallRaise && expectedBigRaise >= expectedCall && expectedBigRaise >= expectedFold) {
		   return raise(game, me, bigRaiseValue, getPercentage(winChance)+": BIG BLUE FTW");
	   } else if (expectedSmallRaise >= expectedCall && expectedSmallRaise >= expectedFold) {
		   return raise(game, me, smallRaiseValue, getPercentage(winChance)+": BIG BLUE FTW");
	   } else if (expectedCall >= expectedFold) {
		   return call(game, me, getPercentage(winChance)+": WHY NOT?");
	   } else {
		   return checkOrFold(getPercentage(winChance)+": NULLPONTER EXCEPTION");
	   }
   }
   
   private double chanceToWin(Game game, Player me) {
	   return calculateWinChance(getHandRank(game, me), opponentsInPot(game, me))*Math.pow(AGGRESSION_FACTOR, numMoves(game, me, pseudo_aggressive));
   }
   
   private double calculateWinChance(double rank, int opponents) {
	   return Math.pow(rank, opponents);
   }
   
   private double expectedValueFromRaise(Game game, Player me, double winChance, double raise) {
	   double total = Math.min(me.getStackSize(), raise + toCall(game, me));
	   double winSize = game.getPotSize() + me.getStackSize() + raise;
	   double loseSize = me.getStackSize() - total;
	   return winSize*winChance + loseSize*(1-winChance);
   }
   
   private double expectedValueFromCall(Game game, Player me, double winChance) {
	   double winSize = game.getPotSize() + me.getStackSize();
	   double loseSize = Math.max(0, me.getStackSize() - toCall(game, me));
	   return winSize*winChance + loseSize*(1-winChance);
   }
   
   private int opponentsInPot(Game game, Player me) {
	   int cnt = 0;
	   for (Player player : game.getPlayers()) {
		   if (player.isInPot()) {
			   cnt++;
		   }
	   }
	   cnt--; // Don't count self
	   return Math.max(0, cnt);
   }
   
   private int numMoves(Game game, Player me, Move... moves) {
	   int cnt = 0;
	   for (Player player : game.getPlayers()) {
		   Move m = player.getLastMove();
		   for (int i = 0; i < moves.length; i++) {
			   if (m == moves[i]) {
				   cnt++; break;
			   }
		   }
	   }
	   return cnt;
   }
   
   private String getPercentage(double winChance) {
	   BigDecimal percentage = new BigDecimal(winChance*100).setScale(2, RoundingMode.HALF_UP);
	   return "P(win) = "+percentage+"%";
   }
}
