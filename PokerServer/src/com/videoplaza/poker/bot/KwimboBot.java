package com.videoplaza.poker.bot;

import com.videoplaza.poker.game.model.Card;
import com.videoplaza.poker.game.model.Game;
import com.videoplaza.poker.game.model.Player;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class KwimboBot extends AbstractTournamentBot {

   private static final Random rnd = new Random(System.currentTimeMillis());
   private static final List<Integer[]> firstRaise = new ArrayList<Integer[]>() {{
      add(new Integer[]{14, 14, 0});
      add(new Integer[]{13, 13, 0});
      add(new Integer[]{14, 13, 1});
      add(new Integer[]{12, 12, 0});
      add(new Integer[]{14, 13, 0});
      add(new Integer[]{11, 11, 0});
      add(new Integer[]{14, 12, 0});
      add(new Integer[]{14, 12, 1});
      add(new Integer[]{14, 11, 0});
   }};

   private static final List<Integer[]> earlyRaise = new ArrayList<Integer[]>(){{
      addAll(firstRaise);
      add(new Integer[]{10, 10, 0});
      add(new Integer[]{14, 11, 0});
      add(new Integer[]{14, 11, 1});
      add(new Integer[]{13, 12, 1});
   }};

   private static final List<Integer[]> earlyCall = new ArrayList<Integer[]>(){{
      add(new Integer[]{13, 12, 0});
   }};


   private static final List<Integer[]> middleRaise = new ArrayList<Integer[]>(){{
      addAll(earlyRaise);
      add(new Integer[]{13, 12, 0});
      add(new Integer[]{13, 11, 1});
      add(new Integer[]{12, 11, 1});
   }};

   private static final List<Integer[]> middleCall = new ArrayList<Integer[]>(){{
      add(new Integer[]{14, 10, 1});
      add(new Integer[]{14, 10, 0});
      add(new Integer[]{13, 11, 0});
      add(new Integer[]{12, 11, 0});
      add(new Integer[]{13, 10, 1});
      add(new Integer[]{9, 9, 0});
      add(new Integer[]{8, 8, 0});
   }};

   private static final List<Integer[]> lateRaise = new ArrayList<Integer[]>(){{
      addAll(firstRaise);
      add(new Integer[]{14, 10, 1});
      add(new Integer[]{13, 10, 1});
      add(new Integer[]{13, 11, 0});
      add(new Integer[]{12, 11, 0});
      add(new Integer[]{9, 9, 0});
      add(new Integer[]{8, 8, 0});
   }};

   private static final List<Integer[]> lateCall = new ArrayList<Integer[]>(){{
      add(new Integer[]{7, 7, 0});
      add(new Integer[]{6, 6, 0});
      add(new Integer[]{5, 5, 0});
      add(new Integer[]{12, 10, 1});
      add(new Integer[]{12, 10, 0});
      add(new Integer[]{13, 10, 0});
      add(new Integer[]{11, 10, 1});
      add(new Integer[]{11, 10, 0});
      add(new Integer[]{11, 9, 1});
   }};

   @Override
   public String getAvatarImageUrl() {
      return "http://ocobiega.org/wp-content/uploads/2010/11/Poker_FaceCzyste.png";
   }

   @Override
   public String getCreator() {
      return "Daniel, Quentin, Geir";
   }

   @Override
   public String getName(HttpServletRequest req) {
      return "Kwimbo";
   }

   @Override
   public Bet play(Game game, Player me, HttpServletRequest req) {
      try {
         if (game.getCards().size() == 0) {
            return preFlop(game, me);
         }
         float handrank = getHandRank(game, me);
         System.out.println("Handrank " + handrank);
         if (handrank > 0.98f) {
            return allIn(me, "Here is all I've got");
         }else if (handrank > 0.90f) {
            return raise(game, me, game.getHighestBet() * 2, "Can't read my P p p p poker face (Mum! mum! mum! mum!)");
         }else if (handrank > 0.75f) {
            return raise(game, me, game.getHighestBet() + game.getMinimumRaise(), "I'm not in a good mood");
         }else if (handrank > 0.55f) {
            return call(game, me, "It is time to take it easy");
         }
         return checkOrFold("This is a game for losers");
      } catch (Exception e) {
         e.printStackTrace();
         return new Bet(0, "Exception");
      }
   }

   private Bet preFlop(Game game, Player me) throws Exception {
      /*Card[] cards = getMyCards(me);


      // TODO crazy containment strategies

      // Probabilistic PreFlop strategies
      int position = (getDistanceToDealer(game,me)-2) % getPlayersIn(game) ;

      if (position == 0 && me.getCurrentBet()==0) {
         //TODO check if 0 or 1
         if (contains(cards, firstRaise))
         {
            return (raise(game, me, game.getHighestBet() + game.getMinimumRaise(), ""));
         }
         return checkOrFold("");

      } else if (position <= 2 && me.getCurrentBet()==0) {
         if (contains(cards, earlyRaise)) {
            return (raise(game, me, game.getHighestBet() + game.getMinimumRaise(), ""));
         }
         else if(contains(cards, earlyCall)){
            call(game,me,"");
         }
         return checkOrFold("");

      } else if (position <= 4 && me.getCurrentBet()==0) {
         if (contains(cards, middleRaise)) {
            return (raise(game, me, game.getHighestBet() + game.getMinimumRaise(), ""));
         } else if (contains(cards, middleCall)) {
            return call(game, me, "");
         }
         return checkOrFold("");
      } else {
         if (contains(cards, lateRaise)) {
            return (raise(game, me, game.getHighestBet() + game.getMinimumRaise(), ""));
         } else if (contains(cards, lateCall)) {
            return call(game, me, "");
         }
         return checkOrFold("");
      }
*/

      Card[] cards = getMyCards(me);

      // Calculate the minimum stacksize in the game
      int minStackSize = Integer.MAX_VALUE;
      for (Player p:game.getPlayers()){
         if (p.getPosition() != me.getPosition())
            minStackSize = Math.min(minStackSize, p.getStackSize());
      }
      // All-in Strategies when clear defeat
      if (me.getStackSize() <= game.getStartStack()/10 || me.getStackSize() <= minStackSize/2){
         if((cards[0].getRank() + cards[1].getRank()) / 28f > 0.2f)
            return allIn(me,"Winning game");
      }

      if (cards[0].getRank() == cards[1].getRank() || contains(cards,firstRaise)) {
         return raise(game, me, game.getHighestBet() + 2 * game.getMinimumRaise(), "Let's have some fun");
      }
      float handrank = (cards[0].getRank() + cards[1].getRank()) / 28f;
      System.out.println("Preflop handrank " + handrank);
      if (handrank > 0.4f) {
         return call(game, me, "I'm a lumberjack!");
      }
      return checkOrFold("I am gonna take a nap");

   }

   boolean contains(Card[] ourCards, List<Integer[]> list) {
      for (Integer[] item : list) {
         if (((item[0] == ourCards[0].getRank() && item[1] == ourCards[1].getRank())
                 || (item[1] == ourCards[0].getRank() && item[0] == ourCards[1].getRank())) && item[2] == (isSuitedPair(ourCards))) {
            return true;
         }
      }
      return false;
   }

   int isSuitedPair(Card[] ourCards) {
      if (ourCards[0].getSuit() == ourCards[1].getSuit())
         return 1;
      return 0;
   }

   int getPlayersIn(Game game){
      int playersIn = 0;
      for (Player p:game.getPlayers()){
         if (p.isIn())
            playersIn++;
      }
      return playersIn;
   }

   int getDistanceToDealer(Game g, Player me){
      ArrayList<Player> listOfInPlay = new ArrayList<Player>();
      listOfInPlay.add(g.getPlayers().get(g.getDealer()));
      for (int i = g.getDealer()+1; i !=g.getDealer(); i = (i + 1) % g.getPlayers().size()){
         if(g.getPlayers().get(i).isIn()){
            listOfInPlay.add(g.getPlayers().get(i));
         }
      }
      return listOfInPlay.indexOf(me);
   }

}
