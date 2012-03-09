package com.videoplaza.poker.server.game;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import com.videoplaza.poker.game.model.Game;

public class PokerGameTest {

   @Test
   public void testChipIntegrity() throws IOException {
      Game game = Game.restoreFromFile("test_data_game_start.json");
      PokerGame pokerGame = new PokerGame(game, 1000);
      boolean integrity = pokerGame.checkBordIntegrity();
      assert (integrity);
      pokerGame.game.getPlayers().get(0).setStackSize(0);
      integrity = pokerGame.checkBordIntegrity();
      assert (!integrity);
   }

   //@Test
   public void testErrorRound() throws IOException {

      Lobby.getInstance().restoreFromFile("eda501ea-9929-42a3-80a5-cdcfbaed6910_1.json");

   }

   //@Test
   public void testLastRound() throws IOException {
      Game game = Game.restoreFromFile("462bf637-f759-4c5a-b453-39a81b160c3e_44.json");

      PokerGame pokerGame = new PokerGame(game, 1000);

      pokerGame.distributePot();

      assertTrue("chips!", pokerGame.checkBordIntegrity());

   }

   @Test
   public void testOneRound() throws IOException {
      Game game = Game.restoreFromFile("test_data_game_start.json");
      PokerGame pokerGame = new PokerGame(game, 1000);
      pokerGame.doRound();
      boolean integrity = pokerGame.checkBordIntegrity();
      assert (integrity);
   }

   @Test
   public void testSetPauseLengths() throws IOException {
      Game game = Game.restoreFromFile("test_data_game_start.json");
      PokerGame pokerGame = new PokerGame(game, 1000);
      pokerGame.doRound();
      boolean integrity = pokerGame.checkBordIntegrity();
      assert (integrity);
   }

   //@Test
   public void testSplitPot() throws IOException {
      Game game = Game.restoreFromFile("chip_fail.json");

      PokerGame pokerGame = new PokerGame(game, 1000);

      pokerGame.distributePot();

      assertTrue("chips!", pokerGame.checkBordIntegrity());

   }

}
