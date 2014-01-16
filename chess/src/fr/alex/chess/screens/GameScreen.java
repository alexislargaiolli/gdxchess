package fr.alex.chess.screens;

import java.io.IOException;

import com.alonsoruibal.chess.Board;
import com.alonsoruibal.chess.Move;
import com.alonsoruibal.chess.bitboard.BitboardUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import fr.alex.chess.ChessCamController;
import fr.alex.chess.model.MCase;
import fr.alex.chess.model.MChessBoard;
import fr.alex.chess.model.MChessEntity;
import fr.alex.chess.model.MPiece;
import fr.alex.chess.model.ui.DialogPromotion;
import fr.alex.chess.model.ui.EndDialog;
import fr.alex.chess.net.Net;
import fr.alex.chess.utils.Assets;
import fr.alex.chess.utils.Game;
import fr.alex.chess.utils.Notifier;
import fr.alex.chess.utils.Notifier.NotifType;

public class GameScreen extends BaseScreen implements InputProcessor {
	protected PerspectiveCamera cam;
	protected ModelBatch modelBatch;
	protected SpriteBatch batch;
	protected Environment environment;
	protected ChessCamController camController;
	protected MChessBoard chessBoard;
	protected ShapeRenderer renderer;
	protected final Vector3 cameraInitialPosition = new Vector3(30f, 30f, 30f);
	protected Board board;
	protected MPiece curPiece;
	protected int lastFromIndex;
	protected int lastToIndex;
	protected Notifier notifier;
	protected InputMultiplexer multiplexer;
	protected Label messageLabel;
	protected DialogPromotion dialogPromote;
	protected Stage gameStage;
	protected char promoteChoice;
	protected boolean paused;
	protected EndDialog dialogWin;

	public enum GAME_STATE {
		CONSTRUCT, CONNECTING, WAITING_OPPONENT, MY_TURN, OPPONENT_TURN, END, OPPONENT_LEFT
	}

	protected boolean waitingForPromotion;

	protected GAME_STATE state;

	public GameScreen(UiApp app) {
		super(app);
		gameStage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		mainTable.setBackground(app.skin.getDrawable("window1"));
		modelBatch = new ModelBatch();
		batch = new SpriteBatch();
		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		chessBoard = new MChessBoard();
		environment = new Environment();
		camController = new ChessCamController(cam);
		notifier = new Notifier(app);
		addActor(notifier);
		//messageLabel.setText(Game.localize.getUiText("game.waiting.oppenent"));
		messageLabel = new Label(Game.localize.getUiText("game.waiting.oppenent"), app.skin);
		mainTable.add(messageLabel);

		dur = .5f;
		interpolation = Interpolation.pow2;
	}

	@Override
	public void loadContent() {
		Assets.loadTextureAtlas("ui/ui.atlas");
	}

