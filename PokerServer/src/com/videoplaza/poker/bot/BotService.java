package com.videoplaza.poker.bot;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class BotService {

   public static int port = 8084;

   public static void main(String[] args) throws Exception {
      if (args.length > 0) {
         port = Integer.parseInt(args[0]);
      }

      Server server = new Server(port);

      ServletContextHandler context = new ServletContextHandler(server, "/bot", ServletContextHandler.SESSIONS);
      context.addServlet(new ServletHolder(new BotServlet()), "/*");

      server.start();
   }

}
