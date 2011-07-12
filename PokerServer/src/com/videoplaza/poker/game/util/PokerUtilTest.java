package com.videoplaza.poker.game.util;

import static ch.lambdaj.Lambda.joinFrom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.videoplaza.poker.game.model.Card;
import com.videoplaza.poker.game.model.Deck;
import com.videoplaza.poker.game.model.Suit;

public class PokerUtilTest {
   /**
    * @param args
   */
   public static void main(String[] args) {
      List<Card> cards = new ArrayList<Card>();
      cards.add(new Card(Suit.SPADES, 3));
      cards.add(new Card(Suit.SPADES, 14));
      cards.add(new Card(Suit.SPADES, 4));
      cards.add(new Card(Suit.DIAMONDS, 8));
      cards.add(new Card(Suit.SPADES, 5));
      cards.add(new Card(Suit.CLUBS, 12));
      cards.add(new Card(Suit.SPADES, 2));
      System.out.println(joinFrom(cards) + " is worth " + PokerUtil.bestHand(cards));
      cards.clear();
      cards.add(new Card(Suit.SPADES, 3));
      cards.add(new Card(Suit.SPADES, 14));
      cards.add(new Card(Suit.SPADES, 7));
      cards.add(new Card(Suit.DIAMONDS, 8));
      cards.add(new Card(Suit.SPADES, 5));
      cards.add(new Card(Suit.CLUBS, 12));
      cards.add(new Card(Suit.SPADES, 2));
      System.out.println(joinFrom(cards) + " is worth " + PokerUtil.bestHand(cards));
      cards.clear();
      cards.add(new Card(Suit.SPADES, 3));
      cards.add(new Card(Suit.SPADES, 14));
      cards.add(new Card(Suit.DIAMONDS, 4));
      cards.add(new Card(Suit.CLUBS, 8));
      cards.add(new Card(Suit.SPADES, 5));
      cards.add(new Card(Suit.CLUBS, 12));
      cards.add(new Card(Suit.SPADES, 2));
      System.out.println(joinFrom(cards) + " is worth " + PokerUtil.bestHand(cards));
      cards.clear();
      cards.add(new Card(Suit.SPADES, 3));
      cards.add(new Card(Suit.SPADES, 14));
      cards.add(new Card(Suit.DIAMONDS, 3));
      cards.add(new Card(Suit.CLUBS, 3));
      cards.add(new Card(Suit.SPADES, 3));
      cards.add(new Card(Suit.CLUBS, 12));
      cards.add(new Card(Suit.SPADES, 2));
      System.out.println(joinFrom(cards) + " is worth " + PokerUtil.bestHand(cards));
      cards.clear();
      cards.add(new Card(Suit.SPADES, 13));
      cards.add(new Card(Suit.SPADES, 10));
      cards.add(new Card(Suit.DIAMONDS, 13));
      cards.add(new Card(Suit.CLUBS, 13));
      cards.add(new Card(Suit.SPADES, 9));
      cards.add(new Card(Suit.CLUBS, 7));
      cards.add(new Card(Suit.SPADES, 13));
      System.out.println(joinFrom(cards) + " is worth " + PokerUtil.bestHand(cards));
      cards.clear();
      cards.add(new Card(Suit.SPADES, 13));
      cards.add(new Card(Suit.SPADES, 10));
      cards.add(new Card(Suit.DIAMONDS, 13));
      cards.add(new Card(Suit.CLUBS, 13));
      cards.add(new Card(Suit.SPADES, 9));
      cards.add(new Card(Suit.CLUBS, 7));
      cards.add(new Card(Suit.SPADES, 7));
      System.out.println(joinFrom(cards) + " is worth " + PokerUtil.bestHand(cards));
      cards.clear();
      cards.add(new Card(Suit.SPADES, 3));
      cards.add(new Card(Suit.SPADES, 10));
      cards.add(new Card(Suit.DIAMONDS, 3));
      cards.add(new Card(Suit.CLUBS, 3));
      cards.add(new Card(Suit.SPADES, 9));
      cards.add(new Card(Suit.CLUBS, 9));
      cards.add(new Card(Suit.SPADES, 13));
      System.out.println(joinFrom(cards) + " is worth " + PokerUtil.bestHand(cards));
      cards.clear();
      cards.add(new Card(Suit.SPADES, 3));
      cards.add(new Card(Suit.SPADES, 10));
      cards.add(new Card(Suit.DIAMONDS, 3));
      cards.add(new Card(Suit.CLUBS, 3));
      cards.add(new Card(Suit.SPADES, 7));
      cards.add(new Card(Suit.CLUBS, 9));
      cards.add(new Card(Suit.SPADES, 13));
      System.out.println(joinFrom(cards) + " is worth " + PokerUtil.bestHand(cards));
      cards.clear();
      cards.add(new Card(Suit.SPADES, 13));
      cards.add(new Card(Suit.SPADES, 10));
      cards.add(new Card(Suit.DIAMONDS, 13));
      cards.add(new Card(Suit.CLUBS, 13));
      cards.add(new Card(Suit.SPADES, 9));
      cards.add(new Card(Suit.CLUBS, 8));
      cards.add(new Card(Suit.SPADES, 7));
      System.out.println(joinFrom(cards) + " is worth " + PokerUtil.bestHand(cards));
      cards.clear();
      cards.add(new Card(Suit.SPADES, 5));
      cards.add(new Card(Suit.SPADES, 3));
      cards.add(new Card(Suit.DIAMONDS, 5));
      cards.add(new Card(Suit.CLUBS, 5));
      cards.add(new Card(Suit.SPADES, 4));
      cards.add(new Card(Suit.CLUBS, 2));
      cards.add(new Card(Suit.SPADES, 13));
      System.out.println(joinFrom(cards) + " is worth " + PokerUtil.bestHand(cards));
      cards.clear();
      cards.add(new Card(Suit.SPADES, 5));
      cards.add(new Card(Suit.SPADES, 3));
      cards.add(new Card(Suit.DIAMONDS, 5));
      cards.add(new Card(Suit.CLUBS, 3));
      cards.add(new Card(Suit.SPADES, 4));
      cards.add(new Card(Suit.CLUBS, 2));
      cards.add(new Card(Suit.SPADES, 13));
      System.out.println(joinFrom(cards) + " is worth " + PokerUtil.bestHand(cards));
      cards.clear();
      cards.add(new Card(Suit.SPADES, 5));
      cards.add(new Card(Suit.SPADES, 3));
      cards.add(new Card(Suit.DIAMONDS, 5));
      cards.add(new Card(Suit.CLUBS, 13));
      cards.add(new Card(Suit.SPADES, 4));
      cards.add(new Card(Suit.CLUBS, 2));
      cards.add(new Card(Suit.SPADES, 13));
      System.out.println(joinFrom(cards) + " is worth " + PokerUtil.bestHand(cards));
      cards.clear();
      cards.add(new Card(Suit.SPADES, 2));
      cards.add(new Card(Suit.SPADES, 3));
      cards.add(new Card(Suit.DIAMONDS, 2));
      cards.add(new Card(Suit.SPADES, 4));
      cards.add(new Card(Suit.CLUBS, 5));
      System.out.println(joinFrom(cards) + " is worth " + PokerUtil.bestHand(cards));
      cards.clear();
      cards.add(new Card(Suit.SPADES, 14));
      cards.add(new Card(Suit.SPADES, 12));
      cards.add(new Card(Suit.DIAMONDS, 11));
      cards.add(new Card(Suit.CLUBS, 13));
      cards.add(new Card(Suit.SPADES, 9));
      cards.add(new Card(Suit.CLUBS, 2));
      cards.add(new Card(Suit.SPADES, 8));
      System.out.println(joinFrom(cards) + " is worth " + PokerUtil.bestHand(cards));
      cards.clear();

      cards.add(new Card(Suit.SPADES, 9));
      cards.add(new Card(Suit.CLUBS, 2));
      cards.add(new Card(Suit.CLUBS, 9));

      List<Card> holeCards = new ArrayList<Card>();
      holeCards.add(new Card(Suit.SPADES, 2));
      holeCards.add(new Card(Suit.DIAMONDS, 9));
      System.out.println("Rank of your hand is " + PokerUtil.getHandRank(holeCards, cards));
      {
         // Test case from http://www.youtube.com/watch?v=AKaruHWIdh4 @ 4:21
         List<List<Card>> players = new ArrayList<List<Card>>();
         cards.clear();
         cards.add(new Card(Suit.DIAMONDS, 2));
         cards.add(new Card(Suit.DIAMONDS, 11));
         cards.add(new Card(Suit.DIAMONDS, 7));
         List<Card> player1 = new ArrayList<Card>();
         player1.add(new Card(Suit.HEARTS, 5));
         player1.add(new Card(Suit.HEARTS, 6));
         List<Card> player2 = new ArrayList<Card>();
         player2.add(new Card(Suit.DIAMONDS, 12));
         player2.add(new Card(Suit.DIAMONDS, 13));
         List<Card> player3 = new ArrayList<Card>();
         player3.add(new Card(Suit.CLUBS, 13));
         player3.add(new Card(Suit.CLUBS, 2));

         players.add(player1);
         players.add(player2);
         players.add(player3);

         double[] percent = PokerUtil.getProbabilities(players, cards);
         System.out.println("Chances are: " + Arrays.toString(percent));
      }
      {
         // Test case from http://www.youtube.com/watch?v=AKaruHWIdh4 @ 8:12
         List<List<Card>> players = new ArrayList<List<Card>>();
         cards.clear();
         cards.add(new Card(Suit.CLUBS, 7));
         cards.add(new Card(Suit.DIAMONDS, 4));
         cards.add(new Card(Suit.CLUBS, 2));
         List<Card> player1 = new ArrayList<Card>();
         player1.add(new Card(Suit.SPADES, 11));
         player1.add(new Card(Suit.HEARTS, 11));
         List<Card> player2 = new ArrayList<Card>();
         player2.add(new Card(Suit.SPADES, 12));
         player2.add(new Card(Suit.HEARTS, 12));

         players.add(player1);
         players.add(player2);

         double[] percent = PokerUtil.getProbabilities(players, cards);
         System.out.println("Chances are: " + Arrays.toString(percent));
      }
      {
         // Test case from http://www.youtube.com/watch?v=WO8k47_lP0M @ 0:49
         List<List<Card>> players = new ArrayList<List<Card>>();
         cards.clear();
         cards.add(new Card(Suit.SPADES, 2));
         cards.add(new Card(Suit.HEARTS, 5));
         cards.add(new Card(Suit.DIAMONDS, 14));
         List<Card> player1 = new ArrayList<Card>();
         player1.add(new Card(Suit.HEARTS, 13));
         player1.add(new Card(Suit.HEARTS, 14));
         List<Card> player2 = new ArrayList<Card>();
         player2.add(new Card(Suit.CLUBS, 14));
         player2.add(new Card(Suit.SPADES, 11));
         List<Card> player3 = new ArrayList<Card>();
         player3.add(new Card(Suit.DIAMONDS, 2));
         player3.add(new Card(Suit.CLUBS, 2));

         players.add(player1);
         players.add(player2);
         players.add(player3);

         double[] percent = PokerUtil.getProbabilities(players, cards);
         System.out.println("Chances are: " + Arrays.toString(percent));
      }

      System.out.println("Dealing random deck:");
      Deck deck = new Deck();
      deck.shuffle();
      Card card;
      while ((card = deck.deal()) != null) {
         System.out.print(card.toString() + " ");
      }
      System.out.println();
      /*{
         // Test case from http://www.youtube.com/watch?v=WO8k47_lP0M @ 0:33
         List<List<Card>> players = new ArrayList<List<Card>>();
         cards.clear();
         List<Card> player1 = new ArrayList<Card>();
         player1.add(new Card(Suit.HEARTS, 13));
         player1.add(new Card(Suit.HEARTS, 14));
         List<Card> player2 = new ArrayList<Card>();
         player2.add(new Card(Suit.CLUBS, 14));
         player2.add(new Card(Suit.SPADES, 11));
         List<Card> player3 = new ArrayList<Card>();
         player3.add(new Card(Suit.DIAMONDS, 2));
         player3.add(new Card(Suit.CLUBS, 2));

         players.add(player1);
         players.add(player2);
         players.add(player3);

         double[] percent = PokerUtil.getProbabilities(players, cards);
         System.out.println("Chances are: " + Arrays.toString(percent));
      }*/
   }
}
