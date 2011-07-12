package com.videoplaza.poker.server.game;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.videoplaza.poker.game.model.Card;
import com.videoplaza.poker.game.model.Game;
import com.videoplaza.poker.game.model.Game.State;
import com.videoplaza.poker.game.model.Player;
import com.videoplaza.poker.game.model.Player.Move;
import com.videoplaza.poker.server.speech.SpeechUtil;

public class Lobby {

   private static final int MAX_PLAYERS_PER_TABLE = 10;

   public static Lobby getInstance() {
      return instance;
   }

   private List<Game> games;
   private volatile Game display;

   private static int counter = 0;

   private static Lobby instance = new Lobby();

   private Lobby() {
      // do nothing
   }

   public synchronized void addMockBot() {
      playerConnect("", "MockBot " + counter++, "None", "img/no_user.jpg", true);
   }

   public synchronized void addMockBot(String name) {
      playerConnect("", "MockBot " + counter++, "None", "", true);
   }

   public void displayEvent(Game state, String event) {
      Game g = new Game(state);
      display = g;
      g.setPreviousPlayer(-1);
      g.setLastEvent(event);
      System.out.println(event);

      System.out.print("Cards on table are ");
      for (Card card : g.getCards()) {
         System.out.print(card);
      }
      System.out.println();
      SpeechUtil.say(event);
      try {
         Thread.sleep(state.getBetPauseLength());
      } catch (InterruptedException e) {
         e.printStackTrace();
      }
   }

   public void displayPlayerMove(Game state, Player previousPlayer, Player nextPlayer) {
      Game g = new Game(state);
      display = g;
      g.setLastEvent(null);
      if (previousPlayer != null) {
         SpeechUtil.Voice playerVoice = SpeechUtil.Voice.values()[previousPlayer.getPosition() + 1];
         g.setPreviousPlayer(previousPlayer.getPosition());

         StringBuilder dealerMessage = new StringBuilder();
         Move move = previousPlayer.getLastMove();
         dealerMessage.append(": ");
         dealerMessage.append(move);
         if (move == Move.RAISE || move == Move.ALL_IN || move == Move.CALL || move == Move.BET) {
            dealerMessage.append(", ");
            dealerMessage.append(previousPlayer.getLastBet());
         }
         System.out.println(previousPlayer.toString() + dealerMessage.toString() + ". " + previousPlayer.getMessage());
         SpeechUtil.say(previousPlayer.getName() + dealerMessage.toString());
         SpeechUtil.say(previousPlayer.getMessage(), playerVoice);
      }

      if (nextPlayer != null) {
         g.setNextPlayer(nextPlayer.getPosition());
      }
      try {
         Thread.sleep(state.getBetPauseLength());
      } catch (InterruptedException e) {
         e.printStackTrace();
      }
   }

   public Game getDisplay() {
      return display;
   }

   public List<Game> getGames() {
      return games;
   }

   public void loadConfig(String fileName) throws IOException {
      if (fileName == null || fileName.length() == 0) {
         fileName = "table.txt";
      }
      FileReader rf = new FileReader(new File(fileName));
      BufferedReader br = new BufferedReader(rf);
      String line = br.readLine();
      while (line != null) {
         // parse bot config
         line = line.trim();

         if (line.length() > 0) {
            if (!line.startsWith("#")) {
               playerConnect(line);
            }
         }
         line = br.readLine();
      }
   }

