package fr.alex.chess.screens;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import fr.alex.chess.ChessGame;
import fr.alex.chess.GameInstance;
import fr.alex.chess.GameInstanceSettings;

public class NetworkScreen implements Screen {

	private ChessGame game;
	private Stage stage;
	private Map<String, GameInstance> gameInstances;
	private List gameList;
	private String selectedGame;
	private TextButton connect;

	public NetworkScreen(final ChessGame game) {
		super();
		gameInstances = new HashMap<String, GameInstance>();
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		this.game = game;
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		Table header = new Table();
		header.setSize(w, h * .2f);
		header.setPosition(0, h * .8f);
		stage.addActor(header);
		// title.align( Align.center);
		Label lbTitle = new Label("Parties en ligne", game.skin);
		header.add(lbTitle);
		header.row();
		Table content = new Table();
		content.setSize(w, h * .6f);
		content.setPosition(0, h * .2f);
		gameList = new List(new String[0], game.skin);
		gameList.addListener(new ClickListener(){
			public void clicked (InputEvent event, float x, float y) {
				selectedGame = gameList.getSelection();
			}
		});
		content.add(gameList);
		stage.addActor(content);
		
		Table footer = new Table();
		footer.setSize(w, h * .2f);
		footer.setPosition(0, 0);
		connect = new TextButton("Start", game.skin);
		connect.addListener(new ClickListener(){
			public void clicked (InputEvent event, float x, float y) {
				GameInstance gameInstance = gameInstances.get(selectedGame);
				gameInstance.setPlayer2(game.player);
				GameInstanceSettings settings = new GameInstanceSettings();
				settings.setWhite(gameInstance.getPlayer2());
				settings.setBlack(gameInstance.getPlayer1());
				gameInstance.setSettings(settings);
				game.setScreen(new ChessGameScreen(game, gameInstance));
			}
		});
		footer.add(connect).width(150);
		stage.addActor(footer);
		
		refresh();
	}
	
	private void refresh(){
		selectedGame = null;
		for(GameInstance game : this.game.network.getGames()){
			gameInstances.put(game.getGameName(), game);
		}
		gameList.setItems(gameInstances.keySet().toArray(new String[gameInstances.size()]));
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		stage.act();
		stage.draw();
		Table.drawDebug(stage);
		connect.setVisible(selectedGame != null);
	}

	@Override
	public void resize(int width, int height) {
		stage.setViewport(width, height, true);
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
