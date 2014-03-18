package fr.alex.chess;

public class GameInstance {
	protected String gameId;
	protected String playerId;
	protected Player white;
	protected Player black;
	protected GameInstanceSettings settings;
	
	public GameInstance(String gameId, String playerId) {
		this.gameId = gameId;
		this.playerId = playerId;
	}
	
	public boolean isReady(){
		return gameId != null && playerId != null && white != null && black != null && settings != null;
	}

	public GameInstanceSettings getSettings() {
		return settings;
	}

	public void setSettings(GameInstanceSettings settings) {
		this.settings = settings;
	}	

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}
	
	public Player getWhite() {
		return white;
	}	
	
	public void setWhite(Player white) {
		this.white = white;
	}

	public Player getBlack() {
		return black;
	}

	public void setBlack(Player black) {
		this.black = black;
	}

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}
}
