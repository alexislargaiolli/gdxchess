package fr.alex.chess;

public class Player {
	protected String id;
	protected String skin = "defaut";
	
	public Player(String id) {
		super();
		this.id = id;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSkin() {
		return skin;
	}

	public void setSkin(String skin) {
		this.skin = skin;
	}
}
