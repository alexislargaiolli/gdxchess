package fr.alex.chess.model.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import fr.alex.chess.ChessGame;

public class PromotionWidget extends Table{
	protected char promotion;
	public static final float BUTTON_WIDTH = 150;
	
	public PromotionWidget(Skin skin){
		super(skin);
		TextButton btQueen = new TextButton(ChessGame.localize.getUiText("piece.queen"), skin);
		TextButton btRook = new TextButton(ChessGame.localize.getUiText("piece.rook"), skin);
		TextButton btKnight = new TextButton(ChessGame.localize.getUiText("piece.knight"), skin);
		TextButton btBishop = new TextButton(ChessGame.localize.getUiText("piece.bishop"), skin);
		
		this.add(btQueen).width(BUTTON_WIDTH);
		this.row();
		this.add(btRook).width(BUTTON_WIDTH);
		this.row();
		this.add(btKnight).width(BUTTON_WIDTH);
		this.row();
		this.add(btBishop).width(BUTTON_WIDTH);
		
		btQueen.addListener(new ClickListener(){
			public void clicked (InputEvent event, float x, float y) {
				promotion = 'Q';
				event.stop();
			}
		});
		
		btRook.addListener(new ClickListener(){
			public void clicked (InputEvent event, float x, float y) {
				promotion = 'R';
				event.stop();
			}
		});
		
		btKnight.addListener(new ClickListener(){
			public void clicked (InputEvent event, float x, float y) {
				promotion = 'K';
				event.stop();
			}
		});
		
		btBishop.addListener(new ClickListener(){
			public void clicked (InputEvent event, float x, float y) {
				promotion = 'B';
				event.stop();
			}
		});
		reset();
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
