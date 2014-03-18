package fr.alex.chess.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import fr.alex.chess.ChessGame;

public class ConnectionScreen implements Screen {

	private final ChessGame game;
	private BitmapFont font;
	private SpriteBatch batch;
	private final String loading = "Chargement";
	private final String error = "Erreur";
	private Vector2 pos;
	
	public ConnectionScreen(ChessGame game) {
		super();
		this.game = game;
		font = new BitmapFont();
		batch = new SpriteBatch();
		pos = new Vector2();		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		if(game.assets.update() && game.network.isConnected()){
			game.setScreen(new ChessGameScreen(game));
		}		
		else if(game.network.isError()){
			font.draw(batch, error, 10, 10);
		}
		else{
			font.draw(batch, loading, 10, 10);
		}
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		pos = new Vector2(width * .5f, height * .5f);
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
