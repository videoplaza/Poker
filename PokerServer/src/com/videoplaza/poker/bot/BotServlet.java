package com.videoplaza.poker.bot;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.URLCodec;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.videoplaza.poker.game.model.Game;
import com.videoplaza.poker.game.model.Player;

public class BotServlet extends HttpServlet {

   private static final long serialVersionUID = 3277126036475556350L;
   private URLCodec encoder = new URLCodec();

   private Bot botImpl = new YourBot();

   @Override
   protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      doPost(req, resp);
   }

   @Override
   protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

      if (req.getParameter("init") != null) {
         // get bot params
         String name = botImpl.getName(req);
         String creator = botImpl.getCreator();
         String imageUrl = botImpl.getAvatarImageUrl();
         StringBuilder sb = new StringBuilder();
         // write bot params to response
         sb.append("name=");
         sb.append(URLEncoder.encode(name == null ? "" : name, "UTF-8"));
         sb.append("&creator=");
         sb.append(URLEncoder.encode(creator == null ? "" : creator, "UTF-8"));
         sb.append("&image_url=");
         sb.append(URLEncoder.encode(imageUrl == null ? "" : imageUrl, "UTF-8"));
         resp.getWriter().println(sb.toString());
         return;
      }

      String xml = req.getParameter("game");
      XStream xstream = new XStream(new JettisonMappedXmlDriver());
      Game game = null;
      try {
         game = (Game) xstream.fromXML(xml);
      } catch (Exception e1) {
         e1.printStackTrace();
      }
      if (null == game) {
         try {
            resp.getWriter().println("0 " + encoder.encode("I failed at reading the game data!"));
         } catch (EncoderException e) {
            e.printStackTrace();
         }
      } else {
         Bet bet;
         Player me = game.getPlayers().get(game.getNextPlayer());
         if (me == null) {
            String msg = "I am not in the list of players";
            System.err.println();
            bet = new Bet(0, msg);
         } else {
            bet = botImpl.play(game, me, req);
         }
         int value = null != bet ? bet.getValue() : 0;

         String chatMessage = "";
         if (null != bet.getChatMessage()) {
            chatMessage = " " + encode(bet.getChatMessage());
         }

         resp.getWriter().println(value + chatMessage);
      }

   }

   private String encode(String string) {
      String encodedString = null;
      try {
         encodedString = encoder.encode(string);
      } catch (EncoderException e) {
         System.err.println("Encoding of string failed.");
         e.printStackTrace();
         throw new RuntimeException("Encoding of string failed");
      }
      return encodedString;
   }

}