package com.videoplaza.poker.bot;


import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;

import com.videoplaza.poker.game.model.Card;
import com.videoplaza.poker.game.model.Game;
import com.videoplaza.poker.game.model.Player;

public class ActualBot extends AbstractTournamentBot {

   private static final float JOIN_FACTOR = 0.3f;
   private static final float CALL_FACTOR = 0.5f;


@Override
   public String getAvatarImageUrl() {
      return "http://i-cias.com/e.o/slides/khomeini01.jpg";
   }

   @Override
   public String getCreator() {
      return "Ayatolla Khomeini";
   }

   @Override
   public String getName(HttpServletRequest req) {
      return "Actual";
   }

   @Override
   public Bet play(Game game, Player me, HttpServletRequest req) {
       int numPlayersAfter = getNumPlayersAfter(game, me);
	   int numPlayersIn = getNumPlayersIn(game, me);
	   int numPlayers = getNumPlayers(game, me);
	   
	   float pos = getPos(numPlayers, numPlayersIn, numPlayersAfter);
	   int potsize = game.getPotSize();
	   int joinCost=game.getHighestBet();
	   float potOdds=getPotOdds(joinCost, potsize);
	   
	   float handRank;
	   if(game.getCards().size() == 0){
		   handRank = getHandRank(me);
	   } else {
		   handRank = this.getHandRank(game, me);
	   }
	   float kvalite=getKvalitet(potOdds,pos,handRank);//potOdds*(pos*handRank);
	   boolean call = getCall(kvalite);
	   int betSize = 0;
	   if(call){
		   Bet bet = getBetsize(pos, potsize, handRank, game, me);
		   return bet;
	   }
	   else return new Bet(betSize,getComment());
   }
   private String getComment(){
	   return "Allahu akbar";
   }
   private Bet getBetsize(float pos, float potsize, float handRank, Game game, Player me){
	   try{
	   float rank = pos*potsize*handRank;
	   if(rank<0.5) return call(game, me, getComment());
	   if(rank<0.75) return new Bet(game.getMinimumRaise(), getComment());
	   if(rank<0.9) return new Bet(game.getHighestBet()*2, getComment());
	   else return allIn(me, getComment());
	   }catch(Exception e){
		   System.out.println("whaaaaat?");
		   return null;
	   }
   }
   private boolean getCall(float kval){
	   if(kval>CALL_FACTOR) return true;
	   return false;
   }
   private float getHandRank(Player me){
	   List<Card> kort = me.getHoleCards();
	   Card fst = kort.get(0);
	   Card snd = kort.get(1);
	   int fstRank = fst.getRank();
	   int sndRank = snd.getRank();
	   int value = fstRank + sndRank;
	   //Par
	   if(fstRank == sndRank) value += 12;
	   //F�rg chans
	   if(fst.getSuit() == snd.getSuit()) value += 4;
	  //Connected
	   if(fstRank == sndRank+1 || fstRank == sndRank-1) value += 3;
	   return (float)value/30.0f;
   }
   private float getPotOdds(float joincost, float potsize){
	   if(joincost == 0) return 1;
	   else return (1-(joincost/potsize)/4)+0.75f;
   }
   private float getKvalitet(float potOdds, float pos, float handrank){
	   //g�nger eventuella coffec
	   return potOdds*pos*handrank;
   }
   

   private float getPos(int numPlayers, int numPlayersIn, int numPlayersAfter) {
	   float invertedVal = numPlayersIn + (numPlayersAfter * JOIN_FACTOR);
	   return invertedVal == 0 ? 1.0f : (1.0f/invertedVal)/4 +0.75f;
   }
   
   
   
  @Test
  public void testPosValue(){
	  float posVal = getPos(10, 4, 2);
	  System.out.println("Pos val: " + posVal);
	  posVal = getPos(10, 0, 4);
	  System.out.println("Pos val: " + posVal);  
  }

private int getNumPlayersAfter(Game game, Player me) {
	   List<Player> players = game.getPlayers();
	   
	   int numPlayersAfter = 0;
	   for(int i = 1; i < players.size(); i++){
		   int pos = (me.getPosition() + i) % players.size();
		   Player playerAtPos = players.get(pos);
 		   if(playerAtPos.getStackSize() == 0)
 			   continue;
		   numPlayersAfter++;
		   if(pos == game.getDealer())
			   break;
	   }
	   return numPlayersAfter;
   }

   private int getNumPlayersIn(Game game, Player me) {
	   List<Player> players = game.getPlayers();
	   int numPlayersAfter = 0;
	   for(int i = 1; i < players.size(); i++){
		   int pos = (me.getPosition() - i + players.size()) % players.size();
		   Player playerAtPos = players.get(pos);
 		   if(!playerAtPos.isInPot())
 			   continue;
		   numPlayersAfter++;
		   if(pos == game.getDealer())
			   break;
	   }
	   return numPlayersAfter;
   }

   private int getNumPlayers(Game game, Player me) {
	   return getNumPlayersAfter(game, me) + getNumPlayersIn(game, me);
   }
   


private Bet preFlop(Game game, Player me) throws Exception {
      Card[] cards = getMyCards(me);
      if (cards[0].getRank() == cards[1].getRank()) {
         return allIn(me, "Pocket pairs");
      }
      float handrank = (cards[0].getRank() + cards[1].getRank()) / 28f;
      System.out.println("Preflop handrank " + handrank);
      if (handrank > 0.2f) {
         return call(game, me, "Good preflop hand");
      }
      return checkOrFold("Bad preflop hand");
   }
}
