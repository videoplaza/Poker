package com.videoplaza.poker.bot;

import java.util.List;

import com.videoplaza.poker.exception.IllegalBetException;
import com.videoplaza.poker.game.model.Card;
import com.videoplaza.poker.game.model.Game;
import com.videoplaza.poker.game.model.Player;
import com.videoplaza.poker.game.util.PokerUtil;
import com.videoplaza.poker.server.bot.BotResponse;

public abstract class AbstractTournamentBot implements com.videoplaza.poker.server.bot.Bot, Bot {

   @Override
   public BotResponse askForMove(Game game, Player player) {
      Bet bet = play(game, player, null);
      return new BotResponse(bet.getChatMessage(), bet.getValue());
   }

   /*
    * Bet all chips 
    */
   protected Bet allIn(Player player, String message) throws IllegalBetException {
      return new Bet(player.getStackSize(), message);
   }

   /*
    * Find what amount needed to call
    */
   protected Bet call(Game game, Player player, String message) throws IllegalBetException {
      int bet = toCall(game, player);
      if (bet > player.getStackSize()) {
         throw new IllegalBetException("Cannot affor bet " + bet + " (stack size is " + player.getStackSize() + ")");
      }
      return new Bet(bet, message);
   }

   /*
    * Fold if current best is not 0, in that case check 
    */
   protected Bet checkOrfold(String message) throws IllegalBetException {
      return new Bet(0, message);
   }

   /*
    * Get the cards that are currently on the table (0,3,4 or 5 cards)
    */
   protected Card[] getCardsOnTable(Game game) {
      return (Card[]) game.getCards().toArray();
   }

   /*
    * Get the rank of your hand, 0-1 where 1 is the best possible hand to make with the cards available on the table.
    */
   protected float getHandRank(Game game, Player player) {
      if (game.getCards().size() < 3)
         throw new IllegalArgumentException("No cards on the table");
      return PokerUtil.getHandRank(player.getHoleCards(), game.getCards());
   }

   /*
    * Get the cards that I am currently holding (0 or 2 cards)
    */
   protected Card[] getMyCards(Player me) {
      List<Card> cards = me.getHoleCards();
      return cards == null ? new Card[0] : (Card[]) cards.toArray(new Card[] {});
   }

   /*
    * Find what amount is needed to call and raise that bet with chipsToRaise
    */
   protected Bet raise(Game game, Player player, int chipsToRaise, String message) throws IllegalBetException {
      int bet = toCall(game, player) + chipsToRaise;
      if (bet > player.getStackSize()) {
         throw new IllegalBetException("Cannot affor bet " + bet + " (stack size is " + player.getStackSize() + ")");
      }
      if (bet < game.getMinimumRaise()) {
         throw new IllegalBetException("Raise is too small (minimum raise is " + game.getMinimumRaise() + ")");
      }
      return new Bet(bet, message);
   }

   /*
    * Calculate the amount of chips I need to call
    */
   private int toCall(Game game, Player myself) {
      return game.getHighestBet() - myself.getCurrentBet();
   }

}