	@Override
	public void initialize() {
		chessBoard.initialize();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.6f, 0.6f, 0.6f, 1f));
		environment.add(new DirectionalLight().set(0.5f, 0.5f, 0.5f, -.9f, -.9f, -.9f));
		environment.add(new DirectionalLight().set(0.5f, 0.5f, 0.5f, .9f, .9f, .9f));

		board = new Board();
		board.startPosition();
		chessBoard.initFromEngine(board);
		notifier.initialize();

		System.out.println(chessBoard.getCenter().x + " "+chessBoard.getCenter().z);
		cam.position.set(chessBoard.getCenter().x, 40f, chessBoard.getCenter().z*.5f);
		cam.update();
		cam.lookAt(chessBoard.getCenter().x, 0f, chessBoard.getCenter().z);
		cam.near = .1f;
		cam.far = 300f;
		cam.update();
		camController.setFocus(new Vector3(chessBoard.getCenter().x, 0f, chessBoard.getCenter().z));
		
		DefaultShader.defaultCullFace = 0;

		state = GAME_STATE.CONSTRUCT;
		gameStage.clear();
	}

	@Override
	public BaseScreen show() {
		if (dialogWin != null) {
			mainTable.removeActor(dialogWin);
		}
		switchState(GAME_STATE.CONNECTING);
		return super.show();
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		if (paused)
			return;
		gameStage.act(delta);
		camController.update();
		chessBoard.update(delta);
	}

	@Override
	public void draw() {
		Gdx.gl.glClear(GL10.GL_DEPTH_BUFFER_BIT);
		switch (state) {
		case END:
			break;
		case MY_TURN:
			modelBatch.begin(cam);
			chessBoard.debug(renderer);
			chessBoard.draw(modelBatch, environment);
			modelBatch.end();
			break;
		case OPPONENT_TURN:
			modelBatch.begin(cam);
			chessBoard.debug(renderer);
			chessBoard.draw(modelBatch, environment);
			modelBatch.end();
			break;
		case CONNECTING:
			break;
		default:
			break;

		}
		gameStage.draw();
	}

	public void switchState(GAME_STATE nextState) {
		System.out.println("Switch to: " + nextState);
		switch (state) {
		case CONNECTING:
			messageLabel.setVisible(false);
			
			/*if (Game.player.isWhite())
				cam.position.set(chessBoard.getCenter().x, 35f, -10f);
			else
				cam.position.set(chessBoard.getCenter().x, 35f, BoardSettings.width + 5f);
			*/
			if (Game.player.isWhite())
				camController.startCinematicWhiteBegin();
			else
				camController.startCinematicBlackBegin();
				
			UiApp.inputs.addProcessor(gameStage);
			UiApp.inputs.addProcessor(this);
			UiApp.inputs.addProcessor(camController);
			break;
		case WAITING_OPPONENT:
			messageLabel.setVisible(false);
			break;
		case END:
			break;
		case MY_TURN:
			notifier.hide(NotifType.PLAYER_TURN);
			break;
		case OPPONENT_TURN:
			notifier.hide(NotifType.OPPONENT_TURN);
			break;
		case CONSTRUCT:
			break;
		default:
			break;
		}
		switch (nextState) {
		case CONNECTING:
			messageLabel.setText(Game.localize.getUiText("game.waiting.connection"));
			messageLabel.setVisible(true);
			Net.connectGame(this);
			break;
		case WAITING_OPPONENT:
			messageLabel.setText(Game.localize.getUiText("game.waiting.oppenent"));
			messageLabel.setVisible(true);
			break;
		case END:
			dialogWin = new EndDialog(app, board.isEndGame());
			mainTable.add(dialogWin);
			dialogWin.show();
			break;
		case MY_TURN:
			notifier.show(NotifType.PLAYER_TURN);
			break;
		case OPPONENT_TURN:
			notifier.show(NotifType.OPPONENT_TURN);
			break;
		case CONSTRUCT:
			break;
		case OPPONENT_LEFT:
			dialogWin = new EndDialog(app, -2);
			mainTable.add(dialogWin);
			dialogWin.show();
			break;
		default:
			break;
		}
		state = nextState;
	}

	public void opponentLeft() {
		switchState(GAME_STATE.OPPONENT_LEFT);
	}

	public void opponentMoved(String data) {
		String[] words = data.split(";");
		int start = Integer.parseInt(words[0]);
		int end = Integer.parseInt(words[1]);
		promoteChoice = '0';
		if (words.length > 2) {
			promoteChoice = words[2].charAt(0);
		}
		MPiece pieceToMove = chessBoard.getPiece(start);
		MCase endCase = chessBoard.getCase(end);
		MPiece pieceAttack = endCase.getCurPiece();
		if (pieceAttack == null) {
			move(pieceToMove, endCase);
		} else {
			eat(pieceToMove, pieceAttack);
		}
	}

	public void connectionEtablished() {
		switchState(GAME_STATE.WAITING_OPPONENT);
	}

	public void opponentFound() {
		nextTurn();
	}

	public void connectionClosed() {

	}

	public void resetView() {
		cam.position.set(chessBoard.getCenter().x, 35f, -15f);
		cam.lookAt(chessBoard.getCenter().x, 0f, chessBoard.getCenter().z);
		cam.update();
		camController.update();
	}

	/**
	 * Check the game state : chess, white win, black win, draw or not finished
	 */
	private void checkGameState() {
		notifier.show(NotifType.CHESS, board.getCheck());
		int state = board.isEndGame();
		switch (state) {
		case 1:
			switchState(GAME_STATE.END);
			Gdx.app.log("", "White win");
			break;
		case -1:
			switchState(GAME_STATE.END);
			Gdx.app.log("", "Black win");
			break;
		case 99:
			switchState(GAME_STATE.END);
			Gdx.app.log("", "Draw");
			break;
		default:
		}
	}

	/**
	 * Toggle state between my_turn and opponent_turn
	 */
	private void nextTurn() {
		if (getTurn()) {
			switchState(GAME_STATE.MY_TURN);
		} else {
			switchState(GAME_STATE.OPPONENT_TURN);
		}
	}

	/**
	 * Handle castling, en passant and promotion moves
	 * 
	 * @param move
	 * @return true if a special move has been handle, false otherwise
	 */
	private boolean handleSpecialMove(int move, int fromIndex, int toIndex) {

		boolean handle = false;
		switch (Move.getMoveType(move)) {
		case 1: // King side castling
			sendMove(fromIndex, toIndex);
			castling(board.getTurn(), true);
			board.doMove(move);
			handle = true;

			break;
		case 2: // Queen side castling			
			sendMove(fromIndex, toIndex);
			castling(board.getTurn(), false);
			board.doMove(move);
			handle = true;
			break;
		case 3: // En passant
			sendMove(fromIndex, toIndex);
			long square = board.getPassantSquare();
			int eatenPieceIndex = BitboardUtils.algebraic2Index(BitboardUtils.square2Algebraic(square));
			if (board.getTurn()) {
				eatenPieceIndex -= 8;
			} else {
				eatenPieceIndex += 8;
			}
			board.doMove(move);
			MPiece piece = chessBoard.getPiece(fromIndex);
			MPiece eatenPiece = chessBoard.getPiece(eatenPieceIndex);
			MCase c = chessBoard.getCase(toIndex);
			completeMovePiece(piece, c);
			destroyPiece(eatenPiece);
			handle = true;
			break;
		case 4: // Promotion			
			MPiece pieceToPromote = chessBoard.getPiece(fromIndex);
			MCase dest = chessBoard.getCase(toIndex);
			MPiece destPiece = dest.getCurPiece();
			if (destPiece != null) {
				destroyPiece(destPiece);
				dest.setCurPiece(null);
			}
			completeMovePiece(pieceToPromote, dest);
			if (getTurn()) {
				if (!gameStage.getActors().contains(dialogPromote, true)) {
					dialogPromote = new DialogPromotion(this, Game.player.isWhite(), app);
					gameStage.addActor(dialogPromote);
				}
				dialogPromote.show();
				waitingForPromotion = true;
				System.out.println("Promotion ?");
			} else {
				handlePromotion(promoteChoice);
			}
			handle = true;
			break;
		}
		return handle;
	}

	/**
	 * Move a piece to a case and updates piece and case attributes Add the
	 * piece to the list of current moving piece and set the game state as
	 * PIECE_MOVE
	 * 
	 * @param piece
	 *            piece to move
	 * @param dest
	 *            destination case
	 */
	private void completeMovePiece(MPiece piece, MCase dest) {
		simpleMovePiece(piece, dest);
		piece.getCurCase().setCurPiece(null);
		piece.setCurCase(dest);
		dest.setCurPiece(piece);
	}

	/**
	 * Only move the piece without updating piece and case attribute Add the
	 * piece to the list of current moving piece and set the game as PIECE_MOVE
	 * 
	 * @param piece
	 * @param dest
	 */
	private void simpleMovePiece(MPiece piece, MCase dest) {
		piece.moveTo(dest.getPosition().x, dest.getPosition().z);
	}

	/**
	 * Handle eaten piece
	 * 
	 * @param piece
	 */
	private void destroyPiece(MPiece piece) {
		piece.setDead(true);
	}

	/**
	 * Handle illegal move
	 */
	private void illegalMove() {

	}

	/**
	 * Check legality of move Perform move it's a legal move
	 * 
	 * @param piece
	 * @param dest
	 */
	private void move(MPiece piece, MCase dest) {
		lastFromIndex = piece.getCurCase().getIndex();
		lastToIndex = dest.getIndex();
		int move = Move.getFromString(board, BitboardUtils.index2Algebraic(lastFromIndex) + BitboardUtils.index2Algebraic(lastToIndex), true);
		// Verify legality and play
		if (board.isMoveLegal(move)) {
			if (!handleSpecialMove(move, lastFromIndex, lastToIndex)) {
				board.doMove(move);
				sendMove(lastFromIndex, lastToIndex);
				completeMovePiece(piece, dest);
				nextTurn();
			}
			checkGameState();

		} else {
			illegalMove();
		}
	}

	/**
	 * Send move to the opponent if it's current player move
	 * 
	 * @param fromIndex
	 * @param toIndex
	 */
	private void sendMove(int fromIndex, int toIndex) {
		if (state == GAME_STATE.MY_TURN) {
			try {
				Net.sendMove(fromIndex, toIndex);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Send move with promotion choice to the opponent if it's current player
	 * move
	 * 
	 * @param fromIndex
	 * @param toIndex
	 * @param promoteChoice
	 */
	private void sendPromoteMove(int fromIndex, int toIndex, char promoteChoice) {
		if (state == GAME_STATE.MY_TURN) {
			try {
				Net.sendPromoteMove(fromIndex, toIndex, promoteChoice);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Handle piece eating
	 * 
	 * @param p1
	 * @param p2
	 */
	private void eat(MPiece p1, MPiece p2) {
		lastFromIndex = p1.getCurCase().getIndex();
		lastToIndex = p2.getCurCase().getIndex();
		int move = Move.getFromString(board, BitboardUtils.index2Algebraic(lastFromIndex) + BitboardUtils.index2Algebraic(lastToIndex), true);
		// Verify legality and play
		if (board.isMoveLegal(move)) {
			if (!handleSpecialMove(move, lastFromIndex, lastToIndex)) {
				board.doMove(move);
				sendMove(lastFromIndex, lastToIndex);
				MCase dest = p2.getCurCase();
				completeMovePiece(p1, dest);
				destroyPiece(p2);
				nextTurn();
			}
			checkGameState();
		} else {
			illegalMove();
		}
	}

	/**
	 * 
	 * @return true if it's current player turn
	 */
	private boolean getTurn() {
		if (!Net.enable) {
			return true;
		} else {
			return Game.player.isWhite() == board.getTurn();
		}
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

		simpleMovePiece(king, kingDestCase);
		simpleMovePiece(rook, rookDestCase);
	}

	/**
	 * Handle piece promotion
	 * 
	 * @param p
	 *            promotion choice, values : Q (queen), R (rook), K (Knight), B
	 *            (bishop)
	 */
	public void handlePromotion(char p) {
		sendPromoteMove(lastFromIndex, lastToIndex, p);
		int move = Move.getFromString(board, BitboardUtils.index2Algebraic(lastFromIndex) + BitboardUtils.index2Algebraic(lastToIndex) + p, true);
		board.doMove(move);

		MPiece piece = chessBoard.getPiece(lastToIndex);
		piece.promote(p);
		nextTurn();
		waitingForPromotion = false;
	}

	@Override
	public boolean keyDown(int keycode) {

		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Keys.R) {
			resetView();
		}
		if (waitingForPromotion) {
			boolean white = board.getTurn();
			if (keycode == Keys.Q) {
				handlePromotion(white ? 'Q' : 'q');
			} else if (keycode == Keys.R) {
				handlePromotion(white ? 'R' : 'r');
			} else if (keycode == Keys.K) {
				handlePromotion(white ? 'K' : 'k');
			} else if (keycode == Keys.B) {
				handlePromotion(white ? 'B' : 'b');
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character) {

		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (state == GAME_STATE.MY_TURN) {
			Ray ray = cam.getPickRay(screenX, screenY);
			MChessEntity entity = chessBoard.getCaseClicked(ray);
			if (curPiece == null) { // If no piece was selected, this piece is just highlighted
				if (entity instanceof MPiece) {
					entity.hightlight(true);
					curPiece = (MPiece) entity;
				}
			} else {
				if (entity instanceof MCase) { // If a piece is selected and a case has just been clicked
					MCase curCase = (MCase) entity;
					if (curCase.getCurPiece() == null) { // If the case is free Piece moving
						move(curPiece, curCase);
					} else { // If a piece is on this case Piece eating
						eat(curPiece, curCase.getCurPiece());
					}
					curPiece.hightlight(false);
					curPiece = null;

				} else if (entity instanceof MPiece && curPiece.isWhite() != entity.isWhite()) { // If a piece is selected and ennemy piece has just been clicked Piece eating
					MPiece piece = (MPiece) entity;
					eat(curPiece, piece);
					curPiece.hightlight(false);
					curPiece = null;
				} else if (entity instanceof MPiece) { // If a piece is selected and ally piece has just been clicked Highlight changing
					curPiece.hightlight(false);
					entity.hightlight(true);
					curPiece = (MPiece) entity;
				}
			}
			return entity != null;
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {

		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {

		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {

		return false;
	}

	@Override
	public boolean scrolled(int amount) {

		return false;
	}

	@Override
	public void hide() {
		UiApp.inputs.removeProcessor(gameStage);
		UiApp.inputs.removeProcessor(this);
		UiApp.inputs.removeProcessor(camController);
		super.hide();
	}

	@Override
	public void onBackPress() {

	}

	@Override
	public void dispose() {

	}

	@Override
	public void pause() {
		paused = true;
	}

	@Override
	public void resume() {
		paused = false;
	}
}
