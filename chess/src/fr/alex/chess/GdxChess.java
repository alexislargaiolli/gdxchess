package fr.alex.chess;

import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import fr.alex.chess.player.Player;
import fr.alex.chess.screens.BaseScreen;
import fr.alex.chess.screens.ConnectScreen;
import fr.alex.chess.screens.GameScreen;
import fr.alex.chess.screens.LoadingScreen;
import fr.alex.chess.screens.NetScreen;
import fr.alex.chess.screens.ScreenManager;
import fr.alex.chess.screens.UiApp;
import fr.alex.chess.utils.Game;
import fr.alex.chess.utils.LocalizationManager;
import fr.alex.chess.utils.LocalizationManager.Language;

public class GdxChess extends UiApp {
	
	@Override
	public void render() {
		
		Game.tween.update(Gdx.graphics.getDeltaTime());
		super.render();
		//Table.drawDebug(this.stage);
	}
	
	@Override
	protected String atlasPath() {
		return "ui/chessui.atlas";
	}

	@Override
	protected String skinPath() {

		return null;
	}

	@Override
	protected void styleSkin(Skin skin, TextureAtlas atlas) {
		new Styles().styleSkin(skin, atlas);
	}

	@Override
	protected BaseScreen getFirstScreen() {		
		LocalizationManager localize = new LocalizationManager();
		localize.setLanguage(Language.fr);
		Game.localize = localize;
		Game.tween = new TweenManager();
		Game.screens = new ScreenManager();
		Game.player = new Player();
		
		//Screen instantiation
		Game.chess = new GameScreen(this);
		LoadingScreen loading = new LoadingScreen(this);		
		Game.screens.addScreen("loading", loading);
		Game.screens.addScreen("net", new NetScreen(this));
		Game.screens.addScreen("connect", new ConnectScreen(this));
		Game.screens.addScreen("game", Game.chess);
		//Game.screens.addScreen("test", new TestScreen(this));
		loading.setNextScreen("connect");
		Game.screens.loadContent();
		
		return loading;
	}
}
