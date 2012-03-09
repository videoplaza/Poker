package com.videoplaza.poker.server.util;

import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.videoplaza.poker.game.model.Card;
import com.videoplaza.poker.game.model.Game;
import com.videoplaza.poker.game.model.Player;

public class GameMessageUtil {
   public static Game applyPlayerPerpective(Game game, Player player) {
      Game g = new Game(game);
      for (Player p : g.getPlayers()) {
         if (player.getPosition() != p.getPosition()) {
            p.setHoleCards(new ArrayList<Card>());
            p.setBotUrl("");
         }
      }
      g.setNextPlayer(player.getPosition());
      return g;
   }

   public static String serializeStateForPlayer(Game game, Player player) {
      XStream xstream = new XStream(new JettisonMappedXmlDriver());
      StringWriter jsonWriter = new StringWriter();
      xstream.toXML(GameMessageUtil.applyPlayerPerpective(game, player), jsonWriter);
      try {
         return URLEncoder.encode("game", "UTF-8") + "=" + URLEncoder.encode(jsonWriter.getBuffer().toString(), "UTF-8") + "\r\n\r\n";
      } catch (UnsupportedEncodingException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
         return "";
      }
   }
}
