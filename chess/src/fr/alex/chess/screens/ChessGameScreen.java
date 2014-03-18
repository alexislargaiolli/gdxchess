package fr.alex.chess.screens;

import java.util.List;

import com.alonsoruibal.chess.Board;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;

import fr.alex.chess.ChessCamController;
import fr.alex.chess.ChessCamController.State;
import fr.alex.chess.ChessGame;
import fr.alex.chess.model.MChessBoard;
import fr.alex.chess.model.ui.PromotionWidget;
import fr.alex.chess.net.request.MoveRequest;

public class ChessGameScreen implements Screen {

	public enum GameState {
		SYNCHRONIZE, INITIALIZATION, NEW_TURN, PIECE_SELECTION, DESTINATION_SELECTION, CHECK_MOVE, PERFORM_MOVE, WAITING_OPPONENT, PROMOTION, END
	}

	protected static final Vector3 cameraInitialPosition = new Vector3(30f, 30f, 30f);

	protected final ChessGame game;
	protected PerspectiveCamera cam;
	protected ModelBatch modelBatch;
	protected Environment environment;
	protected ChessCamController camController;
	protected MChessBoard chessBoard;
	protected boolean white;
	protected GameState state;
	protected PromotionWidget promotionWidget;

	private SpriteBatch batch;
	private String infoMyTurn;
	private String infoOpponentTurn;
	private String infoChess;
	private String currentInfo;

	protected String promotion = "";
	protected int turnCount;

	protected Vector2 infoPosition;
	protected BitmapFont font;

	private MoveRequest currentMove;

