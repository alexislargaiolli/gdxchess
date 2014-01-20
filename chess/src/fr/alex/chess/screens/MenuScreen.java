package fr.alex.chess.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import fr.alex.chess.ChessGame;

public class MenuScreen implements Screen{

	private final ChessGame game;
	private Stage stage;
	
	public MenuScreen(final ChessGame game) {
		super();
		this.game = game;
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		Table main = new Table();
		TextButton play = new TextButton("Play", game.skin);
		play.addListener(new ClickListener(){
			public void clicked (InputEvent event, float x, float y) {
				game.setScreen(new NetworkScreen(game));
			}
		});
		TextButton options = new TextButton("Options", game.skin);
		main.setFillParent(true);
		main.add(play).width(150).pad(5);
		main.row();
		main.add(options).width(150).pad(5);
		stage.addActor(main);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		stage.act();
		stage.draw();
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
