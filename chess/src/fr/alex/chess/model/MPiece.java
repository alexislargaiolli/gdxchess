package fr.alex.chess.model;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenAccessor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;

import fr.alex.chess.ChessGame;
import fr.alex.chess.Player;

public class MPiece extends MChessEntity implements TweenAccessor<MPiece> {

	public static final int POSITION_XZ = 1;

	protected Vector3 position;

	/**
	 * 3D model instance
	 */
	private ModelInstance instance;

	protected float moveDuration = 0.2f;

	protected float moveTime = 0;

	/**
	 * Type of the piece r n b q k p
	 */
	protected char value;

	/**
	 * Index in board pieces array
	 */
	protected int index;

	private BoundingBox boundingBox;

	private MCase curCase;

	private MCase nextCase;

	private boolean white;

	private boolean dead;

	private final Color initialColor;
	
	private Player player;

	protected Tween move;

	public MPiece(char p, ModelInstance instance, Player player) {
		super();
		this.value = p;
		this.player = player;
		this.white = "RNBQKP".indexOf(value) >= 0;
		this.position = new Vector3(0, ChessModelCreator.getDepth(p) * .5f, 0);
		this.instance = instance;
		this.boundingBox = new BoundingBox();
		this.instance.calculateBoundingBox(boundingBox);
		this.dead = false;
		this.initialColor = white ? Color.LIGHT_GRAY : Color.DARK_GRAY;
		hightlight(false);
		move = null;
	}

	public void promote(char p) {
		this.value = p;
		this.instance = ChessModelCreator.createPiece(p, player.getSkin());
		position.y = ChessModelCreator.getDepth(p) * .5f;
		instance.transform.setToTranslation(position.x, position.y, position.z);
		this.hightlight(false);
		// setPosition(position.x, position.y);
	}

	public void setPosition(float x, float z) {
		this.position.x = x;
		this.position.z = z;
		instance.transform.setToTranslation(position.x, position.y, position.z);
		updateBoundingBox(x, z);
	}

	public boolean isClick(Ray ray) {
		if (dead)
			return false;
		return Intersector.intersectRayBounds(ray, boundingBox, null);
	}

	public void hightlight(boolean hightlight) {
		if (hightlight) {
			((ColorAttribute) instance.materials.get(0).get(ColorAttribute.Diffuse)).color.set(Color.GREEN);
		} else {
			((ColorAttribute) instance.materials.get(0).get(ColorAttribute.Diffuse)).color.set(initialColor);
		}
	}

	public void moveTo(MCase dest) {
		move = Tween.to(this, POSITION_XZ, 0.5f).target(dest.getPosition().x, dest.getPosition().z).start(ChessGame.tween);
		this.nextCase = dest;
	}

	public void update(float delta) {
		if (move != null) {
			if (move.isFinished()) {
				move = null;
				if (curCase.getCurPiece().equals(this)) {
					curCase.setCurPiece(null);
				}
				curCase = nextCase;
				curCase.setCurPiece(this);
			}
		}
	}

	private void updateBoundingBox(float x, float z) {
		Vector3 min = boundingBox.min;
		min.x = x - ChessModelCreator.getWidth(value) * .5f;
		min.y = position.y - ChessModelCreator.getHeight(value) * .5f;
		min.z = z - ChessModelCreator.getDepth(value) * .5f;
		Vector3 max = boundingBox.max;
		max.x = x + ChessModelCreator.getWidth(value) * .5f;
		max.y = position.y + ChessModelCreator.getHeight(value) * .5f;
		max.z = z + ChessModelCreator.getDepth(value) * .5f;
		boundingBox.set(min, max);
	}

	public void draw(ModelBatch modelBatch, Environment environment) {
		modelBatch.render(instance, environment);
	}

	public char getValue() {
		return value;
	}

	public void setValue(char value) {
		this.value = value;
	}

	public BoundingBox getBoundingBox() {
		return boundingBox;
	}

	public MCase getCurCase() {
		return curCase;
	}

	public void setCurCase(MCase curCase) {
		this.curCase = curCase;
	}

	public boolean isWhite() {
		return white;
	}

	public void setWhite(boolean white) {
		this.white = white;
	}

	public boolean isDead() {
		return dead;
	}

	public void setDead(boolean dead) {
		this.dead = dead;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public int getValues(MPiece target, int tweenType, float[] returnValues) {
		switch (tweenType) {
		case POSITION_XZ:
			returnValues[0] = target.position.x;
			returnValues[1] = target.position.z;
			return 2;
		default:
			assert false;
			return -1;
		}
	}

	@Override
	public void setValues(MPiece target, int tweenType, float[] newValues) {
		switch (tweenType) {
		case POSITION_XZ:
			target.setPosition(newValues[0], newValues[1]);
			break;
		default:
			assert false;
			break;
		}
	}

	public Vector3 getPosition() {
		return position;
	}

	public void setPosition(Vector3 position) {
		this.position = position;
	}
}
