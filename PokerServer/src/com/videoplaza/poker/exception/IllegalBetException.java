package com.videoplaza.poker.exception;

public class IllegalBetException extends Exception {

   private static final long serialVersionUID = 1L;

   public IllegalBetException(String message) {
      super(message);
   }
}
