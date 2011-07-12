package com.videoplaza.poker.server.game;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.videoplaza.poker.server.servlets.DisplayServlet;
import com.videoplaza.poker.server.servlets.PokerServlet;

public class PokerServer {
   public static void main(String[] args) throws Exception {
      Server server = new Server(8080);

      ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
      context.setContextPath("/");
      server.setHandler(context);

      ServletHolder holder = context.addServlet(org.eclipse.jetty.servlet.DefaultServlet.class, "/files/*");
      holder.setInitParameter("resourceBase", "files/BotPoker");
      holder.setInitParameter("pathInfoOnly", "true");

      // Serve some hello world servlets
      context.addServlet(new ServletHolder(new PokerServlet()), "/*");
      context.addServlet(new ServletHolder(new DisplayServlet("Buongiorno Mondo")), "/it/*");
      context.addServlet(new ServletHolder(new DisplayServlet("Bonjour le Monde")), "/fr/*");
      server.start();
      server.join();

   }
}