	public ChessGameScreen(ChessGame game) {
		super();
		font = new BitmapFont();
		this.game = game;
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		modelBatch = new ModelBatch();
		batch = new SpriteBatch();
		cam = new PerspectiveCamera(67, w, h);
		chessBoard = new MChessBoard();
		environment = new Environment();
		camController = new ChessCamController(this, game, cam);

		chessBoard.initialize();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.6f, 0.6f, 0.6f, 1f));
		environment.add(new DirectionalLight().set(0.5f, 0.5f, 0.5f, -.9f, -.9f, -.9f));
		environment.add(new DirectionalLight().set(0.5f, 0.5f, 0.5f, .9f, .9f, .9f));

		Board board = new Board();
		board.startPosition();
		chessBoard.initFromEngine(board, game.instance.getWhite().getSkin(), game.instance.getBlack().getSkin());

		cam.position.set(chessBoard.getCenter().x, 40f, chessBoard.getCenter().z * .5f);
		cam.lookAt(chessBoard.getCenter().x, 0f, chessBoard.getCenter().z);
		cam.near = .1f;
		cam.far = 300f;
		cam.update();
		camController.setFocus(new Vector3(chessBoard.getCenter().x, 0f, chessBoard.getCenter().z));

		DefaultShader.defaultCullFace = 0;
		white = game.instance.getPlayerId().equals(game.instance.getWhite().getId());
		if (white) {
			camController.startCinematicWhiteBegin();
		} else {
			camController.startCinematicBlackBegin();
		}
		promotionWidget = new PromotionWidget();

		infoMyTurn = ChessGame.localize.getUiText("game.player.label.turn");
		infoOpponentTurn = ChessGame.localize.getUiText("game.opponent.label.turn");
		infoChess = ChessGame.localize.getUiText("game.chess");
		currentInfo = "";
		infoPosition = new Vector2();
		
		state = GameState.SYNCHRONIZE;

		turnCount = 0;
		game.network.synchronize();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		update(delta);
		draw();
	}

	public void update(float delta) {
		camController.update();
		chessBoard.update(delta);
		switch (state) {
		case SYNCHRONIZE:
			if (game.network.isSynchronizationOver()) {
				if (!game.network.getMoveReceived().isEmpty()) {
					synchronize(game.network.getMoveReceived());
					turnCount = game.network.getMoveReceived().get(game.network.getMoveReceived().size() - 1).getIndex();
					game.network.clearMove();
				}
				state = GameState.INITIALIZATION;
			}
			break;
		case INITIALIZATION:
			if (camController.getState() == State.IDLE) {
				state = GameState.NEW_TURN;
			}
			break;
		case NEW_TURN:
			if (checkGameState()) {
				state = GameState.END;
			} else {

				turnCount++;
				promotion = null;
				currentInfo = infoChess;
				if (isMyTurn()) { // Player turn
					Gdx.app.log("turn", "white");
					currentInfo = infoMyTurn;
					currentMove = new MoveRequest(turnCount, -1, -1, null, game.instance.getGameId(), game.instance.getPlayerId());
					state = GameState.PIECE_SELECTION;
				} else { // Opponent turn
					Gdx.app.log("turn", "black");
					currentInfo = infoOpponentTurn;
					state = GameState.WAITING_OPPONENT;
				}
			}
			break;
		case PIECE_SELECTION:

			break;
		case DESTINATION_SELECTION:

			break;
		case CHECK_MOVE:
			int moveId = chessBoard.getMoveId(currentMove.getStart(), currentMove.getEnd(), null);
			if (chessBoard.isLegalMove(moveId)) {
				if (chessBoard.isPromotion(moveId)) {
					state = GameState.PROMOTION;
					promotionWidget.reset();
				} else {
					state = GameState.PERFORM_MOVE;
				}
			} else {
				currentMove.setEnd(-1);
				state = GameState.DESTINATION_SELECTION;
			}
			break;
		case PROMOTION:
			if (Gdx.input.isTouched()) {
				promotionWidget.handleInput(Gdx.input.getX(), Gdx.input.getY());
			}
			if (promotionWidget.hasSelected()) {
				currentMove.setPromote(promotionWidget.getPromotion() + "");
				state = GameState.PERFORM_MOVE;
			}
			break;
		case PERFORM_MOVE:
			if (isMyTurn()) {
				game.network.sendMove(currentMove);
			}
			chessBoard.move(currentMove.getStart(), currentMove.getEnd(), currentMove.getPromote(), true);
			chessBoard.unhightLight();
			
			state = GameState.NEW_TURN;
			break;
		case WAITING_OPPONENT:
			if (game.network.hasReceivedMove()) {
				currentMove = game.network.getReceivedMove();
				game.network.clearMove();
				state = GameState.PERFORM_MOVE;
			}
			break;
		case END:
			break;
		default:
			break;
		}
	}

	private void synchronize(List<MoveRequest> moveRequests) {
		for (MoveRequest mr : moveRequests) {
			chessBoard.move(mr.getStart(), mr.getEnd(), mr.getPromote(), false);
		}
	}

	/**
	 * Check the game state : chess, white win, black win, draw or not finished
	 */
	private boolean checkGameState() {
		int state = chessBoard.getState();
		if (state == 1) {
			if (white) {
				currentInfo = ChessGame.localize.getUiText("game.victory");
			} else {
				currentInfo = ChessGame.localize.getUiText("game.defeat");
			}
		} else if (state == -1) {
			if (white) {
				currentInfo = ChessGame.localize.getUiText("game.defeat");
			} else {
				currentInfo = ChessGame.localize.getUiText("game.victory");
			}
		} else if (state == 99) {
			currentInfo = ChessGame.localize.getUiText("game.draw");
		}
		if (state == 1 || state == -1 || state == 99) {
			return true;
		}
		return false;
	}

	private boolean isMyTurn(){
		return chessBoard.getTurn() == white;
	}
	
	public void handleClick(float x, float y) {
		if (state == GameState.PIECE_SELECTION || state == GameState.DESTINATION_SELECTION) {
			Ray ray = cam.getPickRay(x, y);
			switch (state) {
			case PIECE_SELECTION:
				int start = chessBoard.getClickedPiece(ray);
				if (start != -1) {
					if (chessBoard.isWhite(start) == white) {
						currentMove.setStart(start);
						chessBoard.highlight(start);
						state = GameState.DESTINATION_SELECTION;
					}
				}
				break;
			case DESTINATION_SELECTION:
				int end = chessBoard.getClicked(ray);
				if (end == -1) {
					chessBoard.unhightLight();
					state = GameState.PIECE_SELECTION;
				} else if (chessBoard.isPiece(end)) {
					if(chessBoard.isWhite(end) == white){
						currentMove.setStart(end);
						chessBoard.highlight(end);
					}
					else{
						currentMove.setEnd(end);
						state = GameState.CHECK_MOVE;
					}
				} else {
					currentMove.setEnd(end);
					state = GameState.CHECK_MOVE;
				}
				break;
			case CHECK_MOVE:
				break;
			case END:
				break;
			case INITIALIZATION:
				break;
			case NEW_TURN:
				break;
			case PERFORM_MOVE:
				break;
			case PROMOTION:
				break;
			case SYNCHRONIZE:
				break;
			case WAITING_OPPONENT:
				break;
			default:
				break;
			}
		}
	}

	public void draw() {
		modelBatch.begin(cam);
		chessBoard.draw(modelBatch, environment);
		modelBatch.end();
		batch.begin();
		font.draw(batch, currentInfo, infoPosition.x, infoPosition.y);
		if (state == GameState.PROMOTION) {
			promotionWidget.draw(batch);
		}
		
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		promotionWidget.resize(width, height);
		infoPosition = new Vector2(width * .05f, height * .05f);
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(camController);
	}

	@Override
	public void hide() {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {

	}

}
