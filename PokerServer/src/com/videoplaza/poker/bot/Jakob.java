package com.videoplaza.poker.bot;

import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import com.videoplaza.poker.game.model.Card;
import com.videoplaza.poker.game.model.Game;
import com.videoplaza.poker.game.model.Player;
import com.videoplaza.poker.game.model.Player.Move;
import com.videoplaza.poker.game.util.PokerUtil;

public class Jakob implements Bot {
   private final Random random = new Random(System.currentTimeMillis());

   String[] sayings = { "I keep trying to lose weight....but it keeps finding me.", "Nom nom nom", "Stressed spelled backwards is desserts. Coincidence?",
         "I'm in shape. Round is a shape.", "I think I just ate my willpower.", "Ask not what you can do for your country. Ask what's for lunch.",
         "Only skinny white girls eat salads.", "We should stop at burger king for appetizers!", "LAY OFF... I'M STARVING!",
         "I went to smell it and I ate it by accident", "I'm not overweight. I'm just nine inches too short.", "...There is just more to love.",
         "...I'm just big boned.", "Do you find these tears kind of manly in a weird way?",
         "I just think if you hire Bono to hang out at your party he should let you win Wii Bowling and not embarrass you in front of your kids.",
         "Ladies, if you'd like to meet hot hunks in your area with a sizable Lego collection and type 2 diabetes may I recommend my Ford Focus.",
         "That's OK, shop around. See what else is out there. But Hulk knows you will come crawling back. Hulk positive." };

   @Override
   public String getAvatarImageUrl() {
      return "http://dl.dropbox.com/u/10219393/Screenshots/o.png";
   }

   @Override
   public String getCreator() {
      return "Jakob";
   }

   @Override
   public String getName(HttpServletRequest req) {
      Parameters p = new Parameters(req);
      return "Jakob";
   }

   @Override
   public Bet play(Game game, Player me, HttpServletRequest req) {
      Parameters parameters = new Parameters(req);
      if (game.getCards().size() == 0)
         return new Bet(playPreFlop(game, me), sayings[random.nextInt(sayings.length)]);
      int bet = (int) (normalBet(game, me) * parameters.normal + aggressiveBet(game, me) * parameters.aggressive + boringBet(game, me) * parameters.boring + crazyBet(
            game, me) * parameters.crazy);
      return new Bet(bet, "Like a bot.");

   }

   private double aggressiveBet(Game game, Player me) {
      return toCall(game, me) * 2;
   }

   private double boringBet(Game game, Player me) {
      float handRank = PokerUtil.getHandRank(me.getHoleCards(), game.getCards());

      if (handRank > .85)
         if (me.getLastMove() == Move.WAITING)
            return toCall(game, me) + game.getMinimumRaise();
         else
            return toCall(game, me);
      return 0;
   }

   /*
   Pre-Flop Basic Strategy
   A   K   Q   J   10  9   8   7   6   5   4   3   2
   A   1   1   2   2   3   5   5   5   5   5   5   5   5   A
   K   2   1   2   3   4   7   7   7   7   7   7   7   7   K
   Q   3   4   1   3   4   5   7   S   U   I   T   E   D   Q
   J   4   5   5   1   3   4   6   8   C   A   R   D   S   J
   10  6   6   6   5   2   4   5   7                       10
   9   8   8   8   7   7   3   4   5   8                   9
   8               8   8   7   4   5   6   8               8
   7                           8   5   5   6   8           7
   6                               8   5   6   7           6
   5   O   F   F   S   U   I   T       8   6   6   7       5
   4   C   A   R   D   S                   8   7   7   8   4
   3                                               7   8   3
   2                                                   7   
   */

   private double crazyBet(Game game, Player me) {
      return random.nextInt((int) (1.5 * me.getStackSize()));
   }

