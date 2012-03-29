Videoplaza Poker Bot Tournament
===============================

Teach your computer to play poker against other computers!

Getting started
---------------

1. Clone the repository.
2. Edit *com.videoplaza.poker.bot.YourBot* to do something clever in the *play* method. Inspect the *Game* and *Player* arguments given to find out about the current state of the game. Don not forget to choose name and picture for your bot.
3. Run *com.videoplaza.poker.bot.PokerBotTest* as a JUnit test to run a couple of tournaments and see how well your bot does.
4. Repeat step 2-3 until it is time for competition.
5. Run *com.videoplaza.poker.bot.BotService* as a Java application
6. Give your hostname or IP-address to the person running the tournament and sit back.

Competition time!
-----------------
To run a tournament, start by editing *table.txt* to include URLs of all the participating bots (default is http://&lt;ip&gt;:8084/bot/). Then run *com.videoplaza.poker.server.game.PokerServer* as a Java application. 

Hook up a screen that everyone can watch (as well as some speakers if you are on a Mac). Go to http://localhost:8080/files/index.htm

Go to the administration web page at http://localhost:8080/ (on another screen), click "load_conf" and then "start_game". You can also change the pace of the tournament and raise the blinds using this page.

Technical details / Do I have to use Java?
------------------------------------------
The only requirement of a bot is to respond to HTTP POST requests with a single plain text line containing a space separated integer (your bet) and a message. The game state information will be in the post body as JSON. That being said, it is probably faster to get started with Java or Jython (see PokerServer/src-jython/pokerbot/bot.py).

About
-----
The poker tournament software was started as a little side project by some Videoplaza (http://www.videoplaza.com) employees in 2011. It is provided as is, but if you make something cool please send us a pull request!

