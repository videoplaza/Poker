package com.videoplaza.poker.game.model;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
   private List<Card> cards = new ArrayList<Card>();
   private int topOfStack = 0;

   public Deck() {
      for (Suit suit : Suit.values()) {
         for (int i = 2; i < 14; i++)
            cards.add(new Card(suit, i));
      }
   }

   public Card deal() {
      if (topOfStack < cards.size())
         return cards.get(topOfStack++);
      return null;
   }

   public Card peek(int index) {
      return cards.get(index);
   }

   public void shuffle() {
      SecureRandom random = new SecureRandom();
      Collections.shuffle(cards, random);
   }
}
