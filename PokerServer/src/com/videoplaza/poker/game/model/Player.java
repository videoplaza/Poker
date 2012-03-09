package com.videoplaza.poker.game.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.videoplaza.poker.server.bot.Bot;
import com.videoplaza.poker.server.bot.MockBot;
import com.videoplaza.poker.server.bot.NetworkBot;

public class Player {

   private String botUrl;

   private int position;
   private String name;
   private String author;
   private String pictureUrl;

   private int stackSize;
   private int currentBet;
   private int lastBet;

   private boolean isIn;

   private List<Card> holeCards = new ArrayList<Card>();

   private String message;

   private Move lastMove;

   private boolean isMockBot;

   public Player() {
   }

   public Player(Player copy) {
      botUrl = copy.botUrl;
      position = copy.position;
      name = copy.name;
      author = copy.author;
      pictureUrl = copy.pictureUrl;
      stackSize = copy.stackSize;
      currentBet = copy.currentBet;
      isIn = copy.isIn;

      if (copy.holeCards != null)
         holeCards = new ArrayList<Card>(copy.holeCards);
      message = copy.message;

      lastMove = copy.lastMove;
      lastBet = copy.lastBet;
   }

   public boolean canContinue() {
      Move[] moves = new Move[] { Move.BIG_BLIND, Move.CALL, Move.CHECK, Move.RAISE, Move.SMALL_BLIND, Move.WAITING, Move.BET };
      return Arrays.asList(moves).contains(lastMove);
   }

   public void decreaseStackSize(int amount) {
      amount = Math.max(0, amount);
      stackSize -= amount;
   }

   public String getAuthor() {
      return author;
   }

   public Bot getBot() {
      if (isMockBot())
         return new MockBot(this);
      return new NetworkBot(this);
   }

   public String getBotUrl() {
      return botUrl;
   }

   public int getCurrentBet() {
      return currentBet;
   }

   public List<Card> getHoleCards() {
      return holeCards;
   }

   public int getLastBet() {
      return lastBet;
   }

   public Move getLastMove() {
      return lastMove;
   }

   public String getMessage() {
      return message;
   }

   public String getName() {
      return name;
   }

   public String getPictureUrl() {
      return pictureUrl;
   }

   public int getPosition() {
      return position;
   }

   public int getStackSize() {
      return stackSize;
   }

   public void increaseCurrentBet(int playerBet) {
      currentBet += playerBet;
   }

   public void increaseStackSize(int amount) {
      stackSize += amount;
   }

   public boolean isAllIn() {
      return lastMove == Move.ALL_IN || lastMove == Move.RAISE_ALL_IN;
   }

   public boolean isIn() {
      return isIn;
   }

   public boolean isInPot() {
      Move[] moves = new Move[] { Move.BIG_BLIND, Move.CALL, Move.CHECK, Move.RAISE, Move.SMALL_BLIND, Move.WAITING, Move.BET, Move.ALL_IN, Move.RAISE_ALL_IN };
      return Arrays.asList(moves).contains(lastMove);
   }

   public boolean isMockBot() {
      return isMockBot;
   }

   public void setAuthor(String author) {
      this.author = author;
   }

   public void setBotUrl(String botUrl) {
      this.botUrl = botUrl;
   }

   public void setCurrentBet(int currentBet) {
      this.currentBet = currentBet;
   }

   public void setHoleCards(List<Card> holeCards) {
      this.holeCards = holeCards;
   }

   public void setLastBet(int lastBet) {
      this.lastBet = lastBet;
   }

   public void setLastMove(Move lastMove) {
      this.lastMove = lastMove;
      if (lastMove == Move.OUT || lastMove == Move.FOLD)
         isIn = false;
      else
         isIn = true;
   }

   public void setMessage(String message) {
      this.message = message;
   }

   public void setMockBot(boolean isMockBot) {
      this.isMockBot = isMockBot;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setPictureUrl(String pictureUrl) {
      this.pictureUrl = pictureUrl;
   }

   public void setPosition(int position) {
      this.position = position;
   }

   public void setStackSize(int stackSize) {
      this.stackSize = stackSize;
   }

   @Override
   public String toString() {
      if (holeCards != null && holeCards.size() == 2) {
         return name + "($" + stackSize + ", " + holeCards.get(0) + " " + holeCards.get(1) + ")";
      }
      return name + "($" + stackSize + ", no cards)";
   }

   public enum Move {
      SMALL_BLIND, BIG_BLIND, CHECK, CALL, RAISE, FOLD, ALL_IN, WAITING, RAISE_ALL_IN, OUT, BET;
      @Override
      public String toString() {
         return name().toLowerCase().replace('_', ' ');
      }
   }

}
