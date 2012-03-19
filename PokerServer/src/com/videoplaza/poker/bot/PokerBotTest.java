package com.videoplaza.poker.bot;

import java.io.IOException;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import com.videoplaza.poker.game.model.Game;
import com.videoplaza.poker.game.model.Player;
import com.videoplaza.poker.server.game.MockDisplay;
import com.videoplaza.poker.server.game.PokerGame;

public class PokerBotTest {

   private static final int SEED = 0x5eed;
   private static final int NB_TOURNAMENTS = 10;
   private static Random random = new Random(SEED);
   private PokerGame pokerGame;

   @Before
   public void loadAndPrepare() throws IOException {
      Game game = Game.restoreFromFile("test_data_game_start.json");
      pokerGame = new PokerGame(game, 1000, new MockDisplay(), random);
      for (Player player : game.getPlayers()) {
         player.setBot(new ExampleBotImpl());
      }
      YourBot bot = new YourBot();
      game.getPlayers().get(0).setBot(bot);
      game.getPlayers().get(0).setName(bot.getName(null));
      /*
      int multiplier = 10000;
      for (Player player : pokerGame.game.getPlayers()) {
         player.setStackSize(player.getStackSize() * multiplier);
      }
      */
      //pokerGame.game.setStartStack(pokerGame.game.getStartStack() * multiplier);
   }

   @Test
   public void testBot() throws IOException {
      int wins = 0;
      int maxBB = 0;
      for (int i = 0; i < NB_TOURNAMENTS; i++) {
         loadAndPrepare();
         int[] smallBlinds = { 20, 40, 50, 75, 100, 150, 300, 450, 500, 1000, 1500, 2000, 3000, 4000, 5000 };
         long handCounter = 0;
         int smallBlind = pokerGame.getGame().getSmallBlind();
         int bigBlind = pokerGame.getGame().getBigBlind();
         while (pokerGame.getGame().getState() == Game.State.PLAYING) {
            //pokerGame.getGame().saveToFile("failState.json");
            long seed = random.nextLong();
            random.setSeed(seed);
            System.out.println("Current seed " + seed);
            pokerGame.doRound();
            boolean integrity = pokerGame.checkChipIntegrity();
            assert (integrity);
            pokerGame.updateGameState();
            handCounter++;
            if (handCounter % 10 == 0) {
               smallBlind = smallBlinds[(int) (handCounter / 10)];
               bigBlind = smallBlind * 2;
               System.out.println("Blinds going up to " + smallBlind + "/" + bigBlind);
               pokerGame.getGame().setSmallBlind(smallBlind);
               pokerGame.getGame().setBigBlind(bigBlind);
               maxBB = Math.max(maxBB, bigBlind);
            }
         }
         if (pokerGame.getGame().getPlayers().get(0).getStackSize() > 0) {
            wins++;
         }
      }
      System.out.println("Bot won " + wins + " out of " + NB_TOURNAMENTS + " tournaments.");
      System.out.println("Max BB was " + maxBB);
   }
}
