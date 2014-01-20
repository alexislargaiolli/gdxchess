package fr.alex.chess.net;

import java.util.ArrayList;
import java.util.List;

import fr.alex.chess.GameInstance;
import fr.alex.chess.Player;

public class Network {
	
	protected List<GameInstance> games;	
	
	public Network() {
		super();
		games = new ArrayList<GameInstance>();
		Player opponent = new Player("Opponent");
		GameInstance game = new GameInstance("Partie 1");
		game.setPlayer1(opponent);
		games.add(game);
		game = new GameInstance("Partie 2");
		game.setPlayer1(opponent);
		games.add(game);
	}

	public List<GameInstance> getGames() {
		return games;
	}

	public void setGames(List<GameInstance> games) {
		this.games = games;
	}

	public void connect(){
		
	}
	
	public boolean isConnected(){
		return true;
	}
	
}