   public synchronized boolean playerConnect(String botUrlString) {

      long startTimer = System.currentTimeMillis();
      try {
         // connect to bot
         URL botUrl = new URL(botUrlString);
         HttpURLConnection connection = (HttpURLConnection) botUrl.openConnection();
         connection.setRequestMethod("POST");
         connection.setDoOutput(true);

         String data = "init=true";

         // send init request to bot 
         OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
         data = data + "\r\n\r\n";
         writer.write(data);
         writer.flush();
         connection.setConnectTimeout(5000);
         connection.setReadTimeout(5000);

         // read bots response
         BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
         StringBuilder response = new StringBuilder();
         String inputLine;
         while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
         }
         in.close();

         // parse bots response
         String responseString = response.toString();
         System.out.println("Response from " + botUrl + ": " + responseString);
         responseString = responseString.trim();

         String[] params = responseString.split("&");
         Map<String, String> paramValues = new HashMap<String, String>();
         for (String param : params) {
            String[] keyValPair = param.split("=");
            if (keyValPair.length != 2) {
               System.out.println("Invalid param: " + param);
               continue;
            }
            paramValues.put(URLDecoder.decode(keyValPair[0], "UTF-8"), URLDecoder.decode(keyValPair[1], "UTF-8"));
         }

         String name = paramValues.get("name");
         String author = paramValues.get("creator");
         String pictureUrl = paramValues.get("image_url");

         if (name == null || author == null) {
            throw new Exception("invalid response: " + responseString);
         }

         // add bot to table
         playerConnect(botUrlString, name, author, pictureUrl, false);

         System.out.print("Bot " + name + " connected at " + botUrlString + " with author " + author + " and pictureUrl " + pictureUrl);
         return true;
      } catch (Exception e) {
         e.printStackTrace();
      }
      return false;
   }

   public synchronized void playerConnect(String botUrl, String name, String author, String pictureUrl, boolean isMockBot) {

      // create a player
      Player player = new Player();
      player.setAuthor(author);
      player.setName(name);
      player.setPictureUrl(pictureUrl);
      player.setBotUrl(botUrl);
      player.setMockBot(isMockBot);

      if (getGames() == null) {
         games = new ArrayList<Game>();
      }

      // check if there is room for this player on existing table
      boolean playerAdded = false;
      for (Game game : getGames()) {
         if (game.getState() != Game.State.WAITING)
            continue;
         if (game.getPlayers().size() < MAX_PLAYERS_PER_TABLE) {
            // ok to add player
            int position = game.getPlayers().size();
            player.setPosition(position);
            game.getPlayers().add(player);
            playerAdded = true;
            System.out.println("Player " + player + " connected to existing table " + game.getId());
         }
      }
      if (!playerAdded) {
         // need to create a new table
         Game game = new Game();
         game.setState(State.WAITING);
         game.setBetPauseLength(2000);
         game.setEndPauseLength(10000);
         game.setSmallBlind(20);
         game.setBigBlind(40);
         player.setPosition(0);
         game.getPlayers().add(player);
         getGames().add(game);
         System.out.println("Player " + player + " connected to new table " + game.getId());
      }
   }

   public void restoreFromFile(String file) {
      try {
         Game toRestore = Game.restoreFromFile(file);
         System.out.println("Restoring game " + toRestore.getId() + " from " + file);
         if (games == null) {
            games = new ArrayList<Game>();
         }
         games.add(toRestore);
         // found game, spawn game thread
         int totalStack = 0;
         for (Player player : toRestore.getPlayers()) {
            totalStack += player.getStackSize();
         }
         PokerGame gameRunnable = new PokerGame(toRestore, totalStack / toRestore.getPlayers().size());
         Thread gameThread = new Thread(gameRunnable);
         System.out.println("Spawning new game thread for restored table with " + toRestore.getPlayers().size() + " players");
         //toRestore.setState(Game.State.PLAYING);
         gameThread.run();
         System.out.println("... and not blocking servlet worker thread");
         return;
      } catch (Exception e1) {
         e1.printStackTrace();
      }
   }

   public void setBlinds(String tableId, int smallBlind) {
      for (Game game : games) {
         if (game.getId().equals(tableId)) {
            // found game, set blinds
            int bigBlind = smallBlind * 2;
            System.out.println("Setting blinds for table " + tableId + " to " + smallBlind + "/" + bigBlind);
            game.setSmallBlind(smallBlind);
            game.setBigBlind(bigBlind);
            return;
         }
      }
      System.out.println("No table with id " + tableId);
   }

   public void setPauseLengths(String tableId, int break1, int break2) {
      // set pause lengths
      for (Game game : games) {
         if (game.getId().equals(tableId)) {
            // found game, set blinds
            System.out.println("Setting pause lengths for table " + tableId + " to " + break1 + "/" + break2);
            game.setBetPauseLength(break1);
            game.setEndPauseLength(break2);
            return;
         }
      }
      System.out.println("No table with id " + tableId);
   }

   private void spawnTable(Game game, int startStack) {
      for (Player player : game.getPlayers()) {
         player.setStackSize(startStack);
      }
      // found game, spawn game thread
      PokerGame gameRunnable = new PokerGame(game, startStack);
      Thread gameThread = new Thread(gameRunnable);
      System.out.println("Spawning new game thread with " + game.getPlayers().size());
      game.setState(Game.State.PLAYING);
      gameThread.run();
      System.out.println("... and not blocking servlet worker thread");
   }

   public void startGame(String tableId, int startStack) {
      if (tableId == null || tableId.length() == 0) {
         Game game = games.get(games.size() - 1);
         spawnTable(game, startStack);
         return;
      }
      for (Game game : games) {
         if (game.getId().equals(tableId)) {
            spawnTable(game, startStack);
            return;
         }
      }
      System.out.println("No table with id " + tableId);
   }

   public void startPracticeGame(String gameId) {
      int startStack = 10000;
      if (getGames() == null) {
         games = new ArrayList<Game>();
      }
      if (games.size() == 0) {
         Game game = new Game();
         game.setState(State.WAITING);
         game.setBetPauseLength(2000);
         game.setEndPauseLength(10000);
         game.setSmallBlind(20);
         game.setBigBlind(40);
         getGames().add(game);
      }
      Game game = games.get(0);
      while (game.getPlayers().size() < MAX_PLAYERS_PER_TABLE) {
         addMockBot();
      }
      startGame(game.getId(), startStack);
   }

}
