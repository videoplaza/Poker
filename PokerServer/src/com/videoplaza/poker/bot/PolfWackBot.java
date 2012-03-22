package com.videoplaza.poker.bot;

import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import com.videoplaza.poker.game.model.Card;
import com.videoplaza.poker.game.model.Game;
import com.videoplaza.poker.game.model.Player;

public class PolfWackBot extends AbstractTournamentBot {

   private int activePlayers;

   private static final Random rnd = new Random(System.currentTimeMillis());

   @Override
   public String getAvatarImageUrl() {
      return "http://1.bp.blogspot.com/-Z2Rv9ZjgZfU/T11Xx7zSUYI/AAAAAAAAAwk/QS4Vgfw-6wY/s1600/I+Win+You+Lose.jpg";
   }

   @Override
   public String getCreator() {
      return "Polf Wack";
   }

   @Override
   public String getName(HttpServletRequest req) {
      return "Polf Wack";
   }

   @Override
   public Bet play(Game game, Player me, HttpServletRequest req) {

      try {
         if (game.getCards().size() == 0) {
            return preFlow(game, me);
         } else
            return turnBet(game, me);
      } catch (Exception e) {
         return new Bet(0, "Exception");
      }/*
       float handrank = getHandRank(game, me);
       System.out.println("Handrank " + handrank);
       if (handrank > 0.5f) {
         return raise(game, me, game.getHighestBet() * 2, "Good postflop handrank");
       }
       return checkOrFold("Bad postflop handrank");
       } catch (Exception e) {
       e.printStackTrace();
       return new Bet(0, "Exception");
       }*/
   }

   private void p(String s) {
      System.out.println(s);
   }

   private Bet preFlop(Game game, Player me) throws Exception {
      Card[] cards = getMyCards(me);
      if (cards[0].getRank() == cards[1].getRank()) {
         return allIn(me, "Pocket pairs");
      }
      float handrank = (cards[0].getRank() + cards[1].getRank()) / 28f;
      System.out.println("Preflop handrank " + handrank);
      if (handrank > 0.2f) {
         return call(game, me, "Good preflop hand");
      }
      return checkOrFold("Bad preflop hand");
   }

   private Bet preFlow(Game game, Player me) throws Exception {

      System.out.println("Hand: " + me.getHoleCards());

      activePlayers = -1;
      for (Player p : game.getPlayers()) {
         if (p.isInPot())
            activePlayers++;
      }
      int tier = (int) HandValues.flopValues(me.getHoleCards());
      if (tier < 2)
         return new Bet(this.toCall(game, me) + game.getMinimumRaise() * 2, "Imma woop yo' ass");
      else if (tier < 3)
         return new Bet(this.toCall(game, me), "Imma maybe woop yo' ass");
      else
         return new Bet(0, "This will be so hard!");
      /*
       double value = HandValues.flopValues(me.getHoleCards());
       int price = this.toCall(game, me);
       int bounty = game.getPotSize();
       double potValue = bounty / price;

       if (value > 0) {
          return new Bet((int) (value * value * 10 * game.getMinimumRaise() + price), "I'll win this lol");
       } else
          return new Bet(0, "you suck");*/
   }

   private Bet turnBet(Game game, Player me) throws Exception {
      double winChance = Math.pow(this.getHandRank(game, me), activePlayers);
      //double ev = game.getPotSize() * Math.pow(winChance, 3) * 2;
      double risk = this.toCall(game, me) / game.getPotSize();

      int cost = this.toCall(game, me);
      int raise = (me.getStackSize() - cost) / game.getBigBlind();

      if (risk <= winChance) {
         if (winChance > 0.75)
            return new Bet(raise + this.toCall(game, me), "This hand is incredible!");
         return new Bet(this.toCall(game, me), "I might take this!");
      }
      int price = this.toCall(game, me);

      return new Bet(0, "...");
      //return new Bet((int) ev, "I bet because I have a good hand.");
   }
}
