package fr.alex.chess;

public class GameInstanceSettings {
	private Player white;
	private Player black;
	
	public GameInstanceSettings() {
		super();
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
	
}
