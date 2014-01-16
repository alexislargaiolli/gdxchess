package fr.alex.chess.player;

public class Player {
	private String name = "player";
	/**
	 * True if this player if white, false if he's black
	 */
	private boolean white = true;
	
	public Player() {
		super();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isWhite() {
		return white;
	}
	public void setWhite(boolean white) {
		this.white = white;
	}
}
