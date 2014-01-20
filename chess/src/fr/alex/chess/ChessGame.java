package fr.alex.chess;

import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import fr.alex.chess.net.ClientProducer;
import fr.alex.chess.net.Network;
import fr.alex.chess.screens.MenuScreen;
import fr.alex.chess.utils.LocalizationManager;
import fr.alex.chess.utils.LocalizationManager.Language;

public class ChessGame extends Game {
	
	public static TweenManager tween;
	public static LocalizationManager localize;
	
	static{
		
	}
	
	public SpriteBatch batch;
	public AssetManager assets;
	public Network network;
	public Skin skin;
	public ClientProducer clientProducer;	
	public String generalServiceUrl;
	public int generalServicePort;
	public String gameServiceUrl;
	public int gameServicePort;
	public Player player;
	
	@Override
	public void create() {				
		localize = new LocalizationManager();
		localize.setLanguage(Language.fr);
		tween = new TweenManager();
		network = new Network();
		assets = new AssetManager();
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		this.setScreen(new MenuScreen(this));
	}

	@Override
	public void dispose() {
		assets.dispose();
	}

	@Override
	public void render() {		
		super.render();
		assets.update();
		tween.update(Gdx.graphics.getDeltaTime());
	}
}
