package fr.alex.chess.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;

import fr.alex.chess.ChessGame;
import fr.alex.chess.GameInstance;

public class GwtLauncher extends GwtApplication {

	private ChessGame game;

	@Override
	public GwtApplicationConfiguration getConfig() {
		GwtApplicationConfiguration cfg = new GwtApplicationConfiguration(1024,
				768);
		cfg.antialiasing = true;
		return cfg;
	}

	@Override
	public ApplicationListener getApplicationListener() {				
		game = new ChessGame();
		game.clientProducer = new GWTClientProducer();
		game.gameServiceUrl = "127.0.0.1";
		game.gameServicePort = 3000;
		game.generalServiceUrl = "127.0.0.1";
		game.generalServicePort = 80;
		return game;
	}

	@Override
	public void onModuleLoad() {
		super.onModuleLoad();
		Element elt = DOM.getElementById("embed-fr.alex.chess.GwtDefinition");
		String[] classes = elt.getClassName().split(" ");
		String gameid = "";
		String playerid = "";
		for(String cla : classes){
			if(cla.startsWith("gameid")){
				gameid = cla.replace("gameid-", "");
			}
			else if(cla.startsWith("playerid")){
				playerid = cla.replace("playerid-", "");
			}
		}
		game.instance = new GameInstance(gameid, playerid);
		// retrieve the map of GET parameters
		/*Map<String, List<String>> getParams = Window.Location.getParameterMap();
		String gameid = getParams.get("gameid").get(0);
		
		String playerid = getParams.get("playerid").get(0);
		instance = new GameInstance(gameid, playerid);*/
	}
}