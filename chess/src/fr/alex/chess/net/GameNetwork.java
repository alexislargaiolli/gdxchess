package fr.alex.chess.net;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

import fr.alex.chess.ChessGame;
import fr.alex.chess.GameInstance;

public class GameNetwork {
	protected ClientMSG client;
	protected GameInstance gameInstance;
	protected ChessGame game;
	protected String REQUEST_PREFIX;

	public GameNetwork(ChessGame game, GameInstance gameInstance) {
		this.game = game;
		this.gameInstance = gameInstance;
		String url = game.generalServiceUrl;
		int port = game.gameServicePort;
		client = new ClientMSG(game.clientProducer.produceClient(url, port));
		REQUEST_PREFIX = "";
	}
	
	public void requestSettings(){
		client.sendMessage("");
		Json json = new Json();
		json.writeValue("game", gameInstance.getGameId());
	}
}
