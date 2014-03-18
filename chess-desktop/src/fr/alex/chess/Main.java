package fr.alex.chess;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "chess";
		cfg.useGL20 = true;
		cfg.vSyncEnabled = false;
		cfg.width = 1024;
		cfg.height = 768;
		
		ChessGame game = new ChessGame();
		game.clientProducer = new WSClientProducer();
		game.gameServiceUrl = "127.0.0.1";
		game.gameServicePort = 3000;
		game.generalServiceUrl = "127.0.0.1";
		game.generalServicePort = 80;
		
		new LwjglApplication(game, cfg);
	}
}
