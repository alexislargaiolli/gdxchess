package fr.alex.chess.net;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import fr.alex.chess.ChessGame;
import fr.alex.chess.GameInstanceSettings;
import fr.alex.chess.Player;
import fr.alex.chess.net.request.AuthReponse;
import fr.alex.chess.net.request.MoveRequest;

public class Net {
	private ClientMSG client;
	private ChessGame game;
	private String url;
	private boolean error;
	private boolean synchronizationOver;
	private List<MoveRequest> moveReceived;

	public Net(ChessGame game) {
		this.game = game;
		this.synchronizationOver = false;
		moveReceived = new ArrayList<MoveRequest>();
		this.url = game.gameServiceUrl + ":" + game.gameServicePort;
	}

	public void connect(String gameId) {
		Gdx.app.log("Net", "connect()");
		this.client = new ClientMSG(game.clientProducer.produceClient(game.gameServiceUrl, game.gameServicePort), game);
		this.client.connect();
	}

	public void disconnect() {
		if (client != null && client.isConnected()) {
			client.close();
		}
	}

	public boolean isConnected() {
		if (client == null)
			return false;
		if (!client.isConnected())
			return false;

		return game.instance.isReady();
	}

	public void onMessage(String message) {
		Gdx.app.log("Net", "message(): " + message);
		JsonReader reader = new JsonReader();
		JsonValue json = reader.parse(message);
		String type = json.get("type").asString();
		if (type.equals("auth")) {
			handleAuth();
		} else if (type.equals("synch")) {

		} else if (type.equals("auth-response")) {
			handleAuthReponse(json);
		} else if (type.equals("move")) {
			handleMove(json);
		}
	}

	private void handleAuth() {
		AuthReponse response = new AuthReponse(game.instance.getGameId(), game.instance.getPlayerId());
		client.sendMessage(response.toJSON());
	}

	private void handleAuthReponse(JsonValue json) {
		if (json.getInt("auth") == 1) {
			String opponentId = json.getString("opponent");
			String creatorId = json.getString("creator");
			game.instance.setBlack(new Player(opponentId));
			game.instance.setWhite(new Player(creatorId));
			game.instance.setSettings(new GameInstanceSettings());
		} else {
			error = true;
		}
	}

	public void handleMove(JsonValue json) {
		MoveRequest move = new MoveRequest(json.getInt("index"), json.getInt("start"), json.getInt("end"), json.getString("promote"), json.getString("gameId"), json.getString("playerId"));
		moveReceived.add(move);
	}

	public void sendMove(MoveRequest move) {		
		client.sendMessage(move.toJSON());
	}

	public void synchronize() {
		Gdx.app.log("Net", "synchronize()");
		String url = "http://" + game.gameServiceUrl + ":" + game.gameServicePort + "/game/moves/" + game.instance.getGameId();
		Gdx.app.log("synchronize", url);
		HttpRequest request = new HttpRequest("GET");
		request.setUrl(url);
		Gdx.net.sendHttpRequest(request, new HttpResponseListener() {

			@Override
			public void handleHttpResponse(HttpResponse httpResponse) {

				JsonReader reader = new JsonReader();
				String response = httpResponse.getResultAsString();
				JsonValue json = reader.parse(response);

				Iterator<JsonValue> it = json.iterator();
				while (it.hasNext()) {
					JsonValue child = it.next();
					int index = child.getInt("index");
					int start = child.getInt("start");
					int end = child.getInt("end");
					String promote = child.getString("promote");
					String gameId = child.getString("game");
					String playerId = child.getString("player");
					MoveRequest move = new MoveRequest(index, start, end, promote, gameId, playerId);
					moveReceived.add(move);
				}
				synchronizationOver = true;
			}

			@Override
			public void failed(Throwable t) {
				synchronizationOver = false;
				t.printStackTrace();
				Gdx.app.log("synchronize()", "error : " + t.getMessage());
			}
		});
	}

	public boolean hasReceivedMove() {
		return moveReceived.size() > 0;
	}

	public MoveRequest getReceivedMove() {
		return moveReceived.get(0);
	}

	public void clearMove() {
		moveReceived.clear();
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isError() {
		return error;
	}

	public boolean isSynchronizationOver() {
		return synchronizationOver;
	}

	public void setSynchronizationOver(boolean synchronizationOver) {
		this.synchronizationOver = synchronizationOver;
	}

	public List<MoveRequest> getMoveReceived() {
		return moveReceived;
	}

	public void setMoveReceived(List<MoveRequest> moveReceived) {
		this.moveReceived = moveReceived;
	}

}
