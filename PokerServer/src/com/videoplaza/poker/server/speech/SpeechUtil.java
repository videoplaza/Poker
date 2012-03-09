package com.videoplaza.poker.server.speech;

import java.io.IOException;

import com.videoplaza.poker.game.model.Player.Move;

public class SpeechUtil {

   /**
    * @param args
    */
   public static void main(String[] args) {
      //say("dun dun dun dun dun dun dun dun dun dun dun dun dun dun dun dun dun dun dun dun dun dun", Voice.BAD_NEWS);
      //say("jakob");
      say(Move.CALL.toString());
   }

   public static void say(String obsenity) {
      say(obsenity, null);
   }

   public static void say(String obsenity, Voice voice) {
      String[] cmd;
      if (voice != null) {
         cmd = new String[] { "say", "-v", voice.toString(), obsenity };
      } else {
         cmd = new String[] { "say", obsenity };
      }

      ProcessBuilder pb = new ProcessBuilder(cmd);
      try {

         Process p = pb.start();
         p.waitFor();

      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      } catch (InterruptedException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

   }

   public static enum Voice {
      // Male
      ALEX, BRUCE, FRED, JUNIOR, RALPH,
      // Female
      AGNES, KATHY, PRINCESS, VICKI, VICTORIA,
      // Novelty
      ALBERT, BAD_NEWS, BAHH, BELLS, BOING, BUBBLES, CELLOS, DERANGED, GOOD_NEWS, HYSTERICAL, PIPE_ORGAN, TRINOIDS, WHISPER, ZARVOX;

      @Override
      public String toString() {
         return (name().charAt(0) + name().toLowerCase().substring(1)).replace('_', ' ');
      }

   }

}
