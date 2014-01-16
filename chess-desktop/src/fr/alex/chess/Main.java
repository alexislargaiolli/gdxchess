package fr.alex.chess;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import fr.alex.chess.utils.Game;

public class Main {
	private static String host = "chess-enov.rhcloud.com";
	private static int hallPort = 8000;
	
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "chess";
		cfg.useGL20 = false;
		cfg.width = 480;
		cfg.height = 320;
		
		Game.com = new WSClient(host, hallPort);
		new LwjglApplication(new ChessGame(), cfg);
	}
}
