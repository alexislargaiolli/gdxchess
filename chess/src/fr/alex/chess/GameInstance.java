package fr.alex.chess;

public class GameInstance {
	protected String gameId;
	protected String gameName;
	protected Player player1;
	protected Player player2;
	protected GameInstanceSettings settings;
	
	public GameInstanceSettings getSettings() {
		return settings;
	}

	public void setSettings(GameInstanceSettings settings) {
		this.settings = settings;
	}

	public GameInstance(String name) {
		super();
		this.gameName = name;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public Player getPlayer1() {
		return player1;
	}

	public void setPlayer1(Player player1) {
		this.player1 = player1;
	}

	public Player getPlayer2() {
		return player2;
	}

	public void setPlayer2(Player player2) {
		this.player2 = player2;
	}	
}
