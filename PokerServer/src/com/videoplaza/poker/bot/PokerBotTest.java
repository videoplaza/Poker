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

   private static final int SEED = 2;
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
      game.getPlayers().get(0).setBot(new YourBot());
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
      for (int i = 0; i < NB_TOURNAMENTS; i++) {
         loadAndPrepare();
         while (pokerGame.getGame().getState() == Game.State.PLAYING) {
            //pokerGame.getGame().saveToFile("failState.json");
            long seed = random.nextLong();
            random.setSeed(seed);
            System.out.println("Current seed " + seed);
            pokerGame.doRound();
            boolean integrity = pokerGame.checkChipIntegrity();
            assert (integrity);
            pokerGame.updateGameState();
         }
         if (pokerGame.getGame().getPlayers().get(0).getStackSize() > 0) {
            wins++;
         }
      }
      System.out.println("Bot won " + wins + " out of " + NB_TOURNAMENTS + " tournaments.");
   }
}
