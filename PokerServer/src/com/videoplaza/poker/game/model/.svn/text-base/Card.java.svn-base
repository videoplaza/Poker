package com.videoplaza.poker.game.model;

public class Card {
   private Suit suit;
   private int rank;

   public Card(Suit suit, int rank) {
      this.suit = suit;
      this.rank = rank;
   }

   @Override
   public boolean equals(Object other) {
      if (other == null)
         return false;
      if (!(other instanceof Card))
         return false;
      Card otherCard = (Card) other;
      return (suit == otherCard.suit && rank == otherCard.rank);
   }

   public int getRank() {
      return rank;
   }

   public Suit getSuit() {
      return suit;
   }

   @Override
   public int hashCode() {
      return suit.ordinal() * 20 + rank;
   }

   @Override
   public String toString() {
      return rankAsString() + suit.getSymbol();
   }

   private String rankAsString() {
      if (rank <= 10)
         return Integer.toString(rank);
      if (rank == 11)
         return "J";
      if (rank == 12)
         return "Q";
      if (rank == 13)
         return "K";
      return "A";
   }
}
