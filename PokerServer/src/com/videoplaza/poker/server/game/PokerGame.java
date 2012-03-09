package com.videoplaza.poker.server.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.videoplaza.poker.game.model.Card;
import com.videoplaza.poker.game.model.Deck;
import com.videoplaza.poker.game.model.Game;
import com.videoplaza.poker.game.model.Game.State;
import com.videoplaza.poker.game.model.Player;
import com.videoplaza.poker.game.model.Player.Move;
import com.videoplaza.poker.game.util.PokerUtil;
import com.videoplaza.poker.server.bot.BotResponse;

public class PokerGame implements Runnable {

   protected Game game;
   private PokerDisplay display;
   private Random random;

   public PokerGame(Game game, int startStack, PokerDisplay display, Random random) {
      super();
      this.game = game;
      this.display = display;
      this.random = random;
      game.setStartStack(startStack);
   }

   public void doRound() {
      // initialize board for new round
      game.setCards(new ArrayList<Card>());
      game.setHighestBet(0);
      game.setMinimumRaise(game.getBigBlind());
      game.setPotSize(0);
      game.setTimeToBet(true);
      game.setDealer(resolveDealer());
      int currentPlayerIndex = resolveActivePlayer(game.getDealer(), 3);
      Deck deck = new Deck();
      deck.shuffle(random);

      getDisplay().displayEvent(game, "Dealing hole cards");
      for (Player player : game.getPlayers()) {
         player.setCurrentBet(0);
         player.setMessage("");
         if (player.getStackSize() <= 0) {
            player.setLastMove(Move.OUT);
            player.setHoleCards(new ArrayList<Card>());
         } else {
            player.setLastMove(Move.WAITING);
            player.setHoleCards(Arrays.asList(deck.deal(), deck.deal()));
         }
      }
      postBlinds();

      int round = 0;
      if (doBettingRound(currentPlayerIndex, ++round))
         return;

      // toss a card
      deck.deal();

      // flop
      for (int i = 0; i < 3; i++) {
         game.getCards().add(deck.deal());
      }
      getDisplay().displayEvent(game, "Dealing flop");
      if (doBettingRound((game.getDealer() + 1) % game.getPlayers().size(), ++round))
         return;

      // toss a card
      deck.deal();

      // turn
      game.getCards().add(deck.deal());
      getDisplay().displayEvent(game, "Dealing turn");
      if (doBettingRound((game.getDealer() + 1) % game.getPlayers().size(), ++round))
         return;

      // toss a card
      deck.deal();

      // river
      game.getCards().add(deck.deal());
      getDisplay().displayEvent(game, "Dealing river");
      if (doBettingRound((game.getDealer() + 1) % game.getPlayers().size(), ++round))
         return;

      game.setTimeToBet(false);

      distributePot();
   }

   public void run() {
      getDisplay().displayEvent(game, "Starting new tournament. Blinds are " + game.getSmallBlind() + " / " + game.getBigBlind() + ".");
      int deal = 0;
      while (game.getState() == Game.State.PLAYING) {
         if (deal == 0)
            game.saveToFile(game.getId() + "_start.json");
         else
            game.saveToFile(game.getId() + "_" + deal + ".json");
         checkChipIntegrity();
         getDisplay().displayEvent(game, "Starting tournament deal " + ++deal);
         doRound();
         checkChipIntegrity();
         try {
            Thread.sleep(game.getEndPauseLength());
         } catch (InterruptedException e) {
            e.printStackTrace();
         }
         updateGameState();
      }
   }

   public void setDisplay(PokerDisplay display) {
      this.display = display;
   }

   protected boolean checkChipIntegrity() {
      int totalChips = 0;
      for (Player player : game.getPlayers()) {
         totalChips += player.getStackSize();
      }
      if (totalChips != (game.getStartStack() * game.getPlayers().size())) {
         System.out.println("Lost chip integrity! total is " + totalChips);
         assert false;
         return false;
      } else {
         System.out.println("Chip integrity!");
      }
      return true;
   }

