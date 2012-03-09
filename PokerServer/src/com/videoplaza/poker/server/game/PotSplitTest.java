package com.videoplaza.poker.server.game;

import java.io.IOException;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import com.videoplaza.poker.game.model.Game;
import com.videoplaza.poker.game.model.Player;
import com.videoplaza.poker.server.bot.AllInBot;

public class PotSplitTest {
   private static final Random RANDOM = new Random(3);
   private PokerGame pokerGame;

   @Before
   public void loadAndPrepare() throws IOException {
      Game game = Game.restoreFromFile("test_data_game_start.json");
      pokerGame = new PokerGame(game, 1000, new MockDisplay(), RANDOM);
      for (Player player : game.getPlayers()) {
         player.setBot(new AllInBot());
      }
      int multiplier = 0;
      for (Player player : pokerGame.game.getPlayers()) {
         player.setStackSize(player.getStackSize() * multiplier++);
      }
      pokerGame.game.setStartStack(4500);
   }

   @Test
   public void testChipIntegrity() throws IOException {
      while (true) {
         loadAndPrepare();
         while (pokerGame.game.getState() == Game.State.PLAYING) {
            pokerGame.game.saveToFile("splitState.json");
            long seed = RANDOM.nextLong();
            RANDOM.setSeed(seed);
            System.out.println("Current seed " + seed);
            pokerGame.doRound();
            boolean integrity = pokerGame.checkChipIntegrity();
            assert (integrity);
            pokerGame.updateGameState();
         }
      }
   }
}
