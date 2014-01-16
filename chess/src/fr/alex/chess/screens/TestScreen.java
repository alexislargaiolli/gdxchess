package fr.alex.chess.screens;

import com.alonsoruibal.chess.Config;
import com.alonsoruibal.chess.evaluation.CompleteEvaluator;
import com.alonsoruibal.chess.evaluation.Evaluator;
import com.alonsoruibal.chess.search.SearchEngine;
import com.alonsoruibal.chess.search.SearchParameters;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.math.Vector3;

import fr.alex.chess.ChessCamController;
import fr.alex.chess.model.BoardSettings;
import fr.alex.chess.model.MChessBoard;

public class TestScreen extends BaseScreen implements InputProcessor{
	protected PerspectiveCamera cam;
	protected ModelBatch modelBatch;
	protected Environment environment;
	protected ModelInstance pion;
	protected ChessCamController camController;
	protected MChessBoard board;
	protected final Vector3 cameraInitialPosition = new Vector3(30f, 30f, 30f);
	protected Evaluator evaluator;
	protected SearchEngine engine;
	protected SearchParameters searchParameters;
	protected int timeValues[] = { 1000, 2000, 5000, 15000, 30000, 60000 };
	protected int timeDefaultIndex = 0;
	
	public TestScreen(UiApp app) {
		super(app);
		mainTable.setBackground(app.skin.getDrawable("window1"));
		mainTable.setColor(app.skin.getColor("lt-blue"));
		modelBatch = new ModelBatch();
		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		environment = new Environment();
		
		board = new MChessBoard();
		environment = new Environment();
		camController = new ChessCamController(cam);
	}	
	
	public void resetView() {
		//cam.position.set(board.getCenter().x, 35f, -15f);
		cam.position.set(board.getCenter().x, 35f, BoardSettings.width);
		cam.update();
		cam.lookAt(board.getCenter().x, 0f, board.getCenter().z);
		cam.update();
		//camController.update();
	}
	
	@Override
	public void onBackPress() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadContent() {
		
	}

	@Override
	public void initialize() {
		//cam.position.set(board.getCenter().x, 30f, -10f);
		cam.position.set(board.getCenter().x, 30f, BoardSettings.width+5f);
		cam.lookAt(board.getCenter().x, 0f, board.getCenter().z);
		cam.near = .1f;
		cam.far = 300f;
		cam.update();

		camController.setFocus(new Vector3(board.getCenter().x, 0f, board.getCenter().z));

		board.initialize();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.6f, 0.6f, 0.6f, 1f));
		environment.add(new DirectionalLight().set(0.5f, 0.5f, 0.5f, -.9f, -.9f, -.9f));
		environment.add(new DirectionalLight().set(0.5f, 0.5f, 0.5f, .9f, .9f, .9f));
		
		Config config = new Config();
		config.setTranspositionTableSize(8);
		engine = new SearchEngine(config);
		evaluator = new CompleteEvaluator(config);
		searchParameters = new SearchParameters();
		searchParameters.setMoveTime(timeValues[timeDefaultIndex]);

		board.initFromEngine(engine.getBoard());
		DefaultShader.defaultCullFace = GL10.GL_FRONT;
	}
	
	@Override
	public BaseScreen show(){
		UiApp.inputs.addProcessor(camController);
		UiApp.inputs.addProcessor(this);
		
		return this;
	}

	@Override
	public void dispose() {
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
	public void draw() {
		Gdx.gl.glClear(GL10.GL_DEPTH_BUFFER_BIT);	
		modelBatch.begin(cam);
		board.draw(modelBatch, environment);
		modelBatch.end();
	}

	@Override
	public void act(float delta) {
		camController.update();
		board.update(delta);
		super.act(delta);
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Keys.R) {
			camController.startCinematicBlackBegin();
		}
		if (keycode == Keys.E) {
			camController.startCinematicWhiteBegin();
		}
		if (keycode == Keys.D) {
			board.getPiece(5).hightlight(true);
			Vector3 target = board.getPiece(5).getPosition();
			camController.startEndCinematic(target);
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
