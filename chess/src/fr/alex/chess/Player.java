package fr.alex.chess;

public class Player {
	protected String pseudo;

	public Player(String pseudo) {
		super();
		this.pseudo = pseudo;
	}

	public String getPseudo() {
		return pseudo;
	}

	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}	
}
