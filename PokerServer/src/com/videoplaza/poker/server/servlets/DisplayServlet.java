package com.videoplaza.poker.server.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.videoplaza.poker.game.model.Game;
import com.videoplaza.poker.server.game.Lobby;

public class DisplayServlet extends HttpServlet {
   /**
    * 
    */
   private static final long serialVersionUID = 1L;

   String greeting;

   public DisplayServlet() {
   }

   public DisplayServlet(String string) {
      greeting = string;
   }

   @Override
   protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      Game game = Lobby.getInstance().getDisplay();
      if (game != null) {
         resp.setStatus(HttpServletResponse.SC_OK);
         //resp.setContentType("text/html;charset=utf-8");
         resp.setContentType("application/json;charset=utf-8");
         XStream xstream = new XStream(new JettisonMappedXmlDriver());
         xstream.toXML(game, resp.getWriter());
         resp.flushBuffer();
      } else {
         resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
      }
   }
}
