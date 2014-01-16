package fr.alex.chess.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;

/**
 * 3D representation of a chess board case
 * @author Jahal
 *
 */
public class MCase extends MChessEntity {
	/**
	 * Row index in chess board
	 */
	private int iRow;
	/**
	 * Col index in chess board
	 */
	private int iCol;
	
	private int index;
	
	/**
	 * True if the case is white, false if the case is black
	 */
	private boolean white;
	
	/**
	 * 3D model instance
	 */
	private ModelInstance instance;
	
	private Vector3 position;
	
	private BoundingBox boundingBox;

	private MPiece curPiece;
	
	public MCase(Vector3 position, boolean white) {
		this.position = position;
		this.white = white;
		instance = white ? ChessModelCreator.createWidthCase() : ChessModelCreator.createBlackCase();
		instance.transform.setToTranslation(position.x, position.y, position.z);
		boundingBox = new BoundingBox();		
		instance.calculateBoundingBox(boundingBox);
		boundingBox.set(new Vector3(boundingBox.min.x + position.x, .1f, boundingBox.min.z + position.z), new Vector3(boundingBox.max.x + position.x, 0.1f, boundingBox.max.z + position.z));
		
	}
	
	public void update(float delta){
		
	}
	
	public void debug(ShapeRenderer renderer){
		
	}
	
	public void draw(ModelBatch modelBatch, Environment environment){
		modelBatch.render(instance, environment);
	}
	
	public boolean isClick(Ray ray){
		return Intersector.intersectRayBounds(ray, boundingBox, null);
	}
	
	public void hightlight(boolean hightlight){
		if(hightlight){
			((ColorAttribute)instance.materials.get(0).get(ColorAttribute.Diffuse)).color.set(Color.RED);
		}
		else{
			((ColorAttribute)instance.materials.get(0).get(ColorAttribute.Diffuse)).color.set(white?Color.WHITE:Color.BLACK);
		}
	}

	public int getiRow() {
		return iRow;
	}

	public void setiRow(int iRow) {
		this.iRow = iRow;
	}

	public int getiCol() {
		return iCol;
	}

	public void setiCol(int iCol) {
		this.iCol = iCol;
	}

	public boolean isWhite() {
		return white;
	}

	public void setWhite(boolean white) {
		this.white = white;
	}

	public ModelInstance getInstance() {
		return instance;
	}

	public void setInstance(ModelInstance instance) {
		this.instance = instance;
	}

	public BoundingBox getBoundingBox() {
		return boundingBox;
	}

	public void setBoundingBox(BoundingBox boundingBox) {
		this.boundingBox = boundingBox;
	}

	public Vector3 getPosition() {
		return position;
	}

	public void setPosition(Vector3 position) {
		this.position = position;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public MPiece getCurPiece() {
		return curPiece;
	}

	public void setCurPiece(MPiece curPiece) {
		this.curPiece = curPiece;
	}
}
