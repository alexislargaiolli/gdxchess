package fr.alex.chess;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "chess";
		cfg.useGL20 = true;
		cfg.width = 1024;
		cfg.height = 768;
		
		ChessGame game = new ChessGame();
		game.clientProducer = new WSClientProducer();
		game.gameServiceUrl = "chess-enov.rhcloud.com";
		game.gameServicePort = 8000;
		game.generalServiceUrl = "chess-enov.rhcloud.com";
		game.generalServicePort = 8000;
		
		new LwjglApplication(game, cfg);
	}
}
