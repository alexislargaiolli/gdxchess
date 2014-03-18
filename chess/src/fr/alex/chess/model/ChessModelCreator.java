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

import fr.alex.chess.ChessGame;

public class ChessModelCreator {

	private ModelBuilder modelBuilder;
	@SuppressWarnings("rawtypes")
	private ModelLoader loader;
	private Model whiteCase;
	private Model blackCase;
	private ChessGame game;

	public ChessModelCreator(ChessGame game) {
		this.game = game;
		modelBuilder = new ModelBuilder();
		loader = new ObjLoader();
		whiteCase = modelBuilder.createBox(BoardSettings.caseSize, 1f,
				BoardSettings.caseSize,
				new Material(ColorAttribute.createDiffuse(Color.WHITE)), Usage.Position | Usage.Normal);
		blackCase = modelBuilder.createBox(BoardSettings.caseSize, 1f,
				BoardSettings.caseSize,
				new Material(ColorAttribute.createDiffuse(Color.BLACK)),
				Usage.Position | Usage.Normal);		
	}
	
	public void load(String skin){
		this.game.assets.load(skin+"/k.g3db", Model.class);
		this.game.assets.load(skin+"/n.g3db", Model.class);
		this.game.assets.load(skin+"/p.g3db", Model.class);
		this.game.assets.load(skin+"/q.g3db", Model.class);
		this.game.assets.load(skin+"/r.g3db", Model.class);
		this.game.assets.load(skin+"/b.g3db", Model.class);
	}
	
	/**
	 * 
	 * @param p
	 * @return the width of the specified piece
	 */
	public float getWidth(char p){
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
	
	public float getHeight(char p){
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
	
	public float getDepth(char p){
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

	public ModelInstance createWidthCase() {
		return new ModelInstance(whiteCase);
	}

	public ModelInstance createBlackCase() {
		return new ModelInstance(blackCase);
	}

	public ModelInstance createPiece(char c, String skin) {
		String fileName = skin+"/"+ String.valueOf(c).toLowerCase() +".g3db";
		this.game.assets.get(fileName, Model.class);
		Model model = loader.loadModel(Gdx.files.internal(skin+"/"+ String.valueOf(c).toLowerCase() +".obj"));
		ModelInstance instance = new ModelInstance(model);
		return instance;
	}
}
