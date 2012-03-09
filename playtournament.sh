for i in {1..100}
do

java -Dfile.encoding=UTF-8 -classpath /Users/jakob/Workspaces/Playground/Poker/PokerServer/bin:/Users/jakob/Workspaces/Playground/Poker/PokerServer/lib/lambdaj-2.3.2-with-dependencies.jar:/Users/jakob/Workspaces/Playground/Poker/PokerServer/lib/jetty/jetty-ajp-7.3.0.v20110203.jar:/Users/jakob/Workspaces/Playground/Poker/PokerServer/lib/jetty/jetty-annotations-7.3.0.v20110203.jar:/Users/jakob/Workspaces/Playground/Poker/PokerServer/lib/jetty/jetty-client-7.3.0.v20110203.jar:/Users/jakob/Workspaces/Playground/Poker/PokerServer/lib/jetty/jetty-continuation-7.3.0.v20110203.jar:/Users/jakob/Workspaces/Playground/Poker/PokerServer/lib/jetty/jetty-deploy-7.3.0.v20110203.jar:/Users/jakob/Workspaces/Playground/Poker/PokerServer/lib/jetty/jetty-http-7.3.0.v20110203.jar:/Users/jakob/Workspaces/Playground/Poker/PokerServer/lib/jetty/jetty-io-7.3.0.v20110203.jar:/Users/jakob/Workspaces/Playground/Poker/PokerServer/lib/jetty/jetty-jmx-7.3.0.v20110203.jar:/Users/jakob/Workspaces/Playground/Poker/PokerServer/lib/jetty/jetty-jndi-7.3.0.v20110203.jar:/Users/jakob/Workspaces/Playground/Poker/PokerServer/lib/jetty/jetty-plus-7.3.0.v20110203.jar:/Users/jakob/Workspaces/Playground/Poker/PokerServer/lib/jetty/jetty-policy-7.3.0.v20110203.jar:/Users/jakob/Workspaces/Playground/Poker/PokerServer/lib/jetty/jetty-rewrite-7.3.0.v20110203.jar:/Users/jakob/Workspaces/Playground/Poker/PokerServer/lib/jetty/jetty-security-7.3.0.v20110203.jar:/Users/jakob/Workspaces/Playground/Poker/PokerServer/lib/jetty/jetty-server-7.3.0.v20110203.jar:/Users/jakob/Workspaces/Playground/Poker/PokerServer/lib/jetty/jetty-servlet-7.3.0.v20110203.jar:/Users/jakob/Workspaces/Playground/Poker/PokerServer/lib/jetty/jetty-servlets-7.3.0.v20110203.jar:/Users/jakob/Workspaces/Playground/Poker/PokerServer/lib/jetty/jetty-util-7.3.0.v20110203.jar:/Users/jakob/Workspaces/Playground/Poker/PokerServer/lib/jetty/jetty-webapp-7.3.0.v20110203.jar:/Users/jakob/Workspaces/Playground/Poker/PokerServer/lib/jetty/jetty-websocket-7.3.0.v20110203.jar:/Users/jakob/Workspaces/Playground/Poker/PokerServer/lib/jetty/jetty-xml-7.3.0.v20110203.jar:/Users/jakob/Workspaces/Playground/Poker/PokerServer/lib/servlet-api-2.5.jar:/Users/jakob/Workspaces/Playground/Poker/PokerServer/lib/xstream/cglib-nodep-2.2.jar:/Users/jakob/Workspaces/Playground/Poker/PokerServer/lib/xstream/dom4j-1.6.1.jar:/Users/jakob/Workspaces/Playground/Poker/PokerServer/lib/xstream/jdom-1.1.jar:/Users/jakob/Workspaces/Playground/Poker/PokerServer/lib/xstream/jettison-1.0.1.jar:/Users/jakob/Workspaces/Playground/Poker/PokerServer/lib/xstream/joda-time-1.6.jar:/Users/jakob/Workspaces/Playground/Poker/PokerServer/lib/xstream/stax-1.2.0.jar:/Users/jakob/Workspaces/Playground/Poker/PokerServer/lib/xstream/stax-api-1.0.1.jar:/Users/jakob/Workspaces/Playground/Poker/PokerServer/lib/xstream/wstx-asl-3.2.7.jar:/Users/jakob/Workspaces/Playground/Poker/PokerServer/lib/xstream/xml-writer-0.2.jar:/Users/jakob/Workspaces/Playground/Poker/PokerServer/lib/xstream/xom-1.1.jar:/Users/jakob/Workspaces/Playground/Poker/PokerServer/lib/xstream/xpp3_min-1.1.4c.jar:/Users/jakob/Workspaces/Playground/Poker/PokerServer/lib/xstream/xstream-1.3.1.jar:/Users/jakob/Workspaces/Playground/Poker/PokerServer/lib/xstream/xstream-benchmark-1.3.1.jar:/Users/jakob/Workspaces/Playground/Poker/PokerServer/lib/junit-4.0.jar com.videoplaza.poker.server.game.PokerServer > pokeroutput.txt &

sleep 1

curl 'http://localhost:8080/?table_file=&action=load_conf' > /dev/null

curl 'http://localhost:8080/?table=&deal_pause=0&end_pause=0&action=set_pause_lengths' > /dev/null

curl 'http://localhost:8080/?table=&action=start_game' > /dev/null

grep 'Tournament ended, winner was' pokeroutput.txt >> pokerresults.txt

tail -n1 pokerresults.txt

ps -a | grep [c]om.videoplaza.poker.server.game.PokerServer | sed 's/ .*//' | xargs kill

echo "Done."

done
