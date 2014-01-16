package fr.alex.chess.net;

import java.io.IOException;

import com.badlogic.gdx.utils.GdxRuntimeException;

import fr.alex.chess.player.GameInfo;
import fr.alex.chess.screens.GameScreen;
import fr.alex.chess.utils.Game;

public class Net {
	public static boolean enable = true;
	private static boolean gameIdReceived;
	private static String[] gameids;
	private static boolean gameListReceived;
	private static boolean gameListAsked;
	private static boolean gameIdAsked;
	private static ClientMSG client;
	private static GameScreen game;
	
	public static void connectGame(GameScreen game) {
		Net.game = game;
		if (enable) {
			try {
				connectToGame();
				game.connectionEtablished();
			} catch (GdxRuntimeException gdxEx) {
				gdxEx.printStackTrace();
			}
		}
	}

	public static void connect() {
		if (enable) {
			try {
				gameIdReceived = false;
				gameListReceived = false;
				client = new ClientMSG();
			} catch (GdxRuntimeException gdxEx) {
				gdxEx.printStackTrace();
			}
		} else {

		}
	}
	
	public static void disconnect(){
		if(enable && client != null && client.isConnected()){
			client.close();
		}
	}

	public static boolean isConnected() {
		return client != null ? client.isConnected() : false;
	}	
	
	public static void onMessage(String message){
		String messageType = message.substring(0, 1);
		message = message.substring(1, message.length());
		System.out.println("Net: receive " + message);
		if (messageType.equals("?")) { // Server requests info
			if(message.equals("name")){
				Net.sendToHallSocket("#name:"+Game.player.getName());
			}
		} else if (messageType.equals("#")) { // Server pushes info
			if (message.startsWith("games")) {
				gameids = message.substring(message.indexOf(":") + 1, message.length()).split(";");							
				gameListAsked = false;
				gameListReceived = true;
			} else if (message.startsWith("gameid")) {							
				GameInfo.gameid = message.substring(message.indexOf(":") + 1, message.length());
				gameIdReceived = true;
				gameIdAsked = false;
			}
			else if(message.equals("empty")){
				gameListAsked = false;
				gameListReceived = true;
				gameids = null;
			}
			else if (message.equals("white")) {
				Game.player.setWhite(true);
			} else if (message.equals("black")) {
				Game.player.setWhite(false);
			} else if (message.equals("yourturn")) {
				
			} else if (message.equals("opponentTurn")) {

			} else if (message.equals("waitingopponent")) {
				
			} else if (message.equals("opponentFound")) {
				game.opponentFound();
			} else if (message.startsWith("move")) {				
				String lastMove = message.substring(message.indexOf(":") + 1, message.length());
				game.opponentMoved(lastMove);
			}
			else if(message.equals("opponentHasLeft")){				
				game.opponentLeft();				
			}
		}
	}
	
	public static void connectToGame(){
		Net.sendToHallSocket("#connect:"+GameInfo.gameid);
		System.out.println("Net: connect");
	}
	
	public static void askGameList() throws IOException {
		gameListReceived = false;
		gameListAsked = true;
		sendToHallSocket("?games");
	}

	public static void askGameId() throws IOException {
		gameIdReceived = false;
		gameIdAsked = true;
		sendToHallSocket("?create");
	}

	public static void sendToHallSocket(String message) {	
		if (enable && client != null) {			
			client.sendMessage(message);
			System.out.println("Data sent: " + message);
		}
	}

	public static void sendMove(int start, int end) throws IOException {
		sendToHallSocket("#move:" + start + ";" + end);
	}

	public static void sendPromoteMove(int start, int end, char promoteChoice) throws IOException {
		sendToHallSocket("#move:" + start + ";" + end + ";" + promoteChoice);
	}
	
	public static String[] getGameids() {
		return gameids;
	}

	public static boolean isGameIdReceived() {
		return gameIdReceived;
	}

	public static boolean isGameListReceived() {
		return gameListReceived;
	}

	public static boolean isGameListAsking() {
		return gameListAsked;
	}

	public static boolean isGameIdAsked() {
		return gameIdAsked;
	}

}
