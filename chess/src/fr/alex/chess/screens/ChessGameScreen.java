package fr.alex.chess.screens;

import com.alonsoruibal.chess.Board;
import com.alonsoruibal.chess.Move;
import com.alonsoruibal.chess.bitboard.BitboardUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import fr.alex.chess.ChessCamController;
import fr.alex.chess.ChessCamController.State;
import fr.alex.chess.ChessGame;
import fr.alex.chess.GameInstance;
import fr.alex.chess.model.MCase;
import fr.alex.chess.model.MChessBoard;
import fr.alex.chess.model.MChessEntity;
import fr.alex.chess.model.MPiece;
import fr.alex.chess.model.ui.EndGameWidget;
import fr.alex.chess.model.ui.PromotionWidget;

public class ChessGameScreen implements Screen {

	public enum GameState {
		INITIALIZATION, NEW_TURN, PIECE_SELECTION, DESTINATION_SELECTION, CHECK_MOVE, PERFORM_MOVE, WAITING_OPPONENT, PROMOTION, END
	}

	protected static final Vector3 cameraInitialPosition = new Vector3(30f, 30f, 30f);

	protected final ChessGame game;
	protected GameInstance gameInstance;
	protected PerspectiveCamera cam;
	protected ModelBatch modelBatch;
	protected Environment environment;
	protected ChessCamController camController;
	protected MChessBoard chessBoard;
	protected Board board;
	protected Stage stage;
	protected boolean white;
	protected GameState state;
	protected MPiece selectedPiece;
	protected MChessEntity destination;
	protected int move;
	protected PromotionWidget promotionWidget;
	protected int lastFromIndex;
	protected int lastToIndex;
	protected Label labelPlayerTurn;
	protected Label labelOpponentTurn;
	protected Label labelEchec;