   protected void distributePot() {
      //saveGameState("chip_fail.json");
      int remainingPot = game.getPotSize();
      List<Player> remainingPlayersInPot = new ArrayList<Player>();
      for (Player player : game.getPlayers()) {
         if (player.isInPot()) {
            remainingPlayersInPot.add(player);
         }
      }
      int round = 1;
      while (remainingPot > 0 && remainingPlayersInPot.size() > 0) {
         System.out.println("------------------------------Distribute pot round " + round++);
         System.out.println("Remaining pot: " + remainingPot);
         System.out.println("Remaining players in pot: " + remainingPlayersInPot.toString());
         // calculate smallest bet among remaining players
         int minBet = Integer.MAX_VALUE;
         for (Player player : remainingPlayersInPot) {
            if (player.getCurrentBet() < minBet) {
               minBet = player.getCurrentBet();
            }
         }
         System.out.println("Minbet: " + minBet);

         // calculate size of side pot         
         int sidePot = 0;
         for (Player player : game.getPlayers()) {
            sidePot += Math.min(player.getCurrentBet(), minBet);
            player.setCurrentBet(Math.max(0, player.getCurrentBet() - minBet));
         }
         System.out.println("Sidepot: " + sidePot);
         // get winners and split side pot amongst them
         List<Player> winners = PokerUtil.getWinningPlayers(remainingPlayersInPot, game.getCards());
         int winning = sidePot / winners.size();
         int remains = sidePot - (winning * winners.size());

         int[] winPerPlayer = new int[game.getPlayers().size()];
         for (Player winner : winners) {
            winPerPlayer[winner.getPosition()] += winning;
         }
         // distribute remains
         int worstPosition = game.getDealer() + 1 % game.getPlayers().size();
         while (remains > 0) {
            Player player = game.getPlayers().get(worstPosition);
            if (winners.contains(player)) {
               winPerPlayer[worstPosition]++;
               remains--;
            }
            worstPosition++;
            worstPosition %= game.getPlayers().size();
         }

         for (int i = 0; i < game.getPlayers().size(); i++) {
            if (winPerPlayer[i] > 0) {
               Player winner = game.getPlayers().get(i);
               getDisplay().displayEvent(game, winner.getName() + " won " + winPerPlayer[i] + " chips.");
               winner.increaseStackSize(winPerPlayer[i]);
            }

         }
         System.out.println("Winners: " + winners.toString());
         Set<Player> toRemove = new HashSet<Player>();
         for (Player player : remainingPlayersInPot) {
            if (player.getCurrentBet() <= 0) {
               // player only came to this level
               toRemove.add(player);
            }
         }
         remainingPlayersInPot.removeAll(toRemove);
         remainingPot -= sidePot;
      }
   }

   protected void updateGameState() {
      Player winner = null;
      for (Player player : game.getPlayers()) {
         if (player.getStackSize() > 0) {
            if (winner == null) {
               winner = player;
            } else {
               return;
            }
         }
      }
      getDisplay().displayEvent(game, "Tournament ended, winner was " + winner.getName());
      game.setState(State.FINISHED);
   }

   private void applyBet(Player player, int bet) {
      player.increaseCurrentBet(bet);
      player.decreaseStackSize(bet);
      player.setLastBet(bet);
      game.setHighestBet(Math.max(game.getHighestBet(), player.getCurrentBet()));
      game.increasePotSize(bet);
      int raiseAmount = player.getCurrentBet() - game.getHighestBet();
      if (raiseAmount > 0) {
         game.setMinimumRaise(bet);
      }
   }

   private boolean checkIfFinished() {
      Player winner = null;
      for (Player player : game.getPlayers()) {
         if (player.isIn()) {
            if (winner == null) {
               winner = player;
            } else {
               return false;
            }
         }
      }
      winner.increaseStackSize(game.getPotSize());
      for (Player player : game.getPlayers()) {
         player.setCurrentBet(0);
      }
      getDisplay().displayEvent(game, winner.getName() + " won " + game.getPotSize() + " chips.");
      return true;
   }

