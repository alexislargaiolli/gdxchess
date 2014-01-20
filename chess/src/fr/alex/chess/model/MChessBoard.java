package fr.alex.chess.model;

import com.alonsoruibal.chess.Board;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Array;

public class MChessBoard {

	/**
	 * List of all instantiate case
	 */
	protected MCase[] cases;

	protected Array<MPiece> pieces;

	protected MPiece curPiece;

	protected Vector3 center;

	public MChessBoard() {
		cases = new MCase[64];
		pieces = new Array<MPiece>();
		center = new Vector3(BoardSettings.caseSize * 3.5f, 0,
				BoardSettings.caseSize * 3.5f);
	}

	public void initialize() {
		pieces.clear();

		int col = 0;
		int row = 0;
		for (int i = 0; i < 64; i++) {

			boolean color;
			if (row % 2 == 0) {
				color = i % 2 == 0;
			} else {
				color = i % 2 != 0;
			}

			float x = col * BoardSettings.caseSize;
			float z = row * BoardSettings.caseSize;
			MCase c = new MCase(new Vector3(x, 0, z), color);
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

	public void initFromEngine(Board board) {
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
					MPiece piece = new MPiece(p,
							ChessModelCreator.createPiece(p));
					piece.setPosition(c.getPosition().x, c.getPosition().z);
					pieces.add(piece);
					c.setCurPiece(piece);
					piece.setCurCase(c);
					iCase--;
				}
			}
		}
	}

	public void movePiece(MPiece piece, MCase start, MCase end) {
		start.setCurPiece(null);
		end.setCurPiece(piece);
		piece.moveTo(end.getPosition().x, end.getPosition().z);
	}

	public MChessEntity getEntityClicked(Ray ray) {
		MChessEntity selected = null;
		for (MPiece piece : pieces) {
			if (piece.isClick(ray) && selected == null) {
				selected = piece;
			}
		}
		if (selected == null) {
			for (MCase c : cases) {
				if (c.isClick(ray)) {
					selected = c;
				}
			}
		}
		return selected;
	}

	public MCase getCaseClicked(Ray ray) {
		MCase selected = null;
		for (MCase c : cases) {
			if (c.isClick(ray)) {
				selected = c;
			}
		}
		return selected;
	}

	public MPiece getPieceClicked(Ray ray) {
		MPiece selected = null;
		for (MPiece piece : pieces) {
			if (piece.isClick(ray) && selected == null) {
				selected = piece;
			}
		}
		return selected;
	}

	public void update(float delta) {
		for (MPiece piece : pieces) {
			if (!piece.isDead()) {
				piece.update(delta);
			}
		}
	}

	public MCase getCase(int i) {
		return cases[i];
	}

	public MPiece getPiece(int i) {
		return cases[i].getCurPiece();
	}

	public void debug(ShapeRenderer renderer) {

	}

	public void draw(ModelBatch modelBatch, Environment environment) {
		for (MCase c : cases) {
			c.draw(modelBatch, environment);
		}
		for (MPiece piece : pieces) {
			if (!piece.isDead()) {
				piece.draw(modelBatch, environment);
			}
		}
	}

	public Vector3 getCenter() {
		return center;
	}
}