	public ChessGameScreen(ChessGame game, GameInstance gameInstance) {
		super();
		this.game = game;
		this.gameInstance = gameInstance;
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		stage = new Stage(w, h);
		modelBatch = new ModelBatch();
		cam = new PerspectiveCamera(67, w, h);
		chessBoard = new MChessBoard();
		environment = new Environment();
		camController = new ChessCamController(this, game, cam);

		chessBoard.initialize();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.6f, 0.6f, 0.6f, 1f));
		environment.add(new DirectionalLight().set(0.5f, 0.5f, 0.5f, -.9f, -.9f, -.9f));
		environment.add(new DirectionalLight().set(0.5f, 0.5f, 0.5f, .9f, .9f, .9f));

		board = new Board();
		board.startPosition();
		chessBoard.initFromEngine(board, gameInstance.getSettings().getWhite().getSkin(), gameInstance.getSettings().getBlack().getSkin());

		cam.position.set(chessBoard.getCenter().x, 40f, chessBoard.getCenter().z * .5f);
		cam.update();
		cam.lookAt(chessBoard.getCenter().x, 0f, chessBoard.getCenter().z);
		cam.near = .1f;
		cam.far = 300f;
		cam.update();
		camController.setFocus(new Vector3(chessBoard.getCenter().x, 0f, chessBoard.getCenter().z));

		DefaultShader.defaultCullFace = 0;
		white = gameInstance.getSettings().getWhite() == game.player;
		if (white){
			camController.startCinematicWhiteBegin();
		}
		else{
			camController.startCinematicBlackBegin();
		}
		promotionWidget = new PromotionWidget(game.skin);
		promotionWidget.setFillParent(true);
		promotionWidget.setVisible(false);
		labelPlayerTurn = new Label(ChessGame.localize.getUiText("game.player.label.turn"), game.skin);
		labelOpponentTurn = new Label(ChessGame.localize.getUiText("game.opponent.label.turn"), game.skin);
		labelEchec = new Label(ChessGame.localize.getUiText("game.chess"), game.skin);
		labelEchec.setPosition(w * .5f - labelPlayerTurn.getWidth() * .5f, h * .85f);
		labelEchec.getColor().a = 0;
		stage.addActor(labelEchec);
		labelPlayerTurn.setPosition(w * .5f - labelPlayerTurn.getWidth() * .5f, h * .9f);
		labelPlayerTurn.getColor().a = 0;
		labelOpponentTurn.setPosition(w * .5f - labelOpponentTurn.getWidth() * .5f, h * .9f);
		labelOpponentTurn.getColor().a = 0;
		stage.addActor(labelPlayerTurn);
		stage.addActor(labelOpponentTurn);

		stage.addActor(promotionWidget);
		// promotionWidget.setPosition(200, 200);
		state = GameState.INITIALIZATION;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		update(delta);
		draw();
	}

	public void update(float delta) {
		stage.act(delta);
		camController.update();
		chessBoard.update(delta);
		switch (state) {
		case INITIALIZATION:
			if (camController.getState() == State.IDLE) {
				state = GameState.NEW_TURN;
			}
			break;
		case NEW_TURN:
			if (checkGameState()) {
				state = GameState.END;
			} else {
				labelEchec.addAction(Actions.alpha(board.getCheck() ? 1f : 0f, 0.5f, Interpolation.exp10));
				if (white == board.getTurn()) { // Player turn
					labelOpponentTurn.addAction(Actions.alpha(0, 0.5f, Interpolation.pow2Out));
					labelPlayerTurn.addAction(Actions.alpha(1f, 2f, Interpolation.pow2));

					state = GameState.PIECE_SELECTION;
				} else { // Opponent turn
					labelPlayerTurn.addAction(Actions.alpha(0, 0.5f, Interpolation.pow2Out));
					labelOpponentTurn.addAction(Actions.alpha(1f, 2f, Interpolation.pow2));
					state = GameState.WAITING_OPPONENT;
				}
			}
			break;
		case PIECE_SELECTION:

			break;
		case DESTINATION_SELECTION:

			break;
		case CHECK_MOVE:
			lastFromIndex = selectedPiece.getCurCase().getIndex();
			lastToIndex = 0;
			if (destination instanceof MPiece) {
				lastToIndex = ((MPiece) destination).getCurCase().getIndex();
			} else {
				lastToIndex = ((MCase) destination).getIndex();
			}
			move = Move.getFromString(board, BitboardUtils.index2Algebraic(lastFromIndex) + BitboardUtils.index2Algebraic(lastToIndex), true);
			if (board.isMoveLegal(move)) {
				System.out.println("Legal move: " + lastFromIndex + " - " + lastToIndex);
				if (Move.getMoveType(move) == 4) {
					state = GameState.PROMOTION;
					promotionWidget.reset();
					promotionWidget.setVisible(true);
				} else {
					state = GameState.PERFORM_MOVE;
				}
			} else {
				destination = null;
				state = GameState.DESTINATION_SELECTION;
			}
			break;
		case PROMOTION:
			if (promotionWidget.hasSelected()) {
				char promotion = promotionWidget.getPromotion();
				move = Move.getFromString(board, BitboardUtils.index2Algebraic(lastFromIndex) + BitboardUtils.index2Algebraic(lastToIndex) + promotion, true);
				promotionWidget.setVisible(false);
				state = GameState.PERFORM_MOVE;
			}
			break;
		case PERFORM_MOVE:
			switch (Move.getMoveType(move)) {
			case 1: // King side castling
				System.out.println("king casteling");
				castling(white, true);
				break;
			case 2: // Queen side castling
				System.out.println("queen castling");
				castling(white, false);
				break;
			case 3: // En passant
				System.out.println("en passant");
				long square = board.getPassantSquare();
				int eatenPieceIndex = BitboardUtils.algebraic2Index(BitboardUtils.square2Algebraic(square));
				if (board.getTurn()) {
					eatenPieceIndex -= 8;
				} else {
					eatenPieceIndex += 8;
				}
				destroy(chessBoard.getPiece(eatenPieceIndex));
				handleMove(selectedPiece, destination);
				break;
			case 4: // Promotion
				System.out.println("promotion");
				handleMove(selectedPiece, destination);
				break;
			default:
				System.out.println("normal move");
				handleMove(selectedPiece, destination);
				break;
			}
			board.doMove(move);
			selectedPiece.hightlight(false);
			selectedPiece = null;
			destination = null;
			white = !white;
			state = GameState.NEW_TURN;
			break;
		case WAITING_OPPONENT:

			break;
		}
	}

	/**
	 * Check the game state : chess, white win, black win, draw or not finished
	 */
	private boolean checkGameState() {
		int state = board.isEndGame();
		EndGameWidget endGameWidget = null;
		String message = "";
		if (state == 1) {
			if (white) {
				message = ChessGame.localize.getUiText("game.victory");
			} else {
				message = ChessGame.localize.getUiText("game.defeat");
			}
		} else if (state == -1) {
			if (white) {
				message = ChessGame.localize.getUiText("game.defeat");
			} else {
				message = ChessGame.localize.getUiText("game.victory");
			}
		} else if (state == 99) {
			message = ChessGame.localize.getUiText("game.draw");
		}
		if (state == 1 || state == -1 || state == 99) {
			labelOpponentTurn.addAction(Actions.alpha(0, 0.5f, Interpolation.pow2Out));
			labelPlayerTurn.addAction(Actions.alpha(0f, 0.5f, Interpolation.pow2));
			labelEchec.addAction(Actions.alpha(0f, 0.5f, Interpolation.pow2));
			
			endGameWidget = new EndGameWidget(game.skin, message);
			endGameWidget.setPosition(Gdx.graphics.getWidth() * .5f - endGameWidget.getWidth() * .5f, Gdx.graphics.getHeight() *.5f - endGameWidget.getHeight() * .5f);
			stage.addActor(endGameWidget);
			endGameWidget.getColor().a = 0;
			endGameWidget.addAction(Actions.alpha(1f, 1f, Interpolation.swingOut));
			endGameWidget.getExitButton().addListener(new ClickListener(){
				public void clicked (InputEvent event, float x, float y) {
					game.setScreen(new MenuScreen(game));
				}
			});
			return true;
		}
		return false;
	}

	/**
	 * Handle castling
	 * 
	 * @param white
	 *            true if it's a white move
	 * @param kingSide
	 *            true if it's a king side castling, false if it's a queen side
	 *            castling
	 */
	private void castling(boolean white, boolean kingSide) {
		int kingIndex = white ? 3 : 59;
		int rookIndex = white ? (kingSide ? 0 : 7) : (kingSide ? 56 : 63);
		int kingDest = white ? (kingSide ? 1 : 5) : (kingSide ? 57 : 61);
		int rookDest = white ? (kingSide ? 2 : 4) : (kingSide ? 58 : 60);

		MPiece king = chessBoard.getPiece(kingIndex);
		MPiece rook = chessBoard.getPiece(rookIndex);
		MCase kingDestCase = chessBoard.getCase(kingDest);
		MCase rookDestCase = chessBoard.getCase(rookDest);

		handleMove(king, kingDestCase);
		handleMove(rook, rookDestCase);
	}

	private void handleMove(MPiece from, MChessEntity to) {
		MCase dest = getCase(to);
		MPiece piece = getPiece(to);
		if (piece != null) {
			destroy(piece);
		}
		from.moveTo(dest);
	}

	private void destroy(MPiece piece) {
		MCase cas = piece.getCurCase();
		piece.setCurCase(null);
		cas.setCurPiece(null);
		piece.setDead(true);
	}

	private MCase getCase(MChessEntity entity) {
		MCase cas = null;
		if (entity instanceof MCase) {
			cas = (MCase) entity;
		} else {
			MPiece piece = (MPiece) entity;
			cas = piece.getCurCase();
		}
		return cas;
	}

	private MPiece getPiece(MChessEntity entity) {
		MPiece piece = null;
		if (entity instanceof MCase) {
			piece = ((MCase) entity).getCurPiece();
		} else {
			piece = (MPiece) entity;
		}
		return piece;
	}

	public void handleClick(float x, float y) {
		if (state == GameState.PIECE_SELECTION || state == GameState.DESTINATION_SELECTION) {
			Ray ray = cam.getPickRay(x, y);
			switch (state) {
			case PIECE_SELECTION:
				selectedPiece = chessBoard.getPieceClicked(ray);
				if (selectedPiece != null) {
					selectedPiece.hightlight(true);
					state = GameState.DESTINATION_SELECTION;
				}

				break;
			case DESTINATION_SELECTION:
				destination = chessBoard.getEntityClicked(ray);
				if (destination == null) {
					selectedPiece.hightlight(false);
					selectedPiece = null;
					state = GameState.PIECE_SELECTION;
				} else if (destination instanceof MPiece) {
					MPiece piece = (MPiece) destination;
					if (piece.isWhite() == white) {
						selectedPiece.hightlight(false);
						selectedPiece = piece;
						selectedPiece.hightlight(true);
					} else {
						state = GameState.CHECK_MOVE;
					}
				} else if (destination instanceof MCase) {
					MCase cas = (MCase) destination;
					if (cas.getCurPiece() == null) {
						state = GameState.CHECK_MOVE;
					} else {
						MPiece piece = cas.getCurPiece();
						if (piece.isWhite() == white) {
							selectedPiece.hightlight(false);
							selectedPiece = piece;
							selectedPiece.hightlight(true);
						} else {
							state = GameState.CHECK_MOVE;
						}
					}
				}
				break;
			}
		}
	}

	public void draw() {
		modelBatch.begin(cam);
		chessBoard.draw(modelBatch, environment);
		modelBatch.end();
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.setViewport(width, height, true);
	}

	@Override
	public void show() {
		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(stage);
		multiplexer.addProcessor(camController);
		Gdx.input.setInputProcessor(multiplexer);

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
