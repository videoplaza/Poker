package com.videoplaza.poker.server.bot;

public class BotResponse {
   public String message;
   public int amount;

   public BotResponse(String message, int amount) {
      this.message = message;
      this.amount = amount;
   }
}
