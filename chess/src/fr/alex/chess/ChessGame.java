package fr.alex.chess;

import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;

import fr.alex.chess.model.ChessModelCreator;
import fr.alex.chess.net.ClientProducer;
import fr.alex.chess.net.Net;
import fr.alex.chess.screens.ConnectionScreen;
import fr.alex.chess.utils.LocalizationManager;
import fr.alex.chess.utils.LocalizationManager.Language;

public class ChessGame extends Game {

	public static TweenManager tween;
	public static LocalizationManager localize;
	public static ChessModelCreator model;
	public GameInstance instance;
	public AssetManager assets;	
	public Net network;
	public ClientProducer clientProducer;
	public String generalServiceUrl;
	public int generalServicePort;
	public String gameServiceUrl;
	public int gameServicePort;	
	
	@SuppressWarnings("static-access")
	@Override
	public void create() {
		Gdx.graphics.setVSync(false);
		Gdx.app.setLogLevel(Gdx.app.LOG_DEBUG);
		Gdx.app.log("ChessGame", "create()");
		localize = new LocalizationManager();
		localize.setLanguage(Language.fr);
		tween = new TweenManager();
		network = new Net(this);
		assets = new AssetManager();		
		model = new ChessModelCreator(this);
		model.load("defaut");
		if (instance == null) {
			instance = new GameInstance("5325ff8ede38d77a3529192e", "53242eac02c2c52814512238");
			//instance = new GameInstance("531b4f8b05d605710c4ba22e","531b12489ea8d5781e941ef3");
			Gdx.app.log("ChessGame.create()", "Static - gameid: " + instance.gameId + " - playerid: " + instance.getPlayerId());
		}
		else{
			Gdx.app.log("ChessGame.create()", "From parameter - gameid: " + instance.gameId + " - playerid: " + instance.getPlayerId());
		}
		this.network.connect(instance.getGameId());
		this.setScreen(new ConnectionScreen(this));
	}

	@Override
	public void dispose() {
		assets.dispose();
	}

	@Override
	public void render() {
		super.render();
		tween.update(Gdx.graphics.getDeltaTime());
	}
}
