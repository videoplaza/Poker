package com.videoplaza.poker.bot;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import com.videoplaza.poker.game.model.Card;
import com.videoplaza.poker.game.model.Suit;

public class HandValues {

   @Test
   public void test() {
      System.out.println(flopValues(Arrays.asList(new Card(Suit.HEARTS, 13), new Card(Suit.SPADES, 13))));
   }

   public static double flopValues(List<Card> hand) {
      boolean suit = false;
      suit = (hand.get(0).getSuit().equals(hand.get(1).getSuit()));
      HashMap<String, Integer> values = new HashMap<String, Integer>();

      String currentHand = handString(hand.get(0).getRank()) + handString(hand.get(1).getRank()) + ((suit) ? " s" : "");
      System.out.println(currentHand);
      values.put("AA", 1);
      values.put("KK", 1);
      values.put("QQ", 1);
      values.put("JJ", 1);
      values.put("AK s", 1);
      values.put("AQ s", 2);
      values.put("TT", 2);
      values.put("AK", 2);
      values.put("AJ s", 2);
      values.put("KQ s", 2);
      values.put("99", 3);
      values.put("AT s", 3);
      values.put("AQ", 3);
      values.put("KJ s", 3);
      values.put("88", 4);
      values.put("QJ s", 3);
      values.put("KT s", 4);
      values.put("A9 s", 5);
      values.put("AJ", 4);
      values.put("QT s", 4);
      values.put("KQ", 4);
      values.put("77", 5);
      values.put("JT s", 3);
      values.put("A8 s", 5);
      values.put("K9 s", 6);
      values.put("AT", 6);
      values.put("A5 s", 6);
      values.put("A7s", 6);
      values.put("KJ", 6);
      values.put("66", 6);
      values.put("T9 s", 7);
      values.put("A4 s", 7);
      values.put("Q9 s", 7);
      /*
      values.put("AA", 2.32);
      values.put("KK", 1.67);
      values.put("QQ", 1.22);
      values.put("JJ", 0.86);
      values.put("AK s", 0.78);
      values.put("AQ s", 0.59);
      values.put("TT", 0.58);
      values.put("AK", 0.51);
      values.put("AJ s", 0.44);
      values.put("KQ s", 0.39);
      values.put("99", 0.38);
      values.put("AT s", 0.32);
      values.put("AQ", 0.31);
      values.put("KJ s", 0.29);
      values.put("88", 0.25);
      values.put("QJ s", 0.23);
      values.put("KT s", 0.20);
      values.put("A9 s", 0.19);
      values.put("AJ", 0.19);
      values.put("QT s", 0.17);
      values.put("KQ", 0.16);
      values.put("77", 0.16);
      values.put("JT s", 0.15);
      values.put("A8 s", 0.10);
      values.put("K9 s", 0.09);
      values.put("AT", 0.08);
      values.put("A5 s", 0.08);
      values.put("A7s", 0.08);
      values.put("KJ", 0.08);
      values.put("66", 0.07);
      values.put("T9 s", 0.05);
      values.put("A4 s", 0.05);
      values.put("Q9 s", 0.05);
      values.put("J9 s", 0.04);
      values.put("QJ", 0.03);
      values.put("A6 s", 0.03);
      values.put("55", 0.02);
      values.put("A3 s", 0.02);
      values.put("K8 s", 0.01);
      values.put("KT", 0.01);
      values.put("98 s", 0.00);
      values.put("T8 s", -0.00);
      values.put("K7 s", -0.00);
      values.put("A2 s", 0.00);*/

      if (values.containsKey(currentHand))
         return values.get(currentHand);
      else
         return 8;
   }

   private static String handString(int rank) {
      if (rank < 10)
         return "" + rank;
      return new String[] { "T", "J", "Q", "K", "A" }[rank - 10];
   }
}
