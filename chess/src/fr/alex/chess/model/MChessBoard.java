package fr.alex.chess.model;

import com.alonsoruibal.chess.Board;
import com.alonsoruibal.chess.Move;
import com.alonsoruibal.chess.bitboard.BitboardUtils;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;

import fr.alex.chess.ChessGame;

public class MChessBoard {

	/**
	 * List of all instantiate case
	 */
	protected MCase[] cases;

	protected MPiece[] pieces;

	protected Board board;

	protected MPiece curPiece;

	protected Vector3 center;	

	public MChessBoard() {
		cases = new MCase[64];
		pieces = new MPiece[64];
		center = new Vector3(BoardSettings.caseSize * 3.5f, 0, BoardSettings.caseSize * 3.5f);
	}

	public void initialize() {
		int col = 0;
		int row = 0;
		for (int i = 0; i < 64; i++) {
			pieces[i] = null;
			boolean color;
			if (row % 2 == 0) {
				color = i % 2 == 0;
			} else {
				color = i % 2 != 0;
			}

			float x = col * BoardSettings.caseSize;
			float z = row * BoardSettings.caseSize;
			MCase c = new MCase(new Vector3(x, -1f, z), color);
			c.setiCol(col);
			c.setiRow(row);
			c.setIndex(i);
			cases[i] = c;
			col++;
			if (col == 8) {
				col = 0;
				row++;
			}
		}
	}

	public void initFromEngine(Board board, String whiteSkin, String blackSkin) {
		this.board = board;
		String fen = board.getFen();
		int i = 0;
		int iCase = 63;
		while (i < fen.length() && iCase >= 0) {
			char p = fen.charAt(i++);
			if (p != '/') {
				int number = 0;
				try {
					number = Integer.parseInt(String.valueOf(p));
					iCase -= number;
				} catch (Exception ignored) {
					MCase c = cases[iCase];
					
					String skin = isWhite(p) ? whiteSkin : blackSkin;
					MPiece piece = new MPiece(p, ChessGame.model.createPiece(p, skin), skin);
					piece.setPosition(c.getPosition().x, c.getPosition().z);
					pieces[iCase] = piece;
					iCase--;
				}
			}
		}
	}

	public boolean isBlack(char p) {
		return p == 'p' || p == 'r' || p == 'n' || p == 'b' || p == 'q' || p == 'k';
	}

	public boolean isWhite(char p) {
		return p == 'P' || p == 'R' || p == 'N' || p == 'B' || p == 'Q' || p == 'K';
	}

	public void move(int start, int end, String promotion, boolean anim) {
		int move = getMoveId(start, end, promotion);
		switch (Move.getMoveType(move)) {
		case 1: // King side castling
			castling(board.getTurn(), true, anim);
			break;
		case 2: // Queen side castling
			castling(board.getTurn(), false, anim);
			break;
		case 3: // En passant
			long square = board.getPassantSquare();
			int eatenPieceIndex = BitboardUtils.algebraic2Index(BitboardUtils.square2Algebraic(square));
			if (board.getTurn()) {
				eatenPieceIndex -= 8;
			} else {
				eatenPieceIndex += 8;
			}
			pieces[eatenPieceIndex].setDead(true);
			handleMove(start, end, anim);
			break;
		case 4: // Promotion
			handleMove(start, end, anim);
			break;
		default:
			handleMove(start, end, anim);
			break;
		}
		board.doMove(move);
	}	

	private void handleMove(int start, int end, boolean anim) {
		if (pieces[end] != null) {
			pieces[end].setDead(true);
		}
		pieces[start].move(cases[end].getPosition(), anim);
		pieces[end] = pieces[start];
		pieces[start] = null;
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
	private void castling(boolean white, boolean kingSide, boolean anim) {
		int kingIndex = white ? 3 : 59;
		int rookIndex = white ? (kingSide ? 0 : 7) : (kingSide ? 56 : 63);
		int kingDest = white ? (kingSide ? 1 : 5) : (kingSide ? 57 : 61);
		int rookDest = white ? (kingSide ? 2 : 4) : (kingSide ? 58 : 60);

		MPiece king = pieces[kingIndex];
		MPiece rook = pieces[rookIndex];

		king.move(cases[kingDest].getPosition(), anim);
		if (kingIndex != rookDest) {
			pieces[kingIndex] = null;
		}
		pieces[kingDest] = king;

		rook.move(cases[rookDest].getPosition(), anim);
		if (rookIndex != kingDest) {
			pieces[rookIndex] = null;
		}
		pieces[rookDest] = rook;
	}

	public void highlight(int index) {
		unhightLight();
		curPiece = pieces[index];
		curPiece.hightlight(true);
	}

	public void unhightLight() {
		if (curPiece != null) {
			curPiece.hightlight(false);
		}
	}
	
	public int getClickedPiece(Ray ray){
		for(int i=0; i<64; ++i){
			if (pieces[i] != null && pieces[i].isClick(ray)) {
				return i;
			}
		}
		return -1;		
	}
	
	public int getClicked(Ray ray){
		for(int i=0; i<64; ++i){
			if (pieces[i] != null && pieces[i].isClick(ray)) {
				return i;
			}
			else if(cases[i].isClick(ray)){
				return i;
			}
		}
		return -1;
	}
	
	public boolean isWhite(int index){
		return pieces[index] != null && pieces[index].isWhite();
	}

	public boolean isPiece(int index){
		return pieces[index] != null;
	}
	
	public boolean isPromotion(int moveId){
		return moveId == 4;
	}
	
	public boolean isLegalMove(int id){
		return board.isMoveLegal(id);
	}
	
	public int getMoveId(int start, int end, String promotion){
		return Move.getFromString(board, BitboardUtils.index2Algebraic(start) + BitboardUtils.index2Algebraic(end) + (promotion != null && !promotion.equals("null") ? promotion : ""), false);
	}

	public void update(float delta) {
		for (int i =0; i<64; ++i) {
			if (pieces[i] != null && !pieces[i].isDead()) {
				pieces[i].update(delta);
			}
		}
	}

	public MCase getCase(int i) {
		return cases[i];
	}

	public MPiece getPiece(int i) {
		return pieces[i];
	}

	public boolean getTurn(){
		return board.getTurn();
	}
	
	public int getState(){
		return board.isEndGame();
	}
	
	public void debug(ShapeRenderer renderer) {

	}

	public void draw(ModelBatch modelBatch, Environment environment) {
		for (int i = 0; i < 64; ++i) {
			cases[i].draw(modelBatch, environment);
			MPiece piece = pieces[i];
			if (piece != null) {
				if (!piece.isDead()) {
					piece.draw(modelBatch, environment);
				}
			}
		}
	}

	public Vector3 getCenter() {
		return center;
	}
}
