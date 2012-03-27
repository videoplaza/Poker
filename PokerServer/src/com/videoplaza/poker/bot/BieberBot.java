package com.videoplaza.poker.bot;

import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import com.videoplaza.poker.game.model.Card;
import com.videoplaza.poker.game.model.Game;
import com.videoplaza.poker.game.model.Player;

public class BieberBot extends AbstractTournamentBot {

   private static final Random rnd = new Random(System.currentTimeMillis());

   private String[] comment = { "#5DAYS and counting. #BOYFRIEND WORLDWIDE", "love seeing my fans guess how #BOYFRIEND goes. @LOUforBIEBER good try.",
         "@carlyraejepsen really happy and proud of u. just the beginning. see u at @theEllenShow tomorrow!",
         "AUSTRALIA!! u got #CALLMEMAYBE by @CarlyRaeJepsen to #1 on ITUNES!! #SWAG!! The rest of the world let's do the same!", "@pattiemallette love u",
         "my fans = swag",
         "@justinbieber OH MY BIEBER #6DAY UNTIL WE SHOCK THE WORLD. WE GONA GET THIS TO #1 TURN THE HATERS INTO FANS! #BOYFRIEND do you notice me? 17",
         "@justinbieber #6DAYS. I can't wait anymore. BOYFRIEND IS GOING TO BE THE BOMB. 11", "#6days http://justinbiebermusic.com/boyfriend holy spaghetti",
         "Justin's probably dying at these videos", "@justinbieber #6DAYS TO #BOYFRIEND, I'M SO EXCITED!!!!! I CAN'T WAIT!!! OMG!! Te amo ♥ 1" };

   Random rand = new Random();

   //List<Tweet> tweets;

   @Override
   public String getAvatarImageUrl() {
      return "http://justinbieberphoto.net/wp-content/uploads/2011/11/justin-bieber-picture-edited-Love-edition.gif";
   }

   @Override
   public String getCreator() {
      return "JB";
   }

   @Override
   public String getName(HttpServletRequest req) {
      return "Beliber";
   }

   @Override
   public Bet play(Game game, Player me, HttpServletRequest req) {
      try {
         if (game.getCards().size() == 0) {
            return preFlop(game, me);
         }
         if (game.getCards().size() == 3) {
            return preTurn(game, me);
         }
         if (game.getCards().size() == 4) {
            return preRiver(game, me);
         }
         return postRiver(game, me);
      } catch (Exception e) {
         e.printStackTrace();
         return new Bet(0, "Exception");
      }
   }

   private Bet checkOrfold(String s) {
      return new Bet(0, s);
   }

   private String getComment() {

      try {
         //if(tweets == null || tweets.size()==0){
         //getNewTweets();   

         //}
         int i = comment.length;
         if (i >= 0) {
            int index = rand.nextInt(i);
            String s = comment[index];//.get(index).toString();
            //tweets.remove(index);
            System.out.println(s);
            return s;
         }
         return "";
      } catch (Exception e) {
         // TODO Auto-generated catch block
         //e.printStackTrace();
         return "";
      }

   }

   /*
   	private void getNewTweets() throws TwitterException{
   		Twitter twitter = new TwitterFactory().getInstance();

   		Query query = new Query("source:twitter4j yusukey");
   		QueryResult result = twitter.search(query);

   		tweets = result.getTweets();
   	}
   */
   //	  Api api = Api.builder().build();
   /*	   for (Status status : api.publicTimeline().build().get()) {
        System.out.println(String.format("%s wrote '%s'", status.getUser().getName(), status.getText()));
   7   }*/
   //   return api.publicTimeline().build().get().get(0).getText();

   private Bet postRiver(Game game, Player me) throws Exception {
      float handrank = getHandRank(game, me);
      System.out.println("Handrank " + handrank);
      if (handrank > 0.8f) {
         return raise(game, me, (int) (game.getPotSize() * handrank), getComment());
      }
      return checkOrfold(getComment());
   }

   private Bet preFlop(Game game, Player me) throws Exception {
      Card[] cards = getMyCards(me);

      if (game.getHighestBet() == game.getBigBlind()) {
         if (cards[0].getRank() > 10 && cards[1].getRank() > 10) {
            if (cards[0].getRank() == cards[1].getRank()) {
               return raise(game, me, game.getBigBlind(), getComment());
            }
            if (cards[0].getRank() == 14) {
               if (cards[1].getRank() == 13) {
                  return raise(game, me, game.getBigBlind(), getComment());
               }
               if (cards[1].getRank() == 12) {
                  return raise(game, me, game.getBigBlind(), getComment());
               }
               if (cards[1].getRank() == 11) {
                  return raise(game, me, game.getBigBlind(), getComment());
               }
            }
            if (cards[1].getRank() == 14) {
               if (cards[0].getRank() == 13) {
                  return raise(game, me, game.getBigBlind(), getComment());
               }
               if (cards[0].getRank() == 12) {
                  return raise(game, me, game.getBigBlind(), getComment());
               }
               if (cards[0].getRank() == 11) {
                  return raise(game, me, game.getBigBlind(), getComment());
               }
            }
         } else {
            checkOrFold(getComment());
         }
      } else {
         if (cards[0].getRank() > 10 && cards[1].getRank() > 10) {
            if (cards[0].getRank() == cards[1].getRank()) {
               return call(game, me, getComment());
            }
            if (cards[0].getRank() == 14) {
               if (cards[1].getRank() == 13) {
                  return call(game, me, getComment());
               }
               if (cards[1].getRank() == 12) {
                  return call(game, me, getComment());
               }
               if (cards[1].getRank() == 11) {
                  return call(game, me, getComment());
               }
            }
            if (cards[1].getRank() == 14) {
               if (cards[0].getRank() == 13) {
                  return call(game, me, getComment());
               }
               if (cards[0].getRank() == 12) {
                  return call(game, me, getComment());
               }
               if (cards[0].getRank() == 11) {
                  return call(game, me, getComment());
               }
            }
         } else if (cards[0].getRank() == cards[1].getRank()) {
            return call(game, me, getComment());
         } else {
            return checkOrFold(getComment());
         }
      }
      return checkOrFold(getComment());

   }

   private Bet preRiver(Game game, Player me) throws Exception {
      float handrank = getHandRank(game, me);
      System.out.println("Handrank " + handrank);
      if (handrank > 0.6f) {
         return raise(game, me, (int) (game.getPotSize() * handrank), getComment());
      }
      return checkOrfold(getComment());
   }

   private Bet preTurn(Game game, Player me) throws Exception {
      float handrank = getHandRank(game, me);
      System.out.println("Handrank " + handrank);
      if (handrank > 0.7f) {
         return raise(game, me, (int) (game.getPotSize() * handrank), getComment());
      }
      return checkOrfold(getComment());
   }

}