   private boolean checkRoundActive(Player nextPlayer) {
      if (nextPlayer.getCurrentBet() != game.getHighestBet()) {
         return true;
      }

      if ((nextPlayer.getLastMove() != Move.BIG_BLIND) && (nextPlayer.getLastMove() != Move.WAITING)) {
         return false;
      }

      for (Player player : game.getPlayers()) {
         if (player != nextPlayer) {
            Move lastMove = player.getLastMove();
            if (lastMove == Move.CALL || lastMove == Move.BET || lastMove == Move.CHECK || lastMove == Move.RAISE || lastMove == Move.WAITING
                  || lastMove == Move.SMALL_BLIND || lastMove == Move.BIG_BLIND) {
               return true;
            }

         }
      }
      return false;
   }

   private boolean doBettingRound(int currentPlayerIndex, int round) {
      if (round > 1) {
         // reset player states
         for (Player player : game.getPlayers()) {
            //player.setCurrentBet(0);
            if (player.getLastMove() == Move.RAISE_ALL_IN) {
               player.setLastMove(Move.ALL_IN);
            }
            if (player.getLastMove() == Move.FOLD) {
               player.setLastMove(Move.OUT);
            }
            if (player.getLastMove() != Move.ALL_IN && player.getLastMove() != Move.OUT && player.getLastMove() != Move.FOLD) {
               // player is active
               if (player.getStackSize() == 0) {
                  //break here!
                  System.out.print("");
               }
               player.setLastMove(Move.WAITING);
            }
         }
         game.setMinimumRaise(game.getBigBlind());
      }

      // do round
      Player previousPlayer = null;
      Player currentPlayer = game.getPlayers().get(currentPlayerIndex);
      while (checkRoundActive(currentPlayer)) {
         game.setNextPlayer(currentPlayerIndex);
         if (currentPlayer.canContinue()) {
            doPlayerBet(currentPlayer);
            previousPlayer = currentPlayer;
            game.setPreviousPlayer(currentPlayerIndex);
            if (previousPlayer != null && previousPlayer.getLastMove() != Move.OUT) {
               getDisplay().displayPlayerMove(game, previousPlayer, currentPlayer);
            }
         } else {
            // check if player state needs update
            if (currentPlayer.getLastMove() == Move.RAISE_ALL_IN) {
               System.out.println(currentPlayer.getName() + " was all in, last move " + currentPlayer.getLastMove());
               currentPlayer.setLastMove(Move.ALL_IN);
            }
         }
         currentPlayerIndex = resolveActivePlayer(currentPlayerIndex, 1);
         game.setNextPlayer(currentPlayerIndex);
         currentPlayer = game.getPlayers().get(currentPlayerIndex);
      }
      return checkIfFinished();
   }

   private void doPlayerBet(Player player) {
      int actualBet = 0;
      if (player.getStackSize() > 0) {
         int wantedBet = getBet(player);
         actualBet = getActualBet(wantedBet, player);
         System.out.println(player.getName() + " wanted to bet " + wantedBet + ", actual bet was " + actualBet);
         applyBet(player, actualBet);
      }
   }

   private int getActualBet(int wantedBet, Player player) {

      // always cap bet between 0 and players stack size
      wantedBet = Math.min(player.getStackSize(), wantedBet);
      wantedBet = Math.max(0, wantedBet);

      int neededToCall = game.getHighestBet() - player.getCurrentBet();

      if (wantedBet > 0 && wantedBet == player.getStackSize()) {
         // all-in bet
         if (wantedBet > neededToCall) {
            player.setLastMove(Player.Move.RAISE_ALL_IN);
         } else {
            player.setLastMove(Player.Move.ALL_IN);
         }
         return wantedBet;
      }

      // not allowed to bet less than call minimum 
      if (wantedBet < neededToCall) {
         // fold
         player.setLastMove(Player.Move.FOLD);
         return 0;
      } else if (wantedBet == 0 && neededToCall == 0) {
         // check
         player.setLastMove(Player.Move.CHECK);
         return 0;
      } else if (wantedBet == neededToCall) {
         // call
         player.setLastMove(Player.Move.CALL);
         return wantedBet;
      } else {

         // raise
         int raise = wantedBet - neededToCall;
         if (raise < game.getMinimumRaise()) {
            player.setLastMove(neededToCall == 0 ? Move.CHECK : Player.Move.CALL);
            return neededToCall;
         }
         // raise is bigger that minimum raise 
         if (neededToCall == 0) {
            player.setLastMove(Player.Move.BET);
         } else
            player.setLastMove(Player.Move.RAISE);
         return wantedBet;
      }
   }

