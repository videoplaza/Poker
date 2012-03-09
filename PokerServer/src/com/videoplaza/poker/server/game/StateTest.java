package com.videoplaza.poker.server.game;

import java.io.IOException;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import com.videoplaza.poker.game.model.Game;

public class StateTest {

   private PokerGame pokerGame;

   private static final long FAIL_SEED = 6820828958773489220L;

   @Before
   public void loadAndPrepare() throws IOException {
      Game game = Game.restoreFromFile("failState.json");
      pokerGame = new PokerGame(game, 10000000, new MockDisplay(), new Random(FAIL_SEED));
   }

   @Test
   public void testFailedState() {
      pokerGame.doRound();
   }
}
