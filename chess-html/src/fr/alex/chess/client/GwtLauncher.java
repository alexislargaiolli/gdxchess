package fr.alex.chess.client;

import fr.alex.chess.ChessGame;
import fr.alex.chess.utils.Game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

public class GwtLauncher extends GwtApplication {
	
	private static String host = "chess-enov.rhcloud.com";
	private static int hallPort = 8000;
	
	@Override
	public GwtApplicationConfiguration getConfig () {
		GwtApplicationConfiguration cfg = new GwtApplicationConfiguration(480, 320);
		return cfg;
	}

	@Override
	public ApplicationListener getApplicationListener () {
		Game.com = new GWTClient(host, hallPort);
		return new ChessGame();
	}
}