   private int getBet(Player player) {
      BotResponse response = player.getBot().askForMove(game);
      String playerChatMessage = response.message;
      if (playerChatMessage != null && playerChatMessage.length() > 0) {
         player.setMessage(playerChatMessage);
      } else {
         player.setMessage("");
      }
      return response.amount;
   }

   private PokerDisplay getDisplay() {
      return display;
   }

   private Player getNextActivePlayer(int index) {
      int nextActiveIndex = index % game.getPlayers().size();
      Player nextActive = game.getPlayers().get(nextActiveIndex);
      while (!nextActive.isIn()) {
         nextActiveIndex = ++index % game.getPlayers().size();
         nextActive = game.getPlayers().get(nextActiveIndex);
      }
      return nextActive;
   }

   private void postBlinds() {
      Player smallBlind = getNextActivePlayer(game.getDealer() + 1);
      int smallBlindAmount = game.getSmallBlind();
      if (smallBlindAmount > smallBlind.getStackSize()) {
         smallBlindAmount = smallBlind.getStackSize();
         smallBlind.setLastMove(Move.RAISE_ALL_IN);
      } else {
         smallBlind.setLastMove(Move.SMALL_BLIND);
      }
      smallBlind.decreaseStackSize(smallBlindAmount);
      smallBlind.setLastBet(smallBlindAmount);
      game.increasePotSize(smallBlindAmount);
      smallBlind.setCurrentBet(smallBlindAmount);
      getDisplay().displayEvent(game, smallBlind.getName() + " posts small blind " + smallBlindAmount);
      //Lobby.getInstance().displayPlayerMove(game, smallBlind, null);
      Player bigBlind = getNextActivePlayer(smallBlind.getPosition() + 1);
      int bigBlindAmount = game.getBigBlind();
      if (bigBlindAmount > bigBlind.getStackSize()) {
         bigBlindAmount = bigBlind.getStackSize();
         if (bigBlindAmount > smallBlindAmount) {
            bigBlind.setLastMove(Move.RAISE_ALL_IN);
         } else {
            bigBlind.setLastMove(Move.ALL_IN);
         }
      } else {
         bigBlind.setLastMove(Move.BIG_BLIND);
      }
      bigBlind.decreaseStackSize(bigBlindAmount);
      bigBlind.setCurrentBet(bigBlindAmount);
      game.increasePotSize(bigBlindAmount);
      game.setHighestBet(Math.max(smallBlindAmount, bigBlindAmount));
      getDisplay().displayEvent(game, bigBlind.getName() + " posts big blind " + bigBlindAmount);
      //Lobby.getInstance().displayPlayerMove(game, bigBlind, null);
   }

   private int resolveActivePlayer(int startIndex, int offset) {
      int resultIndex = startIndex;
      for (int i = 0; i < offset; i++) {
         resultIndex = (resultIndex + 1) % game.getPlayers().size();
         Player candidate = game.getPlayers().get(resultIndex);
         while (candidate.getStackSize() == 0 && !candidate.isAllIn()) {
            resultIndex = (resultIndex + 1) % game.getPlayers().size();
            candidate = game.getPlayers().get(resultIndex);
         }
      }
      return resultIndex;
   }

   private int resolveDealer() {
      return resolveActivePlayer(game.getDealer(), 1);
   }

}
