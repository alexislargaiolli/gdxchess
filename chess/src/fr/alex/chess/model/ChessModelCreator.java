package fr.alex.chess.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

public class ChessModelCreator {

	private static ModelBuilder modelBuilder;
	@SuppressWarnings("rawtypes")
	private static ModelLoader loader;
	private static Model whiteCase;
	private static Model blackCase;
	private static Model pion;
	private static Model tour;
	private static Model cavalier;
	private static Model fou;
	private static Model roi;
	private static Model dame;
	private static Model tmp;

	static {
		modelBuilder = new ModelBuilder();
		loader = new ObjLoader();
		whiteCase = modelBuilder.createBox(BoardSettings.caseSize, 1f,
				BoardSettings.caseSize,
				new Material(ColorAttribute.createDiffuse(Color.WHITE)), Usage.Position | Usage.Normal);
		blackCase = modelBuilder.createBox(BoardSettings.caseSize, 1f,
				BoardSettings.caseSize,
				new Material(ColorAttribute.createDiffuse(Color.BLACK)),
				Usage.Position | Usage.Normal);
		/*pion = modelBuilder.createBox(ChessModelCreator.getWidth('p') , ChessModelCreator.getDepth('p'),
				ChessModelCreator.getHeight('p'),
				new Material(ColorAttribute.createDiffuse(Color.LIGHT_GRAY)),
				Usage.Position | Usage.Normal);*/
		pion = loader.loadModel(Gdx.files.internal("pieces/pion.obj"));
		pion.materials.add(new Material(ColorAttribute.createDiffuse(Color.WHITE) ));
		tour = modelBuilder.createBox(ChessModelCreator.getWidth('r'), ChessModelCreator.getDepth('r'),
				ChessModelCreator.getHeight('r'),
				new Material(ColorAttribute.createDiffuse(Color.DARK_GRAY)),
				Usage.Position | Usage.Normal);
		cavalier = modelBuilder.createBox(ChessModelCreator.getWidth('n'), ChessModelCreator.getDepth('n'),
				ChessModelCreator.getHeight('n'),
				new Material(ColorAttribute.createDiffuse(Color.BLUE)),
				Usage.Position | Usage.Normal);
		fou = modelBuilder.createBox(ChessModelCreator.getWidth('b'), ChessModelCreator.getDepth('b'),
				ChessModelCreator.getHeight('b'),
				new Material(ColorAttribute.createDiffuse(Color.BLUE)),
				Usage.Position | Usage.Normal);
		roi = modelBuilder.createBox(ChessModelCreator.getWidth('k'), ChessModelCreator.getDepth('k'),
				ChessModelCreator.getHeight('k'),
				new Material(ColorAttribute.createDiffuse(Color.RED)),
				Usage.Position | Usage.Normal);
		dame = modelBuilder.createBox(ChessModelCreator.getWidth('q'), ChessModelCreator.getDepth('q'),
				ChessModelCreator.getHeight('q'),
				new Material(ColorAttribute.createDiffuse(Color.YELLOW)),
				Usage.Position | Usage.Normal);
		
		tmp = modelBuilder.createBox(1, 1, 1,
				new Material(ColorAttribute.createDiffuse(Color.YELLOW)),
				Usage.Position | Usage.Normal);
	}
	
	/**
	 * 
	 * @param p
	 * @return the width of the specified piece
	 */
	public static float getWidth(char p){
		float width = 0;
		switch (p) {
		case 'p':
			width = BoardSettings.caseSize * .5f;
			break;
		case 'r':
			width = BoardSettings.caseSize * .7f;
			break;
		case 'n':
			width = BoardSettings.caseSize * .5f;
			break;
		case 'b':
			width = BoardSettings.caseSize * .4f;
			break;
		case 'q':
			width = BoardSettings.caseSize * .5f;
			break;
		case 'k':
			width = BoardSettings.caseSize * .5f;
			break;
		case 'P':
			width = BoardSettings.caseSize * .5f;
			break;
		case 'R':
			width = BoardSettings.caseSize * .7f;
			break;
		case 'N':
			width = BoardSettings.caseSize * .5f;
			break;
		case 'B':
			width = BoardSettings.caseSize * .4f;
			break;
		case 'Q':
			width = BoardSettings.caseSize * .5f;
			break;
		case 'K':
			width = BoardSettings.caseSize * .5f;
			break;
		}
		return width;
	}
	
	public static float getHeight(char p){
		float height = 0;
		switch (p) {
		case 'p':
			height = BoardSettings.caseSize * .5f;
			break;
		case 'r':
			height = BoardSettings.caseSize * .7f;
			break;
		case 'n':
			height = BoardSettings.caseSize * .5f;
			break;
		case 'b':
			height = BoardSettings.caseSize * .4f;
			break;
		case 'q':
			height = BoardSettings.caseSize * .5f;
			break;
		case 'k':
			height = BoardSettings.caseSize * .5f;
			break;
		case 'P':
			height = BoardSettings.caseSize * .5f;
			break;
		case 'R':
			height = BoardSettings.caseSize * .7f;
			break;
		case 'N':
			height = BoardSettings.caseSize * .5f;
			break;
		case 'B':
			height = BoardSettings.caseSize * .4f;
			break;
		case 'Q':
			height = BoardSettings.caseSize * .5f;
			break;
		case 'K':
			height = BoardSettings.caseSize * .5f;
			break;
		}
		return height;
	}
	
	public static float getDepth(char p){
		float depth = 0;
		switch (p) {
		case 'p':
			depth = 1f;
			break;
		case 'r':
			depth = 6f;
			break;
		case 'n':
			depth = 5f;
			break;
		case 'b':
			depth = 5f;
			break;
		case 'q':
			depth = 6f;
			break;
		case 'k':
			depth = 6f;
			break;
		case 'P':
			depth =1f;
			break;
		case 'R':
			depth = 6f;
			break;
		case 'N':
			depth = 5f;
			break;
		case 'B':
			depth = 5f;
			break;
		case 'Q':
			depth = 6f;
			break;
		case 'K':
			depth = 6f;
			break;
		}
		return depth;
	}

	public static ModelInstance createWidthCase() {
		return new ModelInstance(whiteCase);
	}

	public static ModelInstance createBlackCase() {
		return new ModelInstance(blackCase);
	}

	public static ModelInstance createPion() {
		return new ModelInstance(pion);
	}

	public static ModelInstance createPiece(char c) {
		ModelInstance instance = null;
		
		switch (c) {
		case 'p':
			instance = new ModelInstance(pion);
			break;
		case 'r':
			instance = new ModelInstance(tour);
			break;
		case 'n':
			instance = new ModelInstance(cavalier);
			break;
		case 'b':
			instance = new ModelInstance(fou);
			break;
		case 'q':
			instance = new ModelInstance(dame);
			break;
		case 'k':
			instance = new ModelInstance(roi);
			break;
		case 'P':
			instance = new ModelInstance(pion);
			break;
		case 'R':
			instance = new ModelInstance(tour);
			break;
		case 'N':
			instance = new ModelInstance(cavalier);
			break;
		case 'B':
			instance = new ModelInstance(fou);
			break;
		case 'Q':
			instance = new ModelInstance(dame);
			break;
		case 'K':
			instance = new ModelInstance(roi);
			break;
		}
		return instance;
	}

	public static ModelInstance createTmp() {
		return new ModelInstance(tmp);
	}

}
