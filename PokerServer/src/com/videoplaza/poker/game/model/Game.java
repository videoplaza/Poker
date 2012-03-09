package com.videoplaza.poker.game.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

public class Game {

   private List<Player> players = new ArrayList<Player>();

   private String id = UUID.randomUUID().toString();

   private String lastEvent;

   private State state;

   private List<Card> cards = new ArrayList<Card>();

   private int workaround;
   private int dealer;
   private int potSize;

   private int bigBlind;

   private int smallBlind;

   private int highestBet;

   private int minimumRaise;

   boolean isTimeToBet;

   private int betPauseLength;

   private int endPauseLength;

   private int startStack;

   private int latestRaisingPlayer;
   private int previousPlayer;
   private int nextPlayer;

   public Game() {
   }

   public Game(Game copy) {
      players = new ArrayList<Player>();
      for (Player player : copy.players) {
         players.add(new Player(player));
      }
      cards = new ArrayList<Card>(copy.cards);

      dealer = copy.dealer;
      id = copy.id;
      state = copy.state;
      potSize = copy.potSize;
      bigBlind = copy.bigBlind;
      smallBlind = copy.smallBlind;
      highestBet = copy.highestBet;
      minimumRaise = copy.minimumRaise;
      isTimeToBet = copy.isTimeToBet;
      betPauseLength = copy.betPauseLength;
      endPauseLength = copy.endPauseLength;
      latestRaisingPlayer = copy.latestRaisingPlayer;
      setPreviousPlayer(copy.getPreviousPlayer());
      setNextPlayer(copy.getNextPlayer());
      lastEvent = copy.lastEvent;
   }

   public int getBetPauseLength() {
      return betPauseLength;
   }

   public int getBigBlind() {
      return bigBlind;
   }

   public List<Card> getCards() {
      return cards;
   }

   public int getDealer() {
      return dealer;
   }

   public int getEndPauseLength() {
      return endPauseLength;
   }

   public int getHighestBet() {
      return highestBet;
   }

   public String getId() {
      return id;
   }

   public String getLastEvent() {
      return lastEvent;
   }

   public int getMinimumRaise() {
      return minimumRaise;
   }

   public int getNextPlayer() {
      return nextPlayer;
   }

   public List<Player> getPlayers() {
      return players;
   }

   public int getPotSize() {
      return potSize;
   }

   public int getPreviousPlayer() {
      return previousPlayer;
   }

   public int getSmallBlind() {
      return smallBlind;
   }

   public int getStartStack() {
      return startStack;
   }

   public State getState() {
      return state;
   }

   public void increasePotSize(int amount) {
      potSize += amount;
   }

   public boolean isTimeToBet() {
      return isTimeToBet;
   }

   public void saveToFile(String stateName) {
      try {
         // serialize game state
         XStream xstream = new XStream(new JettisonMappedXmlDriver());
         StringWriter jsonWriter = new StringWriter();
         xstream.toXML(this, jsonWriter);
         String data = jsonWriter.getBuffer().toString();
         File saveFile = new File(stateName);
         FileWriter fw = new FileWriter(saveFile);
         fw.write(data);
         fw.close();
         System.out.println("Successfully saved game state to file: " + saveFile);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public void setBetPauseLength(int betPauseLength) {
      this.betPauseLength = betPauseLength;
   }

   public void setBigBlind(int bigBlind) {
      this.bigBlind = bigBlind;
   }

   public void setCards(List<Card> cards) {
      this.cards = cards;
   }

   public void setDealer(int dealer) {
      this.dealer = dealer;
   }

   public void setEndPauseLength(int endPauseLength) {
      this.endPauseLength = endPauseLength;
   }

   public void setHighestBet(int highestBet) {
      this.highestBet = highestBet;
   }

   public void setId(String id) {
      this.id = id;
   }

   public void setLastEvent(String lastEvent) {
      this.lastEvent = lastEvent;
   }

   public void setMinimumRaise(int minimumRaise) {
      this.minimumRaise = minimumRaise;
   }

   public void setNextPlayer(int nextPlayer) {
      this.nextPlayer = nextPlayer;
   }

   public void setPotSize(int potSize) {
      this.potSize = potSize;
   }

   public void setPreviousPlayer(int previousPlayer) {
      this.previousPlayer = previousPlayer;
   }

   public void setSmallBlind(int smallBlind) {
      this.smallBlind = smallBlind;
   }

   public void setStartStack(int startStack) {
      this.startStack = startStack;
   }

   public void setState(State state) {
      this.state = state;
   }

   public void setTimeToBet(boolean isTimeToBet) {
      this.isTimeToBet = isTimeToBet;
   }

   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("Table ");
      sb.append(getId());
      sb.append(":");
      sb.append("\nState: ");
      sb.append(state);
      sb.append("\nBlinds: ");
      sb.append(smallBlind);
      sb.append(", ");
      sb.append(bigBlind);
      sb.append("\nPlayers: ");
      for (Player player : players) {
         sb.append(player);
         sb.append(", ");
      }
      sb = new StringBuilder(sb.subSequence(0, sb.length() - 2));
      return sb.toString();
   }

   public static Game restoreFromFile(String filename) throws IOException {
      FileInputStream is = new FileInputStream(filename);
      BufferedReader br = new BufferedReader(new InputStreamReader(is));
      String line = br.readLine();
      StringBuilder xmlBuilder = new StringBuilder();
      while (line != null) {
         xmlBuilder.append(line);
         line = br.readLine();
      }
      String xml = xmlBuilder.toString();
      XStream xstream = new XStream(new JettisonMappedXmlDriver());
      Game toRestore = null;
      toRestore = (Game) xstream.fromXML(xml);
      toRestore.setId(UUID.randomUUID().toString());
      return toRestore;
   }

   public enum State {
      WAITING, PLAYING, FINISHED
   }

}
