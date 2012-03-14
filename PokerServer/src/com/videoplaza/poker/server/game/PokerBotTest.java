package com.videoplaza.poker.server.game;

import java.io.IOException;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import com.videoplaza.poker.bot.ExampleBotImpl;
import com.videoplaza.poker.game.model.Game;
import com.videoplaza.poker.game.model.Player;

public class PokerBotTest {

   private static final int NB_TOURNAMENTS = 10;
   private static Random random = new Random(1);
   private PokerGame pokerGame;

   @Before
   public void loadAndPrepare() throws IOException {
      Game game = Game.restoreFromFile("test_data_game_start.json");
      pokerGame = new PokerGame(game, 1000, new MockDisplay(), random);
      for (Player player : game.getPlayers()) {
         player.setBot(new ExampleBotImpl());
      }
      /*
      int multiplier = 10000;
      for (Player player : pokerGame.game.getPlayers()) {
         player.setStackSize(player.getStackSize() * multiplier);
      }
      */
      //pokerGame.game.setStartStack(pokerGame.game.getStartStack() * multiplier);
   }

   @Test
   public void testUntilFail() throws IOException {

      for (int i = 0; i < NB_TOURNAMENTS; i++) {
         loadAndPrepare();
         while (pokerGame.game.getState() == Game.State.PLAYING) {
            pokerGame.game.saveToFile("failState.json");
            long seed = random.nextLong();
            random.setSeed(seed);
            System.out.println("Current seed " + seed);
            pokerGame.doRound();
            boolean integrity = pokerGame.checkChipIntegrity();
            assert (integrity);
            pokerGame.updateGameState();
         }
      }
   }
}
