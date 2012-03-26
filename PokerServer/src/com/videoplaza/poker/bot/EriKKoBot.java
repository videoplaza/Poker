package com.videoplaza.poker.bot;

import java.util.*;
import java.math.*;

import javax.servlet.http.HttpServletRequest;

import com.videoplaza.poker.game.model.Card;
import com.videoplaza.poker.game.model.Game;
import com.videoplaza.poker.game.model.Player;

/*
 * The über-aggresive winner
 * @author Erik Odenman, Niklas Lundborg
 */
public class EriKKoBot extends AbstractTournamentBot {
	
	public static void main(String[] args) {
		System.out.println(new EriKKoBot().calculateWinChance(0.5d, 4));
	}
	
	private double CALL_LIMIT = 0.1d;
	private double RAISE_LIMIT = 0.3d;
	private double HIGH_RAISE_LIMIT = 0.5d;
	private double CALL_COST_LIMIT = 0.2d;
	private double DESPERATE_STACK_LIMIT = 0.25d;

   private static final Random rnd = new Random(System.currentTimeMillis());

   @Override
   public String getAvatarImageUrl() {
      return "http://ubuntuone.com/5AuuRI1JxZDJsYQZMSTZQp";
   }

   @Override
   public String getCreator() {
      return "ERIKKOOOO";
   }

   @Override
   public String getName(HttpServletRequest req) {
      return "ERIKKOOOO";
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
      if (r1 == r2) {
    	  return raise(game, me, game.getMinimumRaise(), "BLUFFING?"); 
      }
      double winChance = (r1 + r2) / 28d;
      System.out.println("Preflop handrank " + winChance);
      return getBet(game, me, winChance);
   }
   
   private Bet getBet(Game game, Player me, double winChance) throws Exception {
	   if (randomRaise(winChance)) {
		   return raise(game, me, game.getMinimumRaise(), getPercentage(winChance)+": I GOT NOTHING?");
	   }
       if (winChance >= HIGH_RAISE_LIMIT) {
           return raise(game, me, game.getHighestBet() * 2,getPercentage(winChance));
        } else if (winChance >= RAISE_LIMIT) {
        	return raise(game, me, game.getMinimumRaise(), getPercentage(winChance)+": BIG BLUE FTW");
        } else if (winChance >= CALL_LIMIT) {
        	return getCall(game, me, winChance);
        }
       	 return checkOrFold(getPercentage(winChance)+": NULLPONTER EXCEPTION"); 
   }
   
   private Bet getCall(Game game, Player me, double winChance) throws Exception {
	   if (safe(game, me)) {
		   return call(game, me, getPercentage(winChance)+": WHY NOT?");
	   } else {
		   return checkOrFold(getPercentage(winChance)+": DIVIDE BY ZERO");
	   }
   }
   
   private boolean safe(Game game, Player me) {
	   double percentOfTotal = game.getHighestBet()/me.getStackSize();
	   double blindPercentage = game.getBigBlind()/me.getStackSize();
	   if (percentOfTotal <= CALL_COST_LIMIT || blindPercentage > DESPERATE_STACK_LIMIT) {
		   return true;
	   } else {
		   return false;
	   }
   }
   
   private double chanceToWin(Game game, Player me) {
	   return calculateWinChance(getHandRank(game, me), opponentsIn(game, me));
   }
   
   private double calculateWinChance(double rank, int opponents) {
	   return Math.pow(rank, opponents);
   }
   
   private boolean randomRaise(double winChance) {
	   if (rnd.nextDouble()*2 < winChance) {
		   return true;
	   }
	   return false;
   }
   
   // Didn't seem to work versus the other teams
   private int opponentsIn(Game game, Player me) {
	   int cnt = 0;
	   for (Player player : game.getPlayers()) {
		   if (!player.equals(me) && player.isInPot()) {
			   cnt++;
		   }
	   }
	   return cnt;
   }
   
   private String getPercentage(double winChance) {
	   BigDecimal percentage = new BigDecimal(winChance*100).setScale(2, RoundingMode.HALF_UP);
	   return "P(win) = "+percentage+"%";
   }
}
