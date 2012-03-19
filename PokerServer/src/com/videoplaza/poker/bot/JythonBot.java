package com.videoplaza.poker.bot;

import org.python.core.PyInteger;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

import com.videoplaza.poker.game.model.Game;
import com.videoplaza.poker.game.model.Player;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

public class JythonBot extends AbstractTournamentBot {
	
	private static final Bot INSTANCE = new JythonBot();
	
	private static PythonInterpreter interpreter;
	
	static {
      interpreter = new PythonInterpreter();
      interpreter.exec("import sys");
      addSourcePath("src");
      addSourcePath("src-jython");
      interpreter.exec("from pokerbot import bot");
	}

   private static void addSourcePath(String dir) {
      String cwd = System.getProperty("user.dir");
      String path = cwd + File.separator + dir;
      interpreter.exec("sys.path.append('" + path + "')");
   }

   @Override
   public String getAvatarImageUrl() {
      interpreter.exec("image_url = bot.get_image_url()");
      return interpreter.get("image_url").toString();
   }

   @Override
   public String getCreator() {
      interpreter.exec("creator_name = bot.get_creator_name()");
      return interpreter.get("creator_name").toString();
   }

   @Override
   public String getName(HttpServletRequest req) {
      interpreter.exec("bot_name = bot.get_bot_name()");
      return interpreter.get("bot_name").toString();
   }


   @Override
	public Bet play(Game game, Player me, HttpServletRequest req) {
		System.out.println("---------------");
		System.out.println(game.toString());
		System.out.println(game.getCards().toString());
		interpreter.set("game_object", game);
		interpreter.set("me_object", me);
		interpreter.exec("chat_message, value = bot.game(game_object, me_object)");
		PyObject chatMessage = interpreter.get("chat_message");
		PyInteger value = (PyInteger) interpreter.get("value");
		Bet bet = new Bet();
		bet.setValue(value.getValue());
		bet.setChatMessage(chatMessage.toString());
		return bet;
	}
	
	public static Bot getInstance() {
		return INSTANCE;
	}

}