   private double normalBet(Game game, Player me) {
      int nextPlayer = game.getNextPlayer();
      Player myself = game.getPlayers().get(nextPlayer);

      if (game.isTimeToBet()) {

         int toCall = toCall(game, myself);

         System.out.println(myself.getHoleCards());
         if (!game.getCards().isEmpty()) { // Pre-flop
            double handRankFraction = PokerUtil.getHandRank(myself.getHoleCards(), game.getCards());
            System.out.println("Hand rank: " + handRankFraction);

            double toCallPotFraction = ((double) toCall) / ((double) game.getPotSize());
            int numRemaining = 0;
            for (Player p : game.getPlayers()) {
               if (p.isIn())
                  numRemaining++;
            }

            System.out.println(handRankFraction + " " + toCallPotFraction + " " + (1.0 / numRemaining));
            if (handRankFraction >= toCallPotFraction) {

               if (handRankFraction > 1.0 / numRemaining) {
                  // Raise
                  System.out.println("Raising" + (int) (me.getStackSize() * handRankFraction));
                  return (int) (me.getStackSize() * handRankFraction);
               } else {
                  // Call
                  System.out.println("Hand rank = " + handRankFraction + " and to call / pot = " + toCallPotFraction);
                  return toCall;
               }
            } else {
               // Fold/check
               System.out.println("Fold/check: Hand rank = " + handRankFraction + " and to call / pot = " + toCallPotFraction);
               return 0;
            }

         } else {

         }

      }
      return -1;
   }

   private int playPreFlop(Game game, Player me) {
      int toCall = toCall(game, me);
      // Preflop
      int preflop = preFlopValue(me.getHoleCards());
      System.out.println("Preflop value: " + preflop);
      if (preflop == 1) {
         System.out.println("Seriously nice hand you guys, no BS");
         return (toCall + me.getStackSize() / 4);
      }
      if (preflop == 2 && toCall < me.getStackSize() / 10) {
         System.out.println("Gimme cheap flop please");
         return toCall;
      }
      if (preflop == 3)
         return toCall;

      System.out.println("Bad cards");
      return 0;
   }

   private int toCall(Game game, Player myself) {
      return game.getHighestBet() - myself.getCurrentBet();
   }

   private static int preFlopValue(List<Card> holeCards) {
      if (holeCards.isEmpty())
         return Integer.MAX_VALUE;
      Card a = holeCards.get(0);
      Card b = holeCards.get(1);
      int high = Math.max(a.getRank(), b.getRank());
      int low = Math.min(a.getRank(), b.getRank());

      if (high == low) {
         //pair
         System.out.println("pair");
         if (high >= 13)
            return 1;
         if (high >= 7)
            return 2;
         return Integer.MAX_VALUE;
      }
      if (high == low + 1 || (high == 14 && low == 2)) {
         // connectors
         if (a.getSuit() == b.getSuit()) {
            // suited
            System.out.println("suited connectors");
            if (low == 13)
               return 1;
            if (low >= 6)
               return 2;
            return Integer.MAX_VALUE;
         } else {
            // unsuited
            System.out.println("unsuited connectors");
            if (low >= 10)
               return 2;
            return Integer.MAX_VALUE;
         }
      }
      if (a.getSuit() == b.getSuit()) {
         System.out.println("suited");

         if (high == 14)
            return 2;
         if (high == 13 && low >= 10)
            return 2;
         if (high == 12 && low >= 9)
            return 2;
         if (high == 11 && low == 9)
            return 2;
         if (high == 11 && low == 9)
            return 2;
         if (high == 10 && low == 8)
            return 2;
         if (high == 9 && low == 7)
            return 2;
      }
      if (high == 14 && low >= 11) {
         System.out.println("ace high");
         return 2;
      }
      if (low >= 10)
         return 3;
      return Integer.MAX_VALUE;
   }

   private static class Parameters {
      String name;
      double normal;
      double boring;
      double crazy;
      double aggressive;

      public Parameters(HttpServletRequest req) {

         normal = Double.parseDouble(req.getParameter("n"));
         boring = Double.parseDouble(req.getParameter("b"));
         crazy = Double.parseDouble(req.getParameter("c"));
         aggressive = Double.parseDouble(req.getParameter("a"));
         name = "" + (int) normal + ", " + (int) boring + ", " + (int) crazy + ", " + (int) aggressive;
         double sum = normal + boring + crazy + aggressive;
         normal /= sum;
         boring /= sum;
         crazy /= sum;
         aggressive /= sum;
      }

      @Override
      public String toString() {
         return name;
      }
   }

}
