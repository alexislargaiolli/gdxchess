package fr.alex.chess.net;

import fr.alex.chess.utils.Game;

public class GameListItem {
	private String gameid;
	private String prefix;
	
	public String getGameid() {
		return gameid;
	}

	public void setGameid(String gameid) {
		this.gameid = gameid;
	}

	public GameListItem(String gameid) {
		super();
		this.gameid = gameid;
		prefix = Game.localize.getUiText("net.gamelist.item") + " ";
	}

	@Override
	public String toString() {
		return prefix + gameid;
	}
	
	public static GameListItem fromString(String s){
		String id = s.replace(Game.localize.getUiText("net.gamelist.item")+ " ", "");
		return new GameListItem(id);
	}
}
