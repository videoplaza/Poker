package com.videoplaza.poker.game.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Deck {
   private List<Card> cards = new ArrayList<Card>();
   private int topOfStack = 0;
   private boolean rigged = false;

   public Deck() {
      for (Suit suit : Suit.values()) {
         for (int i = 2; i < 14; i++)
            cards.add(new Card(suit, i));
      }
   }

   public Card deal() {
      if (rigged)
         return new Card(Suit.HEARTS, (int) (Math.random() + 13.5));
      if (topOfStack < cards.size())
         return cards.get(topOfStack++);
      return null;
   }

   public Card peek(int index) {
      return cards.get(index);
   }

   public void shuffle(Random rnd) {
      Collections.shuffle(cards, rnd);
   }
}
