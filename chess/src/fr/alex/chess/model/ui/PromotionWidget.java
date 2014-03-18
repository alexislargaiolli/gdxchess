package fr.alex.chess.model.ui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import fr.alex.chess.ChessGame;

public class PromotionWidget{
	protected char promotion;
	public static final float BUTTON_WIDTH = 150;
	
	private BitmapFont font;
	private String queen;
	private String rook;
	private String knight;
	private String bishop;
	
	private Rectangle queenPos;
	private Rectangle rookPos;
	private Rectangle knightPos;
	private Rectangle bishopPos;
	
	public PromotionWidget(){
		font = new BitmapFont();
		
		queen = ChessGame.localize.getUiText("piece.queen");
		rook = ChessGame.localize.getUiText("piece.rook");
		knight = ChessGame.localize.getUiText("piece.knight");
		bishop = ChessGame.localize.getUiText("piece.bishop");
		
		queenPos = new Rectangle(0, 0, 20 ,100);		
		rookPos = new Rectangle(0, 0, 20 ,100);
		knightPos = new Rectangle(0, 0, 20 ,100);
		bishopPos = new Rectangle(0, 0, 20 ,100);		
		
		reset();
	}
	
	public void draw(SpriteBatch batch){
		font.draw(batch, queen, queenPos.x, queenPos.y);
		font.draw(batch, rook, rookPos.x, rookPos.y);
		font.draw(batch, knight, knightPos.x, knightPos.y);
		font.draw(batch, bishop, bishopPos.x, bishopPos.y);
	}
	
	public void handleInput(float x, float y){
		if(queenPos.contains(x, y)){
			promotion = 'Q';
		}
		else if(rookPos.contains(x, y)){
			promotion = 'R';
		}
		else if(knightPos.contains(x, y)){
			promotion = 'K';
		}
		else if(bishopPos.contains(x, y)){
			promotion = 'B';
		}
	}
	
	public void resize(float width, float height){
		queenPos.x = width * .05f;
		queenPos.y = height * .9f;
		
		rookPos.x = width * .05f;
		rookPos.y = height * .75f;
		
		knightPos.x = width * .05f;
		knightPos.y = height * .5f;
		
		bishopPos.x = width * .05f;
		bishopPos.y = height * .35f;
	}
	
	public char getPromotion() {
		return promotion;
	}
	
	public boolean hasSelected(){
		return promotion != '0';
	}

	public void reset(){
		promotion = '0';
	}
}
